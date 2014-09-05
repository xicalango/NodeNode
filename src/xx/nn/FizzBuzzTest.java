package xx.nn;

import xx.nn.nodes.CmpNode.Op;
import xx.nn.nodes.Node;
import xx.nn.nodes.Terminal;

public class FizzBuzzTest {

  public static void main(String[] args) {

    Terminal<String> fizz = new Terminal<>(String.class);
    Terminal<String> buzz = new Terminal<>(String.class);
    Terminal<String> aggregate = new Terminal<>(String.class);

    Terminal<Integer> i = new Terminal<>(Integer.class);
    Terminal<Integer> max = new Terminal<>(Integer.class);
    Terminal<Integer> _3 = new Terminal<>(Integer.class);
    Terminal<Integer> _5 = new Terminal<>(Integer.class);

    Node fizzBuzzBlock = Node._block(outer -> {

      outer.$(Node._for(i.assign(1), i.cmp(Op.LT, max), i.assign(i.add(1)), body -> {
        body.$(aggregate.assign(""));

        body.$(Node._if(i.mod(_3).eq(0), aggregate.assign(aggregate.add(fizz))));
        body.$(Node._if(i.mod(_5).eq(0), aggregate.assign(aggregate.add(buzz))));
        body.$(Node._if(aggregate.eq(""), aggregate.assign(i.cast(String.class))));

        body.$(aggregate.exec(System.out::println, "System.out.println"));
      }));
    });

    FunctionBuilderVisitor fbc = new FunctionBuilderVisitor();

    fbc.traverse(fizzBuzzBlock);

    Context ctx = new SimpleContext();
    ctx.put(fizz, "fizz");
    ctx.put(buzz, "buzz");
    ctx.put(_3, 3);
    ctx.put(_5, 5);
    ctx.put(max, 21);

    fbc.cfn.apply(ctx);

    fizzBuzzBlock.accept(new PrintVisitor());

    Context javaGenCtx = new SimpleContext();
    javaGenCtx.put(fizz, "\"fizz\"");
    javaGenCtx.put(buzz, "\"buzz\"");
    javaGenCtx.put(_3, 3);
    javaGenCtx.put(_5, 5);
    javaGenCtx.put(max, 21);
    javaGenCtx.put(i, "i");
    javaGenCtx.put(aggregate, "aggregate");

    JavaCodeVisitor jvc = new JavaCodeVisitor(javaGenCtx);
    jvc.traverse(fizzBuzzBlock);

    System.out.println(jvc.toString());

  }
}
