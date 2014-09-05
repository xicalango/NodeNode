package xx.nn.nodes;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import xx.nn.Visitor;

public class ActionNode implements Node {

  public Node node;
  public final Consumer<Object> action;
  public final Optional<String> mnemonic;

  public ActionNode(Node node, Consumer<Object> action, String mnemonic) {
    this.node = node;
    this.action = action;
    this.mnemonic = Optional.ofNullable(mnemonic);
  }

  @Override
  public Set<Class<?>> getTypes() {
    return node.getTypes();
  }

  @Override
  public void accept(Visitor v) {
    v.visit(this);
  }

  @Override
  public boolean isConst() {
    return node.isConst();
  }

}
