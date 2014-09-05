package xx.nn.nodes;

import java.util.HashSet;
import java.util.Set;

public abstract class BiNode<T extends Node, U extends Node> implements Node {
  public T node1;
  public U node2;

  public BiNode(T node1, U node2) {
    this.node1 = node1;
    this.node2 = node2;
  }

  @Override
  public Set<Class<?>> getTypes() {
    HashSet<Class<?>> result = new HashSet<>();
    result.addAll(node1.getTypes());
    result.addAll(node2.getTypes());
    return result;
  }

  @Override
  public boolean isConst() {
    return node1.isConst() && node2.isConst();
  }
}
