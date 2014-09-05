package xx.nn.nodes;

import xx.nn.Visitor;

public class SubNode<T extends Node, U extends Node> extends BiNode<T, U> {

  public SubNode(T node1, U node2) {
    super(node1, node2);
  }

  @Override
  public void accept(Visitor v) {
    v.visit(this);
  }

}
