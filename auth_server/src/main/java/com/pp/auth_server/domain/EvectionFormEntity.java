package com.pp.auth_server.domain;

import lombok.Data;

import java.util.Map;

@Data
public class EvectionFormEntity {
    TbEvection tbEvection;
    Map<String, Object> variables;
    String userId;
}
