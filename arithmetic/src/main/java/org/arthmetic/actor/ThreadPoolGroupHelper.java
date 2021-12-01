package org.arthmetic.actor;

import java.util.Random;
import java.util.concurrent.Executor;
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
    private static Executor[] executors;

    static {
        executors = new Executor[8];
        for (int i = 0; i < executors.length; i++) {
            int finalI = i;
            executors[i] = Executors.newSingleThreadExecutor(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("单线程池:" + finalI);
                    return thread;
                }
            });
        }
    }

    public static Executor[] getExecutors() {
        return executors;
    }

    public static Executor randomExecutor() {
        int index = new Random(executors.length).nextInt(executors.length);
        return executors[index];
    }
}
