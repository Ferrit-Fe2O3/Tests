public class SequenceGeneratorImpl implements SequenceGenerator {

    public void a(int n) {
        System.out.print("A: ");
        int result = 0;
        for (int i = 0; i < n; i++) {
            result += 2;
            System.out.print(result + " ");
        }
        System.out.println();
    }

    public void b(int n) {
        System.out.print("B: ");
        int result = 1;
        for (int i = 0; i < n; i++) {
            System.out.print(result + " ");
            result += 2;
        }
        System.out.println();
    }

    public void c(int n) {
        System.out.print("C: ");
        int result = 1;
        int acc = 3;
        for (int i = 0; i < n; i++) {
            System.out.print(result + " ");
            result += acc;
            acc += 2;
        }
        System.out.println();
    }

    public void d(int n) {
        System.out.print("D: ");
        int result;
        for (int i = 0; i < n; i++) {
            result = (int)Math.pow(i, 3);
            System.out.print(result + " ");
        }
        System.out.println();
    }

    public void e(int n) {
        System.out.print("E: ");
        int result = 1;
        for (int i = 0; i < n; i++) {
            System.out.print(result + " ");
            result *= -1;
        }
        System.out.println();
    }

    public void f(int n) {
        System.out.print("F: ");
        int factor = 1;
        for (int i = 1; i <= n; i++) {
            System.out.print(i * factor + " ");
            factor *= -1;
        }
        System.out.println();
    }

    public void g(int n) {
        System.out.print("G: ");
        int result = 1;
        int acc = 3;
        int factor = 1;
        for (int i = 0; i < n; i++) {
            System.out.print(result * factor + " ");
            result += acc;
            acc += 2;
            factor *= -1;
        }
        System.out.println();
    }

    public void h(int n) {
        System.out.print("H: ");
        int result = 1;
        for (int i = 0; i < n; i++) {
            if ((i % 2) == 1)
                System.out.print(0 + " ");
            else {
                System.out.print(result + " ");
                result++;
            }
        }
        System.out.println();
    }

    public void i(int n) {
        System.out.print("I: ");
        int result = 1;
        int factor = 1;
        for (int i = 0; i < n; i++) {
            result *= factor++;
            System.out.print(result + " ");
        }
        System.out.println();
    }

    public void j(int n) {
        System.out.print("J: ");
        int x = 1;
        int y = 0;
        for (int i = 0; i < n; i++) {
            System.out.print(x + " ");
            int buf = x;
            x = x + y;
            y = buf;
        }
        System.out.println();
    }

}
