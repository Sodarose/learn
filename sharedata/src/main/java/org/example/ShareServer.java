package org.example;


import java.util.Map;

public class ShareServer<SyncRpc, Data extends SyncRpc> {

    private RpcService rpcService;
    private RouteTable routeTable;
    /**
     * 数据细分Id
     */
    private long uid;

    private Data data;
    /**
     * 数据同步class
     */
    private Class<SyncRpc> syncRpcClass;
    /**
     * 同步端Rpc同步集合
     */
    private ObserverSupport<SyncRpc> syncClient;
    /**
     * 共享任务执行线程，保证client获取数据与共享数据更新队列保持一致
     */
    private TaskExecutor taskExecutor;

    private Map<ShareKey, Client> routeSyncClientMap;


    public void a(ShareKey shareKey, RpcResponseFuture<Object> future) {
        addSyncClient0(shareKey, future);
    }

    private void addSyncClient0(ShareKey shareKey, RpcResponseFuture<Object> future) {
        SyncRpc syncRpc = rpcService.create(syncRpcClass,
                shareKey.getSlaveRouteId(), shareKey.getDataUid());
        Client client = new Client(shareKey, syncRpc);7
        routeTable.addRouteListener(shareKey.getSlaveRouteId(), client);
        syncClient.attachForever(syncRpc);
        routeSyncClientMap.put(shareKey, client);
        if (future != null) {
            future.set(data);
        }
    }
}
