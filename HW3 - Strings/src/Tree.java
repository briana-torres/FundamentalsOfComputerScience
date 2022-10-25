import tester.*;
import javalib.worldimages.*;
import javalib.funworld.*;
import javalib.worldcanvas.WorldCanvas;

import java.awt.Color;

interface ITree {
  // produces image of given tree object 
  WorldImage draw();
  
  // produces image of combined tree object 
  WorldImage drawCombine(); 
  
  // returns true if any stems or branches face downwards 
  boolean isDrooping();
  
  // returns new tree object 
  ITree combineHelper(double angle); 
  
  // returns the combined image of two trees 
  ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree);
  
  // returns the width of a tree object 
  double getWidth();  
}

class Leaf implements ITree {
  int size; // represents the radius of the leaf
  Color color; // the color to draw it

  /* 
  TEMPLATE
  FIELDS:
  ... this.size ...      -- int 
  ... this.color...      -- Color 
  
  METHODS
  ... this.draw() ...                       -- WorldImage 
  ... this.drawCombine() ...                -- WorldImage
  ... this.isDrooping() ...                 -- boolean 
  ... this.combineHelper(double angle) ...  -- ITree
  ... this.combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) ...                  -- ITree
  ... this.getWidth() ...                   -- double 
 
  
  METHODS FOR FIELDS
  N/A
  
  */
  
  // leaf constructor 
  Leaf(int size, Color color) {
    this.size = size;
    this.color = color;
  }
 
  // produces image of given tree object 
  public WorldImage draw() {
    return new CircleImage(this.size, OutlineMode.SOLID, this.color);
  }
  
  // produces image of combined tree object 
  public WorldImage drawCombine() {
    return new CircleImage(this.size, OutlineMode.SOLID, this.color);
  }

  // returns true if any stems or branches face downwards 
  public boolean isDrooping() {
    return false;
  }
  
  // returns new tree object 
  public ITree combineHelper(double angle) {
    return this; 
  }

  // returns the combined image of two trees 
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, this,
        otherTree.combineHelper(rightTheta));
  }
  
  // returns the width of a tree object 
  public double getWidth() {
    return this.size - -this.size; 
  }
}

class Stem implements ITree {
  // How long this stick is
  int length;
  // The angle (in degrees) of this stem, relative to the +x axis
  double theta;
  // The rest of the tree
  ITree tree;
  
  /* 
  TEMPLATE
  FIELDS:
  ... this.length ...      -- int
  ... this.theta ...       -- double
  ... this.tree ...        -- ITree
  
  METHODS
  ... this.draw() ...                       -- WorldImage 
  ... this.drawCombine() ...                -- WorldImage
  ... this.isDrooping() ...                 -- boolean 
  ... this.combineHelper(double angle) ...  -- ITree
  ... this.combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) ...                  -- ITree
  ... this.getWidth() ...                   -- double 
 
  
  METHODS FOR FIELDS
  ... this.tree.draw() ...                       -- WorldImage
  ... this.tree.drawCombine() ...                -- WorldImage
  ... this.tree.isDrooping() ...                 -- boolean
  ... this.tree.combineHelper(double angle) ...  -- ITree
  ... this.tree.combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) ...                       -- ITree
  ... this.tree.getWidth() ...                   -- double 
  
  */

  // stem constructor 
  Stem(int length, double theta, ITree tree) {
    this.length = length;
    this.theta = theta;
    this.tree = tree;
  } 

  // produces image of given tree object 
  public WorldImage draw() {
    int xVal = (int) (this.length * Math.cos(Math.toRadians(theta)));
    int yVal = (int) (this.length * Math.sin(Math.toRadians(theta)));
    return new OverlayImage(tree.draw().movePinhole(xVal / 2, yVal / 2),
        new LineImage(new Posn(xVal, yVal), Color.BLACK));
  }
  
  // produces image of combined tree object 
  public WorldImage drawCombine() {
    int xVal = (int) (this.length * Math.cos(Math.toRadians(theta)));
    int yVal = (int) (this.length * Math.sin(Math.toRadians(theta)));
    return new OverlayImage(tree.drawCombine().movePinhole(xVal / 2, yVal / 2),
        new LineImage(new Posn(xVal, yVal), Color.BLACK));
  }

  // returns true if any stems or branches face downwards 
  public boolean isDrooping() {
    return (this.theta > 180) || (this.theta < 0) || (this.tree.isDrooping()); 
  }
  
  // returns new tree object 
  public ITree combineHelper(double angle) {
    return this; 
  }

  // returns the combined image of two trees 
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, this,
        otherTree.combineHelper(rightTheta));
  }
  
  // returns the width of a tree object 
  public double getWidth() {
    return 0 + this.tree.getWidth(); 
  }

}

class Branch implements ITree {
  // How long the left and right branches are
  int leftLength;
  int rightLength;
  // The angle (in degrees) of the two branches, relative to the +x axis,
  double leftTheta;
  double rightTheta;
  // The remaining parts of the tree
  ITree left;
  ITree right;
  
  /* 
  TEMPLATE
  FIELDS:
  ... this.leftLength ...      -- int
  ... this.rightLength ...     -- int 
  ... this.leftTheta ...       -- double
  ... this.rightTheta ...      -- double 
  ... this.left ...            -- ITree
  ... this.right ...           -- ITree 
  
  METHODS
  ... this.draw() ...                       -- WorldImage 
  ... this.drawCombine() ...                -- WorldImage
  ... this.isDrooping() ...                 -- boolean 
  ... this.combineHelper(double angle) ...  -- ITree
  ... this.combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) ...                  -- ITree
  ... this.getWidth() ...                   -- double 
 
  
  METHODS FOR FIELDS
  ... this.left.draw() ...                       -- WorldImage
  ... this.left.drawCombine() ...                -- WorldImage
  ... this.left.isDrooping() ...                 -- boolean
  ... this.left.combineHelper(double angle) ...  -- ITree
  ... this.left.combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) ...                       -- ITree
  ... this.left.getWidth() ...                   -- double 
  ... this.right.draw() ...                       -- WorldImage
  ... this.right.drawCombine() ...                -- WorldImage
  ... this.right.isDrooping() ...                 -- boolean
  ... this.right.combineHelper(double angle) ...  -- ITree
  ... this.right.combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) ...                       -- ITree
  ... this.right.getWidth() ...                   -- double 
  
  */

  // branch constructor 
  Branch(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree left,
      ITree right) {
    this.leftLength = leftLength;
    this.rightLength = rightLength;
    this.leftTheta = leftTheta;
    this.rightTheta = rightTheta;
    this.left = left;
    this.right = right;
  }

  // produces image of given tree object 
  public WorldImage draw() {

    WorldImage leftTree = this.left.draw();
    WorldImage rightTree = this.right.draw();
    int xValLeftBranch = (int) (this.rightLength * Math.cos(Math.toRadians(this.rightTheta)));
    int yValLeftBranch = (int) (this.rightLength * Math.sin(Math.toRadians(this.rightTheta)));
    int xValRightBranch = (int) (this.leftLength * Math.cos(Math.toRadians(this.leftTheta)));
    int yValRightBranch = (int) (this.leftLength * Math.sin(Math.toRadians(this.leftTheta)));
    WorldImage leftTreeBranch = new OverlayImage(
        leftTree.movePinhole(-xValLeftBranch, yValLeftBranch),
        new LineImage(new Posn(xValLeftBranch, yValLeftBranch), Color.BLACK)
            .movePinhole(xValLeftBranch / 2, yValLeftBranch / 2));
    WorldImage rightTreeBranch = new OverlayImage(
        rightTree.movePinhole(-xValRightBranch, yValRightBranch),
        new LineImage(new Posn(xValRightBranch, yValRightBranch), Color.BLACK)
            .movePinhole(xValRightBranch / 2, yValRightBranch / 2));
    return new OverlayImage(leftTreeBranch, rightTreeBranch);
  }
  
  // produces image of combined tree object 
  public WorldImage drawCombine() {

    WorldImage leftTree = this.left.drawCombine();
    WorldImage rightTree = this.right.drawCombine();
    int xValLeftBranch = (int) (this.rightLength * Math.cos(Math.toRadians(this.rightTheta)));
    int yValLeftBranch = (int) (this.rightLength * Math.sin(Math.toRadians(this.rightTheta)));
    int xValRightBranch = (int) (this.leftLength * Math.cos(Math.toRadians(this.leftTheta)));
    int yValRightBranch = (int) (this.leftLength * Math.sin(Math.toRadians(this.leftTheta)));
    WorldImage leftTreeBranch = new OverlayImage(
        leftTree.movePinhole(xValLeftBranch, yValLeftBranch),
        new LineImage(new Posn(xValLeftBranch, yValLeftBranch), Color.BLACK)
            .movePinhole(xValLeftBranch / 2, yValLeftBranch / 2));
    WorldImage rightTreeBranch = new OverlayImage(
        rightTree.movePinhole(xValRightBranch, yValRightBranch),
        new LineImage(new Posn(xValRightBranch, yValRightBranch), Color.BLACK)
            .movePinhole(xValRightBranch / 2, yValRightBranch / 2));
    return new OverlayImage(leftTreeBranch, rightTreeBranch);
  }

  // returns true if any stems or branches face downwards 
  public boolean isDrooping() {
    return (this.leftTheta > 180) || (this.leftTheta < 0) || (this.rightTheta > 180)
        || (this.rightTheta < 0) || (this.left.isDrooping()) || (this.right.isDrooping());
  }
  
  // returns new tree object 
  public ITree combineHelper(double angle) {
    return new Branch(this.leftLength, this.rightLength, (angle - 90 + this.leftTheta), 
        (angle - 90 + this.rightTheta), this.left.combineHelper(angle), 
        this.right.combineHelper(angle)); 
  }

  // returns the combined image of two trees 
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, 
        this.combineHelper(leftTheta), otherTree.combineHelper(rightTheta));
  }

  // returns the width of a tree object
  public double getWidth() {
    return ((this.leftLength * Math.cos(Math.toRadians(this.leftTheta))) + right.getWidth())
        - ((this.rightLength * Math.cos(Math.toRadians(this.rightTheta))) + left.getWidth());
  }

}

class ExamplesTree {

  //  Leaf leaf1 = new Leaf(5, Color.BLUE);
  //  Leaf leaf2 = new Leaf(5, Color.RED);
  //  Branch tree1 = new Branch(50, 100, 135, 45, leaf1, leaf2);
  //  Branch tree2 = new Branch(50, 50, 200, 45, leaf1, leaf2);
  //  Branch tree3 = new Branch(100, 100, 135, 45, tree1, tree2);
  //  ITree tree4 = tree3.combine(150, 125, 120, 65, tree2);

  Leaf leaf1 = new Leaf(10, Color.RED);
  Leaf leaf2 = new Leaf(15, Color.BLUE);
  Leaf leaf3 = new Leaf(15, Color.GREEN);
  Leaf leaf4 = new Leaf(8, Color.ORANGE);
  Branch tree1 = new Branch(30, 30, 135, 40, leaf1, leaf2);
  Branch tree2 = new Branch(30, 30, 115, 65, leaf3, leaf4);
  Branch tree3 = new Branch(30, 30, -100, -20, leaf1, leaf2);
  Stem stem1 = new Stem(40, 90, tree1);
  Stem stem2 = new Stem(50, 90, tree2);
  Stem stem3 = new Stem(50, -20, leaf1);
  ITree draw1 = tree1.combine(40, 50, 150, 30, tree2);

  // tests stem image
  boolean testDrawStem(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return c.drawScene(s.placeImageXY(stem3.draw(), 250, 250)) && c.show();
  }

  // tests combined image
  boolean testDrawTree(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return c.drawScene(s.placeImageXY(draw1.drawCombine(), 250, 250)) && c.show();
  }

  // tests tree image
  boolean testDrawTree2(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return c.drawScene(s.placeImageXY(tree3.draw(), 250, 250)) && c.show();
  }

  // tests isDrooping
  boolean testIsDrooping(Tester t) {
    return t.checkExpect(tree1.isDrooping(), false);
  }

  // tests isDrooping
  boolean testIsDrooping2(Tester t) {
    return t.checkExpect(tree3.isDrooping(), true);
  }

  // tests isDrooping
  boolean testIsDrooping3(Tester t) {
    return t.checkExpect(stem3.isDrooping(), true);
  }

}
