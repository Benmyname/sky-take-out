package com.sky.aspect;
/**
 * 自定义切面，用于实现公共字段的自动填充
 */

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;

import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;


@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
    * 切入点
    * */

    @Pointcut("execution(* com.sky.mapper.*.*(..))&&" +
            "@annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointcut(){}
    /**
    * 前置通知，在方法执行前执行
    * */
        @Before("autoFillPointcut()")
        public void autoFill(JoinPoint joinPoint){
         log.info("开始自动填充公共字段...");
         // 1.获取当前被拦截的方法上的数据库操作类型
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
            OperationType operationType = autoFill.value();


            //获取当前被拦截方法参数
            Object[] args = joinPoint.getArgs();
            if(args == null || args.length == 0){
                return;
            }
            // 2.获取当前被拦截方法的参数列表的第一个参数，实体类对象
            Object entity = args[0];


            //赋值数据
            LocalDateTime now = LocalDateTime.now();
            Long currentId = BaseContext.getCurrentId();


            //根据操作类型，为对应的字段通过反射来赋值
            if(operationType == OperationType.INSERT){
                //获取set方法
                try {

                    Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                    Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                    Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                    Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
//通过反射动态的调用set方法，为公共字段赋值
                    setCreateTime.invoke(entity, now);
                    setUpdateTime.invoke(entity, now);
                    setCreateUser.invoke(entity, currentId);
                    setUpdateUser.invoke(entity, currentId);
                } catch (Exception e) {
                    log.error("自动填充公共字段失败", e);
                }
            }else if(operationType == OperationType.UPDATE){
                //获取set方法
                try {
                    Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                    Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
//通过反射动态的调用set方法，为公共字段赋值
                    setUpdateTime.invoke(entity, now);
                    setUpdateUser.invoke(entity, currentId);
                } catch (Exception e) {
                    log.error("自动填充公共字段失败", e);
                }
            }



        }


    }




