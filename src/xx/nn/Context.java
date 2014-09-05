package xx.nn;

import java.util.Optional;

import xx.nn.nodes.Terminal;

public interface Context {
  public <T> Object get(Terminal<T> t);

  public default <T> T getAs(Terminal<T> t) {
    return t.getType().cast(get(t));
  }

  public default <T> Optional<T> getOptional(Terminal<T> t) {
    return Optional.ofNullable(get(t)).flatMap(o -> maybeCast(o, t.getType()));
  }

  public static <T> Optional<T> maybeCast(Object o, Class<T> type) {
    if (type.isAssignableFrom(o.getClass())) {
      return Optional.ofNullable(type.cast(o));
    } else {
      return Optional.empty();
    }
  }

  public <T> void put(Terminal<T> t, Object value);
}
