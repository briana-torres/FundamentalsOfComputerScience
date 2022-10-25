import tester.*;

interface IEntertainment {
  // compute the total price of this Entertainment
  double totalPrice();

  // computes the minutes of entertainment of this IEntertainment
  int duration();

  // produce a String that shows the name and price of this IEntertainment
  String format();

  // checks if two magazines are the same
  boolean sameMagazine(Magazine other);

  // checks if two TV series are the same
  boolean sameTVSeries(TVSeries other);

  // checks if two podcasts are the same
  boolean samePodcast(Podcast other);

  // is this IEntertainment the same as that one?
  boolean sameEntertainment(IEntertainment that);

}

abstract class AEntertainment implements IEntertainment {
  String name;
  double price; // represents price per issue
  int installments; // number of issues per year

  // abstract entertainment constructor
  AEntertainment(String name, double price, int installments) {
    this.name = name;
    this.price = price;
    this.installments = installments;
  }

  // returns the total price of an IEntertainment
  public double totalPrice() {
    return this.price * this.installments;
  }

  // returns the total duration of an IEnterainment
  public int duration() {
    return 50 * this.installments;
  }

  // returns a description of an IEntertainment
  public String format() {
    return this.name + ", " + this.price + ".";
  }

  // returns false
  public boolean sameMagazine(Magazine other) {
    return false;
  }

  // returns false
  public boolean sameTVSeries(TVSeries other) {
    return false;
  }

  // returns false
  public boolean samePodcast(Podcast other) {
    return false;
  }

}

class Magazine extends AEntertainment {
  String genre;
  int pages;

  // constructor for a magazine
  Magazine(String name, double price, String genre, int pages, int installments) {
    super(name, price, installments);
    this.genre = genre;
    this.pages = pages;
  }

  // computes the minutes of entertainment of this Magazine, (includes all
  // installments)
  @Override
  public int duration() {
    return 5 * this.pages * this.installments;
  }

  // checks if two magazines are the same
  @Override
  public boolean sameMagazine(Magazine other) {
    return this.name == other.name && this.price == other.price && this.genre == other.genre
        && this.pages == other.pages && this.installments == other.installments;
  }

  // is this Magazine the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment that) {
    return that.sameMagazine(this);
  }
}

class TVSeries extends AEntertainment {
  String corporation;

  // TV series constructor
  TVSeries(String name, double price, int installments, String corporation) {
    super(name, price, installments);
    this.corporation = corporation;
  }

  // checks if two TV series are the same
  @Override
  public boolean sameTVSeries(TVSeries other) {
    return this.name == other.name && this.price == other.price
        && this.installments == other.installments && this.corporation == other.corporation;
  }

  // is this TVSeries the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment that) {
    return that.sameTVSeries(this);
  }

}

class Podcast extends AEntertainment {

  // podcast constructor
  Podcast(String name, double price, int installments) {
    super(name, price, installments);
  }

  // checks if two podcasts are the same
  @Override
  public boolean samePodcast(Podcast other) {
    return this.name == other.name && this.price == other.price
        && this.installments == other.installments;
  }

  // is this Podcast the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment that) {
    return that.samePodcast(this);
  }
}

class ExamplesEntertainment {
  IEntertainment rollingStone = new Magazine("Rolling Stone", 2.55, "Music", 60, 12);
  IEntertainment houseOfCards = new TVSeries("House of Cards", 5.25, 13, "Netflix");
  IEntertainment serial = new Podcast("Serial", 0.0, 8);
  IEntertainment vogue = new Magazine("Vogue", 5.0, "Fashion", 50, 12);
  IEntertainment got = new TVSeries("Game of Thrones", 0.0, 300, "HBO");
  IEntertainment chd = new Podcast("Call Her Daddy", 0.0, 69);
  Magazine cosmo = new Magazine("Cosmopolitan", 3.0, "Lifestyle", 60, 12);
  TVSeries bones = new TVSeries("Bones", 0.0, 16, "Netflix");
  Podcast eggs = new Podcast("Eggs", 20.0, 1);

  // testing total price method
  boolean testTotalPrice(Tester t) {
    return t.checkInexact(this.rollingStone.totalPrice(), 2.55 * 12, .0001)
        && t.checkInexact(this.houseOfCards.totalPrice(), 5.25 * 13, .0001)
        && t.checkInexact(this.serial.totalPrice(), 0.0, .0001)
        && t.checkInexact(this.vogue.totalPrice(), 5.0 * 12, .0001)
        && t.checkInexact(this.got.totalPrice(), 0.0, .0001)
        && t.checkInexact(this.chd.totalPrice(), 0.0, .0001);
  }

  // tests the duration method
  boolean testDuration(Tester t) {
    return t.checkExpect(this.rollingStone.duration(), 5 * 60 * 12)
        && t.checkExpect(this.houseOfCards.duration(), 50 * 13)
        && t.checkExpect(this.serial.duration(), 50 * 8);
  }

  // tests the format method
  boolean testFormat(Tester t) {
    return t.checkExpect(this.vogue.format(), "Vogue, 5.0.")
        && t.checkExpect(this.got.format(), "Game of Thrones, 0.0.")
        && t.checkExpect(this.chd.format(), "Call Her Daddy, 0.0.");
  }

  // tests the sameMagazine method
  boolean testSameMagazine(Tester t) {
    return t.checkExpect(this.rollingStone.sameMagazine(cosmo), false)
        && t.checkExpect(this.got.sameMagazine(cosmo), false)
        && t.checkExpect(this.chd.sameMagazine(cosmo), false)
        && t.checkExpect(this.cosmo.sameMagazine(cosmo), true);
  }

  // test the sameTVSeries method
  boolean testSameTVSeries(Tester t) {
    return t.checkExpect(this.rollingStone.sameTVSeries(bones), false)
        && t.checkExpect(this.got.sameTVSeries(bones), false)
        && t.checkExpect(this.chd.sameTVSeries(bones), false)
        && t.checkExpect(this.bones.sameTVSeries(bones), true);
  }

  // tests the samePodcast method
  boolean testSamePodcast(Tester t) {
    return t.checkExpect(this.rollingStone.samePodcast(eggs), false)
        && t.checkExpect(this.got.samePodcast(eggs), false)
        && t.checkExpect(this.chd.samePodcast(eggs), false)
        && t.checkExpect(this.eggs.samePodcast(eggs), true);
  }

  // tests the sameEntertainment method
  boolean testSameEntertainment(Tester t) {
    return t.checkExpect(this.rollingStone.sameEntertainment(got), false)
        && t.checkExpect(this.houseOfCards.sameEntertainment(vogue), false)
        && t.checkExpect(this.serial.sameEntertainment(chd), false)
        && t.checkExpect(this.vogue.sameEntertainment(vogue), true)
        && t.checkExpect(this.got.sameEntertainment(got), true)
        && t.checkExpect(this.chd.sameEntertainment(chd), true);
  }

}