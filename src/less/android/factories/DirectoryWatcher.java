package less.android.factories;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.function.Consumer;

public class DirectoryWatcher {
    private static HashMap<String, DirectoryWatcher> diretoryUnderWatch = new HashMap<>();
    private Consumer onChange;

    public static DirectoryWatcher getWatcher(String directory, Consumer onChange) {
        System.out.println("===================>");
        if (diretoryUnderWatch.containsKey(directory)) {
            System.out.println("exists");
            return diretoryUnderWatch.get(directory);
        } else {
            System.out.println("not exists");
            DirectoryWatcher directoryWatcher = new DirectoryWatcher(directory, onChange);
            diretoryUnderWatch.put(directory, directoryWatcher);
            return directoryWatcher;
        }
    }

    public DirectoryWatcher(String directory, Consumer onChange) {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path filePath = Paths.get(directory);

            filePath.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_MODIFY
            );

            WatchKey watchKey;

            while ((watchKey = watchService.take()) != null) {
                for (WatchEvent<?> event: watchKey.pollEvents()) {
                    onChange.accept(event);
                }
                watchKey.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
