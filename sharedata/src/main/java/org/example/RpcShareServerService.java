package org.example;

import java.util.Map;

/**
 * @Auther: kangkang
 * @Date: 2021/10/20 12:02
 * @Description:
 */
public class RpcShareServerService implements RpcShareServer{

    @Override
    public RpcResponseFuture<Object> link(ShareKey shareKey, Object extra) {
        return null;
    }

    @Override
    public void unlink(ShareKey shareKey) {

    }
}
