package com.pp.code_generator.out.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ${entityName?cap_first}UpdateInDTO {

<#list entityFields as entityField>
    <#if entityField.fieldName??>
        <#if entityField.fieldName != "accountId">
            <#if entityField.fieldName == "id">
    @NotBlank
            </#if>
    private ${entityField.fieldType} ${entityField.fieldName};

        </#if>
    </#if>
</#list>

}