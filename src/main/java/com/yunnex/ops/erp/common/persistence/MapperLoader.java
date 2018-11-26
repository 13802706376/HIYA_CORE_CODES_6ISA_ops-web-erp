package com.yunnex.ops.erp.common.persistence;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * Mybatis的mapper文件中的sql语句被修改后, 只能重启服务器才能被加载, 非常耗时,所以就写了一个自动加载的类,
 * 配置后检查xml文件更改,如果发生变化,重新加载xml里面的内容.
 */
//@Service
//@Lazy(false)
public class MapperLoader implements DisposableBean, InitializingBean, ApplicationContextAware {

    private ConfigurableApplicationContext context ;
    private  String basePackage ;
    private HashMap<String, String> fileMapping = new HashMap<String, String>();
    private Scanner scanner ;
    private ScheduledExecutorService service ;
    private static final Log LOGGER = LogFactory.getLog(MapperLoader.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)  {
        this.context = (ConfigurableApplicationContext) applicationContext;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            service = Executors.newScheduledThreadPool(1);
            
            // 获取xml所在包
            MapperScannerConfigurer config = context.getBean(MapperScannerConfigurer.class);
            Field field = config.getClass().getDeclaredField("basePackage");
            field.setAccessible(true);
            basePackage = (String) field.get(config);
            
            // 触发文件监听事件
            scanner = new Scanner();
            scanner.scan();

            int initialDelay = 5;
            int period = 5;
            service.scheduleAtFixedRate(new Task(), initialDelay, period, TimeUnit.SECONDS);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    class Task implements Runnable {
        @Override
        public void run() {
            try {
                if (scanner.isChanged()) {
                    LOGGER.info("*Mapper.xml文件改变,重新加载.");
                    scanner.reloadXML();
                    LOGGER.info("加载完毕.");
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

    }

    @SuppressWarnings({ "rawtypes" })
    class Scanner {
        
        private String[] basePackages;
        private static final String XML_RESOURCE_PATTERN = "**/*.xml";
        private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

        public Scanner() {
            basePackages = StringUtils.tokenizeToStringArray(MapperLoader.this.basePackage,
                    ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
        }

        public Resource[] getResource(String basePackage, String pattern) throws IOException {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + ClassUtils.convertClassNameToResourcePath(context.getEnvironment().resolveRequiredPlaceholders(
                            basePackage)) + "/" + pattern;
            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
            return resources;
        }

        /**
         * 
         * @throws Exception
         */
        public void reloadXML() throws Exception {
            SqlSessionFactory factory = context.getBean(SqlSessionFactory.class);
            Configuration configuration = factory.getConfiguration();
            // 移除加载项
            removeConfig(configuration);
            // 重新扫描加载
            for (String basePac : basePackages) {
                Resource[] resources = getResource(basePac, XML_RESOURCE_PATTERN);
                if (resources != null) {
                    for (int i = 0; i < resources.length; i++) {
                        if (resources[i] == null) {
                            continue;
                        }
                        try {
                            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(resources[i].getInputStream(),
                                    configuration, resources[i].toString(), configuration.getSqlFragments());
                            xmlMapperBuilder.parse();
                        } catch (Exception e) {
                            throw new NestedIOException("Failed to parse mapping resource: '" + resources[i] + "'", e);
                        } finally {
                            ErrorContext.instance().reset();
                        }
                    }
                }
            }

        }

        /**
         * removeConfig
         * @param configuration
         * @throws NoSuchFieldException
         * @throws IllegalAccessException
         */
        private void removeConfig(Configuration configuration) throws NoSuchFieldException, IllegalAccessException {
            Class<?> classConfig = configuration.getClass();
            clearMap(classConfig, configuration, "mappedStatements");
            clearMap(classConfig, configuration, "caches");
            clearMap(classConfig, configuration, "resultMaps");
            clearMap(classConfig, configuration, "parameterMaps");
            clearMap(classConfig, configuration, "keyGenerators");
            clearMap(classConfig, configuration, "sqlFragments");

            clearSet(classConfig, configuration, "loadedResources");

        }

        /**
         * removeConfig
         * @param configuration
         * @throws NoSuchFieldException
         * @throws IllegalAccessException
         */
        private void clearMap(Class<?> classConfig, Configuration configuration, String fieldName)
                        throws NoSuchFieldException, IllegalAccessException {
            Field field = classConfig.getDeclaredField(fieldName);
            field.setAccessible(true);
            Map mapConfig = (Map) field.get(configuration);
            mapConfig.clear();
        }

        /**
         * removeConfig
         * @param configuration
         * @throws NoSuchFieldException
         * @throws IllegalAccessException
         */
        private void clearSet(Class<?> classConfig, Configuration configuration, String fieldName)
                        throws NoSuchFieldException, IllegalAccessException {
            Field field = classConfig.getDeclaredField(fieldName);
            field.setAccessible(true);
            Set setConfig = (Set) field.get(configuration);
            setConfig.clear();
        }

        /**
         * removeConfig
         * @param configuration
         * @throws NoSuchFieldException
         * @throws IllegalAccessException
         */
        public void scan() throws IOException {
            if (!fileMapping.isEmpty()) {
                return;
            }
            for (String base : basePackages) {
                Resource[] resources = getResource(base, XML_RESOURCE_PATTERN);
                if (resources != null) {
                    for (int i = 0; i < resources.length; i++) {
                        String multi_key = getValue(resources[i]);
                        fileMapping.put(resources[i].getFilename(), multi_key);
                    }
                }
            }
        }

        /**
         * removeConfig
         * @param configuration
         * @throws NoSuchFieldException
         * @throws IllegalAccessException
         */
        private String getValue(Resource resource) throws IOException {
            String contentLength = String.valueOf((resource.contentLength()));
            String lastModified = String.valueOf((resource.lastModified()));
            return new StringBuilder(contentLength).append(lastModified).toString();
        }

        /**
         * removeConfig
         * @param configuration
         * @throws NoSuchFieldException
         * @throws IllegalAccessException
         */
        public boolean isChanged() throws IOException {
            boolean isChanged = false;
            for (String base : basePackages) {
                Resource[] resources = getResource(base, XML_RESOURCE_PATTERN);
                if (resources != null) {
                    for (int i = 0; i < resources.length; i++) {
                        String name = resources[i].getFilename();
                        String value = fileMapping.get(name);
                        String multi_key = getValue(resources[i]);
                        if (!multi_key.equals(value)) {
                            isChanged = true;
                            fileMapping.put(name, multi_key);
                        }
                    }
                }
            }
            return isChanged;
        }
    }

    @Override
    public void destroy() throws Exception {
        if (service != null) {
            service.shutdownNow();
        }
    }

}
