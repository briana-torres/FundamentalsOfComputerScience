import java.util.function.BiFunction;
import java.util.function.Function;

import tester.*;

// represents an arithmetic expression
interface IArith {
  // visits the IArith to access the fields
  <R> R accept(IArithVisitor<R> visitor);
}

// represents a constant
class Const implements IArith {
  double num;

  Const(double num) {
    this.num = num;
  }

  // accepts the visitor function for a Const
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitConst(this);
  }

}

class UnaryFormula implements IArith {
  Function<Double, Double> func;
  String name;
  IArith child;

  UnaryFormula(Function<Double, Double> func, String name, IArith child) {
    this.func = func;
    this.name = name;
    this.child = child;
  }

  // accepts the visitor function for a UnaryFormula
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitUnaryFormula(this);
  }
}

class BinaryFormula implements IArith {
  BiFunction<Double, Double, Double> func;
  String name;
  IArith left;
  IArith right;

  BinaryFormula(BiFunction<Double, Double, Double> func, String name, IArith left, IArith right) {
    this.func = func;
    this.name = name;
    this.left = left;
    this.right = right;
  }

  // accepts the visitor function for a BinaryFormula
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitBinaryFormula(this);
  }
}

interface IArithVisitor<R> {
  // calls the accept methods
  R apply(IArith a);

  // visits the Const class
  R visitConst(Const c);

  // visits the UnaryFormula class
  R visitUnaryFormula(UnaryFormula u);

  // visits the BinaryFormula class
  R visitBinaryFormula(BinaryFormula b);
}

class EvalVisitor implements IArithVisitor<Double> {

  // calls the accept method of an IArith
  public Double apply(IArith a) {
    return a.accept(this);
  }

  // returns the double value of a Const
  public Double visitConst(Const c) {
    return c.num;
  }

  // applys the func to the child of the given UnaryFormula
  public Double visitUnaryFormula(UnaryFormula u) {
    Double l = u.child.accept(this);
    return u.func.apply(l);
  }

  // applys the func to the left and right of the given BinaryFormula
  public Double visitBinaryFormula(BinaryFormula b) {
    Double l = b.left.accept(this);
    Double u = b.right.accept(this);

    return b.func.apply(l, u);

  }

}

class PrintVisitor implements IArithVisitor<String> {

  // calls the accept method of the given IArith
  public String apply(IArith a) {
    return a.accept(this);
  }

  // returns the double value of a Const as a String
  public String visitConst(Const c) {
    return Double.toString(c.num);
  }

  // produces a String showing the fully-parenthesized expression
  public String visitUnaryFormula(UnaryFormula u) {
    return "(" + u.name + " " + u.child.accept(this) + ")";
  }

  // produces a String showing the fully-parenthesized expression
  public String visitBinaryFormula(BinaryFormula b) {
    return "(" + b.name + " " + b.left.accept(this) + " " + b.right.accept(this) + ")";
  }

}

class DoublerVisitor implements IArithVisitor<IArith> {

  // calls the accept method on the given IArith
  public IArith apply(IArith a) {
    return a.accept(this);
  }

  // doubles the num value of the given Const
  public IArith visitConst(Const c) {
    return new Const(c.num * 2);
  }

  // produces another IArith, where every Const in the tree has been doubled.
  public IArith visitUnaryFormula(UnaryFormula u) {
    return new UnaryFormula(u.func, u.name, u.child.accept(this));
  }

  // produces another IArith, where every Const in the tree has been doubled.
  public IArith visitBinaryFormula(BinaryFormula b) {
    return new BinaryFormula(b.func, b.name, b.left.accept(this), b.right.accept(this));
  }

}

class NoNegativeResults implements IArithVisitor<Boolean> {

  // calls the accept method on the given IArith
  public Boolean apply(IArith a) {
    return a.accept(this);
  }

  // returns true if the Const is positive
  public Boolean visitConst(Const c) {
    return c.num >= 0;
  }

  // returns true if all the Const in the tree are positive
  public Boolean visitUnaryFormula(UnaryFormula u) {
    return u.child.accept(this) && u.accept(new EvalVisitor()) >= 0;
  }

  // returns true if all the Const in the tree are positive
  public Boolean visitBinaryFormula(BinaryFormula b) {
    return b.left.accept(this) && b.right.accept(this) && b.accept(new EvalVisitor()) >= 0;
  }

}

class SqrFunction implements Function<Double, Double> {

  // squares the given num
  public Double apply(Double num) {
    return num * num;
  }
}

class NegFunction implements Function<Double, Double> {
  // negates the given num
  public Double apply(Double num) {
    return num * -1.0;
  }
}

class PlusFunction implements BiFunction<Double, Double, Double> {

  // adds two given doubles together
  public Double apply(Double num1, Double num2) {
    return num1 + num2;
  }
}

class MinusFunction implements BiFunction<Double, Double, Double> {
  // subtracts two given doubles
  public Double apply(Double num1, Double num2) {
    return num1 - num2;
  }
}

class MulFunction implements BiFunction<Double, Double, Double> {

  // multiplies two given doubles
  public Double apply(Double num1, Double num2) {
    return num1 * num2;
  }
}

class DivFunction implements BiFunction<Double, Double, Double> {

  // divides two given doubles
  public Double apply(Double num1, Double num2) {
    return num1 / num2;
  }
}

class IArithExamples {
  IArithExamples() {
  }

  IArith c1 = new Const(1.0);
  IArith c2 = new Const(4.0);
  IArith c3 = new Const(-4.0);
  IArith c4 = new Const(-6.0);
  IArith c5 = new Const(8.0);
  IArith c6 = new Const(2.0);
  IArith c7 = new Const(-3.8);

  IArith u1 = new UnaryFormula(new NegFunction(), "neg", c1); // -1.0
  IArith u2 = new UnaryFormula(new SqrFunction(), "sqr", c2); // 16
  IArith u3 = new UnaryFormula(new NegFunction(), "neg", u2); // -16

  IArith b1 = new BinaryFormula(new PlusFunction(), "plus", c1, c2); // 5.0
  IArith b2 = new BinaryFormula(new MinusFunction(), "minus", u2, c1); // 15
  IArith b3 = new BinaryFormula(new MulFunction(), "mul", b1, c5); // 40
  IArith b4 = new BinaryFormula(new DivFunction(), "div", u2, c6); // 8

  IArith u4 = new UnaryFormula(new SqrFunction(), "sqr", b1); // 25

  IArith u3Accept = new UnaryFormula(new NegFunction(), "neg",
      new UnaryFormula(new SqrFunction(), "sqr", c5));
  IArith b3Accept = new BinaryFormula(new MulFunction(), "mul",
      new BinaryFormula(new PlusFunction(), "plus", new Const(2.0), new Const(8.0)),
      new Const(16.0));

  void testAccept(Tester t) {
    t.checkInexact(c1.accept(new EvalVisitor()), 1.0, 0.1);
    t.checkInexact(u1.accept(new EvalVisitor()), -1.0, 0.1);
    t.checkInexact(b1.accept(new EvalVisitor()), 5.0, 0.1);
    t.checkExpect(c6.accept(new PrintVisitor()), "2.0");
    t.checkExpect(u2.accept(new PrintVisitor()), "(sqr 4.0)");
    t.checkExpect(b2.accept(new PrintVisitor()), "(minus (sqr 4.0) 1.0)");
    t.checkInexact(c3.accept(new DoublerVisitor()), new Const(-8.0), 0.1);
    t.checkInexact(u3.accept(new DoublerVisitor()), u3Accept, 0.1);
    t.checkInexact(b3.accept(new DoublerVisitor()), b3Accept, 0.1);
    t.checkExpect(c4.accept(new NoNegativeResults()), false);
    t.checkExpect(u2.accept(new NoNegativeResults()), true);
    t.checkExpect(b3.accept(new NoNegativeResults()), true);
  }

  void testApply(Tester t) {
    t.checkInexact(new EvalVisitor().apply(c1), 1.0, 0.1);
    t.checkInexact(new EvalVisitor().apply(u1), -1.0, 0.1);
    t.checkInexact(new EvalVisitor().apply(b1), 5.0, 0.1);
    t.checkExpect(new PrintVisitor().apply(c6), "2.0");
    t.checkExpect(new PrintVisitor().apply(u2), "(sqr 4.0)");
    t.checkExpect(new PrintVisitor().apply(b2), "(minus (sqr 4.0) 1.0)");
    t.checkInexact(new DoublerVisitor().apply(c3), new Const(-8.0), 0.1);
    t.checkInexact(new DoublerVisitor().apply(u3), u3Accept, 0.1);
    t.checkInexact(new DoublerVisitor().apply(b3), b3Accept, 0.1);
    t.checkExpect(new NoNegativeResults().apply(c4), false);
    t.checkExpect(new NoNegativeResults().apply(u2), true);
    t.checkExpect(new NoNegativeResults().apply(b3), true);
    t.checkInexact(new SqrFunction().apply(4.0), 16.0, 0.1);
    t.checkInexact(new SqrFunction().apply(-4.0), 16.0, 0.1);
    t.checkInexact(new SqrFunction().apply(0.0), 0.0, 0.1);
    t.checkInexact(new NegFunction().apply(3.0), -3.0, 0.1);
    t.checkInexact(new NegFunction().apply(-3.0), 3.0, 0.1);
    t.checkInexact(new NegFunction().apply(0.0), 0.0, 0.1);
    t.checkInexact(new PlusFunction().apply(4.0, 2.0), 6.0, 0.1);
    t.checkInexact(new PlusFunction().apply(-4.0, 2.0), -2.0, 0.1);
    t.checkInexact(new PlusFunction().apply(0.0, 0.0), 0.0, 0.1);
    t.checkInexact(new MinusFunction().apply(3.0, 1.0), 2.0, 0.1);
    t.checkInexact(new MinusFunction().apply(0.0, 0.0), 0.0, 0.1);
    t.checkInexact(new MinusFunction().apply(3.0, -1.0), 4.0, 0.1);
    t.checkInexact(new MulFunction().apply(3.0, 2.0), 6.0, 0.1);
    t.checkInexact(new MulFunction().apply(0.0, 3.0), 0.0, 0.1);
    t.checkInexact(new MulFunction().apply(-3.0, 2.0), -6.0, 0.1);
    t.checkInexact(new DivFunction().apply(6.0, 2.0), 3.0, 0.1);
    t.checkInexact(new DivFunction().apply(-6.0, 2.0), -3.0, 0.1);
    t.checkInexact(new DivFunction().apply(0.0, 2.0), 0.0, 0.1);
  }

  void testEvalVisitor(Tester t) {
    t.checkInexact(new EvalVisitor().visitConst((Const) c1), 1.0, 0.1);
    t.checkInexact(new EvalVisitor().visitUnaryFormula((UnaryFormula) u1), -1.0, 0.1);
    t.checkInexact(new EvalVisitor().visitBinaryFormula((BinaryFormula) b1), 5.0, 0.1);
  }

  void testPrintVisitor(Tester t) {
    t.checkExpect(new PrintVisitor().visitConst((Const) c6), "2.0");
    t.checkExpect(new PrintVisitor().visitUnaryFormula((UnaryFormula) u2), "(sqr 4.0)");
    t.checkExpect(new PrintVisitor().visitBinaryFormula((BinaryFormula) b2),
        "(minus (sqr 4.0) 1.0)");
  }

  void testDoublerVisitor(Tester t) {
    t.checkInexact(new DoublerVisitor().visitConst((Const) c3), new Const(-8.0), 0.1);
    t.checkInexact(new DoublerVisitor().visitUnaryFormula((UnaryFormula) u3), u3Accept, 0.1);
    t.checkInexact(new DoublerVisitor().visitBinaryFormula((BinaryFormula) b3), b3Accept, 0.1);
  }

  void testNoNegativeResults(Tester t) {
    t.checkExpect(new NoNegativeResults().visitConst((Const) c4), false);
    t.checkExpect(new NoNegativeResults().visitConst((Const) c7), false);
    t.checkExpect(new NoNegativeResults().visitUnaryFormula((UnaryFormula) u2), true);
    t.checkExpect(new NoNegativeResults().visitBinaryFormula((BinaryFormula) b3), true);
  }

}