package xx.nn;

import xx.nn.nodes.CmpNode.Op;
import xx.nn.nodes.Constant;
import xx.nn.nodes.Node;
import xx.nn.nodes.Terminal;

public class Test2 {

  public static void main(String[] args) {

    int[] ints = new int[] { 5, 3, 1 };

    Terminal<Integer> i = new Terminal<>(Integer.class);

    Node block = Node._block(bb -> {
      bb.$(i.assign(0));

      for (int ii = 0; ii < ints.length; ii++) {
        bb.$(i.assign(i.add(ints[ii])));
      }

      bb.$(Node._if(Constant.mk(true).cmp(Op.EQ, false), i.assign(1)));
    });

    Context ctx = new SimpleContext();
    ctx.put(i, "i");

    // GraphVizVisitor gvv = new GraphVizVisitor(ctx);
    // gvv.init();
    // block.accept(gvv);
    // gvv.finish();
    //
    // System.out.println(gvv.toString());

    JavaCodeVisitor jcv = new JavaCodeVisitor(ctx);
    jcv.traverse(block);

    System.out.println(jcv);

  }

}
