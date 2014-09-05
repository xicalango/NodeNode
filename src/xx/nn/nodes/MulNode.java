package xx.nn.nodes;

import xx.nn.Visitor;

public class MulNode<T extends Node, U extends Node> extends BiNode<T, U> {

  public MulNode(T node1, U node2) {
    super(node1, node2);
  }

  @Override
  public void accept(Visitor v) {
    v.visit(this);
  }

}
