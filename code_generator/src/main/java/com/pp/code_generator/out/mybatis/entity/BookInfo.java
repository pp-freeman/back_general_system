package com.pp.code_generator.out.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

@Data
@TableName("BookInfo")
public class BookInfo {

    @TableId(type = IdType.AUTO)
    private Integer id;

    
    private String name;

    private Date createTime;

    private String createBy;

    private Date modifyTime;

    private String modifyBy;
}