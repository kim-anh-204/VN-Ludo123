package dice;

import static states.Play.random_fixed;

public class Dice {
    private int result = 0;

    public void cast() {
        if (random_fixed % 6 == 0) {
            result = 6;
        } else if (random_fixed % 6 == 1) {
            result = 1;
        } else if (random_fixed % 6 == 2) {
            result = 2;
        } else if (random_fixed % 6 == 3) {
            result = 3;
        } else if (random_fixed % 6 == 4) {
            result = 4;
        } else {
            result = 5;
        }
    }

    public int getResult() {
        return result;
    }

    public void reset() {
        result = 0;
    }
}