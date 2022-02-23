package com.pp.code_generator.out.controller.dto;


import lombok.Data;

@Data
public class ${entityName?cap_first}CreateInDTO {

// TODO javax validation: @NotBlank | @NotNull

<#list entityFields as entityField>
    <#if entityField.fieldName??>
        <#if entityField.fieldName != "id" && entityField.fieldName != "accountId" >
    private ${entityField.fieldType} ${entityField.fieldName};

        </#if>
    </#if>
</#list>

}