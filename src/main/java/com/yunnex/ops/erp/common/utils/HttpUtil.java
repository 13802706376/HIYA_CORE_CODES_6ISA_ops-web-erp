package com.yunnex.ops.erp.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.IgnoreSpecFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * HTTP工具类
 * 
 * @author Huanghaidong
 * 
 */
public class HttpUtil {
    private static final Log LOGGER = LogFactory.getLog(HttpUtil.class);
    private static final int MAX_REQ_TIME = 2000;
    public static final String CONTENT_TYPE_AJ = "application/json";

    
    private HttpUtil() {
      }
    
    /**
     * 发送HTTP GET方式请求，返回请求响应中的字符串，不适应需要post返回响应数据的请求场景
     * 
     * @param reqUrl
     * @return
     */
    public static String sendHttpGetReqUrlToServer(String reqUrl) {
        if (StringUtils.isEmpty(reqUrl))
            return null;
        try {
            URL getUrl = new URL(reqUrl);
            // 根据拼凑的URL，打开连接，URL.openConnection()函数会根据
            // URL的类型，返回不同的URLConnection子类的对象，在这里我们的URL是一个http，因此它实际上返回的是HttpURLConnection
            HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
            // 建立与服务器的连接，并未发送数据
            connection.connect();
            // 发送数据到服务器并使用Reader读取返回的数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String lines;
            while ((lines = reader.readLine()) != null) {
                sb.append(lines);
            }
            reader.close();
            // 断开连接
            connection.disconnect();
            return sb.toString();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 发送HTTP GET方式请求，返回请求响应中的字符串，不适应需要post返回响应数据的请求场景
     * 
     * @param reqUrl
     * @return
     */
    public static String sendHttpGetReqToServer(String reqUrl) {
        if (StringUtils.isEmpty(reqUrl))
            return null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(reqUrl);
            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            try {
                if (null != response1 && null != response1.getEntity()) {
                    return EntityUtils.toString(response1.getEntity(), StandardCharsets.UTF_8.toString());
                }
            } catch (ParseException e) {
                LOGGER.error("向Http服务端发送请求时发生异常，原因：", e);
            } finally {
                if (response1 != null)
                    response1.close();
            }
        } catch (IOException e) {
            LOGGER.error("向Http服务端发送请求时发生异常，原因：", e);
        } finally {
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    LOGGER.error("关闭httpclient时发生异常，原因：", e);
                }
            }
        }
    
        return null;
    }

    /**
     * 发送HTTP GET方式请求，返回请求响应中的字符串，不适应需要post返回响应数据的请求场景
     * 
     * @param reqUrl
     * @return
     */
    public static String sendHttpGetReqToServer4CookiePolicy(String reqUrl, String cookieSpec) {
        if (StringUtils.isEmpty(reqUrl))
            return null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            RequestConfig config = RequestConfig.custom().setCookieSpec(cookieSpec).build();
            HttpGet httpGet = new HttpGet(reqUrl);
            httpGet.setConfig(config);
            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            try {
                if (null != response1 && null != response1.getEntity()) {
                    return EntityUtils.toString(response1.getEntity(), StandardCharsets.UTF_8);
                }
            } catch (ParseException e) {
                LOGGER.error("向Http服务端发送请求时发生异常，原因：", e);
            } finally {
                if (response1 != null)
                    response1.close();
            }
        } catch (IOException e) {
            LOGGER.error("向Http服务端发送请求时发生异常，原因：", e);
        } finally {
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    LOGGER.error("关闭httpclient时发生异常，原因：", e);
                }
            }
        }
    
        return null;
    }

    /**
     * 高性能方式发送请求到服务器
     * 
     * @param reqUrl
     * @return
     */
    public static String sendHttpGetReqToServerByHighPerformance(String reqUrl) {
        try {
            Content result = Request.Get(reqUrl).execute().returnContent();
            if (null == result)
                return null;
            return result.asString();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 高性能方式发送请求到服务器，同时限制在2秒之内必须返回与响应
     * 
     * @param reqUrl
     * @return
     */
    public static String sendHttpGetReqToServerByHighPerformanceAndTimeout(String reqUrl) {
        try {
            Content result = Request.Get(reqUrl).socketTimeout(MAX_REQ_TIME).connectTimeout(MAX_REQ_TIME).execute().returnContent();
            if (null == result)
                return null;
            return result.asString();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 高性能方式发送请求到服务器
     * 
     * @param reqUrl
     * @return
     */
    public static String sendHttpGetReqToServerByCustomCookie(String reqUrl) {
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(MAX_REQ_TIME).setSocketTimeout(MAX_REQ_TIME).setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();

        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieSpecRegistry(RegistryBuilder.<CookieSpecProvider> create().register(CookieSpecs.IGNORE_COOKIES, new IgnoreSpecFactory()).build())
                .setDefaultRequestConfig(requestConfig).build();

        HttpGet httpGet = new HttpGet(reqUrl);
        CloseableHttpResponse response1 = null;
        try {
            response1 = httpclient.execute(httpGet);
            if (null != response1 && null != response1.getEntity()) {
                return EntityUtils.toString(response1.getEntity(), StandardCharsets.UTF_8);
            }
        } catch (ParseException e) {
            LOGGER.error("向Http服务端发送请求时发生异常，原因：", e);
        } catch (ClientProtocolException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                if (response1 != null)
                    response1.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return null;

    }

    /**
     * 发送HTTP post方式请求
     * 
     * @param httpServerUrl 请求地址
     * @param map 携带的参数值
     * @return
     */
    public static String sendHttpPostReqToServerByParams(String httpServerUrl, Map<String, String> map) {
        if (StringUtils.isEmpty(httpServerUrl))
            return null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String result = null;
        String status = null;
        try {
            HttpPost httpPost = new HttpPost(httpServerUrl);
            if (null != map && !map.isEmpty()) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            }
            CloseableHttpResponse response1 = httpclient.execute(httpPost);
            try {
                if (null != response1) {
                    if (null != response1.getStatusLine())
                        status = response1.getStatusLine().toString();
                    if (null != response1.getEntity())
                        result = EntityUtils.toString(response1.getEntity(), StandardCharsets.UTF_8);
                }
            } catch (ParseException e) {
                LOGGER.error("向Http服务端发送请求时发生异常，原因：", e);
            } finally {
                if (response1 != null)
                    response1.close();
            }
        } catch (IOException e) {
            LOGGER.error("向Http服务端发送请求时发生异常，原因：", e);
        } finally {
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    LOGGER.error("关闭httpclient时发生异常，原因：", e);
                }
            }
        }
        LOGGER.info(String.format("向Http服务端发送请求，请求地址：%s，请求返回状态：%s，请求返回报文：%s", httpServerUrl, status, result));
        return result;
    }

    /**
     * 发送HTTP post方式请求
     * 
     * @param httpServerUrl 请求地址
     * @param reqBody 请求的body字符串
     * @return
     */
    public static String sendHttpPostReqToServerByReqbody(String httpServerUrl, String reqBody, String contentType) {
        if (StringUtils.isEmpty(httpServerUrl))
            return null;
        if (StringUtils.isEmpty(contentType))
            contentType = "application/x-www-form-urlencoded";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String result = null;
        String status = null;
        try {
            HttpPost httpPost = new HttpPost(httpServerUrl);
            if (StringUtils.isNotEmpty(reqBody)) {
                StringEntity reqBodyEntity = new StringEntity(reqBody, StandardCharsets.UTF_8);
                reqBodyEntity.setContentType(contentType);
                httpPost.setEntity(reqBodyEntity);
            }
            CloseableHttpResponse response1 = httpclient.execute(httpPost);
            try {
                if (null != response1) {
                    if (null != response1.getStatusLine())
                        status = response1.getStatusLine().toString();
                    if (null != response1.getEntity())
                        result = EntityUtils.toString(response1.getEntity(), StandardCharsets.UTF_8);
                }
            } catch (ParseException e) {
                LOGGER.error("向Http服务端发送请求时发生异常，原因：", e);
            } finally {
                if (response1 != null)
                    response1.close();
            }
        } catch (IOException e) {
            LOGGER.error("向Http服务端发送请求时发生异常，原因：", e);
        } finally {
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    LOGGER.error("关闭httpclient时发生异常，原因：", e);
                }
            }
        }
        LOGGER.info(String.format("向Http服务端发送请求，请求地址：%s，请求报文：%s，请求返回状态：%s，请求返回报文：%s", httpServerUrl, reqBody, status, result));
        return result;
    }

}
