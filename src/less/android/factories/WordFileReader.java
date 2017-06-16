package less.android.factories;

import less.android.interfaces.FileWordOperator;
import less.android.utils.WordChecker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ThreadLocalRandom;

public class WordFileReader extends Thread {
    private FileWordOperator fileWordOperator;
    private String fileName;

    public WordFileReader(String fileName, FileWordOperator fileWordOperator) {
        this.fileWordOperator = fileWordOperator;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        read();
//        watch();
    }

    private void watch() {
        DirectoryWatcher.getWatcher(Paths.get(fileName).getParent().toString(), (e) -> {});
    }

    private void readNIO() {
        Path pathToFile = Paths.get(fileName);
        try {
            BufferedReader fileBuffer = Files.newBufferedReader(pathToFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read() {
        try(BufferedReader file = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = file.readLine()) != null) {
                for (String word : WordChecker.getWordsFromString(line)) {
                    if (! fileWordOperator.isShutdown()) {
                        fileWordOperator.addWord(word);
                    } else {
                        System.out.println(fileWordOperator.getName() + " is finished.");
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
}
