package tech.buildrun.mockito;

import java.util.HashMap;
import java.util.Map;

public class RealDatabase implements Database {

    private Map<Integer, String> data = new HashMap<>();

    public RealDatabase() {
        data.put(1, "INACTIVE");
        data.put(2, "INACTIVE");
    }

    @Override
    public String getStatus(int id) {
        return data.getOrDefault(id, "UNKNOWN");
    }
}
