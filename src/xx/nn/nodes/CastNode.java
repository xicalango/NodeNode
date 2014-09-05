package xx.nn.nodes;

import java.util.Collections;
import java.util.Set;

import xx.nn.Visitor;

public class CastNode<T extends Node, D> implements Node {

  public T node;
  public Class<D> destinationType;

  public CastNode(T node, Class<D> destinationType) {
    this.node = node;
    this.destinationType = destinationType;
  }

  @Override
  public Set<Class<?>> getTypes() {
    return Collections.singleton(destinationType);
  }

  @Override
  public void accept(Visitor v) {
    v.visit(this);
  }

  @Override
  public boolean isConst() {
    return node.isConst();
  }

}
