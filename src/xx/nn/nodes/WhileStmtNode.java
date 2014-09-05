package xx.nn.nodes;

import java.util.Set;

import xx.nn.Visitor;

public class WhileStmtNode<T extends Node, U extends Node> implements StatementNode {

  public T testNode;
  public U blockNode;

  public WhileStmtNode(T testNode, U blockNode) {
    this.testNode = testNode;
    this.blockNode = blockNode;
  }

  @Override
  public void accept(Visitor v) {
    v.visit(this);
  }

  @Override
  public Set<Class<?>> getTypes() {
    return blockNode.getTypes();
  }

  @Override
  public boolean isConst() {
    return testNode.isConst() && blockNode.isConst();
  }

}
