public class Main {

    public static void main(String[] args) {
        System.out.println(passwordValidator("login5_4", "password_11", "password_11")); // true
//        System.out.println(passwordValidator("login5_4", "password", "password_11")); // false
//        System.out.println(passwordValidator("logi&n5_4", "password_11", "password_11")); // false
    }

    public static boolean passwordValidator(String login, String password, String confirmPassword) {
        try {
            checkLogin(login);
            checkPassword(password, confirmPassword);
        } catch (WrongLoginException | WrongPasswordException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void checkLogin(String login) throws WrongLoginException {
        if (!login.toLowerCase().matches("[a-z0-9_]+")) {
            throw new WrongLoginException("Логин содержит недопустимые символы");
        } else if (login.length() > 20) {
            throw new WrongLoginException("Логин слишком длинный");
        }
    }

    private static void checkPassword(String password, String confirmPassword) throws WrongPasswordException {
        if (!password.toLowerCase().matches("[a-z0-9_]+")) {
            throw new WrongPasswordException("Пароль содержит недопустимые символы");
        } else if (password.length() > 20) {
            throw new WrongPasswordException("Пароль слишком длинный");
        } else if (!password.equals(confirmPassword)) {
            throw new WrongPasswordException("Пароль и подтверждение не совпадают");
        }
    }

}