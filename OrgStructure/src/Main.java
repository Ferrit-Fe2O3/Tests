import java.io.File;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        OrgStructureParserImpl parser = new OrgStructureParserImpl();

        Employee boss = parser.parseStructure(new File("1.CSV"));

        // Выводим полученые данные
        if (boss != null) {
            for (Employee employee : parser.getEmployees().values()) {
                System.out.println(employee.getId() + ": " + employee.getPosition() + " " + employee.getName());
                System.out.println("Подчиненные:");
                if (employee.getSubordinate().size() > 0) {
                    for (Employee subemployee : employee.getSubordinate()) {
                        System.out.println("\t" + subemployee.getId() + ": " +
                                subemployee.getPosition() + " - " + subemployee.getName());
                    }
                } else {
                    System.out.println("\tНет");
                }
                System.out.println();
            }
        } else {
            System.out.println("Входной файл содержит некорректные данные");
        }
    }



}