package com.yunnex.ops.erp.modules.file.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 文件信息Entity
 * @author yunnex
 * @version 2017-12-16
 */
public class ErpFileInfo extends DataEntity<ErpFileInfo> {

    private static final long serialVersionUID = 1L;
    private String name; // 文件名
    private String type; // 文件类型/扩展名
    private Long size; // 文件大小
    private String path; // 文件路径



    public ErpFileInfo(String name, String type, Long size, String path) {
        super();
        this.name = name;
        this.type = type;
        this.size = size;
        this.path = path;
    }

    public ErpFileInfo() {
        super();
    }

    public ErpFileInfo(String id) {
        super(id);
    }

    @Length(min = 0, max = 512, message = "文件名长度必须介于 0 和 512 之间")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 0, max = 12, message = "文件类型/扩展名长度必须介于 0 和 12 之间")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Length(min = 0, max = 512, message = "文件路径长度必须介于 0 和 512 之间")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
