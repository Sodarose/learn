package cn.xinghua.agent;

import cn.xinghua.ScriptHotLoadWatch;

/**
 * @author 26016
 * @description
 * @date 2023-12-15 11:54
 **/
public class AgentMain {


    public static void main(String[] args) throws Exception {
        // 启动监听
        ScriptHotLoadWatch.start("C:\\Users\\26016\\IdeaProjects\\learn\\hotfix-agent\\target\\hotfix-agent-1.0-SNAPSHOT.jar", "C:\\Users\\26016\\IdeaProjects\\learn\\hotfix\\src\\main\\resources\\script");
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(5_000);
                    TestA.print();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(1000_000);
    }

}
