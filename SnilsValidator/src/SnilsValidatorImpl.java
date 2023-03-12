public class SnilsValidatorImpl implements SnilsValidator{

    public boolean validate(String snils) {
        if (snils == null)
            return false;
        for (int i = 0; i < 11; i++) {
            if (!Character.isDigit(snils.charAt(i)))
                return false;
        }
        boolean result = true;
        if (snils.length() != 11) {
            result = false;
        } else {
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += Character.digit(snils.charAt(i), 10) * (9 - i);
            }
            int checkDigit;
            if (sum < 100) {
                checkDigit = sum;
            } else if (sum == 100) {
                checkDigit = 0;
            } else if ((checkDigit = sum % 101) == 100) {
                checkDigit = 0;
            }
            if (checkDigit != Integer.parseInt(snils.substring(9))) {
                result = false;
            }
        }
        return result;
    }

}
