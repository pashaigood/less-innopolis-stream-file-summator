package less.android.factories;

import less.android.interfaces.FileWordOperator;
import less.android.utils.WordChecker;

import java.util.ArrayList;

public class Summator extends FileWordOperator {
    private ArrayList<Integer> numbers;
    private Integer incorrentCount = 0;

    public Summator(String[] files) {
        super(files);
        numbers = new ArrayList<Integer>() {{
            add(0);
        }};
    }

    @Override
    public void addWord(String inputNumber) {
        Integer number = Integer.parseInt(inputNumber);

        if (! WordChecker.check(inputNumber)) {
            incorrentCount++;
            return;
        }

        if (number < 0) {
//            System.out.println("Log: " + number + " is lower then 0");
            incorrentCount++;
            return;
        }
        Integer result;
        synchronized (this) {
            numbers.add(number);
            result = getResult();
        }

//        System.out.println("Current result is: " + result);
    }

    private Integer getResult() {
        return numbers.stream().reduce((sum, value) -> sum + value).orElse(0);
    }

    @Override
    protected void onFinish() {
        System.out.println("================>");
        System.out.println("Finale result is: " + getResult());
        System.out.println("Count of number is: " + (numbers.size() - 1));
        System.out.println("Count of incorrect numbers: " + incorrentCount);
    }
}
