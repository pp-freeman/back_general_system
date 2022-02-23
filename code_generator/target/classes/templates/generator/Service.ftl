package com.pp.code_generator.out.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pp.code_generator.out.mybatis.entity.${entityName?cap_first};
import com.pp.code_generator.out.service.bo.${entityName?cap_first}CreateInBO;
import com.pp.code_generator.out.service.bo.${entityName?cap_first}QueryInBO;
import com.pp.code_generator.out.service.bo.${entityName?cap_first}UpdateInBO;
import org.apache.ibatis.javassist.NotFoundException;

public interface ${entityName?cap_first}Service {

    ${entityName?cap_first} loadById(String id);

    ${entityName?cap_first} create(${entityName?cap_first}CreateInBO createArgs);

    void update(${entityName?cap_first}UpdateInBO updateArgs) throws NotFoundException;

    void delete(String id) throws NotFoundException;

    IPage<${entityName?cap_first}> pageQuery(${entityName?cap_first}QueryInBO args, int page, int size);
}