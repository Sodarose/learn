package cn.arthmetic.actor;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 任务邮箱
 *
 * @Auther: kangkang
 * @Date: 2021/11/29 21:31
 * @Description:
 */
public class MailBox {

    public static Map<Class<AbstractActor>, MailBox> mailBoxMap = new ConcurrentHashMap<>();

    /**
     * 邮箱归属
     */
    private Class<? extends AbstractActor> actorClass;

    /**
     * 默认执行线程
     */
    private Executor defaultExecutor;

    /**
     * fifo 队列
     */
    private ConcurrentLinkedQueue<Task> taskQueue = new ConcurrentLinkedQueue<>();

    /**
     * 任务技术器
     */
    private AtomicInteger counter = new AtomicInteger(0);

    public MailBox(Class<? extends AbstractActor> actorClass, Executor executor) {
        this.actorClass = actorClass;
        this.defaultExecutor = executor;
    }

    public synchronized static MailBox createMailBox(Class<AbstractActor> actorClass, Executor executor) {
        if (mailBoxMap.containsKey(actorClass)) {
            return mailBoxMap.get(actorClass);
        }
        MailBox mailBox = new MailBox(actorClass, executor);
        mailBoxMap.put(actorClass, mailBox);
        return mailBox;
    }

    public static MailBox getMailBox(Class<AbstractActor> actorClass) {
        return mailBoxMap.get(actorClass);
    }

    public void addTask(Runnable runnable) {
        addTask(defaultExecutor, runnable);
    }

    public void addTask(Executor executor, Runnable runnable) {
        Task task = new Task(executor, runnable);
        taskQueue.add(task);
        int count = counter.incrementAndGet();
        if (count == 1) {
            run();
        }
    }

    private void run() {
        Task task = taskQueue.poll();
        assert task != null;
        task.executor.execute(task);
    }


    private class Task implements Runnable {

        private Executor executor;

        private Runnable runnable;

        public Task(Executor executor, Runnable runnable) {
            this.executor = executor;
            this.runnable = runnable;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                counter.decrementAndGet();
                if (counter.get() <= 0) {
                    break;
                }
                Task task = taskQueue.poll();
                assert task != null;
                if (task.executor == executor) {
                    executor.execute(task);
                } else {
                    task.executor.execute(task);
                }
            }
        }

        public Executor getExecutor() {
            return executor;
        }

        public Runnable getRunnable() {
            return runnable;
        }
    }
}
