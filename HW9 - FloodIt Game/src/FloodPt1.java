import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import java.util.*;

//Represents a single square of the game area
class Cell {
  // In logical coordinates, with the origin at the top-left corner of the screen
  int x;
  int y;
  Color color;
  boolean flooded;
  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;

  // Cell constructor for makeBoard method
  Cell(Color color) {
    this.color = color;
  }

  Cell(int x, int y, Color color, Cell left, Cell top, Cell right, Cell bottom) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
  }

  // EFFECT: Updates the current scene by drawing all the cells according to their
  // positions
  public void drawAt(int col, int row, WorldScene scene, int boardSize) {
    int xCoord = col * 20 + 10 + ((500 - boardSize * 20) / 2);
    int yCoord = row * 20 + 10 + ((500 - boardSize * 20) / 2);
    this.x = xCoord;
    this.y = yCoord;

    scene.placeImageXY(this.drawCell(), xCoord, yCoord);

  }

  // Returns an image of the cell
  public WorldImage drawCell() {
    return new RectangleImage(20, 20, OutlineMode.SOLID, this.color);
  }

  // EFFECT: Updates the current cells neighbors flood field to true if the color
  // matches
  public void flood(Color c) {
    if (this.left != null && !this.left.flooded && this.left.color.equals(c)) {
      this.left.flooded = true;
    }
    if (this.top != null && !this.top.flooded && this.top.color.equals(c)) {
      this.top.flooded = true;
    }
    if (this.right != null && !this.right.flooded && this.right.color.equals(c)) {
      this.right.flooded = true;
    }
    if (this.bottom != null && !this.bottom.flooded && this.bottom.color.equals(c)) {
      this.bottom.flooded = true;
    }
  }

}

class FloodItWorld extends World {
  // All the cells of the game
  ArrayList<Cell> tiles;
  ArrayList<ArrayList<Cell>> board = new ArrayList<ArrayList<Cell>>();
  ArrayList<Color> colorList = new ArrayList<Color>(Arrays.asList(Color.PINK, Color.CYAN,
      Color.GREEN, Color.ORANGE, Color.YELLOW, Color.MAGENTA, Color.BLUE, Color.RED));

  int boardSize = 10;
  int colorSize = 8;
  int clicks = 0;
  int maxClicks = 30;

  Random rand;

  // Convenience constructor
  FloodItWorld(int size, int colors) {
    this.boardSize = size;
    this.colorSize = colors;
    this.rand = new Random();
    this.makeBoard();
    this.setNeighbors();
  }

  // Constructor for testing
  FloodItWorld(int size, Random rand) {
    this.boardSize = size;
    this.rand = rand;
    this.makeBoard();
  }

  // Main Constructor that is used in big bang
  FloodItWorld(int size, int colors, Random rand) {
    this.boardSize = size;
    this.colorSize = colors;
    this.rand = rand;
    this.makeBoard();
    this.setNeighbors();
    if (this.boardSize <= 5) {
      this.colorSize = 3;
      this.maxClicks = 5;
    }
    else if (this.boardSize < 10 && this.boardSize > 5) {
      this.colorSize = 5;
      this.maxClicks = 15;
    }
    else {
      this.colorSize = 8;
      this.maxClicks = 20 + this.boardSize;
    }
  }

  // Draws the game
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(500, 500);

    for (int i = 0; i < this.board.size(); i++) {
      for (int j = 0; j < this.board.get(i).size(); j++) {
        this.board.get(i).get(j).drawAt(j, i, scene, this.boardSize);
      }
    }

    scene.placeImageXY(
        new TextImage(Integer.toString(this.clicks) + "/" + Integer.toString(this.maxClicks),
            Color.black),
        250, 450);

    if (this.clicks > this.maxClicks) {
      scene.placeImageXY(new TextImage("You Lose :(", Color.black), 250, 480);
    }

    if (this.allFlooded() && this.clicks <= this.maxClicks) {
      scene.placeImageXY(new TextImage("You Win :)", Color.black), 250, 480);
    }

    return scene;
  }

  // EFFECT: Updates the current scene to change the color of the flooded cells
  public void onTick() {
    this.floodWorld();
  }

  // EFFECT: Updates the board to change the color of the flooded cells
  public void floodWorld() {
    Color current = this.board.get(0).get(0).color;
    for (int i = 0; i < this.board.size(); i++) {
      for (int j = 0; j < this.board.get(i).size(); j++) {
        Cell c = this.board.get(i).get(j);
        if (c.flooded) {
          c.color = current;
          c.flood(current);
        }
      }
    }
    makeScene();
  }

  // Checks if every cell on the board is flooded
  public boolean allFlooded() {
    boolean status = true;
    for (int i = 0; i < this.board.size(); i++) {
      for (int j = 0; j < this.board.get(i).size(); j++) {
        if (this.board.get(i).get(j).flooded) {
          status = true;
        }
        else {
          status = false;
          return status;
        }
      }
    }
    return status;
  }

  // Returns the cell that was clicked
  public Cell clicked(Posn pos) {
    Cell clicked = null;
    for (int i = 0; i < this.board.size(); i++) {
      for (int j = 0; j < this.board.get(i).size(); j++) {
        if ((this.board.get(i).get(j).x - 10 <= pos.x && pos.x <= this.board.get(i).get(j).x + 10)
            && (this.board.get(i).get(j).y - 10 <= pos.y
                && pos.y <= this.board.get(i).get(j).y + 10)) {
          clicked = this.board.get(i).get(j);
        }
      }
    }
    return clicked;
  }

  // EFFECT: Changes the color and flooded fields of the first cell
  public void changeFirst(Cell cell) {
    if (cell != null) {
      board.get(0).get(0).color = cell.color;
      board.get(0).get(0).flooded = true;
    }
  }

  // EFFECT: Changes the fields of the first cell and updates the click count
  public void onMouseClicked(Posn pos) {
    if ((pos.x < ((500 - boardSize * 20) / 2) || pos.x > 500 - ((500 - boardSize * 20) / 2))
        || (pos.y < ((500 - boardSize * 20) / 2) || pos.y > 500 - ((500 - boardSize * 20) / 2))) {
      return;
    }
    else {
      this.changeFirst(this.clicked(pos));
      this.clicks += 1;
    }

  }

  // EFFECT: Resets the board on "r" key event
  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      this.board = new ArrayList<ArrayList<Cell>>();
      this.clicks = 0;
      this.makeBoard();
      this.setNeighbors();
    }
  }

  // EFFECT: Initializes the board
  public void makeBoard() {

    for (int i = 0; i < this.boardSize; i++) {
      board.add(new ArrayList<Cell>());
      for (int j = 0; j < this.boardSize; j++) {
        board.get(i).add(new Cell(this.colorList.get(rand.nextInt(this.colorSize))));
      }
    }

  }

  // EFFECT: Sets the neighbors of every cell
  public void setNeighbors() {
    for (int i = 0; i < this.board.size(); i++) {
      for (int j = 0; j < this.board.get(i).size(); j++) {
        if (i != board.size() - 1 && j != board.size() - 1 && i != 0 && j != 0) {
          board.get(i).get(j).left = board.get(i).get(j - 1);
          board.get(i).get(j).top = board.get(i - 1).get(j);
          board.get(i).get(j).right = board.get(i).get(j + 1);
          board.get(i).get(j).bottom = board.get(i + 1).get(j);
        }
        if (i == 0 && j != 0 && j != board.size() - 1) {
          board.get(i).get(j).left = board.get(i).get(j - 1);
          board.get(i).get(j).right = board.get(i).get(j + 1);
          board.get(i).get(j).bottom = board.get(i + 1).get(j);
        }
        if (j == 0 && i != 0 && i != board.size() - 1) {
          board.get(i).get(j).top = board.get(i - 1).get(j);
          board.get(i).get(j).right = board.get(i).get(j + 1);
          board.get(i).get(j).bottom = board.get(i + 1).get(j);
        }
        if (i == board.size() - 1 && j != 0 && j != board.size() - 1) {
          board.get(i).get(j).left = board.get(i).get(j - 1);
          board.get(i).get(j).top = board.get(i - 1).get(j);
          board.get(i).get(j).right = board.get(i).get(j + 1);
        }
        if (j == board.size() - 1 && i != 0 && i != board.size() - 1) {
          board.get(i).get(j).left = board.get(i).get(j - 1);
          board.get(i).get(j).top = board.get(i - 1).get(j);
          board.get(i).get(j).bottom = board.get(i + 1).get(j);
        }
        if (i == 0 && j == 0) {
          board.get(i).get(j).right = board.get(i).get(j + 1);
          board.get(i).get(j).bottom = board.get(i + 1).get(j);
        }
        if (i == 0 && j == board.size() - 1) {
          board.get(i).get(j).left = board.get(i).get(j - 1);
          board.get(i).get(j).bottom = board.get(i + 1).get(j);
        }
        if (i == board.size() - 1 && j == 0) {
          board.get(i).get(j).top = board.get(i - 1).get(j);
          board.get(i).get(j).right = board.get(i).get(j + 1);
        }
        if (i == board.size() - 1 && j == board.size() - 1) {
          board.get(i).get(j).left = board.get(i).get(j - 1);
          board.get(i).get(j).top = board.get(i - 1).get(j);
        }
      }
    }
  }

}

class ExamplesFloodIt {

  FloodItWorld g;
  Cell c1;
  Cell c2;
  Cell c3;
  Cell c4;
  Cell c5;
  Cell c6;
  Cell c7;
  Cell c8;
  Cell c9;
  Cell c10;
  Cell c11;
  Cell c12;
  Cell c13;
  Cell c14;
  Cell c15;
  Cell c16;
  Cell c17;
  Cell c18;
  Cell c19;
  Cell c20;
  Cell c21;
  Cell c22;
  Cell c23;
  Cell c24;
  Cell c25;
  ArrayList<ArrayList<Cell>> b1;
  WorldScene w1;
  WorldScene w2;

  void initConditions() {
    c1 = new Cell(Color.MAGENTA);
    c1.flooded = true;
    c2 = new Cell(Color.YELLOW);
    c2.flooded = true;
    c3 = new Cell(Color.PINK);
    c3.flooded = true;
    c4 = new Cell(Color.BLUE);
    c4.flooded = true;
    c5 = new Cell(Color.PINK);
    c5.flooded = true;
    c6 = new Cell(Color.YELLOW);
    c6.flooded = true;
    c7 = new Cell(Color.BLUE);
    c7.flooded = true;
    c8 = new Cell(Color.YELLOW);
    c8.flooded = true;
    c9 = new Cell(Color.CYAN);
    c9.flooded = true;
    c10 = new Cell(Color.BLUE);
    c10.flooded = true;
    c11 = new Cell(Color.MAGENTA);
    c11.flooded = true;
    c12 = new Cell(Color.GREEN);
    c12.flooded = true;
    c13 = new Cell(Color.BLUE);
    c13.flooded = true;
    c14 = new Cell(Color.PINK);
    c14.flooded = true;
    c15 = new Cell(Color.PINK);
    c15.flooded = true;
    c16 = new Cell(Color.ORANGE);
    c16.flooded = true;
    c17 = new Cell(Color.BLUE);
    c17.flooded = true;
    c18 = new Cell(Color.CYAN);
    c18.flooded = true;
    c19 = new Cell(Color.RED);
    c19.flooded = true;
    c20 = new Cell(Color.BLUE);
    c20.flooded = true;
    c21 = new Cell(Color.CYAN);
    c21.flooded = true;
    c22 = new Cell(Color.MAGENTA);
    c22.flooded = true;
    c23 = new Cell(Color.RED);
    c23.flooded = true;
    c24 = new Cell(Color.YELLOW);
    c24.flooded = true;
    c25 = new Cell(Color.RED);
    c25.flooded = true;
    b1 = new ArrayList<ArrayList<Cell>>(
        Arrays.asList((new ArrayList<Cell>(Arrays.asList(c1, c2, c3, c4, c5))),
            (new ArrayList<Cell>(Arrays.asList(c6, c7, c8, c9, c10))),
            (new ArrayList<Cell>(Arrays.asList(c11, c12, c13, c14, c15))),
            (new ArrayList<Cell>(Arrays.asList(c16, c17, c18, c19, c20))),
            (new ArrayList<Cell>(Arrays.asList(c21, c22, c23, c24, c25)))));
    w1 = new WorldScene(500, 500);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.MAGENTA), 210, 210);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.YELLOW), 230, 210);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.PINK), 250, 210);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.BLUE), 270, 210);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.PINK), 290, 210);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.YELLOW), 210, 230);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.BLUE), 230, 230);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.YELLOW), 250, 230);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.CYAN), 270, 230);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.BLUE), 290, 230);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.MAGENTA), 210, 250);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.GREEN), 230, 250);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.BLUE), 250, 250);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.PINK), 270, 250);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.PINK), 290, 250);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.ORANGE), 210, 270);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.BLUE), 230, 270);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.CYAN), 250, 270);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED), 270, 270);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.BLUE), 290, 270);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.CYAN), 210, 290);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.MAGENTA), 230, 290);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED), 250, 290);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.YELLOW), 270, 290);
    w1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED), 290, 290);
    w1.placeImageXY(new TextImage(Integer.toString(0) + "/" + Integer.toString(5), Color.black),
        250, 450);
    w2 = new WorldScene(200, 200);
    w2.placeImageXY(c1.drawCell(), 210, 210);
    w2.placeImageXY(new TextImage(Integer.toString(0) + "/" + Integer.toString(30), Color.black),
        250, 450);
    g = new FloodItWorld(10, 8, new Random(3));

  }

  void testGame(Tester t) {
    initConditions();
    g.bigBang(500, 500, 0.1);

  }

  void testMakeBoard(Tester t) {
    initConditions();
    FloodItWorld g2 = new FloodItWorld(5, new Random(3));
    g2.makeBoard();
    g2.board.get(1).get(1).flooded = true; 
    g2.board.get(0).get(0).flooded = true; 
    t.checkExpect(g2.board.get(1).get(1), b1.get(1).get(1));
    t.checkExpect(g2.board.get(0).get(0), b1.get(0).get(0));

  }

  void testMakeScene(Tester t) {
    initConditions();
    FloodItWorld g3 = new FloodItWorld(5, 8, new Random(3));
    t.checkExpect(g3.makeScene(), w1);
  }

  void testSetNeighbors(Tester t) {
    initConditions();
    t.checkExpect(g.board.get(1).get(1).top, g.board.get(0).get(1));
    t.checkExpect(g.board.get(1).get(1).bottom, g.board.get(2).get(1));
    t.checkExpect(g.board.get(1).get(1).left, g.board.get(1).get(0));
    t.checkExpect(g.board.get(1).get(1).right, g.board.get(1).get(2));
    t.checkExpect(g.board.get(0).get(0).bottom, g.board.get(1).get(0));
    t.checkExpect(g.board.get(0).get(0).right, g.board.get(0).get(1));
    t.checkExpect(g.board.get(0).get(3).right, g.board.get(0).get(4));
    t.checkExpect(g.board.get(g.boardSize - 1).get(g.boardSize - 1).left,
        g.board.get(g.boardSize - 1).get(g.boardSize - 2));
  }

  void testDrawCell(Tester t) {
    initConditions();
    t.checkExpect(c1.drawCell(), new RectangleImage(20, 20, OutlineMode.SOLID, c1.color));
    t.checkExpect(c2.drawCell(), new RectangleImage(20, 20, OutlineMode.SOLID, c2.color));
    t.checkExpect(c3.drawCell(), new RectangleImage(20, 20, OutlineMode.SOLID, c3.color));
  }

  void testDrawAt(Tester t) {
    initConditions();
    WorldScene s1 = new WorldScene(200, 200);
    c1.drawAt(0, 0, s1, 5);
    t.checkExpect(s1, w2);
  }
  
  void testFlood(Tester t) {
    initConditions();
    Cell test = g.board.get(0).get(0); 
    test.flood(Color.YELLOW);
    t.checkExpect(g.board.get(0).get(1).flooded, true);
    t.checkExpect(g.board.get(1).get(0).flooded, false);
  }
  
  void testOnTick(Tester t) {
    initConditions();
  }
  
  void testFloodWorld(Tester t) {
    initConditions();
     
    
  }
  
  void testAllFlooded(Tester t) {
    initConditions();
    FloodItWorld g4 = new FloodItWorld(5, 8, new Random(3));
    g4.board = b1; 
    t.checkExpect(g.allFlooded(), false);
    t.checkExpect(g4.allFlooded(), true);
  }
  
  void testClicked(Tester t) {
    initConditions();
  
  }
  
  void testChangeFirst(Tester t) {
    initConditions();
  }
  
  void testOnMouseClicked(Tester t) {
    initConditions();
  }
  
  void testOnKeyEvent(Tester t) {
    initConditions();
  }
  
}
