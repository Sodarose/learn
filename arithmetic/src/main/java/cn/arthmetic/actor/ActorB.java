package cn.arthmetic.actor;

import java.util.concurrent.Executor;

/**
 * @Auther: kangkang
 * @Date: 2021/11/29 21:54
 * @Description:
 */
public class ActorB extends AbstractActor{

    public ActorB(Executor executor) {
        super(executor);
    }

    @Override
    Receive receive() {
        return null;
    }
}
