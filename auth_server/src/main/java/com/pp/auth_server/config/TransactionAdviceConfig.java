package com.pp.auth_server.config;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局事务配置
 *
 * @author pengpan
 * @date 2020/12/14
 */
@Aspect
@Order(2)
@Configuration
public class TransactionAdviceConfig {

    /**
     * 配置方法过期时间，默认-1,永不超时
     */
    private final static int TX_METHOD_TIME_OUT = 10;

    private static final String POITCUT_EXPRESSION = "execution(* com.pp.auth_server.controller.*.*(..))";
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public TransactionInterceptor txadvice() {
        /** 配置事务管理规则
         nameMap声明具备需要管理事务的方法名.
         这里使用public void addTransactionalMethod(String methodName, TransactionAttribute attr)
         */
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        Map<String, TransactionAttribute> nameMap = new HashMap<>(16);
        /*只读事物、不做更新删除等*/
        /*事务管理规则*/
        RuleBasedTransactionAttribute readOnlyRule = new RuleBasedTransactionAttribute();
        /*设置当前事务是否为只读事务，true为只读*/
        readOnlyRule.setReadOnly(false);
        /* transactiondefinition 定义事务的隔离级别；
         *  PROPAGATION_REQUIRED 如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中*/
        readOnlyRule.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        RuleBasedTransactionAttribute requireRule = new RuleBasedTransactionAttribute();
        /*抛出异常后执行切点回滚*/
        requireRule.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        /*PROPAGATION_REQUIRED:事务隔离性为1，若当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。这是默认值。 */
        requireRule.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        /*设置事务失效时间，超过10秒,可根据hytrix，则回滚事务*/
        requireRule.setTimeout(TX_METHOD_TIME_OUT);

        nameMap.put("*", requireRule);
        source.setNameMap(nameMap);
        return new TransactionInterceptor(platformTransactionManager, source);
    }

    /**
     * 设置切面=切点pointcut+通知TxAdvice
     *
     * @return
     */
    @Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(POITCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, txadvice());
    }
}