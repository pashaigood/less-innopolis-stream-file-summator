package less.android.interfaces;

import less.android.factories.WordFileReader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class FileWordOperator extends Thread {
    private final String[] files;
    protected final ExecutorService poolExecutor;
    volatile private boolean isShutdown = false;

    public FileWordOperator(String[] files) {
        poolExecutor = Executors.newWorkStealingPool(10);
        this.files = files;
    }

    public boolean isShutdown() {
        return isShutdown;
    }

    public void shutdown() {
        poolExecutor.shutdownNow();
        this.isShutdown = true;
    }

    public abstract void addWord(String word);

    @Override
    public void run() {
        for (String file: files) {
            Thread child = new WordFileReader(file, this);
            poolExecutor.execute(child);
        }


        poolExecutor.shutdown();
        while (! poolExecutor.isTerminated()) {
            synchronized (poolExecutor) {
                try {
                    poolExecutor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        this.onFinish();
        System.out.println(getName() + " is processed files:");
        for (String file: files) {
            System.out.printf("     %s\n", file);
        }
    }

    protected void onFinish() {

    }
}
