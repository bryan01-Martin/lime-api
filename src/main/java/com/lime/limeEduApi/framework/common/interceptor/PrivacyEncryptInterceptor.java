package com.lime.limeEduApi.framework.common.interceptor;

import com.lime.limeEduApi.framework.common.domain.DefaultTableRow;
import com.lime.limeEduApi.framework.common.encrypt.AES256;
import com.lime.limeEduApi.framework.common.encrypt.EncryptField;
import com.lime.limeEduApi.framework.common.util.CommonUtil;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Intercepts
(
        {
                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
                ,@Signature(type = Executor.class, method = "query",  args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
                ,@Signature(type = Executor.class, method = "query",  args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
        }
)
public class PrivacyEncryptInterceptor implements Interceptor {

    public Logger log = LoggerFactory.getLogger(this.getClass());

    private EncryptField.AES256[] encryptFields = EncryptField.AES256.values();
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object[] args = invocation.getArgs();

        String queryMethod = invocation.getMethod().getName();
        String mapperId = ((MappedStatement) args[0]).getId();

        if (!CommonUtil.isNull(args[1])) {
            //encrypt -> parameter가 list형일 수도 있어 list형인 경우를 먼저 확인
            if(args[1] instanceof ArrayList){
                if(((ArrayList<?>) args[1]).size()>0){
                    ((ArrayList<?>) args[1]).stream().forEach(v -> {
                        try {
                            this.encryptObject(v);
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }else{
                this.encryptObject(args[1]);
            }
            if ("query".equals(queryMethod)) {

                // select 쿼리인 경우는 resultSet을 받아 추가 decrypt작업을 수행
                if (!mapperId.contains("selectKey")) {
                    Object resultSet = invocation.proceed();
                    if (!CommonUtil.isNull(resultSet)) {
                        if(resultSet instanceof ArrayList){
                            if(((ArrayList<?>) resultSet).size()>0){
                                ((ArrayList<?>) resultSet).stream().forEach(r -> {
                                    try {
                                        this.decryptObject(r);
                                    } catch (Throwable e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            }
                        }else{
                            this.decryptObject(resultSet);
                        }
                    }
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // NOP
    }

    private void encryptObject(Object param) {

        Class<?> cls = param.getClass();
        Method[] methods = cls.getMethods();
        List<String> getterNames = Arrays.stream(methods)
                                        .filter(m ->(m.getName().contains("get")))
                                        .map(m->m.getName()).collect(Collectors.toList());
        getterNames.stream().forEach(gn -> {

            try {
                Method method = cls.getMethod(gn);
                Object obj = method.invoke(param);
                if(obj instanceof  ArrayList){
                    ((ArrayList<?>)obj).stream().forEach(o -> this.encryptObject(o));
                }else if (obj instanceof DefaultTableRow){
                    this.encryptObject(obj);
                }

                // Object의 getter중 encryptFields에 해당 하는 것들만 암호화 처리
                Arrays.stream(encryptFields).filter(v-> gn.equals(new StringBuffer("get").append(v.name()).toString())).forEach(v -> {
                    try {
                        StringBuffer getMethodSb = new StringBuffer("get").append(v.name());
                        Method getMethod = cls.getMethod(getMethodSb.toString());
                        String flatStr = (String) getMethod.invoke(param);

                        StringBuffer setMethodSb = new StringBuffer("set").append(v.name());
                        Method setMethod = cls.getMethod(setMethodSb.toString(), String.class);
                        setMethod.invoke(param, AES256.encrypt(flatStr));
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

            }catch (Exception e){
//                log.info("암호화 로직 수행시 오류 발생");
            }
        });
    }

    private void decryptObject(Object param) {

        /* param = resultSet */

        Class<?> cls = param.getClass();
        Method[] methods = cls.getMethods();
        List<String> getterNames = Arrays.stream(methods)
                                    .filter(m ->(m.getName().contains("get")))
                                    .map(m->m.getName()).collect(Collectors.toList());
        getterNames.stream().forEach(gn -> {

            try {
                Method method = cls.getMethod(gn);
                Object obj = method.invoke(param);
                if(obj instanceof  ArrayList){
                    ((ArrayList<?>)obj).stream().forEach(o -> this.decryptObject(o));
                }else if(obj instanceof DefaultTableRow){
                    this.decryptObject(obj);
                }

                // Object의 getter중 encryptFields에 해당 하는 것들만 복호화 처리
                Arrays.stream(encryptFields).filter(v-> gn.equals(new StringBuffer("get").append(v.name()).toString())).forEach(v -> {
                    try {
                        StringBuffer getMethodSb = new StringBuffer("get").append(v.name());
                        Method getMethod = cls.getMethod(getMethodSb.toString());
                        String encStr = (String) getMethod.invoke(param);

                        StringBuffer setMethodSb = new StringBuffer("set").append(v.name());
                        Method setMethod = cls.getMethod(setMethodSb.toString(), String.class);
                        setMethod.invoke(param, AES256.decrypt(encStr));
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

            }catch (Exception e){
//                log.info("복호화 로직 수행시 오류 발생");
            }
        });
    }

}