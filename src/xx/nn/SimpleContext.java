package xx.nn;

import java.util.HashMap;
import java.util.Map;

import xx.nn.nodes.Terminal;

public class SimpleContext implements Context {

  private Map<Terminal, Object> map = new HashMap<>();

  @Override
  public <T> Object get(Terminal<T> t) {
    return map.get(t);
  }

  @Override
  public <T> void put(Terminal<T> t, Object value) {
    map.put(t, value);
  }

}
