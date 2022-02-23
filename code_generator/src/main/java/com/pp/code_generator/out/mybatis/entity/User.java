package com.pp.code_generator.out.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

@Data
@TableName("User")
public class User {

    @TableId(type = IdType.AUTO)
    private Integer id;

    
    private String account;

    
    private String userName;

    
    private String passWord;

    private Date createTime;

    private String createBy;

    private Date modifyTime;

    private String modifyBy;
}