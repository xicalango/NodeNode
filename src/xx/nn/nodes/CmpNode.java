package xx.nn.nodes;

import xx.nn.Visitor;

public class CmpNode<T extends Node, U extends Node> extends BiNode<T, U> {

  public static enum Op {
    EQ("=="),
    LT("<"),
    GT(">"),
    LTEQ("<="),
    GTEQ(">=");

    public final String javaSign;

    private Op(String javaSign) {
      this.javaSign = javaSign;
    }

  }

  public Op op;

  public CmpNode(T node1, U node2, Op op) {
    super(node1, node2);
    this.op = op;
  }

  @Override
  public void accept(Visitor v) {
    v.visit(this);
  }

}
