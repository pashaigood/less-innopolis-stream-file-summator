package less.android.interfaces;

import less.android.factories.WordFileReader;
import java.util.Arrays;

public abstract class FileWordOperator extends Thread {
    private final String[] files;
    volatile private boolean isShutdown = false;
    private WordFileReader[] wordFileReaders;

    public FileWordOperator(String[] files) {
        this.files = files;
    }

    public boolean isShutdown() {
        return isShutdown;
    }

    public void shutdown() {
        Arrays.stream(wordFileReaders).forEach(WordFileReader::close);
        isShutdown = true;
    }

    public abstract void addWord(String word);

    @Override
    public void run() {
        wordFileReaders = Arrays.stream(files).map(WordFileReader::new).toArray(WordFileReader[]::new);
        Arrays.stream(wordFileReaders).parallel().forEach(wordFileReaders -> {
            wordFileReaders.onNewWord(this::addWord);
            wordFileReaders.read();
        });

        if (! isShutdown) {
            this.onFinish();
            System.out.println(getName() + " is processed files:");
            for (String file: files) {
                System.out.printf("     %s\n", file);
            }
        }
        shutdown();
    }

    protected void onFinish() {

    }
}
