package xx.nn.nodes;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;

import xx.nn.Visitor;

public interface Node {

  public default ActionNode exec(Consumer<Object> action) {
    return new ActionNode(this, action, null);
  }

  public default ActionNode exec(Consumer<Object> action, String mnemonic) {
    return new ActionNode(this, action, mnemonic);
  }

  public default <T> AssignStmtNode<Node, Node> assign(Object other) {
    return new AssignStmtNode<>(this, new Constant<>(other));
  }

  public default ModuloNode<Node, Node> mod(Object other) {
    return new ModuloNode<>(this, new Constant<>(other));
  }

  public default AddNode<Node, Node> add(Object other) {
    return new AddNode<>(this, new Constant<>(other));
  }

  public default SubNode<Node, Node> sub(Object other) {
    return new SubNode<>(this, new Constant<>(other));
  }

  public default MulNode<Node, Node> mul(Object other) {
    return new MulNode<>(this, new Constant<>(other));
  }

  public default EqNode<Node, Node> eq(Object other) {
    return new EqNode<>(this, new Constant<>(other));
  }

  public default CmpNode<Node, Node> cmp(CmpNode.Op op, Object other) {
    return new CmpNode<>(this, new Constant<>(other), op);
  }

  public default IfNode<Node, Node, Node> test(Object thenNode, Object elseNode) {
    return new IfNode<>(this, new Constant<>(thenNode), new Constant<>(elseNode));
  }

  public default IfNode<Node, Node, Node> test(Object thenNode) {
    return new IfNode<>(this, new Constant<>(thenNode), NOP_NODE);
  }

  public default <T> AssignStmtNode<Node, Node> assign(Node other) {
    return new AssignStmtNode<>(this, other);
  }

  public default ModuloNode<Node, Node> mod(Node other) {
    return new ModuloNode<>(this, other);
  }

  public default AddNode<Node, Node> add(Node other) {
    return new AddNode<>(this, other);
  }

  public default SubNode<Node, Node> sub(Node other) {
    return new SubNode<>(this, other);
  }

  public default MulNode<Node, Node> mul(Node other) {
    return new MulNode<>(this, other);
  }

  public default EqNode<Node, Node> eq(Node other) {
    return new EqNode<>(this, other);
  }

  public default CmpNode<Node, Node> cmp(CmpNode.Op op, Node other) {
    return new CmpNode<>(this, other, op);
  }

  public default IfNode<Node, Node, Node> test(Node thenNode, Node elseNode) {
    return new IfNode<>(this, thenNode, elseNode);
  }

  public default <D> CastNode<Node, D> cast(Class<D> to) {
    return new CastNode<Node, D>(this, to);
  }

  public static IfNode<Node, Node, Node> _if(Node testNode, Node thenNode, Node elseNode) {
    return new IfNode<>(testNode, thenNode, elseNode);
  }

  public static IfNode<Node, Node, Node> _if(Node testNode, Node thenNode) {
    return new IfNode<>(testNode, thenNode, NOP_NODE);
  }

  public static WhileStmtNode<Node, Node> _while(Node testNode, Consumer<BlockBuilder> blockBuilder) {
    return new WhileStmtNode<Node, Node>(testNode, new BlockNode(blockBuilder));
  }

  public static Node _for(Node init, Node testNode, Node recurring, Consumer<BlockBuilder> blockBuilder) {
    return _block(outer -> {
      outer.$(init);
      outer.$(_while(testNode, blockBuilder.andThen(inner -> inner.$(recurring))));
    });
  }

  public static BlockNode _block(Consumer<BlockBuilder> blockBuilder) {
    return new BlockNode(blockBuilder);
  }

  public static BlockNode _block(Node... nodes) {
    return new BlockNode(Arrays.asList(nodes));
  }

  public static final Node NOP_NODE = new Node() {

    @Override
    public Set<Class<?>> getTypes() {
      return Collections.emptySet();
    }

    @Override
    public void accept(Visitor v) {
      v.visitNop();
    }

    @Override
    public boolean isConst() {
      return true;
    }

  };

  public default <E> Terminal<E> leaf() {
    return null;
  }

  public boolean isConst();

  public Set<Class<?>> getTypes();

  public void accept(Visitor v);
}
