package com.pp.auth_server.service;

import com.pp.auth_server.domain.LogDO;

import java.util.List;

public interface ISysLogService {
    //保存系统日志
    public int saveLog(LogDO logDO);
    //得到日志列表
    public List<LogDO> getSysLogList(int pageStart, int pageSize, String userId, String operation);
    //得到日志列表总数
    public int getSysLogListCount(String userId, String operation);
    //通过id删除日志
    public int deleteLogById(LogDO logDO);
    //得到操作日志
    public List<LogDO> getOpSysLog(int pageStart, int pageSize, String userId, String operation);
    //得到操作日志总数
    public int getOpSysLogCount(String userId, String operation);
}
