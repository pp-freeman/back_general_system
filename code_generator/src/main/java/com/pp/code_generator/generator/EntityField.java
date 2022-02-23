package com.pp.code_generator.generator;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class EntityField implements Serializable {

    @NotBlank
    private String fieldType;

    @NotBlank
    private String fieldName;

    @NotBlank
    private String dbColumn;

    @NotBlank
    private String dbType;

    @NotBlank
    private String auto = "of";

    private String notNull = "off";

    private String comment;

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setDbColumn(String dbColumn) {
        this.dbColumn = dbColumn;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    public void setNotNull(String notNull) {
        this.notNull = notNull;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFieldType() {
        return fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getDbColumn() {
        return dbColumn;
    }

    public String getDbType() {
        return dbType;
    }

    public String getAuto() {
        return auto;
    }

    public String getNotNull() {
        return notNull;
    }

    public String getComment() {
        return comment;
    }
}
