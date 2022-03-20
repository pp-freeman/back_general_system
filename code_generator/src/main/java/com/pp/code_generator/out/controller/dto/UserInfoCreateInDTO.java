package com.pp.code_generator.out.controller.dto;


import lombok.Data;

@Data
public class UserInfoCreateInDTO {

// TODO javax validation: @NotBlank | @NotNull

    private String account;

    private String name;


}