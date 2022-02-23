package com.pp.code_generator.out.service.bo;

import lombok.Data;

@Data
public class UserQueryInBO {

	private String[] ids;

    private String account;

    private String userName;

    private String passWord;

}