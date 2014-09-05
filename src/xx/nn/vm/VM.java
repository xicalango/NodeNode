package xx.nn.vm;

public class VM {

  private int[] ram = new int[100];
  private int[] reg = new int[6];

  public int getRam(int i) {
    return ram[i];
  }

  public void run(Op[] ops) {

    reg[5] = 0;
    boolean running = true;

    while (running) {
      if (reg[5] >= ops.length) {
        running = false;
        continue;
      }

      final Op curop = ops[reg[5]];
      reg[5]++;

      switch (curop.opCode) {
        case ADD:
          reg[curop.par1] = reg[curop.par1] + reg[curop.par2];
          break;
        case EQ:
          reg[curop.par1] = reg[curop.par1] == reg[curop.par2] ? 1 : 0;
          break;
        case GT:
          reg[curop.par1] = reg[curop.par1] > reg[curop.par2] ? 1 : 0;
          break;
        case GTEQ:
          reg[curop.par1] = reg[curop.par1] >= reg[curop.par2] ? 1 : 0;
          break;
        case JZ:
          reg[5] = reg[curop.par1] == 0 ? curop.par2 : reg[5];
          break;
        case JMP:
          reg[5] = curop.par1;
          break;
        case JNZ:
          reg[5] = reg[curop.par1] != 0 ? curop.par2 : reg[5];
          break;
        case LOAD:
          reg[curop.par1] = ram[curop.par2];
          break;
        case LT:
          reg[curop.par1] = reg[curop.par1] < reg[curop.par2] ? 1 : 0;
          break;
        case LTEQ:
          reg[curop.par1] = reg[curop.par1] <= reg[curop.par2] ? 1 : 0;
          break;
        case MOD:
          reg[curop.par1] = reg[curop.par1] % reg[curop.par2];
          break;
        case MUL:
          reg[curop.par1] = reg[curop.par1] * reg[curop.par2];
          break;
        case NOP:
          break;
        case STORE:
          ram[curop.par1] = reg[curop.par2];
          break;
        case SUB:
          reg[curop.par1] = reg[curop.par1] - reg[curop.par2];
          break;
        case PUT:
          reg[curop.par1] = curop.par2;
          break;
        case MOV:
          reg[curop.par1] = reg[curop.par2];
          break;
      }
    }

  }
}
