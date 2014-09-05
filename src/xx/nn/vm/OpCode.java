package xx.nn.vm;

public enum OpCode {

  NOP,
  LOAD,
  STORE,
  PUT,
  MOV,
  ADD,
  SUB,
  MUL,
  MOD,
  LT,
  GT,
  EQ,
  LTEQ,
  GTEQ,
  JZ,
  JMP,
  JNZ;

  public Op op(int par1, int par2) {
    return new Op(this, par1, par2);
  }

}
