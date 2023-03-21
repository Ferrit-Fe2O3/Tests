public class ComplexNumberTest {
    public static void main(String[] args) {
        // Конструктор с одним параметром
        ComplexNumber complexNumber = new ComplexNumber(5);
        System.out.println(complexNumber);
        System.out.println();

        // Конструктор с двумя параметрами
        ComplexNumber complexNumber1 = new ComplexNumber(3, 2);
        System.out.println(complexNumber1);
        System.out.println();

        ComplexNumber complexNumber2 = new ComplexNumber(1, 2);

        // Сумма комплексных чисел
        ComplexNumber sum = complexNumber1.add(complexNumber2);
        System.out.println(sum);
        System.out.println();

        // Разность комплексных чисел
        ComplexNumber diff = complexNumber1.sub(complexNumber2);
        System.out.println(diff);
        System.out.println();

        // Произведение комплексных чисел
        ComplexNumber mul = complexNumber1.mul(complexNumber2);
        System.out.println(mul);
        System.out.println();

        // Модуь комплексного числа
        System.out.println(complexNumber1.mod());
    }
}