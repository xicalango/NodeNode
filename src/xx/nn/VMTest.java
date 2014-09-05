package xx.nn;

import xx.nn.nodes.CmpNode.Op;
import xx.nn.nodes.Node;
import xx.nn.nodes.Terminal;
import xx.nn.vm.VM;

public class VMTest {

  public static void main(String[] args) {

    Terminal<Integer> t = Terminal.create(Integer.class);
    Terminal<Integer> u = Terminal.create(Integer.class);

    Node store = Node._block(block -> {
      block.$(u.assign(0));
      block.$(Node._while(u.cmp(Op.LT, 3), bb -> {
        bb.$(u.assign(u.add(1)));
        bb.$(u.assign(u.add(1)));

      }));
    });

    VMVisitor vmv = new VMVisitor();
    vmv.traverse(store);

    VM vm = new VM();

    vm.run(vmv.getOps().toArray(new xx.nn.vm.Op[0]));

    for (int i = 0; i < vmv.getOps().size(); i++) {
      System.out.println(i + " " + vmv.getOps().get(i));
    }

    // vmv.getOps().forEach(System.out::println);

    // System.out.println(vmv.getOps());

    System.out.println(vm.getRam(1));
    // System.out.println(vm.getRam(2));

  }
}
