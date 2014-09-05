package xx.nn;

import xx.nn.nodes.Node;
import xx.nn.nodes.Terminal;

public final class SumFn {

  public final Terminal<Integer> sum = new Terminal<>(Integer.class);

  // private final Terminal<Integer> i = new Terminal<>(Integer.class);

  public Node genTreeFor(int[] array) {

    return Node._block(block -> {

      block.$(sum.assign(0));

      for (int i = 0; i < array.length; i++) {
        block.$(sum.assign(sum.add(array[i])));
      }

    });

  }

  public static void main(String[] args) {

    SumFn fn = new SumFn();

    int[] array = new int[] { 1, 1, 1, 1 };

    Node sumNode = fn.genTreeFor(array);

    FunctionBuilderVisitor fbv = new FunctionBuilderVisitor();

    fbv.traverse(sumNode);

    System.out.println(fbv.cfn.apply(new SimpleContext()));

    sumNode.accept(new PrintVisitor());

  }

}
