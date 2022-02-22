package com.pp.auth_server.dao;

import com.pp.auth_server.domain.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMenuDao {
    public List<Menu> findMenus(String id);

    public List<String> getButtonByUser(String id);
}
