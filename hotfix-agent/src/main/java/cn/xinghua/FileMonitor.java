package cn.xinghua;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author 26016
 * @description 文件监听器
 * @date 2023-12-15 14:53
 **/
public class FileMonitor {

    /**
     * 根路径
     */
    private final Path rootPath;

    /**
     * 需要监听事件组
     */
    private final WatchEvent.Kind<Path>[] eventKinds;

    /**
     * 新建回调
     */
    private final Consumer<File> onCreate;

    /**
     * 修改回调
     */
    private final Consumer<File> onModify;

    /**
     * 删除回调
     */
    private final Consumer<File> onDelete;

    /**
     * 监听服务
     */
    private final WatchService watchService;

    /**
     * 监控key->路径
     */
    private final Map<WatchKey, Path> keys;

    public FileMonitor(Path rootPath, WatchEvent.Kind<Path>[] eventKinds,
                       Consumer<File> onCreate, Consumer<File> onModify, Consumer<File> onDelete) {
        this.rootPath = rootPath;
        this.watchService = newWatchService(rootPath);
        this.eventKinds = eventKinds;
        this.onCreate = onCreate;
        this.onModify = onModify;
        this.onDelete = onDelete;
        this.keys = new HashMap<>();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void main(String[] args) throws Exception {
        WatchEvent.Kind[] eventKinds = new WatchEvent.Kind[]{
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE
        };
        Path path = Paths.get("C:\\Users\\26016\\IdeaProjects\\learn\\hotfix-agent\\src\\main\\resources\\script");
        FileMonitor fileMonitor = new FileMonitor(path,
                eventKinds,
                file -> System.err.printf("新增:%s\n", file.getName()),
                file -> System.err.printf("修改:%s\n", file.getName()),
                file -> System.err.printf("删除:%s\n", file.getName())
        );
        fileMonitor.start();
        Thread.sleep(100_0000);
    }

    /**
     * 启动
     */
    public void start() {
        walkAndRegisterDirectories(rootPath);
        Thread thread = new Thread(this::process);
        thread.setName("watch_file");
        thread.setDaemon(true);
        thread.start();
    }


    /**
     * 遍历起始路径其下的所有文件夹并且注册所有的文件监听
     *
     * @param start 起始路径
     */
    private void walkAndRegisterDirectories(final Path start) {
        try {
            Files.walkFileTree(start, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    registerDirectory(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 注册监听
     *
     * @param path 路径
     */
    private void registerDirectory(final Path path) {
        try {
            WatchKey watchKey = path.register(watchService, eventKinds);
            keys.put(watchKey, path);
            System.err.printf("监听路径:%s\n", path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 处理程序
     */
    @SuppressWarnings({"unchecked", "rawtypes", "BusyWait"})
    private void process() {
        while (!Thread.currentThread().isInterrupted()) {
            WatchKey key = null;
            try {
                key = watchService.take();
                Path watchPath = keys.get(key);
                if (watchPath == null) {
                    continue;
                }
                // 等待3s方便以便处理更多的事件
                Thread.sleep(3_000);
                List watchEvents = key.pollEvents();
                handleWatchEvents(watchPath, watchEvents);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                if (key != null) {
                    key.reset();
                    Path watchPath = keys.get(key);
                    if (!Files.exists(watchPath)) {
                        keys.remove(key);
                    }
                }
            }
        }
    }

    /**
     * 处理事件
     *
     * @param watchPath   监听路径
     * @param watchEvents 监听事件
     */
    protected void handleWatchEvents(final Path watchPath, final List<WatchEvent<Path>> watchEvents) {
        for (WatchEvent<Path> watchEven : watchEvents) {
            System.err.println(watchEven.context().toFile());
            Path target = watchPath.resolve(watchEven.context());
            File file = target.toFile();
            if (file.isFile()) {
                onEvent(watchEven, file);
            }
            if (file.isDirectory() && watchEven.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                walkAndRegisterDirectories(target);
            }
        }
    }

    /**
     * 处理单个事件
     *
     * @param event 监听事件
     * @param file  文件
     */
    protected void onEvent(final WatchEvent<Path> event, final File file) {
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


    /**
     * 新建一个监听服务
     *
     * @param rootPath 路径
     */
    private WatchService newWatchService(final Path rootPath) {
        try {
            return rootPath.getFileSystem().newWatchService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
