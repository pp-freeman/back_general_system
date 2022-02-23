package com.pp.code_generator.out.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserUpdateInDTO {

    @NotBlank
    private Integer id;

    private String account;

    private String userName;

    private String passWord;


}