package com.pp.code_generator.out.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pp.code_generator.out.mybatis.entity.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapper<Book> {

}