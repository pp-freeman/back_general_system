package com.pp.auth_server.aspect;

import com.alibaba.fastjson.JSON;
import com.pp.auth_server.anotation.Log;
import com.pp.auth_server.controller.ShiroController;
import com.pp.auth_server.domain.LogDO;
import com.pp.auth_server.domain.SysToken;
import com.pp.auth_server.domain.User;
import com.pp.auth_server.service.IShiroService;
import com.pp.auth_server.service.ISysLogService;
import com.pp.auth_server.utils.HttpContextUtil;
import com.pp.auth_server.utils.IPUtils;
import com.pp.auth_server.utils.TokenUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

/**
 * 日志切面
 *
 * @author pengpan
 * @date 2020/12/14
 */
@Aspect
@Order(1)
@Component
public class LogAspect {

    @Autowired
    IShiroService shiroService;
    @Autowired
    ISysLogService sysLogService;




    @Pointcut("@annotation(com.pp.auth_server.anotation.Log)")
    public void logPointCut(){

    }



    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable{
        long startTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长
        long time = System.currentTimeMillis() - startTime;

        saveLog(point,time,result);
        return result;
    }
    @AfterThrowing(pointcut = "logPointCut()", throwing = "e")
    public void saveExceptionLog(JoinPoint joinPoint, Throwable e) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        LogDO logDO = new LogDO();
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            logDO.setId(UUID.randomUUID().toString());
            logDO.setOperation("异常");
            //请求的方法名
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = signature.getName();
            String[] arr = className.split("\\.");
            className = arr[arr.length-1];
            logDO.setMethod(className+"."+methodName+"()");
            // 请求的参数
            String str = "";
            for(int i=0;i<joinPoint.getArgs().length;i++){
                if(joinPoint.getArgs()[i] instanceof ServletRequest || joinPoint.getArgs()[i] instanceof ServletResponse){
                    joinPoint.getArgs()[i] = "";
                }
            }
            if(methodName.equals("uploadDoc")){
                str = "Lorg.springframework.web.multipart.MultipartFile";
            }else{
                if(JSON.toJSONString(joinPoint.getArgs()).length()>80){
                    str = JSON.toJSONString(joinPoint.getArgs()).substring(0,80)+".....]";
                }else{
                    str = JSON.toJSONString(joinPoint.getArgs());
                }
            }
            logDO.setParams(str);
            //设置IP地址
            logDO.setIp(IPUtils.getIPAddress(request));
            //用户名
            User user = null;
            if(logDO.getOperation().equals("登录")){
                user = ShiroController.user;
            }else{
                String token = TokenUtils.getRequestToken((HttpServletRequest) request);

                SysToken tokenEntity = shiroService.findByToken(token);
                //2. token失效
                if (tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()) {
                    throw new IncorrectCredentialsException("token失效，请重新登录");
                }
                //3. 调用数据库的方法, 从数据库中查询 username 对应的用户记录
                user = shiroService.findByUserId(tokenEntity.getUserId());
            }

            if(user!=null){
                logDO.setUserId(user.getId());
                logDO.setName(user.getUsername());
            }
            logDO.setTime((int)10);
            //系统当前事情
            Date date = new Date();
            logDO.setGmtCreate(date);
            logDO.setResult(e.getMessage());
            sysLogService.saveLog(logDO);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


    void saveLog(ProceedingJoinPoint joinPoint,long time,Object object) throws InterruptedException{
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogDO sysLog = new LogDO();
        Log syslog = method.getAnnotation(Log.class);
        if(syslog != null){
            //注解上的描述
            sysLog.setOperation(syslog.value());
        }
        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        String[] arr = className.split("\\.");
        className = arr[arr.length-1];
        sysLog.setMethod(className+"."+methodName+"()");
        //请求的参数
        if(object != null){
            if(object.toString().contains("\"state\":200")||object.toString().contains("\"status\":200")||object.toString().contains("success")){
                sysLog.setResult("操作成功");
            }else{
                sysLog.setResult("操作失败");
            }
        }else{
            sysLog.setResult("操作成功");
        }

        String str = "";
        for(int i=0;i<joinPoint.getArgs().length;i++){
            if(joinPoint.getArgs()[i] instanceof ServletRequest || joinPoint.getArgs()[i] instanceof ServletResponse){
                joinPoint.getArgs()[i] = "";
            }
        }
        if(methodName.equals("uploadDoc")){
            str = "Lorg.springframework.web.multipart.MultipartFile";
        }else{
            if(JSON.toJSONString(joinPoint.getArgs()).length()>80){
                str = JSON.toJSONString(joinPoint.getArgs()).substring(0,80)+".....]";
            }else{
                str = JSON.toJSONString(joinPoint.getArgs());
            }
        }

        sysLog.setParams(str);
        //获取request
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        //设置IP地址
        sysLog.setIp(IPUtils.getIPAddress(request));
        //用户名
        User user = null;
        if(sysLog.getOperation().equals("登录")){
            user = ShiroController.user;
        }else{
            String token = TokenUtils.getRequestToken((HttpServletRequest) request);

            SysToken tokenEntity = shiroService.findByToken(token);
            //2. token失效
            if (tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()) {
                throw new IncorrectCredentialsException("token失效，请重新登录");
            }
            //3. 调用数据库的方法, 从数据库中查询 username 对应的用户记录
            user = shiroService.findByUserId(tokenEntity.getUserId());
        }

        if(user!=null){
            sysLog.setUserId(user.getId());
            sysLog.setName(user.getUsername());
        }
        sysLog.setTime((int) time);
        //系统当前事情
        Date date = new Date();
        sysLog.setGmtCreate(date);
        sysLog.setId(UUID.randomUUID().toString());
        sysLogService.saveLog(sysLog);
    }
}
