package cn.xinghua;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author 26016
 * @description 热更文件监控
 * @date 2023-12-15 12:19
 **/
public class ScriptFileMonitor extends FileMonitor {

    /**
     * 回调
     */
    private Consumer<Set<File>> action;

    public ScriptFileMonitor(Path rootPath, Consumer<Set<File>> action) {
        //noinspection unchecked
        super(rootPath, new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY}, null, null, null);
        this.action = action;
    }

    @Override
    protected void handleWatchEvents(Path watchPath, List<WatchEvent<Path>> watchEvents) {
        super.handleWatchEvents(watchPath, watchEvents);
        Set<File> files = watchEvents.stream()
                .filter(pathWatchEvent -> pathWatchEvent.kind() == StandardWatchEventKinds.ENTRY_CREATE || pathWatchEvent.kind() == StandardWatchEventKinds.ENTRY_MODIFY)
                .map(pathWatchEvent -> watchPath.resolve(pathWatchEvent.context()).toFile()).collect(Collectors.toSet());
        action.accept(files);
    }

    @Override
    protected void onEvent(WatchEvent<Path> event, File file) {

    }


}
