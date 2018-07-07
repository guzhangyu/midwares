package com.unitymob.midware.mybatis;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ExecutorType;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Intercepts({@Signature(
        type = Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class}
)})
public class BatchInterceptor implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement)args[0];
        Object parameter = args[1];
        Executor executor = (Executor)invocation.getTarget();

        if(parameter instanceof Map){
            Map<String,Object> map=(Map<String,Object>)parameter;
            if(map.containsKey("list")){
                Method method = getMapperMethod(ms.getId());
                if(method!=null){
                    if(method.getAnnotation(Batch.class)!=null){
                        //spring的实现中session与executor绑定，在一个事务中只会有一次openSession
                        // 此处如果有了batch，会有新的executor,但是仍是同一个事务，详见 DefaultSessionFactory的openSessionFromDataSource
                        Executor batchExecutor=ms.getConfiguration().newExecutor(executor.getTransaction(),ExecutorType.BATCH);
                        List list=(List)map.get("list");
                        for(Object o:list){
                            batchExecutor.update(ms,o);
                        }
                        List<BatchResult> results=batchExecutor.flushStatements();

                        int total=0;
                        for(BatchResult batchResult:results){
                            int[] updatedCounts=batchResult.getUpdateCounts();
                            for(int updateCount:updatedCounts){
                                total+=updateCount;
                            }
                        }
                        return total;
                    }
                }
            }
        }

        return executor.update(ms,parameter);
    }

    /**
     * 获得mapper方法
     * @param msId
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     */
    private static Method getMapperMethod(String msId) throws ClassNotFoundException, NoSuchMethodException {
        int index=msId.lastIndexOf('.');
        Class mapper=Class.forName(msId.substring(0,index));
        return mapper.getDeclaredMethod(msId.substring(index+1),List.class);
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
