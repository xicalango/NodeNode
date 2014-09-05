package xx.nn;

import java.util.Optional;

import xx.nn.nodes.AddNode;
import xx.nn.nodes.AssignStmtNode;
import xx.nn.nodes.BlockNode;
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

public class GraphVizVisitor implements Visitor {

  private final StringBuilder sb = new StringBuilder();

  private final Context ctx;

  public int nodeId = 0;

  public int clusterId = 0;

  public int lastNodeId;

  public GraphVizVisitor(Context ctx) {
    this.ctx = ctx;
  }

  public void init() {
    sb.append("digraph G {\n");
  }

  public void finish() {
    sb.append("}");
  }

  @Override
  public String toString() {
    return sb.toString();
  }

  private int addNode(Object label) {
    lastNodeId = nodeId;
    sb.append("node").append(nodeId++).append("[label=\"").append(label).append("\"];\n");
    return lastNodeId;
  }

  private void addEdge(int from, int to, Object labelO) {
    Optional<Object> label = Optional.ofNullable(labelO);
    sb.append("node").append(from).append(" -> node").append(to);
    label.ifPresent(l -> sb.append("[label=\"").append(l).append("\"]"));
    sb.append(";\n");
  }

  @Override
  public <T> void visit(Terminal<T> t) {
    addNode(ctx.get(t));
  }

  @Override
  public <T> void visit(Constant<T> t) {
    addNode(t.value);
  }

  @Override
  public <T extends Node, U extends Node> void visit(WhileStmtNode<T, U> tt) {
    tt.testNode.accept(this);
    int testNodeId = lastNodeId;

    tt.blockNode.accept(this);
    int blockNodeId = lastNodeId;

    int whileNodeId = addNode("while");

    addEdge(whileNodeId, testNodeId, "test");
    addEdge(whileNodeId, blockNodeId, null);

  }

  @Override
  public <T extends Node, U extends Node> void visit(AssignStmtNode<T, U> smt) {
    smt.node1.accept(this);
    int idNodeId = lastNodeId;

    smt.node2.accept(this);
    int exprNodeId = lastNodeId;

    int assignNodeId = addNode("assign");

    addEdge(assignNodeId, idNodeId, "id");
    addEdge(assignNodeId, exprNodeId, "expr");
  }

  @Override
  public <T extends Node, U extends Node> void visit(AddNode<T, U> tt) {
    tt.node1.accept(this);
    int op1Id = lastNodeId;

    tt.node2.accept(this);
    int op2Id = lastNodeId;

    int opNodeId = addNode("+");

    addEdge(opNodeId, op1Id, "op1");
    addEdge(opNodeId, op2Id, "op2");
  }

  @Override
  public <T extends Node, U extends Node> void visit(SubNode<T, U> tt) {
    tt.node1.accept(this);
    int op1Id = lastNodeId;

    tt.node2.accept(this);
    int op2Id = lastNodeId;

    int opNodeId = addNode("-");

    addEdge(opNodeId, op1Id, "op1");
    addEdge(opNodeId, op2Id, "op2");
  }

  @Override
  public <T extends Node, U extends Node> void visit(MulNode<T, U> tt) {
    tt.node1.accept(this);
    int op1Id = lastNodeId;

    tt.node2.accept(this);
    int op2Id = lastNodeId;

    int opNodeId = addNode("*");

    addEdge(opNodeId, op1Id, "op1");
    addEdge(opNodeId, op2Id, "op2");
  }

  @Override
  public <T extends Node, U extends Node> void visit(ModuloNode<T, U> tt) {
    tt.node1.accept(this);
    int op1Id = lastNodeId;

    tt.node2.accept(this);
    int op2Id = lastNodeId;

    int opNodeId = addNode("%");

    addEdge(opNodeId, op1Id, "op1");
    addEdge(opNodeId, op2Id, "op2");
  }

  @Override
  public <T extends Node, U extends Node> void visit(EqNode<T, U> tt) {
    tt.node1.accept(this);
    int op1Id = lastNodeId;

    tt.node2.accept(this);
    int op2Id = lastNodeId;

    int opNodeId = addNode("==");

    addEdge(opNodeId, op1Id, "op1");
    addEdge(opNodeId, op2Id, "op2");
  }

  @Override
  public <T extends Node, U extends Node> void visit(CmpNode<T, U> tt) {
    tt.node1.accept(this);
    int op1Id = lastNodeId;

    tt.node2.accept(this);
    int op2Id = lastNodeId;

    int opNodeId = addNode(tt.op.javaSign);

    addEdge(opNodeId, op1Id, "op1");
    addEdge(opNodeId, op2Id, "op2");
  }

  @Override
  public <T extends Node, U extends Node, V extends Node> void visit(IfNode<T, U, V> tt) {
    tt.testNode.accept(this);
    int testId = lastNodeId;

    tt.thenNode.accept(this);
    int thenId = lastNodeId;

    tt.elseNode.accept(this);
    int elseId = lastNodeId;

    int ifId = addNode("if");

    addEdge(ifId, testId, "test");
    addEdge(ifId, thenId, "then");
    addEdge(ifId, elseId, "else");
  }

  @Override
  public void visit(BlockNode block) {
    int lastNode = -1;
    int firstNode = -1;

    sb.append("subgraph cluster").append(clusterId++).append("{\n");

    int i = 0;

    for (Node n : block.nodes) {
      n.accept(this);
      if (firstNode == -1) {
        firstNode = lastNodeId;
        lastNode = lastNodeId;
      }

      if (lastNode != lastNodeId) {
        addEdge(lastNode, lastNodeId, "next " + ((i++) + 1));
      }

      lastNode = lastNodeId;
    }

    sb.append("}\n");

    lastNodeId = firstNode;
  }
}
