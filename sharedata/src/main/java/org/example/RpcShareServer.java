package org.example;

/**
 * @Auther: kangkang
 * @Date: 2021/10/20 11:30
 * @Description:
 */
public interface RpcShareServer {

    RpcResponseFuture<Object> link(ShareKey shareKey, Object extra);

    void unlink(ShareKey shareKey);
}
