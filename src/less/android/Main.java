package less.android;

import less.android.factories.Summator;

public class Main {

    public static void main(String[] args) {

        Summator summator = new Summator(new String[] {
            "./resources/set-1.txt",
            "./resources/set-2.txt",
            "./resources/set-3.txt",
        });
        summator.setName("Summator");
        summator.start();
    }
}
