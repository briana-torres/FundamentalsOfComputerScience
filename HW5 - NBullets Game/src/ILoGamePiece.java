import javalib.funworld.*;
import java.util.Random;

interface ILoGamePiece {

  // returns true if the list is empty
  boolean isEmpty();

  // returns number of elements in a list
  int count();

  // removes any elements in a list outside of set range
  ILoGamePiece fixRange();

  // spawns new ships on the left
  ILoGamePiece createShipsLeft();

  // spawns new ships on the right
  ILoGamePiece createShipsRight();

  // adds list to scene
  WorldScene drawList(WorldScene scene);

  // changes the x values of a list
  ILoGamePiece changeXList();

  // creates a new bullet
  ILoGamePiece createBullet();

  // changes the x and y values of a list
  ILoGamePiece changeXYlist();

  // removes an elements in a list that collide with an IGamePiece
  ILoGamePiece fixCollision();

  // checks if an IGamePiece collides with any element in the list
  ILoGamePiece changeCollision(ILoGamePiece other);

  // changes the collision state of an IGamePiece
  IGamePiece changeState(IGamePiece current);
}

abstract class ALoGamePiece implements ILoGamePiece {

  public int count() {
    return 0;
  }

  public boolean isEmpty() {
    return true;
  }

  public ILoGamePiece fixRange() {
    return this;
  }

  public ILoGamePiece createShipsLeft() {
    throw new IllegalStateException("Method cannot invoked");
  }

  public ILoGamePiece createShipsRight() {
    throw new IllegalStateException("Method cannot invoked");
  }

  public WorldScene drawList(WorldScene scene) {
    return scene;
  }

  public ILoGamePiece changeXList() {
    return this;
  }

  public ILoGamePiece createBullet() {
    throw new IllegalStateException("Method cannot invoked");
  }

  public ILoGamePiece changeXYlist() {
    throw new IllegalStateException("Method cannot invoked");
  }

  public ILoGamePiece fixCollision() {
    return this;
  }

  public ILoGamePiece changeCollision(ILoGamePiece other) {
    return this;
  }

  public IGamePiece changeState(IGamePiece current) {
    return current;
  }

}

class MtLoBullet extends ALoGamePiece {

  MtLoBullet() {
  }

  @Override
  public ILoGamePiece createBullet() {
    return new ConsLoBullet(new Bullet(250, 300, 2, false, 1), this);
  }

  @Override
  public ILoGamePiece changeXYlist() {
    return this;
  }

}

class ConsLoBullet extends ALoGamePiece {
  IGamePiece first;
  ILoGamePiece rest;

  ConsLoBullet(IGamePiece first, ILoGamePiece rest) {
    this.first = first;
    this.rest = rest;
  }

  @Override
  public int count() {
    return 1 + this.rest.count();
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public ILoGamePiece fixRange() {
    if (this.first.inRange()) {
      return new ConsLoBullet(this.first, this.rest.fixRange());
    }
    else {
      return this.rest.fixRange();
    }
  }

  @Override
  public WorldScene drawList(WorldScene scene) {
    scene = this.first.toScene(scene);
    return this.rest.drawList(scene);
  }

  @Override
  public ILoGamePiece createBullet() {
    return new ConsLoBullet(new Bullet(250, 300, 2, false, 1), this);
  }

  @Override
  public ILoGamePiece changeXYlist() {
    return new ConsLoBullet(this.first.changeXYBullet(), this.rest.changeXYlist());
  }

  @Override
  public ILoGamePiece fixCollision() {
    if (this.first.isColliding()) {
      return this.rest.fixCollision();
    }
    else {
      return new ConsLoBullet(this.first, this.rest.fixCollision());
    }
  }

  @Override
  public ILoGamePiece changeCollision(ILoGamePiece other) {
    return new ConsLoBullet(other.changeState(this.first), this.rest.changeCollision(other));
  }

  @Override
  public IGamePiece changeState(IGamePiece current) {

    if (current.fsmd()) {
      if (this.first.dist((ShipLeft) current)) {
        return current.changeCollisionField();
      }
      else {
        return this.rest.changeState(current);
      }
    }
    else {
      if (this.first.dist((ShipRight) current)) {
        return current.changeCollisionField();
      }
      else {
        return this.rest.changeState(current);
      }

    }
  }

}

class MtLoShip extends ALoGamePiece {
  MtLoShip() {
  }

  @Override
  public ILoGamePiece createShipsLeft() {
    Random r = new Random();
    int randShip = r.nextInt(3) + 1;
    int randY1 = r.nextInt((257 - 43) + 1) + 43;
    int randY2 = r.nextInt((257 - 43) + 1) + 43;
    int randY3 = r.nextInt((257 - 43) + 1) + 43;

    if (randShip == 1) {
      return new ConsLoShip(new ShipLeft(10, randY1, 10, false), this);
    }
    else if (randShip == 2) {
      return new ConsLoShip(new ShipLeft(10, randY1, 10, false),
          new ConsLoShip(new ShipLeft(10, randY2, 10, false), this));
    }
    else {
      return new ConsLoShip(new ShipLeft(10, randY1, 10, false),
          new ConsLoShip(new ShipLeft(10, randY2, 10, false),
              new ConsLoShip(new ShipLeft(10, randY3, 10, false), this)));
    }
  }

  @Override
  public ILoGamePiece createShipsRight() {
    Random r = new Random();
    int randShip = r.nextInt(3) + 1;
    int randY1 = r.nextInt((257 - 43) + 1) + 43;
    int randY2 = r.nextInt((257 - 43) + 1) + 43;
    int randY3 = r.nextInt((257 - 43) + 1) + 43;

    if (randShip == 1) {
      return new ConsLoShip(new ShipRight(490, randY1, 10, false), this);
    }
    else if (randShip == 2) {
      return new ConsLoShip(new ShipRight(490, randY1, 10, false),
          new ConsLoShip(new ShipRight(490, randY2, 10, false), this));
    }
    else {
      return new ConsLoShip(new ShipRight(490, randY1, 10, false),
          new ConsLoShip(new ShipRight(490, randY2, 10, false),
              new ConsLoShip(new ShipRight(490, randY3, 10, false), this)));
    }
  }

}

class ConsLoShip extends ALoGamePiece {
  IGamePiece first;
  ILoGamePiece rest;

  ConsLoShip(IGamePiece first, ILoGamePiece rest) {
    this.first = first;
    this.rest = rest;
  }

  @Override
  public int count() {
    return 1 + this.rest.count();
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public ILoGamePiece fixRange() {
    if (this.first.inRange()) {
      return new ConsLoShip(this.first, this.rest.fixRange());
    }
    else {
      return this.rest.fixRange();
    }
  }

  @Override
  public ILoGamePiece createShipsLeft() {
    Random r = new Random();
    int randShip = r.nextInt(3) + 1;
    int randY1 = r.nextInt((257 - 43) + 1) + 43;
    int randY2 = r.nextInt((257 - 43) + 1) + 43;
    int randY3 = r.nextInt((257 - 43) + 1) + 43;

    if (randShip == 1) {
      return new ConsLoShip(new ShipLeft(-10, randY1, 10, false), this);
    }
    else if (randShip == 2) {
      return new ConsLoShip(new ShipLeft(-10, randY1, 10, false),
          new ConsLoShip(new ShipLeft(-10, randY2, 10, false), this));
    }
    else {
      return new ConsLoShip(new ShipLeft(-10, randY1, 10, false),
          new ConsLoShip(new ShipLeft(-10, randY2, 10, false),
              new ConsLoShip(new ShipLeft(-10, randY3, 10, false), this)));
    }
  }

  @Override
  public ILoGamePiece createShipsRight() {
    Random r = new Random();
    int randShip = r.nextInt(3) + 1;
    int randY1 = r.nextInt((257 - 43) + 1) + 43;
    int randY2 = r.nextInt((257 - 43) + 1) + 43;
    int randY3 = r.nextInt((257 - 43) + 1) + 43;

    if (randShip == 1) {
      return new ConsLoShip(new ShipRight(510, randY1, 10, false), this);
    }
    else if (randShip == 2) {
      return new ConsLoShip(new ShipRight(510, randY1, 10, false),
          new ConsLoShip(new ShipRight(510, randY2, 10, false), this));
    }
    else {
      return new ConsLoShip(new ShipRight(510, randY1, 10, false),
          new ConsLoShip(new ShipRight(510, randY2, 10, false),
              new ConsLoShip(new ShipRight(510, randY3, 10, false), this)));
    }

  }

  @Override
  public WorldScene drawList(WorldScene scene) {
    scene = this.first.toScene(scene);
    return this.rest.drawList(scene);

  }

  @Override
  public ILoGamePiece changeXList() {
    return new ConsLoShip(this.first.changeXShip(), this.rest.changeXList());
  }

  @Override
  public ILoGamePiece fixCollision() {
    if (this.first.isColliding()) {
      return this.rest.fixCollision();
    }
    else {
      return new ConsLoShip(this.first, this.rest.fixCollision());
    }
  }

  @Override
  public ILoGamePiece changeCollision(ILoGamePiece other) {
    return new ConsLoShip(other.changeState(this.first), this.rest.changeCollision(other));
  }

  @Override
  public IGamePiece changeState(IGamePiece current) {
    if (this.first.dist((Bullet) current)) {
      return current.changeCollisionField();
    }
    else {
      return this.rest.changeState(current);
    }
  }

}

class ExamplesILoGamePiece {
  ExamplesILoGamePiece() {
  }

}
