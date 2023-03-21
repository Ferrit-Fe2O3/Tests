import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class OrgStructureParserImpl implements OrgStructureParser {

    private HashMap<Long, Employee> employees; // Эта переменная нужно только что бы показать что данные спарсились правильно

    public HashMap<Long, Employee> getEmployees() {
        return employees;
    }

    @Override
    public Employee parseStructure(File csvFile) {
        employees = fileToEmployees(csvFile); // Парсим файл в HashMap
        Employee boss = null;
        // Строим связи начальник-работник
        for (Employee employee : employees.values()) {
            employee.setBoss(employees.get(employee.getBossId())); // Выставляем начальника
            if (employee.getBoss() == null) {
                if (boss != null) {
                    return null; // Если не null значит это второй гендиректор, возвращаем null
                } else {
                    boss = employee; // Если начальника нет, значит это генеральный директор, его вернем из метода
                }
            } else {
                employee.getBoss().addEmployee(employee); // Если начальник есть добовляемся к нему в работники
            }
        }
        return boss;
    }

    // Функция для парсинга данных из файла в HashMap
    private HashMap<Long, Employee> fileToEmployees(File file) {
        HashMap<Long, Employee> employees = new HashMap<>();
        try (FileReader fileReader = new FileReader(file); BufferedReader reader = new BufferedReader(fileReader)) {
            String line;
            boolean firstLine = true;
            HashMap<String, Integer> order = new HashMap<>();
            while ((line = reader.readLine()) != null) {
                // Получаем порядок следования данных из первой строки
                if (firstLine) {
                    String[] fields = line.split(";");
                    for (int i = 0; i < fields.length; i++) {
                        order.put(fields[i], i);
                    }
                    firstLine = false;
                    continue;
                }
                // Заполняем Employee данными
                Employee employee = new Employee();
                String[] fields = line.split(";");
                employee.setId(Long.parseLong(fields[order.get("id")]));
                if (fields[order.get("boss_id")].equals("")) {
                    employee.setBossId(null);
                } else {
                    employee.setBossId(Long.parseLong(fields[order.get("boss_id")]));
                }
                employee.setName(fields[order.get("name")]);
                employee.setPosition(fields[order.get("position")]);
                employees.put(employee.getId(), employee);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employees;
    }



}
