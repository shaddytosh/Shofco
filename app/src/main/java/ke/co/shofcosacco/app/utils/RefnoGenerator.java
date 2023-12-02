package ke.co.shofcosacco.app.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RefnoGenerator {

    public static String refnocountfile = "0.0.0.0.0.0"; // Default file path

    public static String createRefno() {
        try {
            String filename = refnocountfile;
            String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
                    "S", "T", "U", "V", "W", "X", "Y", "Z"};
            int[] numbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
            int maxletterIndex = letters.length - 1;
            int minLetterIndex = 0;

            Random random = new Random();

            int digit7Val = random.nextInt(10);
            int digit8Val = random.nextInt(26);
            int digit9Val = random.nextInt(26);
            int digit10Val = random.nextInt(26);

            int digit6Val = 0;
            int digit5Val = 0;
            int digit4Val = 0;
            int digit3Val = 0;

            if (digit7Val > 9) {
                digit7Val = 0;
            }

            if (digit8Val > maxletterIndex) {
                digit8Val = minLetterIndex;
            }
            if (digit9Val > maxletterIndex) {
                digit9Val = minLetterIndex;
            }
            if (digit10Val > maxletterIndex) {
                digit10Val = minLetterIndex;
            }

            List<Integer> positions = new ArrayList<>();
            String count = "";
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                count = reader.readLine();
            } catch (IOException e) {
                // Handle exception or log error
                e.printStackTrace();
            }

            if (count == null || count.isEmpty() || count.length() < 6) {
                count = "0.0.0." + "00" + "." + "00" + "." + "00";
            }

            String[] parts = count.split("\\.");
            for (String part : parts) {
                positions.add(Integer.parseInt(part));
            }

            int digit1Val = positions.get(0);
            int digit2Val = positions.get(1);
            digit3Val = positions.get(2);
            digit4Val = positions.get(3);
            digit5Val = positions.get(4);
            digit6Val = positions.get(5);

            if (digit1Val > maxletterIndex) {
                digit1Val = minLetterIndex;
            }

            if (digit2Val > maxletterIndex) {
                digit2Val = minLetterIndex;
                digit1Val += 1;
            }
            if (digit3Val > maxletterIndex) {
                if (digit3Val < (maxletterIndex + 10)) {
                    int numVal = digit3Val - maxletterIndex;
                    for (int numberX : numbers) {
                        if (numVal == numberX) {
                            digit3Val = numberX;
                        }
                        if (digit3Val != -1) {
                            break;
                        }
                    }
                } else {
                    digit3Val = minLetterIndex;
                    digit2Val += 1;
                }
            }
            if (digit4Val > 9) {
                digit4Val = minLetterIndex;
                digit3Val += 1;
            }
            if (digit5Val > maxletterIndex) {
                digit5Val = minLetterIndex;
                digit4Val += 1;
            }
            if (digit6Val > maxletterIndex) {
                digit6Val = minLetterIndex;
                digit5Val += 1;
            }

            int lIndex = 0;
            String letter10Val = "";
            String letter9Val = "";
            String letter8Val = "";
            String letter6Val = "";
            String letter5Val = "";
            String letter3Val = "";
            String letter2Val = "";
            String letter1Val = "";

            for (String letterX : letters) {
                if (digit10Val == lIndex) {
                    letter10Val = letterX;
                }
                if (digit9Val == lIndex) {
                    letter9Val = letterX;
                }
                if (digit8Val == lIndex) {
                    letter8Val = letterX;
                }
                if (digit6Val == lIndex) {
                    letter6Val = letterX;
                }
                if (digit5Val == lIndex) {
                    letter5Val = letterX;
                }
                if (digit3Val == lIndex) {
                    letter3Val = letterX;
                }
                if (digit2Val == lIndex) {
                    letter2Val = letterX;
                }
                if (digit1Val == lIndex) {
                    letter1Val = letterX;
                }

                if (!letter1Val.isEmpty() && !letter2Val.isEmpty() && !letter3Val.isEmpty() && !letter5Val.isEmpty() &&
                        !letter6Val.isEmpty() && !letter8Val.isEmpty() && !letter9Val.isEmpty() && !letter10Val.isEmpty()) {
                    break;
                }
                lIndex++;
            }
            digit6Val += 1;

            String letter7Val = "";
            String letter4Val = ""; // Add this line for letter4Val initialization
            for (int numberX : numbers) {
                if (digit4Val == numberX) {
                    letter4Val = Integer.toString(numberX);
                }
                if (digit7Val == numberX) {
                    letter7Val = Integer.toString(numberX);
                }

                if (!letter4Val.isEmpty() && !letter7Val.isEmpty()) {
                    break;
                }
            }


            positions.set(0, digit1Val);
            positions.set(1, digit2Val);
            positions.set(2, digit3Val);
            positions.set(3, digit4Val);
            positions.set(4, digit5Val);
            positions.set(5, digit6Val);
            String refno = letter1Val + letter2Val + letter3Val + letter4Val + letter5Val +
                    letter6Val + letter7Val + letter8Val + letter9Val + letter10Val;

            StringBuilder countBuilder = new StringBuilder();
            for (Integer position : positions) {
                countBuilder.append(position).append(".");
            }
            String _count = countBuilder.toString();
            _count = _count.substring(0, _count.length() - 1); // Remove the last dot

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                writer.write(_count + System.lineSeparator());
            } catch (IOException e) {
                // Handle exception or log error
                e.printStackTrace();
            }

            return refno;
        } catch (Exception e) {
            // Handle exception or log error
            e.printStackTrace();
        }
        return null; // Or return an appropriate default value
    }


}
