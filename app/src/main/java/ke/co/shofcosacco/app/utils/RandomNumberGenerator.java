package ke.co.shofcosacco.app.utils;

import java.util.Random;

public class RandomNumberGenerator {
    public static int generate4DigitNumber() {
        Random random = new Random();
        return random.nextInt(9000) + 1000; // Generates a number between 1000 and 9999
    }
}
