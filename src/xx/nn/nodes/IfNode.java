package xx.nn.nodes;

import java.util.HashSet;
import java.util.Set;

import xx.nn.Visitor;

public class IfNode<T extends Node, U extends Node, V extends Node> implements Node {

  public T testNode;
  public U thenNode;
  public V elseNode;

  public IfNode(T testNode, U thenNode, V elseNode) {
    this.testNode = testNode;
    this.thenNode = thenNode;
    this.elseNode = elseNode;
  }

  @Override
  public void accept(Visitor v) {
    v.visit(this);
  }

  @Override
  public Set<Class<?>> getTypes() {
    HashSet<Class<?>> result = new HashSet<>();
    result.addAll(thenNode.getTypes());
    result.addAll(elseNode.getTypes());
    return result;
  }

  @Override
  public boolean isConst() {
    return testNode.isConst() && thenNode.isConst() && elseNode.isConst();
  }

}
