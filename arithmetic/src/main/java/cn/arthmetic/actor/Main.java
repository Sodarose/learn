package cn.arthmetic.actor;

import java.util.Random;
import java.util.concurrent.Executor;

/**
 * 主测试程序
 *
 * @Auther: kangkang
 * @Date: 2021/11/29 21:53
 * @Description:
 */
public class Main {

    private static Random random = new Random();

    public static void main(String[] args) {
        testOne();
    }

    private static void testOne() {
        // 随机一个线程作为Actor的任务执行线程
        Executor executor = ThreadPoolGroupHelper.randomExecutor();
        ActorA actorA = new ActorA(executor);
        // 调用所有线程向actorA提交任务
        for (int i = 0; i < 1000; i++) {
            Object msg = randomMsg();
            actorA.tell(msg);
        }
    }

    private static Object randomMsg() {
        int index = random.nextInt(100);
        if (index % 2 == 0) {
            return new ActorA.EventA();
        } else {
            return new ActorA.EventA2();
        }
    }
}
