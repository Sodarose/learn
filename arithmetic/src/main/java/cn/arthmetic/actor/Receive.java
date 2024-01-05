package cn.arthmetic.actor;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 消息接受执行器
 *
 * @Auther: kangkang
 * @Date: 2021/11/29 18:19
 * @Description:
 */
public class Receive {

    private ReceiveFunc<Object> receiveFunc;

    public static class Builder {
        private ReceiveFunc<Object> statement = null;

        private void addStatement(Predicate<Object> predicate, Consumer<Object> consumer) {
            ReceiveFunc<Object> temp = o -> {
                if (predicate.test(o)) {
                    consumer.accept(o);
                    return true;
                }
                return false;
            };
            if (this.statement == null) {
                this.statement = temp;
            } else {
                statement = statement.orElse(temp);
            }
        }

        public Builder match(Class<?> objectClass, Consumer<Object> consumer) {
            addStatement(o -> o.getClass() == objectClass, consumer);
            return this;
        }

        public Receive build() {
            Receive receive = new Receive();
            receive.receiveFunc = this.statement;
            return receive;
        }
    }

    interface ReceiveFunc<Msg> {

        boolean receive(Msg msg);

        /**
         * 以匿名函数类的方式组成类似链表结构
         */
        default ReceiveFunc<Msg> orElse(ReceiveFunc<Msg> after) {
            return msg -> {
                if (receive(msg)) {
                    return true;
                } else {
                    return after.receive(msg);
                }
            };
        }
    }

    public ReceiveFunc<Object> getReceiveFunc() {
        return receiveFunc;
    }
}
