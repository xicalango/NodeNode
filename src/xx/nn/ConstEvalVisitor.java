package xx.nn;

import xx.nn.nodes.ActionNode;
import xx.nn.nodes.AddNode;
import xx.nn.nodes.AssignStmtNode;
import xx.nn.nodes.CastNode;
import xx.nn.nodes.CmpNode;
import xx.nn.nodes.Constant;
import xx.nn.nodes.EqNode;
import xx.nn.nodes.IfNode;
import xx.nn.nodes.ModuloNode;
import xx.nn.nodes.MulNode;
import xx.nn.nodes.Node;
import xx.nn.nodes.SubNode;
import xx.nn.nodes.Terminal;
import xx.nn.nodes.WhileStmtNode;

public class ConstEvalVisitor implements Visitor {

  public Object result;

  public ConstEvalVisitor() {
  }

  @Override
  public <T> void visit(Terminal<T> t) {
    throw new IllegalStateException("Const eval with non const terminal");
  }

  @Override
  public <T> void visit(Constant<T> t) {
    result = t.value;
  }

  @Override
  public <T extends Node, U extends Node> void visit(AddNode<T, U> tt) {
    tt.node1.accept(this);
    Object o1 = result;

    tt.node2.accept(this);

    Object o2 = result;

    int i1 = (int) o1;
    int i2 = (int) o2;

    result = i1 + i2;
  }

  @Override
  public <T extends Node, U extends Node> void visit(SubNode<T, U> tt) {
    tt.node1.accept(this);
    Object o1 = result;

    tt.node2.accept(this);

    Object o2 = result;

    int i1 = (int) o1;
    int i2 = (int) o2;

    result = i1 - i2;
  }

  @Override
  public <T extends Node, U extends Node> void visit(MulNode<T, U> tt) {
    tt.node1.accept(this);
    Object o1 = result;

    tt.node2.accept(this);

    Object o2 = result;

    int i1 = (int) o1;
    int i2 = (int) o2;

    result = i1 * i2;
  }

  @Override
  public <T extends Node, U extends Node> void visit(ModuloNode<T, U> tt) {
    tt.node1.accept(this);
    Object o1 = result;

    tt.node2.accept(this);

    Object o2 = result;

    int i1 = (int) o1;
    int i2 = (int) o2;

    result = i1 % i2;
  }

  @Override
  public <T extends Node, U extends Node, V extends Node> void visit(IfNode<T, U, V> tt) {
    tt.testNode.accept(this);
    boolean o1 = (boolean) result;

    if (o1) {
      tt.thenNode.accept(this);
    } else {
      tt.elseNode.accept(this);
    }
  }

  @Override
  public <T extends Node, U extends Node> void visit(EqNode<T, U> tt) {
    tt.node1.accept(this);
    Object o1 = result;

    tt.node2.accept(this);
    Object o2 = result;

    result = o1.equals(o2);
  }

  @Override
  public <T extends Node, U extends Node> void visit(AssignStmtNode<T, U> smt) {
    throw new IllegalStateException("Assignment not const");
  }

  @Override
  public <T extends Node, U extends Node> void visit(WhileStmtNode<T, U> tt) {
    throw new IllegalStateException("While not const");
  }

  @Override
  public <T extends Node, U extends Node> void visit(CmpNode<T, U> tt) {
    tt.node1.accept(this);
    Object o1 = result;

    tt.node2.accept(this);
    Object o2 = result;

    switch (tt.op) {
      case EQ:
        result = o1.equals(o2);
        break;
      case GT:
        result = (int) o1 > (int) o2;
        break;
      case GTEQ:
        result = (int) o1 >= (int) o2;
        break;
      case LT:
        result = (int) o1 < (int) o2;
        break;
      case LTEQ:
        result = (int) o1 <= (int) o2;
        break;
    }

  }

  @Override
  public void visit(ActionNode tt) {
    tt.node.accept(this);
    tt.action.accept(result);
  }

  @Override
  public <T extends Node, D> void visit(CastNode<T, D> tt) {
    tt.node.accept(this);
  }

}
