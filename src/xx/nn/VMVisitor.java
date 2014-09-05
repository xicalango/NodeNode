package xx.nn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xx.nn.nodes.AddNode;
import xx.nn.nodes.AssignStmtNode;
import xx.nn.nodes.CmpNode;
import xx.nn.nodes.Constant;
import xx.nn.nodes.EqNode;
import xx.nn.nodes.Node;
import xx.nn.nodes.Terminal;
import xx.nn.nodes.WhileStmtNode;
import xx.nn.vm.Op;
import xx.nn.vm.OpCode;

public class VMVisitor implements Visitor {

  private List<Op> ops = new ArrayList<>();
  private Map<Terminal<?>, Integer> memoryMap = new HashMap<>();
  private int curMapId = 1;

  private int loadDst = 0;

  public List<Op> getOps() {
    return Collections.unmodifiableList(ops);
  }

  @Override
  public <T> void visit(Constant<T> t) {
    if (t.getType() == Integer.class) {
      ops.add(OpCode.PUT.op(loadDst, (int) t.value));
    } else if (t.getType() == Integer.class) {
      ops.add(OpCode.PUT.op(loadDst, (boolean) t.value ? 1 : 0));
    } else {
      throw new UnsupportedOperationException();
    }
  }

  @Override
  public <T> void visit(Terminal<T> t) {
    int memoryLocation;
    if (!memoryMap.containsKey(t)) {
      throw new IllegalStateException();
    } else {
      memoryLocation = memoryMap.get(t);
    }

    ops.add(OpCode.LOAD.op(loadDst, memoryLocation));
  }

  @Override
  public <T extends Node, U extends Node> void visit(AddNode<T, U> tt) {
    loadDst = 0;
    tt.node1.accept(this);
    loadDst = 1;
    tt.node2.accept(this);
    ops.add(OpCode.ADD.op(0, 1));
  }

  @Override
  public <T extends Node, U extends Node> void visit(AssignStmtNode<T, U> smt) {
    Terminal<T> assignee = smt.node1.leaf();
    int loc;
    if (!memoryMap.containsKey(assignee)) {
      loc = curMapId;
      memoryMap.put(assignee, curMapId++);
    } else {
      loc = memoryMap.get(assignee);
    }

    smt.node2.accept(this);

    ops.add(OpCode.STORE.op(loc, 0));

  }

  @Override
  public <T extends Node, U extends Node> void visit(CmpNode<T, U> tt) {
    tt.node2.accept(this);
    ops.add(OpCode.MOV.op(1, 0));
    tt.node1.accept(this);

    switch (tt.op) {
      case EQ:
        ops.add(OpCode.EQ.op(0, 1));
        break;
      case GT:
        ops.add(OpCode.GT.op(0, 1));
        break;
      case GTEQ:
        ops.add(OpCode.GTEQ.op(0, 1));
        break;
      case LT:
        ops.add(OpCode.LT.op(0, 1));
        break;
      case LTEQ:
        ops.add(OpCode.LTEQ.op(0, 1));
        break;
    }

  }

  @Override
  public <T extends Node, U extends Node> void visit(WhileStmtNode<T, U> tt) {
    int testPC = ops.size();

    tt.testNode.accept(this);

    int testPCEnd = ops.size();

    ops.add(OpCode.JNZ.op(0, testPCEnd + 2));
    ops.add(OpCode.NOP.op(0, 0)); // placeholder

    tt.blockNode.accept(this);

    ops.add(OpCode.JMP.op(testPC, 0));

    int blockEndPC = ops.size();

    ops.set(testPCEnd + 1, OpCode.JMP.op(blockEndPC, 0));

  }

  @Override
  public <T extends Node, U extends Node> void visit(EqNode<T, U> tt) {
    tt.node1.accept(this);
    ops.add(OpCode.MOV.op(1, 0));
    tt.node2.accept(this);

    ops.add(OpCode.EQ.op(0, 1));
  }
}
