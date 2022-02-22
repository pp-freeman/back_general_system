package com.pp.auth_server.service.impl;

import com.pp.auth_server.dao.IDictionDao;
import com.pp.auth_server.domain.Semester;
import com.pp.auth_server.service.IDictionService;
import com.pp.auth_server.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DictionService implements IDictionService {

    @Autowired
    IDictionDao dictionDao;

    @Autowired
    RedisUtil redisUtil;
    //添加字典
    public int addDic(Semester semester){
        redisUtil.flushDiction();
        return dictionDao.addDic(semester);
    }

    //删除字典项
    public int deleteDiction(Semester semester){
        redisUtil.flushDiction();
        return dictionDao.deleteDiction(semester);
    }
    //得到修改字典项
    public Semester getUpdateDic(Semester semester){
        return dictionDao.getUpdateDic(semester);
    }
    //修改字典项
    public int editDiction(Semester semester){
        redisUtil.flushDiction();
        return dictionDao.editDiction(semester);
    }
    //得到字典项
    public Semester getDictionByAll(Semester semester){
        return dictionDao.getDictionByAll(semester);
    }
}
