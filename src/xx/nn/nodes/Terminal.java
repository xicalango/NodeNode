package xx.nn.nodes;

import java.util.Collections;
import java.util.Set;

import xx.nn.Visitor;

public class Terminal<T> implements Node {

  private final Class<T> type;

  public Terminal(Class<T> type) {
    this.type = type;
  }

  public Class<T> getType() {
    return type;
  }

  public boolean isTypeOf(Object o) {
    return type.isAssignableFrom(o.getClass());
  }

  @Override
  public void accept(Visitor v) {
    v.visit(this);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <E> Terminal<E> leaf() {
    return (Terminal<E>) this;
  }

  public static <T> Terminal<T> create(Class<T> type) {
    return new Terminal<>(type);
  }

  @Override
  public Set<Class<?>> getTypes() {
    return Collections.singleton(type);
  }

  @Override
  public boolean isConst() {
    return false;
  }

}
