package less.android.factories;

import less.android.utils.WordChecker;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class WordFileReader implements Closeable {
    private String fileName;
    private boolean isClosed = false;
    private ArrayList<Consumer<String>> consumers;

    public WordFileReader(String fileName) {
        this.fileName = fileName;
        consumers = new ArrayList<>();
    }

    private void readNIO() {
        Path pathToFile = Paths.get(fileName);
        try {
            BufferedReader fileBuffer = Files.newBufferedReader(pathToFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void simulateWork() {
        if (! isClosed) {
            int ext = 10;
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(150 / ext, 300 / ext));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void bufferReader() {
        try(BufferedReader file = new BufferedReader(new FileReader(fileName))) {
            String line;
            while (! isClosed && (line = file.readLine()) != null) {
                for (String word : WordChecker.getWordsFromString(line)) {
                    simulateWork();
                    if (! isClosed) {
                        consumers.forEach(consumer -> consumer.accept(word));
                    } else {
                        return;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.printf("%s. is not exists.\n", fileName);
            return;
        } catch (Exception e) {
            System.err.printf("Some problem occurred while reading %s.\n", fileName);
            return;
        }
    }

    public void read() {
        System.out.println(fileName + " is reading...");
        if (consumers.size() != 0) {
            bufferReader();
        }
        System.out.println(fileName + " is readed");
    }

    public boolean isClosed() {
        return isClosed;
    }

    @Override
    public void close() {
        this.isClosed = true;
    }

    public void onNewWord(Consumer<String> handler) {
        consumers.add(handler);
    }
}
