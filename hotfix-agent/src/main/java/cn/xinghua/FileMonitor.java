package cn.xinghua;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author 26016
 * @description
 * @date 2023-12-15 14:53
 **/
public class FileMonitor {

    /**
     * 根文件or文件夹
     */
    private final File root;

    /**
     * 根路径
     */
    private final Path rootPath;

    /**
     * 搜索深度
     */
    private final int maxDepth;

    /**
     * 需要监听事件组
     */
    private final WatchEvent.Kind<Path>[] eventKinds;

    /**
     * 新建
     */
    private final Consumer<File> onCreate;

    /**
     * 修改
     */
    private final Consumer<File> onModify;

    /**
     * 删除
     */
    private final Consumer<File> onDelete;

    /**
     * 监听服务
     */
    private final WatchService watchService;

    public FileMonitor(File root, Path rootPath, int maxDepth, WatchEvent.Kind<Path>[] eventKinds,
                       Consumer<File> onCreate, Consumer<File> onModify, Consumer<File> onDelete) {
        this.root = root;
        this.rootPath = rootPath;
        this.maxDepth = maxDepth;
        this.watchService = newWatchService(rootPath);
        this.eventKinds = eventKinds;
        this.onCreate = onCreate;
        this.onModify = onModify;
        this.onDelete = onDelete;
    }

    public static void main(String[] args) throws Exception {
        Path path = Paths.get("C:\\Users\\26016\\IdeaProjects\\learn\\hotfix-agent\\src\\main\\resources\\script");
        File file = path.toFile();
        Collection<Path> files = Files.walk(path, 1).collect(Collectors.toSet());
        /*WatchEvent.Kind<Path>[] eventKinds = new WatchEvent.Kind[]{
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE
        };
        Path path = Paths.get("C:\\Users\\26016\\IdeaProjects\\learn\\hotfix-agent\\src\\main\\resources\\script");
        FileMonitor fileMonitor = new FileMonitor(path.toFile(), path, Integer.MAX_VALUE,
                eventKinds, file -> System.err.printf("新增:%s\n", file.getName()), file -> System.err.printf("修改:%s\n", file.getName()), file -> System.err.printf("删除:%s\n", file.getName()));
        fileMonitor.start();
        Thread.sleep(1000_0000);
        System.err.println(path.getFileName());*/
    }

    public void start() {
        openWatch();
        Thread thread = new Thread(this::process);
        thread.setName("watch_file");
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * 开启监听
     */
    private void openWatch() {
        registerPathUnCheck(rootPath);
    }

    /**
     * 处理文件
     */
    private void process() {
        while (!Thread.currentThread().isInterrupted()) {
            WatchKey key = null;
            try {
                // 轮询获取文件修改事件，等待3s方便以便处理更多的事件
                key = watchService.take();
                Thread.sleep(3_000);
                List watchEvents = key.pollEvents();
                handleWatchEvents(rootPath, watchEvents);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                if (key != null) {
                    key.reset();
                }
            }
        }
    }

    /**
     * 处理事件
     *
     * @param rootPath    监听根路径
     * @param watchEvents 监听事件
     */
    private void handleWatchEvents(Path rootPath, List<WatchEvent<Path>> watchEvents) {
        for (WatchEvent<Path> watchEven : watchEvents) {
            Path path = rootPath.resolve(watchEven.context());
            File file = path.toFile();
            onEvent(watchEven, file);
        }
    }

    /**
     * 处理单个事件
     *
     * @param event 监听事件
     * @param file  文件
     */
    private void onEvent(WatchEvent<Path> event, File file) {
        if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
            onCreate.accept(file);
        } else if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
            onModify.accept(file);
        } else if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
            onDelete.accept(file);
        } else {
            throw new RuntimeException(String.format("不支持的事件类型:%s", event.kind().name()));
        }
    }

    private void registerPathUnCheck(Path path) {
        try {
            path.register(watchService, eventKinds);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 新建一个监听服务
     *
     * @param rootPath 路径
     */
    private WatchService newWatchService(Path rootPath) {
        try {
            return rootPath.getFileSystem().newWatchService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
