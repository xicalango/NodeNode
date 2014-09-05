package xx.nn;

import xx.nn.nodes.ActionNode;
import xx.nn.nodes.AddNode;
import xx.nn.nodes.AssignStmtNode;
import xx.nn.nodes.BlockNode;
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

public interface Visitor {

  public default <T> void visit(Terminal<T> t) {

  }

  public default <T> void visit(Constant<T> t) {

  }

  public default void visit(BlockNode block) {
    for (Node n : block.nodes) {
      n.accept(this);
    }
  }

  public default <T extends Node, U extends Node> void visit(WhileStmtNode<T, U> tt) {

  }

  public default <T extends Node, U extends Node> void visit(AssignStmtNode<T, U> smt) {

  }

  public default <T extends Node, U extends Node> void visit(AddNode<T, U> tt) {

  }

  public default <T extends Node, U extends Node> void visit(SubNode<T, U> tt) {

  }

  public default <T extends Node, U extends Node> void visit(MulNode<T, U> tt) {

  }

  public default <T extends Node, U extends Node> void visit(ModuloNode<T, U> tt) {

  }

  public default <T extends Node, U extends Node> void visit(EqNode<T, U> tt) {

  }

  public default <T extends Node, U extends Node> void visit(CmpNode<T, U> tt) {

  }

  public default <T extends Node, U extends Node, V extends Node> void visit(IfNode<T, U, V> tt) {

  }

  public default <T extends Node, D> void visit(CastNode<T, D> tt) {

  }

  public default void visitNop() {

  }

  public default void visit(ActionNode tt) {

  }

  public default void traverse(Node n) {
    n.accept(this);
  }

}
