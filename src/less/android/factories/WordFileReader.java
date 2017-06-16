package less.android.factories;

import less.android.interfaces.FileWordOperator;
import less.android.utils.WordChecker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
//        System.out.printf("%s is readed.\n", fileName);
    }

    private void read() {
        try(BufferedReader file = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = file.readLine()) != null) {
                for (String word : WordChecker.getWordsFromString(line)) {
                    if (! fileWordOperator.isShutdown()) {
                        /*try {
                            // simulate long working.
                            Thread.sleep(ThreadLocalRandom.current().nextInt(10, 20) * 10);
                        } catch (InterruptedException e) {
                            System.out.println(fileName + " is not required anymore.");
                            return;
                        }*/
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
