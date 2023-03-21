public class ComplexNumber {

    private double real;

    private double sup;

    public double getReal() {
        return real;
    }

    public double getSup() {
        return sup;
    }

    public ComplexNumber(double real) {
        this.real = real;
        sup = 0;
    }

    public ComplexNumber(double real, double sup) {
        this.real = real;
        this.sup = sup;
    }

    public ComplexNumber add(ComplexNumber complexNumber) {
        return new ComplexNumber(real + complexNumber.getReal(), sup + getSup());
    }

    public ComplexNumber sub(ComplexNumber complexNumber) {
        return new ComplexNumber(real - complexNumber.getReal(), sup - complexNumber.getSup());
    }

    public ComplexNumber mul(ComplexNumber complexNumber) {
        return  new ComplexNumber(
                real * complexNumber.getReal() - sup * complexNumber.getSup(),
                sup * complexNumber.getReal() + real * complexNumber.getSup());
    }

    public double mod() {
        return Math.sqrt(Math.pow(real, 2) + Math.pow(sup, 2));
    }

    public String toString() {
        return new String(real + " + " + sup + "i");
    }

}
