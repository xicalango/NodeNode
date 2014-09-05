package xx.nn;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

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

public class FunctionBuilderVisitor implements Visitor {

  public Function<Context, Object> cfn;
  private static final ConstEvalVisitor CONST_EVAL_VISITOR = new ConstEvalVisitor();

  @Override
  public <T> void visit(Terminal<T> t) {
    cfn = c -> {
      return c.get(t);
    };
  }

  @Override
  public <T> void visit(Constant<T> t) {
    cfn = c -> t.value;
  }

  @Override
  public <T extends Node, U extends Node> void visit(AddNode<T, U> tt) {
    if (tt.isConst()) {
      CONST_EVAL_VISITOR.traverse(tt);
      cfn = c -> CONST_EVAL_VISITOR.result;
      return;
    }

    tt.node1.accept(this);
    Function<Context, Object> fn1 = cfn;

    tt.node2.accept(this);
    Function<Context, Object> fn2 = cfn;

    Set<Class<?>> types = tt.getTypes();

    if (types.size() == 1) {
      if (types.contains(Integer.class)) {
        cfn = c -> {
          int i1 = (int) fn1.apply(c);
          int i2 = (int) fn2.apply(c);
          return i1 + i2;
        };
        return;
      }
    }

    cfn = c -> {
      Object o1 = fn1.apply(c);
      Object o2 = fn2.apply(c);

      return o1.toString() + o2.toString();
    };
  }

  @Override
  public <T extends Node, U extends Node> void visit(SubNode<T, U> tt) {
    final Set<Class<?>> types = tt.getTypes();
    if (types.size() != 1 || !types.contains(Integer.class)) {
      throw new IllegalArgumentException("sub not defined for: " + types);
    }

    if (tt.isConst()) {
      CONST_EVAL_VISITOR.traverse(tt);
      cfn = c -> CONST_EVAL_VISITOR.result;
      return;
    }

    tt.node1.accept(this);
    Function<Context, Object> fn1 = cfn;

    tt.node2.accept(this);
    Function<Context, Object> fn2 = cfn;

    cfn = c -> {
      int i1 = (int) fn1.apply(c);
      int i2 = (int) fn2.apply(c);
      return i1 - i2;
    };
  }

  @Override
  public <T extends Node, U extends Node> void visit(MulNode<T, U> tt) {
    final Set<Class<?>> types = tt.getTypes();
    if (types.size() != 1 || !types.contains(Integer.class)) {
      throw new IllegalArgumentException("mul not defined for: " + types);
    }

    if (tt.isConst()) {
      CONST_EVAL_VISITOR.traverse(tt);
      cfn = c -> CONST_EVAL_VISITOR.result;
      return;
    }

    tt.node1.accept(this);
    Function<Context, Object> fn1 = cfn;

    tt.node2.accept(this);
    Function<Context, Object> fn2 = cfn;

    cfn = c -> {
      int i1 = (int) fn1.apply(c);
      int i2 = (int) fn2.apply(c);
      return i1 * i2;
    };
  }

  @Override
  public <T extends Node, U extends Node> void visit(ModuloNode<T, U> tt) {
    final Set<Class<?>> types = tt.getTypes();
    if (types.size() != 1 || !types.contains(Integer.class)) {
      throw new IllegalArgumentException("modulo not defined for: " + types);
    }

    if (tt.isConst()) {
      CONST_EVAL_VISITOR.traverse(tt);
      cfn = c -> CONST_EVAL_VISITOR.result;
      return;
    }

    tt.node1.accept(this);
    Function<Context, Object> fn1 = cfn;

    tt.node2.accept(this);
    Function<Context, Object> fn2 = cfn;

    cfn = c -> {
      int i1 = (int) fn1.apply(c);
      int i2 = (int) fn2.apply(c);
      return i1 % i2;
    };
  }

  @Override
  public <T extends Node, U extends Node> void visit(EqNode<T, U> tt) {

    if (tt.node1 == tt.node2) {
      cfn = c -> true;
      return;
    }

    if (tt.isConst()) {
      CONST_EVAL_VISITOR.traverse(tt);
      cfn = c -> CONST_EVAL_VISITOR.result;
      return;
    }

    tt.node1.accept(this);
    Function<Context, Object> fn1 = cfn;

    tt.node2.accept(this);
    Function<Context, Object> fn2 = cfn;

    cfn = c -> {
      Object o1 = fn1.apply(c);
      Object o2 = fn2.apply(c);
      return o1.equals(o2);
    };
  }

  @Override
  public <T extends Node, U extends Node, V extends Node> void visit(IfNode<T, U, V> tt) {
    if (tt.isConst()) {
      CONST_EVAL_VISITOR.traverse(tt);
      cfn = c -> CONST_EVAL_VISITOR.result;
      return;
    }

    if (tt.testNode.isConst()) {
      CONST_EVAL_VISITOR.traverse(tt.testNode);
      if ((boolean) CONST_EVAL_VISITOR.result) {
        tt.thenNode.accept(this);
      } else if (tt.elseNode != Node.NOP_NODE) {
        tt.elseNode.accept(this);
      } else {
        cfn = c -> null;
      }
      return;
    }

    tt.testNode.accept(this);
    Function<Context, Object> fn1 = cfn;

    tt.thenNode.accept(this);
    Function<Context, Object> fn2 = cfn;

    if (tt.elseNode == Node.NOP_NODE) {
      cfn = c -> {
        boolean test = (boolean) fn1.apply(c);
        if (test) {
          return fn2.apply(c);
        }
        return null;
      };
    } else {

      tt.elseNode.accept(this);
      Function<Context, Object> fn3 = cfn;

      cfn = c -> {
        boolean test = (boolean) fn1.apply(c);
        if (test) {
          return fn2.apply(c);
        } else {
          return fn3.apply(c);
        }
      };
    }
  }

  @Override
  public void visitNop() {
    cfn = c -> null;
  }

  @Override
  public <T extends Node, U extends Node> void visit(AssignStmtNode<T, U> smt) {

    Terminal<T> assignee = smt.node1.leaf();

    final Set<Class<?>> types = smt.getTypes();

    if (types.size() != 1) {
      throw new IllegalArgumentException("Incompatible types for assign: " + types);
    }

    if (smt.node1 == smt.node2) {
      cfn = c -> {
        return c.get(assignee);
      };

      return;
    }

    smt.node2.accept(this);
    Function<Context, Object> fn2 = cfn;

    cfn = c -> {
      final Object apply = fn2.apply(c);
      c.put(assignee, apply);
      return apply;
    };
  }

  @Override
  public void visit(BlockNode block) {
    if (block.isConst()) {
      CONST_EVAL_VISITOR.traverse(block);
      cfn = c -> CONST_EVAL_VISITOR.result;
      return;
    }

    List<Function<Context, Object>> fns = new LinkedList<>();
    for (Node n : block.nodes) {
      n.accept(this);
      fns.add(cfn);
    }

    cfn = c -> {
      Object result = null;
      for (Function<Context, Object> fn : fns) {
        result = fn.apply(c);
      }
      return result;
    };
  }

  @Override
  public <T extends Node, U extends Node> void visit(WhileStmtNode<T, U> tt) {

    tt.testNode.accept(this);
    Function<Context, Object> fn1 = cfn;

    tt.blockNode.accept(this);
    Function<Context, Object> fn2 = cfn;

    cfn = c -> {
      Object result = null;
      while ((boolean) fn1.apply(c)) {
        result = fn2.apply(c);
      }
      return result;
    };
  }

  @Override
  public <T extends Node, U extends Node> void visit(CmpNode<T, U> tt) {
    if (tt.isConst()) {
      CONST_EVAL_VISITOR.traverse(tt);
      cfn = c -> CONST_EVAL_VISITOR.result;
      return;
    }

    tt.node1.accept(this);
    Function<Context, Object> fn1 = cfn;

    tt.node2.accept(this);
    Function<Context, Object> fn2 = cfn;

    switch (tt.op) {
      case EQ:
        cfn = c -> fn1.apply(c).equals(fn2.apply(c));
        break;
      case GT:
        cfn = c -> {
          int i1 = (int) fn1.apply(c);
          int i2 = (int) fn2.apply(c);
          return i1 > i2;
        };
        break;
      case GTEQ:
        cfn = c -> {
          int i1 = (int) fn1.apply(c);
          int i2 = (int) fn2.apply(c);
          return i1 >= i2;
        };
        break;
      case LT:
        cfn = c -> {
          int i1 = (int) fn1.apply(c);
          int i2 = (int) fn2.apply(c);
          return i1 < i2;
        };
        break;
      case LTEQ:
        cfn = c -> {
          int i1 = (int) fn1.apply(c);
          int i2 = (int) fn2.apply(c);
          return i1 <= i2;
        };
        break;
    }

  }

  @Override
  public <T extends Node, D> void visit(CastNode<T, D> tt) {
    if (tt.isConst()) {
      CONST_EVAL_VISITOR.traverse(tt);
      cfn = c -> CONST_EVAL_VISITOR.result;
      return;
    }

    tt.node.accept(this);
    Function<Context, Object> fn1 = cfn;

    cfn = c -> {
      return fn1.apply(c);
    };
  }

  @Override
  public void visit(ActionNode tt) {
    if (tt.isConst()) {
      CONST_EVAL_VISITOR.traverse(tt);
      cfn = c -> CONST_EVAL_VISITOR.result;
      return;
    }

    tt.node.accept(this);
    Function<Context, Object> fn1 = cfn;

    cfn = c -> {
      final Object apply = fn1.apply(c);
      tt.action.accept(apply);
      return apply;
    };
  }

}
