package org.example;

import com.sun.corba.se.impl.orbutil.concurrent.Sync;

public class ShareServer<SyncRpc, Data extends SyncRpc> {

    private RpcService rpcService;
    private RouteTable routeTable;
    /**
     * 数据细分Id
     */
    private long uid;
    /**
     * 数据同步class
     */
    private Class<Sync> syncClass;
    /**
     * 同步端Rpc同步集合
     */
    private ObserverSupport<SyncRpc> syncClient;
    /**
     * 共享任务执行线程，保证client获取数据与共享数据更新队列保持一致
     */
    private TaskExecutor taskExecutor;
}
