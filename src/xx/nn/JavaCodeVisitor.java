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

public class JavaCodeVisitor implements Visitor {

  private StringBuilder sb = new StringBuilder();
  private static final ConstEvalVisitor CONST_EVAL_VISITOR = new ConstEvalVisitor();

  private Context ctx;

  public JavaCodeVisitor(Context ctx) {
    this.ctx = ctx;
  }

  public String toJavaVal(Object o) {
    if (o instanceof String) {
      return "\"" + o + "\"";
    } else {
      return String.valueOf(o);
    }
  }

  @Override
  public <T> void visit(Terminal<T> t) {
    sb.append(ctx.get(t));
  }

  @Override
  public <T> void visit(Constant<T> t) {
    sb.append(toJavaVal(t.value));
  }

  @Override
  public <T extends Node, U extends Node> void visit(AddNode<T, U> tt) {
    sb.append("(");
    tt.node1.accept(this);
    sb.append(" + ");
    tt.node2.accept(this);
    sb.append(")");
  }

  @Override
  public <T extends Node, U extends Node> void visit(SubNode<T, U> tt) {
    sb.append("(");
    tt.node1.accept(this);
    sb.append(" - ");
    tt.node2.accept(this);
    sb.append(")");
  }

  @Override
  public <T extends Node, U extends Node> void visit(MulNode<T, U> tt) {
    sb.append("(");
    tt.node1.accept(this);
    sb.append(" * ");
    tt.node2.accept(this);
    sb.append(")");
  }

  @Override
  public <T extends Node, U extends Node> void visit(ModuloNode<T, U> tt) {
    sb.append("(");
    tt.node1.accept(this);
    sb.append(" % ");
    tt.node2.accept(this);
    sb.append(")");
  }

  @Override
  public <T extends Node, U extends Node> void visit(EqNode<T, U> tt) {
    sb.append("(");
    tt.node1.accept(this);
    sb.append(" == ");
    tt.node2.accept(this);
    sb.append(")");
  }

  @Override
  public <T extends Node, U extends Node, V extends Node> void visit(IfNode<T, U, V> tt) {

    if (tt.testNode.isConst()) {
      CONST_EVAL_VISITOR.traverse(tt.testNode);
      if ((boolean) CONST_EVAL_VISITOR.result) {
        tt.thenNode.accept(this);
      } else {
        if (tt.elseNode != Node.NOP_NODE) {
          tt.elseNode.accept(this);
        }
      }
    } else {

      sb.append("if(");
      tt.testNode.accept(this);
      sb.append(") {\n");
      tt.thenNode.accept(this);
      sb.append("}\n");
      if (tt.elseNode != Node.NOP_NODE) {
        sb.append("else {\n");
        tt.elseNode.accept(this);
        sb.append("}\n");
      }
    }
  }

  @Override
  public String toString() {
    return sb.toString();
  }

  @Override
  public <T extends Node, U extends Node> void visit(AssignStmtNode<T, U> smt) {

    if (smt.node1 == smt.node2) {
      return;
    }

    smt.node1.accept(this);

    if (smt.node2 instanceof AddNode<?, ?>) {
      AddNode<?, ?> addNode = (AddNode<?, ?>) smt.node2;

      if (addNode.node1 == smt.node1) {
        if (addNode.node2.isConst()) {
          CONST_EVAL_VISITOR.traverse(addNode.node2);
          if (CONST_EVAL_VISITOR.result.equals(1)) {
            sb.append("++;\n");
          } else {
            sb.append(" += ").append(CONST_EVAL_VISITOR.result).append(";\n");
          }
        } else {
          sb.append(" += ");
          addNode.node2.accept(this);
          sb.append(";\n");
        }
        return;
      }

    } else {

      sb.append(" = ");
      smt.node2.accept(this);
      sb.append(";\n");
    }
  }

  @Override
  public <T extends Node, U extends Node> void visit(WhileStmtNode<T, U> tt) {
    sb.append("while(");
    tt.testNode.accept(this);
    sb.append(") {\n");
    tt.blockNode.accept(this);
    sb.append("}\n");
  }

  @Override
  public <T extends Node, U extends Node> void visit(CmpNode<T, U> tt) {
    sb.append("(");
    tt.node1.accept(this);
    sb.append(tt.op.javaSign);
    tt.node2.accept(this);
    sb.append(")");
  }

  @Override
  public <T extends Node, D> void visit(CastNode<T, D> tt) {
    if (tt.destinationType == String.class) {
      sb.append("String.valueOf(");
      tt.node.accept(this);
      sb.append(")");
    } else {
      sb.append("(").append(tt.destinationType.getCanonicalName()).append(")");
      tt.node.accept(this);
    }
  }

  @Override
  public void visit(ActionNode tt) {
    tt.mnemonic.ifPresent(mn -> {
      sb.append(mn).append("(");
      tt.node.accept(this);
      sb.append(");\n");
    });
  }

}
