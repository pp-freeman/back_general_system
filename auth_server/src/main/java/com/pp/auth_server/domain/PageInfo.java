package com.pp.auth_server.domain;

import lombok.Data;

@Data
public class PageInfo {
    private Integer pagenum;

    private Integer pagesize;

    private String userId;
}
