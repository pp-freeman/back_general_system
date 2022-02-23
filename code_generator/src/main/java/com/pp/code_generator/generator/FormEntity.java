package com.pp.code_generator.generator;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
public class FormEntity implements Serializable {

    private Integer initFlag;

    @NotBlank
    private String tableName;

    @NotBlank
    private String entityName;

    @NotNull
    @Size(min = 1)
    List<EntityField> entityFields;

    public void setInitFlag(Integer initFlag) {
        this.initFlag = initFlag;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public void setEntityFields(List<EntityField> entityFields) {
        this.entityFields = entityFields;
    }

    public Integer getInitFlag() {
        return initFlag;
    }

    public String getTableName() {
        return tableName;
    }

    public String getEntityName() {
        return entityName;
    }

    public List<EntityField> getEntityFields() {
        return entityFields;
    }
}
