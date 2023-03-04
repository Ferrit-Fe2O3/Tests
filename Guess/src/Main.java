import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        int number = new Random().nextInt(99) + 1; // здесь загадывается число от 1 до 99
        int maxAttempts = 10; // здесь задается количество попыток
        System.out.println("Я загадал число. У тебя " + maxAttempts + " попыток угадать.");
        try (Scanner scanner = new Scanner(System.in)) {
            for (int i = 1; i <= maxAttempts + 1; i++) {
                if (i > maxAttempts) {
                    System.out.println("Ты не угадал");
                    break;
                }
                int n = scanner.nextInt();
                if (n == number) {
                    System.out.println("Ты угадал с " + i + " попытки");
                    break;
                }
                if (n > number) {
                    System.out.println("Мое число меньше! Осталось " + (maxAttempts - i) + " попыток");
                } else {
                    System.out.println("Мое число больше! Осталось " + (maxAttempts - i) + " попыток");
                }
            }
        }
    }

}