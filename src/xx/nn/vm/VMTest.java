package xx.nn.vm;

public class VMTest {

  public static void main(String[] args) {
    VM vm = new VM();

    vm.run(new Op[] { //

    OpCode.PUT.op(1, 1), //
        OpCode.PUT.op(2, 2), //
        OpCode.ADD.op(1, 2), //
        OpCode.STORE.op(0, 1)

    });

    System.out.println(vm.getRam(0));

  }

}
