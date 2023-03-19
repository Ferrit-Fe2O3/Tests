public class Main {
    public static void main(String[] args) {
        DatedMap datedMap = new DatedMapImpl();
        datedMap.put("first key", "first value");
        datedMap.put("second key", "second value");
        datedMap.put("third key", "third value");
        System.out.println(datedMap.get("second key"));
        System.out.println(datedMap.containsKey("first key"));
        for (String key : datedMap.keySet()) {
            System.out.println(key);
        }
        datedMap.remove("third key");
        for (String key : datedMap.keySet()) {
            System.out.println(datedMap.getKeyLastInsertionDate(key));
        }
    }
}