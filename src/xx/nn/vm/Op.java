package xx.nn.vm;

public class Op {

  public OpCode opCode;
  public int par1;
  public int par2;

  public Op(OpCode opCode, int par1, int par2) {
    this.opCode = opCode;
    this.par1 = par1;
    this.par2 = par2;
  }

  @Override
  public String toString() {
    return "Op [opCode=" + opCode + " \t, par1=" + par1 + "\t, par2=" + par2 + "]";
  }

}
