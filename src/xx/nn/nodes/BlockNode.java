package xx.nn.nodes;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import xx.nn.Visitor;

public class BlockNode implements StatementNode {

  public final List<Node> nodes;

  public BlockNode(List<Node> nodes) {
    this.nodes = nodes;
  }

  public BlockNode(Consumer<BlockBuilder> nodeBuilder) {
    nodes = new LinkedList<>();
    nodeBuilder.accept(nodes::add);
  }

  @Override
  public void accept(Visitor v) {
    v.visit(this);
  }

  @Override
  public Set<Class<?>> getTypes() {
    return nodes.get(nodes.size() - 1).getTypes();
  }

  @Override
  public boolean isConst() {
    return nodes.get(nodes.size() - 1).isConst();
  }

}
