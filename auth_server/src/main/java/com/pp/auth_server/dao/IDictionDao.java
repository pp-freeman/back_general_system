package com.pp.auth_server.dao;

import com.pp.auth_server.domain.Semester;
import org.springframework.stereotype.Repository;

@Repository
public interface IDictionDao {
    //添加字典
    public int addDic(Semester semester);
    //删除字典项
    public int deleteDiction(Semester semester);
    //得到修改字典项
    public Semester getUpdateDic(Semester semester);
    //修改字典项
    public int editDiction(Semester semester);
    //得到字典项
    public Semester getDictionByAll(Semester semester);
}
