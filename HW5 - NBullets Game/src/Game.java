import tester.*;
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;
import java.util.Random;

class Game extends World {
  int width = 500;
  int height = 300;
  int bullets;
  int destroyedShips;
  int currentTick;
  ILoGamePiece bulletList;
  ILoGamePiece shipList;
  boolean moveBullets;
  boolean moveShips;
  boolean spawnShips;
  boolean deleteShips;
  boolean left;
  boolean spawnBullet;
  boolean deleteBullets;
  boolean colliding;
  Random rand;

  // Game constructor for user
  Game(int bullets) {
    this(bullets, 0, 0, new MtLoShip(), true, true, true, true, new MtLoBullet(), true, true, true,
        true);
  }

  // Game constructor
  Game(int bullets, int destroyedShips, int currentTick, ILoGamePiece shipList, boolean moveShips,
      boolean spawnShips, boolean deleteShips, boolean left, ILoGamePiece bulletList,
      boolean spawnBullet, boolean deleteBullets, boolean moveBullets, boolean colliding) {
    this.bullets = bullets;
    this.destroyedShips = destroyedShips;
    this.currentTick = currentTick;
    this.shipList = shipList;
    this.moveShips = moveShips;
    this.spawnShips = spawnShips;
    this.deleteShips = deleteShips;
    this.left = left;
    this.bulletList = bulletList;
    this.spawnBullet = spawnBullet;
    this.deleteBullets = deleteBullets;
    this.moveBullets = moveBullets;
    this.colliding = colliding;
  }

  // returns scene with updated game information
  @Override
  public WorldScene makeScene() {

    WorldScene scene = new WorldScene(width, height);

    if (this.deleteBullets) {
      scene = this.deleteBulletsHelper(scene);
    }

    if (this.spawnBullet) {
      scene = this.spawnBulletHelper(scene);
    }

    if (this.moveBullets) {
      scene = this.moveBulletsHelper(scene);
    }

    if (this.deleteShips) {
      scene = this.deleteShipsHelper(scene);
    }

    if (this.spawnShips) {
      scene = this.spawnShipsHelper(scene);
    }

    if (this.moveShips) {
      scene = this.moveShipsHelper(scene);
    }

    if (this.colliding) {
      scene = this.collidingHelper(scene);
    }

    scene = this.addInfoToScene(scene);

    return scene;

  }

  // implements methods on game every tick
  public Game onTick() {
    return this.spawnBullets().moveBullets().spawnShips().moveShips().colliding().deleteShips()
        .deleteBullets().incrementGameTick();
  }

  // changes boolean to true
  public Game deleteShips() {
    return new Game(this.bullets, this.destroyedShips, this.currentTick, this.shipList,
        this.moveShips, this.spawnShips, true, this.left, this.bulletList, this.spawnBullet,
        this.deleteBullets, this.moveBullets, this.colliding);
  }

  // changes boolean to true
  public Game moveBullets() {
    return new Game(this.bullets, this.destroyedShips, this.currentTick, this.shipList,
        this.moveShips, this.spawnShips, this.deleteShips, this.left, this.bulletList,
        this.spawnBullet, this.deleteBullets, true, this.colliding);
  }

  // changes boolean to true
  public Game spawnShips() {
    return new Game(this.bullets, this.destroyedShips, this.currentTick, this.shipList,
        this.moveShips, true, this.deleteShips, this.left, this.bulletList, this.spawnBullet,
        this.deleteBullets, this.moveBullets, this.colliding);
  }

  // changes boolean to true
  public Game moveShips() {
    return new Game(this.bullets, this.destroyedShips, this.currentTick, this.shipList, true,
        this.spawnShips, this.deleteShips, this.left, this.bulletList, this.spawnBullet,
        this.deleteBullets, this.moveBullets, this.colliding);
  }

  // changes boolean to true
  public Game deleteBullets() {
    return new Game(this.bullets, this.destroyedShips, this.currentTick, this.shipList,
        this.moveShips, this.spawnShips, this.deleteShips, this.left, this.bulletList,
        this.spawnBullet, true, this.moveBullets, this.colliding);
  }

  // changes boolean to true
  public Game spawnBullets() {
    return new Game(this.bullets, this.destroyedShips, this.currentTick, this.shipList,
        this.moveShips, this.spawnShips, this.deleteShips, this.left, this.bulletList, true,
        this.deleteBullets, this.moveBullets, this.colliding);
  }

  // changes boolean to true
  public Game colliding() {
    return new Game(this.bullets, this.destroyedShips, this.currentTick, this.shipList,
        this.moveShips, this.spawnShips, this.deleteShips, this.left, this.bulletList,
        this.spawnBullet, this.deleteBullets, this.moveBullets, true);
  }

  // increments currentTick by 1
  public Game incrementGameTick() {
    return new Game(this.bullets, this.destroyedShips, this.currentTick + 1, this.shipList,
        this.moveShips, this.spawnShips, this.deleteShips, this.left, this.bulletList,
        this.spawnBullet, this.deleteBullets, this.moveBullets, this.colliding);
  }

  // creates new bullet and subtracts 1 from current bullet count on every space
  // bar press
  public Game onKeyEvent(String key) {
    if (key.equals(" ")) {
      this.bullets = this.bullets - 1;
      this.bulletList = this.bulletList.createBullet();
      return new Game(this.bullets, this.destroyedShips, this.currentTick, this.shipList,
          this.moveShips, this.spawnShips, this.deleteShips, this.left, this.bulletList,
          this.spawnBullet, this.deleteBullets, this.moveBullets, this.colliding);
    }
    else {
      return this;
    }
  }

  // adds current bullet count and destroyed ship count to scene
  WorldScene addInfoToScene(WorldScene scene) {
    return scene.placeImageXY(new TextImage("Bullets: " + Integer.toString(this.bullets)
        + "  Destroyed Ships: " + Integer.toString(this.destroyedShips), Color.black), 100, 20);
  }

  // deletes all ships that have been hit or are out of range
  WorldScene deleteShipsHelper(WorldScene scene) {
    int currentCount = this.shipList.count();
    if (this.shipList.isEmpty()) {
      return scene;
    }
    else {
      this.shipList = this.shipList.fixCollision();
      this.destroyedShips = this.destroyedShips + currentCount - this.shipList.count();
      this.shipList = this.shipList.fixRange();
      return this.shipList.drawList(scene);
    }
  }

  // spawns new ships every second
  WorldScene spawnShipsHelper(WorldScene scene) {

    if (this.currentTick % 4 == 0) {
      this.left = !this.left;
      if (this.left) {
        this.shipList = this.shipList.createShipsLeft();
        return scene;
      }
      else {
        this.shipList = this.shipList.createShipsRight();
        return scene;
      }
    }
    else {
      return scene;
    }

  }

  // moves current shipList
  WorldScene moveShipsHelper(WorldScene scene) {

    this.shipList = this.shipList.changeXList();
    return scene;

  }

  // deletes all bullets that hit a ship or are out of range
  WorldScene deleteBulletsHelper(WorldScene scene) {
    if (this.bulletList.isEmpty()) {
      return scene;
    }
    else {
      this.bulletList = this.bulletList.fixRange();
      this.bulletList = this.bulletList.fixCollision();
      return this.bulletList.drawList(scene);
    }
  }

  // creates new bullets every explosion
  WorldScene spawnBulletHelper(WorldScene scene) {
    // this.bulletList = this.bulletList.createBullets();
    return scene;

  }

  // moves current bulletList
  WorldScene moveBulletsHelper(WorldScene scene) {

    this.bulletList = this.bulletList.changeXYlist();
    return scene;

  }

  // checks if any bullets are colliding with any ships
  WorldScene collidingHelper(WorldScene scene) {
    ILoGamePiece currentB = this.bulletList;
    ILoGamePiece currentS = this.shipList;
    this.bulletList = this.bulletList.changeCollision(currentS);
    this.shipList = this.shipList.changeCollision(currentB);
    return scene;

  }

  // ends game when bullet count is 0 and there are no more bullets on scene
  @Override
  public WorldEnd worldEnds() {
    if (this.bullets == 0 && this.bulletList.isEmpty()) {
      return new WorldEnd(true, this.makeEndScene());
    }
    else {
      return new WorldEnd(false, this.makeEndScene());
    }
  }

  // returns an end scene
  public WorldScene makeEndScene() {
    WorldScene endScene = new WorldScene(this.width, this.height);
    endScene = endScene.placeImageXY(new TextImage("Game Over", Color.red), 250, 140);
    return endScene.placeImageXY(new TextImage("Score: " + this.destroyedShips, Color.red), 250,
        160);

  }
}

class ExamplesMyWorld {
  ExamplesMyWorld() {
  }

  boolean testBigBang(Tester t) {
    Game game = new Game(10);
    return game.bigBang(500, 300, 0.125);
  }
}