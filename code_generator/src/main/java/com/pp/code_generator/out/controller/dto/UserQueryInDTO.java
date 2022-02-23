package com.pp.code_generator.out.controller.dto;


import lombok.Data;

@Data
public class UserQueryInDTO {

	private String[] ids;

    private String account;

    private String userName;

    private String passWord;

}