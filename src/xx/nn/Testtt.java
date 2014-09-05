package xx.nn;

public class Testtt {

  public static void main(String[] args) {
    int i = 1;
    while ((i < 21)) {
      String aggregate = "";
      if (((i % 3) == 0)) {
        aggregate = (aggregate + "fizz");
      }
      if (((i % 5) == 0)) {
        aggregate = (aggregate + "buzz");
      }
      if ((aggregate == "")) {
        aggregate = String.valueOf(i);
      }
      System.out.println(aggregate);
      i = (i + 1);
    }
  }

}
