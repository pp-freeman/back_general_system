package com.pp.code_generator.out.controller.dto;


import lombok.Data;

@Data
public class UserCreateInDTO {

// TODO javax validation: @NotBlank | @NotNull

    private String account;

    private String userName;

    private String passWord;


}