package com.pp.auth_server.service;

import com.pp.auth_server.domain.Menu;

import java.util.List;

public interface IMenuService {
    public List<Menu> findMenu(String id);

    public List<String> getButtonByUser(String id);
}
