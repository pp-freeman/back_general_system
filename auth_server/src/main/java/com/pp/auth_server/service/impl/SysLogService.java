package com.pp.auth_server.service.impl;

import com.pp.auth_server.dao.ISysLogDao;
import com.pp.auth_server.domain.LogDO;
import com.pp.auth_server.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysLogService implements ISysLogService {

    @Autowired
    ISysLogDao sysLogDao;

    //保存系统日志
    public int saveLog(LogDO logDO){
        return sysLogDao.saveLog(logDO);
    }

    //得到日志列表
    public List<LogDO> getSysLogList(int pageStart, int pageSize, String userId, String operation){
        return sysLogDao.getSysLogList(pageStart,pageSize,userId,operation);
    }
    //得到日志列表总数
    public int getSysLogListCount(String userId,String operation){
        return sysLogDao.getSysLogListCount(userId,operation);
    }

    //通过id删除日志
    public int deleteLogById(LogDO logDO){
        return sysLogDao.deleteLogById(logDO);
    }
    //得到操作日志
    public List<LogDO> getOpSysLog(int pageStart, int pageSize, String userId, String operation){
        return sysLogDao.getOpSysLog(pageStart,pageSize,userId,operation);
    }
    //得到操作日志总数
    public int getOpSysLogCount(String userId,String operation){
        return sysLogDao.getOpSysLogCount(userId,operation);
    }
}
