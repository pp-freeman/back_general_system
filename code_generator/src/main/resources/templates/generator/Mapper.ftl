package com.pp.code_generator.out.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pp.code_generator.out.mybatis.entity.${entityName?cap_first};
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ${entityName?cap_first}Mapper extends BaseMapper<${entityName?cap_first}> {

}