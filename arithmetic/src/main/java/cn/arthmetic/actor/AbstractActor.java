package cn.arthmetic.actor;

import java.util.concurrent.Executor;

/**
 * 抽象Actor
 *
 * @Auther: kangkang
 * @Date: 2021/11/29 21:33
 * @Description:
 */
public abstract class AbstractActor {

    /**
     * 邮箱
     */
    private MailBox mailBox;

    /**
     * 消息接受解析器
     */
    private Receive.ReceiveFunc receiveFunc;

    public AbstractActor(Executor executor) {
        this.mailBox = new MailBox(this.getClass(), executor);
        Receive receive = receive();
        receiveFunc = receive.getReceiveFunc();
    }

    abstract Receive receive();

    public void tell(Object msg) {
        mailBox.addTask(() -> processMsg(msg));
    }

    public void tell(Executor executor, Object msg) {
        mailBox.addTask(executor, () -> processMsg(msg));
    }

    private void processMsg(Object msg) {
        receiveFunc.receive(msg);
    }
}
