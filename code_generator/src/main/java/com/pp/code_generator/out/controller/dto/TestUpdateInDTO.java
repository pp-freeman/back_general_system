package com.pp.code_generator.out.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TestUpdateInDTO {

    @NotBlank
    private Integer id;

    private String birthday;


}