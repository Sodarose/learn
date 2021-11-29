package org.arthmetic.actor;

import io.netty.channel.SingleThreadEventLoop;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import sun.management.snmp.util.JvmContextFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 线程组
 *
 * @Auther: kangkang
 * @Date: 2021/11/29 21:39
 * @Description:
 */
public class ThreadPoolGroupHelper {

    private static int threadGroupSize = 8;

    /**
     * 测试用线程池
     */
    private static ExecutorService[] executorGroups;

    static {
        executorGroups = new ExecutorService[8];
        for (int i = 0; i < executorGroups.length; i++) {
            int finalI = i;
            executorGroups[i] = Executors.newSingleThreadExecutor(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("单线程池" + finalI);
                    return thread;
                }
            });
        }
    }
}
