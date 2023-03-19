public class TransliteratorImpl implements Transliterator {

    @Override
    public String transliterate(String source) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            char ch = source.charAt(i);
            switch (ch) {
                case 'А' -> result.append('A');
                case 'Б' -> result.append('B');
                case 'В' -> result.append('V');
                case 'Г' -> result.append('G');
                case 'Д' -> result.append('D');
                case 'Е', 'Ё', 'Э' -> result.append('E');
                case 'Ж' -> result.append("ZH");
                case 'З' -> result.append('Z');
                case 'И', 'Й' -> result.append('I');
                case 'К' -> result.append('K');
                case 'Л' -> result.append('L');
                case 'М' -> result.append('M');
                case 'Н' -> result.append('N');
                case 'О' -> result.append('O');
                case 'П' -> result.append('P');
                case 'Р' -> result.append('R');
                case 'С' -> result.append('S');
                case 'Т' -> result.append('T');
                case 'У' -> result.append('U');
                case 'Ф' -> result.append('F');
                case 'X' -> result.append("KH");
                case 'Ц' -> result.append("TS");
                case 'Ч' -> result.append("CH");
                case 'Ш' -> result.append("SH");
                case 'Щ' -> result.append("SHCH");
                case 'Ы' -> result.append('Y');
                case 'Ь' -> {}
                case 'Ъ' -> result.append("IE");
                case 'Ю' -> result.append("IU");
                case 'Я' -> result.append("IA");
                default -> result.append(ch);
            }
        }
        return result.toString();
    }

}
