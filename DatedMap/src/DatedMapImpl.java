import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class DatedMapImpl implements DatedMap {

    private final HashMap<String, String> innerMap;
    private final HashMap<String, Date> dateMap;

    public DatedMapImpl() {
        innerMap = new HashMap<>();
        dateMap = new HashMap<>();
    }

    @Override
    public void put(String key, String value) {
        innerMap.put(key, value);
        dateMap.put(key, new Date());
    }

    @Override
    public String get(String key) {
        return innerMap.get(key);
    }

    @Override
    public boolean containsKey(String key) {
        return innerMap.containsKey(key);
    }

    @Override
    public void remove(String key) {
        innerMap.remove(key);
        dateMap.remove(key);
    }

    @Override
    public Set<String> keySet() {
        return innerMap.keySet();
    }

    @Override
    public Date getKeyLastInsertionDate(String key) {
        return dateMap.get(key);
    }

}
