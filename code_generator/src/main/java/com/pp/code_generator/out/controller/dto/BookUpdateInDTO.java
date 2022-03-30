package com.pp.code_generator.out.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BookUpdateInDTO {

    @NotBlank
    private Integer id;

    private String name;


}