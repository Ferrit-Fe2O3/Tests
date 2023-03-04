import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            for (int i = 1; i <= n; i++) {
                int result = (int)Math.round((Math.pow(1 + Math.sqrt(2), i) - (Math.pow(1 - Math.sqrt(2), i))) / (
                        2 * Math.sqrt(2)));
                System.out.println(result);
            }
        }
    }

}