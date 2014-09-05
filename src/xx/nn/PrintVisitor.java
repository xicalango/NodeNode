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

public class PrintVisitor implements Visitor {

  private int indent = 0;

  private void printlnIndent(Object s) {
    for (int i = 0; i < indent * 2; i++) {
      System.out.print(' ');
    }
    System.out.println(s);
  }

  @Override
  public <T> void visit(Terminal<T> t) {
    printlnIndent(t.getType().getSimpleName() + "<" + t.hashCode() + ">");
  }

  @Override
  public <T> void visit(Constant<T> t) {
    printlnIndent(t.value);
  }

  @Override
  public <T extends Node, U extends Node> void visit(AddNode<T, U> tt) {
    tt.node1.accept(this);
    printlnIndent(" + ");
    tt.node2.accept(this);
  }

  @Override
  public <T extends Node, U extends Node> void visit(SubNode<T, U> tt) {
    tt.node1.accept(this);
    printlnIndent(" - ");
    tt.node2.accept(this);
  }

  @Override
  public <T extends Node, U extends Node> void visit(MulNode<T, U> tt) {
    tt.node1.accept(this);
    printlnIndent(" * ");
    tt.node2.accept(this);
  }

  @Override
  public <T extends Node, U extends Node> void visit(ModuloNode<T, U> tt) {
    tt.node1.accept(this);
    printlnIndent("%");
    tt.node2.accept(this);
  }

  @Override
  public <T extends Node, U extends Node, V extends Node> void visit(IfNode<T, U, V> tt) {
    printlnIndent("IF");
    indent++;
    tt.testNode.accept(this);
    indent--;
    printlnIndent("THEN");
    indent++;
    tt.thenNode.accept(this);
    indent--;
    printlnIndent("ELSE");
    indent++;
    tt.elseNode.accept(this);
    indent--;
    printlnIndent("END IF");
  }

  @Override
  public <T extends Node, U extends Node> void visit(EqNode<T, U> tt) {
    tt.node1.accept(this);
    printlnIndent("==");
    tt.node2.accept(this);
  }

  @Override
  public <T extends Node, U extends Node> void visit(AssignStmtNode<T, U> smt) {
    smt.node1.accept(this);
    printlnIndent("=");
    smt.node2.accept(this);
    System.out.println();
  }

  @Override
  public <T extends Node, U extends Node> void visit(WhileStmtNode<T, U> tt) {
    printlnIndent("WHILE");
    indent++;
    tt.testNode.accept(this);
    indent--;
    printlnIndent("DO");
    indent++;
    tt.blockNode.accept(this);
    indent--;
    printlnIndent("END WHILE");
  }

  @Override
  public <T extends Node, U extends Node> void visit(CmpNode<T, U> tt) {
    tt.node1.accept(this);
    printlnIndent(tt.op.name());
    tt.node2.accept(this);
  }

  @Override
  public <T extends Node, D> void visit(CastNode<T, D> tt) {
    printlnIndent("CAST");
    indent++;
    tt.node.accept(this);
    indent--;
    printlnIndent("TO " + tt.destinationType);
  }

  @Override
  public void visit(ActionNode tt) {
    printlnIndent("EXEC " + tt.action.toString());
    indent++;
    tt.node.accept(this);
    indent--;
  }

}
