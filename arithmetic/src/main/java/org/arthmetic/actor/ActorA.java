package org.arthmetic.actor;

import java.util.concurrent.Executor;

/**
 * @Auther: kangkang
 * @Date: 2021/11/29 21:54
 * @Description:
 */
public class ActorA extends AbstractActor {

    public ActorA(Executor executor) {
        super(executor);
    }

    @Override
    Receive receive() {
        return new Receive.Builder()
                .match(EventA.class, o -> onEventA((EventA) o))
                .match(EventA2.class, o -> onEventA2((EventA2) o))
                .build();
    }

    private void onEventA(EventA eventA) {
        System.err.println("Thread:" + Thread.currentThread().getName() + "\tProcess:" + eventA.msg);
    }

    private void onEventA2(EventA2 eventA2) {
        System.err.println("Thread:" + Thread.currentThread().getName() + "\tProcess:" + eventA2.msg);
    }

    static class EventA {

        private String msg = "EventA";

        public String getMsg() {
            return msg;
        }
    }

    static class EventA2 {

        private String msg = "EventB";

        public String getMsg() {
            return msg;
        }
    }
}
