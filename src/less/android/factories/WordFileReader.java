package less.android.factories;

import less.android.interfaces.FileWordOperator;
import less.android.utils.WordChecker;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.ThreadLocalRandom;

public class WordFileReader implements Closeable {
    private FileWordOperator fileWordOperator;
    private String fileName;
    private boolean isClosed = false;

    public WordFileReader(String fileName, FileWordOperator fileWordOperator) {
        this.fileWordOperator = fileWordOperator;
        this.fileName = fileName;
    }

    private void readNIO() {
        Path pathToFile = Paths.get(fileName);
        try {
            BufferedReader fileBuffer = Files.newBufferedReader(pathToFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void bufferReader() {
        try(BufferedReader file = new BufferedReader(new FileReader(fileName))) {
            String line;
            while (! isClosed && (line = file.readLine()) != null) {
                for (String word : WordChecker.getWordsFromString(line)) {
                    if (! isClosed) {
                        int ext = 10;
                        Thread.sleep(ThreadLocalRandom.current().nextInt(150 / ext, 300 / ext));
                        fileWordOperator.addWord(word);
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
        bufferReader();
        System.out.println(fileName + " is readed");
    }

    public boolean isClosed() {
        return isClosed;
    }

    @Override
    public void close() {
        this.isClosed = true;
    }
}
