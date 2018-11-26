package com.yunnex.ops.erp.common.utils.excel.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MapperCell {

    /**
     * 在excel文件中某列数据的名称
     *
     * @return 名称
     */
    String cellName() default "";

    /**
     * 在excel中列的顺序，从小到大排
     *
     * @return 顺序
     */
    int order() default 0;

    /**
     * 分组,按组导出
     * 
     * @return
     */
    String[] groups() default {};

}
