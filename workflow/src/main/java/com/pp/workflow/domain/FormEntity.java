package com.pp.workflow.domain;

import lombok.Data;

import java.util.Map;

@Data
public class FormEntity {
    TbEvection tbEvection;
    Map<String, Object> variables;
    String userId;
}
