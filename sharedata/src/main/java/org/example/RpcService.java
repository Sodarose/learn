package org.example;

import com.sun.corba.se.impl.orbutil.concurrent.Sync;

public class RpcService {
    public RpcShareServer getOrCreateWithNodeId(Class<RpcShareServer> rpcShareServerClass, int masterRouteId) {
        return null;
    }


    public <SyncRpc> SyncRpc create(Class<SyncRpc> syncRpcClass, int slaveRouteId, int dataUid) {
        return null;
    }
}
