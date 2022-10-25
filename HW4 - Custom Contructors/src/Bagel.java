import tester.*;

class BagelRecipe {
  double flour;
  double water;
  double yeast;
  double salt;
  double malt;

  /*
   * TEMPLATE FIELDS: ... this.flour ... -- double ... this.water ... -- double
   * ... this.yeast ... -- double ... this.salt ... -- double ... this.malt ... --
   * double
   * 
   * METHODS ... this.sameRecipe(BagelRecipe other) ... -- boolean
   * 
   * METHODS FOR FIELDS N/A
   * 
   */

  // constructs a perfect bagel with given flour and yeast weights
  BagelRecipe(double flour, double yeast) {
    this(flour, flour, yeast, (flour / 20 - yeast), yeast);
  }

  // constructs a bagel recipe with given flour, yeast, and salt volumes
  BagelRecipe(double flour, double yeast, double salt) {
    this(flour * 4.25, flour * 4.25, (yeast / 48) * 5, (salt / 48) * 10, (yeast / 48) * 5);
  }

  // constructor that enforces a perfect bagel recipe
  BagelRecipe(double flour, double water, double yeast, double salt, double malt) {
    if (Math.abs(flour - water) <= 0.001) {
      this.flour = flour;
      this.water = water;
    }
    else {
      throw new IllegalArgumentException("Invalid flour or water weight: " + String.valueOf(flour));
    }
    if (Math.abs(yeast - malt) <= 0.001) {
      this.yeast = yeast;
      this.malt = malt;
    }
    else {
      throw new IllegalArgumentException("Invalid yeast or malt weight: " + String.valueOf(yeast));
    }
    if (Math.abs((salt + yeast) - (flour / 20)) <= 0.001) {
      this.salt = salt;
    }
    else {
      throw new IllegalArgumentException("Invalid salt weight: " + String.valueOf(salt));
    }
  }

  // checks if two given bagel recipes are the same
  boolean sameRecipe(BagelRecipe other) {
    return (Math.abs(this.flour - other.flour) < 0.001)
        && (Math.abs(this.water - other.water) < 0.001)
        && (Math.abs(this.yeast - other.yeast) < 0.001)
        && (Math.abs(this.salt - other.salt) < 0.001) && (Math.abs(this.malt - other.malt) < 0.001);
  }

}

class ExamplesBagelRecipe {
  ExamplesBagelRecipe() { 
  }

  BagelRecipe b1 = new BagelRecipe(20.0, 20.0, 0.5, 0.5, 0.5);
  BagelRecipe b2 = new BagelRecipe(40.0, 40.0, 1.0, 1.0, 1.0);
  BagelRecipe b3 = new BagelRecipe(200.0, 7.0);
  BagelRecipe b4 = new BagelRecipe(50.0, 2.0);
  BagelRecipe b5 = new BagelRecipe(17.0, 28.8, 2.94);
  BagelRecipe b6 = new BagelRecipe(25.5, 48.0, 2.01);

  // tests the various bagel recipe constructors
  boolean testCheckConstructorException(Tester t) {
    return t.checkConstructorException(
        new IllegalArgumentException("Invalid flour or water weight: 25.0"), "BagelRecipe", 25.0,
        20.0, 0.5, 0.5, 0.5)
        && t.checkConstructorException(
            new IllegalArgumentException("Invalid flour or water weight: 20.0"), "BagelRecipe",
            20.0, 25.0, 0.3, 0.3, 0.3)
        && t.checkConstructorException(
            new IllegalArgumentException("Invalid yeast or malt weight: 4.0"), "BagelRecipe", 20.0,
            20.0, 4.0, 0.5, 0.5)
        && t.checkConstructorException(new IllegalArgumentException("Invalid salt weight: 9.0"),
            "BagelRecipe", 20.0, 20.0, 0.5, 9.0, 0.5)
        && t.checkConstructorException(
            new IllegalArgumentException("Invalid yeast or malt weight: 0.5"), "BagelRecipe", 20.0,
            20.0, 0.5, 0.5, 2.0)
        && t.checkConstructorException(
            new IllegalArgumentException("Invalid salt weight: 0.41666666666666663"), "BagelRecipe",
            20.0, 0.5, 2.0);
  }

  // tests the sameRecipe method
  boolean testSameRecipe(Tester t) {
    return t.checkExpect(b1.sameRecipe(b2), false) && t.checkExpect(b5.sameRecipe(b5), true);
  }

}