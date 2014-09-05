package xx.nn;

import java.util.function.Function;

import xx.nn.nodes.CmpNode.Op;
import xx.nn.nodes.Node;
import xx.nn.nodes.Terminal;

public class Test {

  public static Node increment(Node which) {
    return which.assign(which.add(1));
  }

  public static Node addAndGet(Node a, Node b) {
    return a.assign(a.add(b));
  }

  public static Node returnNode(Node a) {
    return a.assign(a);
  }

  public static void main(String[] args) {

    Context ctx = new SimpleContext();

    Terminal<Integer> i = new Terminal<>(Integer.class);
    Terminal<Integer> j = new Terminal<>(Integer.class);
    Terminal<Integer> k = new Terminal<>(Integer.class);

    ctx.put(i, "i");
    ctx.put(j, "j");
    ctx.put(k, "k");

    Node block = Node._block(ns -> {
      ns.$(j.assign(0));

      ns.$(Node._for(i.assign(0), i.cmp(Op.LT, 10), increment(i), wn -> {
        wn.$(addAndGet(j, i));
      }));

      ns.$(returnNode(j));
    });

    JavaCodeVisitor vv = new JavaCodeVisitor(ctx);

    FunctionBuilderVisitor fbv = new FunctionBuilderVisitor();

    block.accept(vv);

    block.accept(fbv);
    Function<Context, Object> fn1 = fbv.cfn;

    Context ctx1 = new SimpleContext();

    ctx1.put(i, 3);
    ctx1.put(j, 2);
    ctx1.put(k, 5);

    block.accept(new PrintVisitor());

    System.out.println(vv.toString());

    System.out.println(fn1.apply(ctx1));

    GraphVizVisitor gvv = new GraphVizVisitor(ctx);
    gvv.init();
    block.accept(gvv);
    gvv.finish();

    System.out.println(gvv.toString());

  }
}
