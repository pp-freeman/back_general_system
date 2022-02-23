package com.pp.code_generator.out.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

@Data
@TableName("${tableName}")
public class ${entityName?cap_first} {

<#list entityFields as entityField>
    <#if entityField.fieldName??>
    <#if entityField.fieldName == "id">@TableId(type = IdType.AUTO)</#if>
    private ${entityField.fieldType} ${entityField.fieldName};

    </#if>
</#list>
    private Date createTime;

    private String createBy;

    private Date modifyTime;

    private String modifyBy;
}