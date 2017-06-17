package less.android;

import less.android.factories.DirectoryWatcher;
import less.android.factories.Summator;

public class Main {
    private static Summator summator;

    public static void main(String[] args) {
        startSummator();
        watch();
    }

    private static void startSummator() {
        if (summator != null) {
            summator.shutdown();
        }

        summator = new Summator(new String[] {
                "./resources/set-1.txt",
                "./resources/set-2.txt",
                "./resources/set-3.txt",
        });
        summator.setName("Summator");
        summator.start();
    }

    private static void watch() {
        DirectoryWatcher.watch("./resources", (e) -> {
            startSummator();
        });
    }
}
