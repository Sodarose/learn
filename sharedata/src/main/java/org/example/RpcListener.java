package org.example;

/**
 * @Auther: kangkang
 * @Date: 2021/10/20 11:39
 * @Description:
 */
public interface RpcListener<R> {

    void onSuc(Object ret);

    void onError(Throwable e);

    TaskExecutor getExecutor();
}
