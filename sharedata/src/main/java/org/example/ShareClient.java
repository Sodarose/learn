package org.example;


public class ShareClient<SyncRpc, Data extends SyncRpc> {

    private RouteTable routeTable;
    private RpcService rpcService;
    private RpcServiceFactory rpcServiceFactory;

    /**
     * 数据同步class
     */
    private Class<SyncRpc> syncClass;
    /**
     * 数据细分唯一Id
     */
    private ShareKey shareKey;
    /**
     * 本地共享数据
     */
    private Data data;
    /**
     * 数据更新线程
     */
    private TaskExecutor updateThread;
    /**
     * 额外数据
     */
    private Object extra;


    public void link2Remote() {
        RpcShareServer rpcShareServer = rpcService.getOrCreateWithNodeId(RpcShareServer.class, shareKey.getMasterRouteId());
        RpcResponseFuture<Object> linkFuture = rpcShareServer.link(shareKey, extra);
        linkFuture.addListener(new RpcListener<Object>() {
            @Override
            public void onSuc(Object ret) {
                Data oldData = data;
                ShareClient.this.data = (Data) ret;
                rpcServiceFactory.regService(syncClass,shareKey.getShareUid(),ShareClient.this.data,updateThread);
                // ......
            }

            @Override
            public void onError(Throwable e) {
                // 异常处理
            }

            @Override
            public TaskExecutor getExecutor() {
                return TaskExecutor.CUR_THREAD;
            }
        });
    }
}
