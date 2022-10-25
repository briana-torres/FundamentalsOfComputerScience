import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;

interface IGamePiece {

  // returns the game piece as an image
  WorldImage draw();

  // checks is game piece is in range
  boolean inRange();

  // places the game piece on a scene
  WorldScene toScene(WorldScene scene);

  // changes the x pos of a ship
  IGamePiece changeXShip();

  // changes the x and y pos of a bullet
  IGamePiece changeXYBullet();

  // returns boolean if game piece is colliding with another game piece
  boolean isColliding();

  // returns true if two game pieces are colliding
  boolean dist(Bullet other);

  // returns true if two game pieces are colliding
  boolean dist(ShipLeft other);

  // returns true if two game pieces are colliding
  boolean dist(ShipRight other);

  // updates collision status
  IGamePiece changeCollisionField();

  // checks if a ship is spawned on the left or right
  boolean fsmd();

}

abstract class AGamePiece implements IGamePiece {
  int x;
  int y;
  int radius;
  boolean colliding;

  AGamePiece(int x, int y, int radius, boolean colliding) {
    this.x = x;
    this.y = y;
    this.radius = radius;
    this.colliding = colliding;
  }

  public WorldImage draw() {
    return new CircleImage(10, OutlineMode.SOLID, Color.cyan);
  }

  public boolean inRange() {
    return this.x >= -10 && this.x <= 510; 
  }

  public WorldScene toScene(WorldScene scene) {
    return scene.placeImageXY(this.draw(), this.x, this.y);
  }

  public IGamePiece changeXYBullet() {
    throw new IllegalStateException("Method cannot invoked");
  }

  public boolean isColliding() {
    return this.colliding;
  }

  public boolean dist(Bullet other) {
    throw new IllegalStateException("Method cannot invoked");
  }

  public boolean dist(ShipLeft other) {
    throw new IllegalStateException("Method cannot invoked");
  }

  public boolean dist(ShipRight other) {
    throw new IllegalStateException("Method cannot invoked");
  }

  public IGamePiece changeCollisionField() {
    this.colliding = !this.colliding;
    return this;
  }

}

class Bullet extends AGamePiece {
  int explosions;

  Bullet(int x, int y, int radius, boolean colliding, int explosions) {
    super(x, y, radius, colliding);
    this.explosions = explosions;
  }

  @Override
  public WorldImage draw() {
    return new CircleImage(this.radius, OutlineMode.SOLID, Color.pink);
  }

  @Override
  public boolean inRange() {
    return this.x >= -10 && this.x <= 510 && this.y >= -10 && this.y <= 310;
  }

  public IGamePiece changeXShip() {
    return this;
  }

  @Override
  public IGamePiece changeXYBullet() {
    if (this.explosions == 1) {
      this.y = this.y - 20;
      return this;
    }
    else {
      return this;
    }
  }

  @Override
  public boolean dist(ShipLeft other) {
    int deltaX = Math.abs(this.x - other.x);
    int deltaY = Math.abs(this.y - other.y);
    double dist = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

    return dist <= this.radius + other.radius;
  }

  @Override
  public boolean dist(ShipRight other) {
    int deltaX = Math.abs(this.x - other.x);
    int deltaY = Math.abs(this.y - other.y);
    double dist = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

    return dist <= this.radius + other.radius;

  }

  public boolean fsmd() {
    throw new IllegalStateException("Method cannot invoked");
  }
}

class ShipLeft extends AGamePiece {

  ShipLeft(int x, int y, int radius, boolean colliding) {
    super(x, y, radius, colliding);
  }

  public IGamePiece changeXShip() {
    this.x = this.x + 10;
    return this;
  }

  @Override
  public boolean dist(Bullet other) {
    int deltaX = Math.abs(this.x - other.x);
    int deltaY = Math.abs(this.y - other.y);
    double dist = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

    return dist <= this.radius + other.radius;
  }

  public boolean dist(ShipLeft other) {
    throw new IllegalStateException("Method cannot invoked");
  }

  public boolean dist(ShipRight other) {
    throw new IllegalStateException("Method cannot invoked");
  }

  public boolean fsmd() {
    return true;
  }

}

class ShipRight extends AGamePiece {

  ShipRight(int x, int y, int radius, boolean colliding) {
    super(x, y, radius, colliding);
  }

  public IGamePiece changeXShip() {
    this.x = this.x - 10;
    return this;
  }

  @Override
  public boolean dist(Bullet other) {
    int deltaX = Math.abs(this.x - other.x);
    int deltaY = Math.abs(this.y - other.y);
    double dist = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

    return dist <= this.radius + other.radius;
  }

  public boolean fsmd() {
    return false;
  }

}

class GamePieceExamples {
  GamePieceExamples() {
  }
}