package xx.nn.nodes;

import xx.nn.Visitor;

public class Constant<T> extends Terminal<T> {

  public T value;

  public Constant(Class<T> type) {
    super(type);
  }

  @SuppressWarnings("unchecked")
  public Constant(T value) {
    super((Class<T>) value.getClass());
    this.value = value;
  }

  @Override
  public void accept(Visitor v) {
    v.visit(this);
  }

  @Override
  public <E> Terminal<E> leaf() {
    return null;
  }

  @Override
  public boolean isConst() {
    return true;
  }

  public static <T> Constant<T> mk(T value) {
    return new Constant<>(value);
  }

}
