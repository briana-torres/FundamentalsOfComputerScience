import tester.*;

// to represent a list of Strings
interface ILoString {
  // combine all Strings in this list into one
  String combine();

  // inserts a string into a list alphabetically
  ILoString insert(String other);

  // sorts a list alphabetically
  ILoString sort();

  // determines if the first element in the list is sorted
  boolean beforeOrAfter();

  // determines if a list is sorted
  boolean isSorted();
  
  // alternates two lists 
  ILoString interleaveHelper(ILoString list); 

  // alternates two lists
  ILoString interleave(ILoString list);

  // sorts a merged list
  ILoString mergeHelper(ILoString list);

  // alternates two lists and sorts the returned list
  ILoString merge(ILoString list);

  // sorts a list in reverse order
  ILoString reverseInsert(String other);

  // reverses a list
  ILoString reverse();

  // determines if a list is doubled
  boolean doubledHelper(String other);

  // returns true if a list is doubled
  boolean isDoubledList();

  // returns count of list
  int count(); 
  
  // returns true if a list is the same in reverse order
  boolean isPalindromeList();
}

// to represent an empty list of Strings
class MtLoString implements ILoString {
  MtLoString() {
  }

  /* 
  TEMPLATE
  FIELDS:
  N/A
  
  METHODS
  ... this.combine() ...                        -- String
  ... this.insert(String other) ...             -- ILoString
  ... this.sort() ...                           -- ILoString
  ... this.beforeOrAfter() ...                  -- boolean 
  ... this.isSorted() ...                       -- boolean
  ... this.interleaveHelper(ILoString list) ... -- ILoString
  ... this.interleave(ILoString list) ...       -- ILoString 
  ... this.mergeHelper(ILoString list) ...      -- ILoString
  ... this.merge(ILoString list) ...            -- ILoString 
  ... this.reverseInsert(String other) ...      -- ILoString 
  ... this.reverse() ...                        -- ILoString 
  ... this.doubledHelper(String other) ...      -- boolean
  ... this.isDoubledList() ...                  -- boolean
  ... this.count() ...                          -- int 
  ... this.isPalindromeList() ...               -- boolean 
  
  METHODS FOR FIELDS
  N/A
  
  */

  // combine all Strings in this list into one
  public String combine() {
    return "";
  }

  // inserts a string into a list
  public ILoString insert(String other) {
    return new ConsLoString(other, this);
  }

  // sorts a list alphabetically
  public ILoString sort() {
    return this;
  }

  // determines if the first element in the list is sorted
  public boolean beforeOrAfter() {
    return true;
  }

  // determines if a list is sorted
  public boolean isSorted() {
    return true;
  }
  
  // alternates two lists {
  public ILoString interleaveHelper(ILoString list) {
    return this; 
  }

  // alternates two lists
  public ILoString interleave(ILoString list) {
    return list.interleaveHelper(this); 
  }

  // sorts a merged list
  public ILoString mergeHelper(ILoString list) {
    return this;
  }

  // alternates two lists and sorts the returned list
  public ILoString merge(ILoString list) {
    return list.mergeHelper(this);
  }

  // sorts a list in reverse order
  public ILoString reverseInsert(String other) {
    return new ConsLoString(other, new MtLoString());
  }

  // reverses a list
  public ILoString reverse() {
    return this;
  }

  // determines if a list is doubled
  public boolean doubledHelper(String other) {
    return this.isDoubledList(); 
  }

  // returns true if a list is doubled
  public boolean isDoubledList() {
    return true;
  }
  
  // returns count of a list 
  public int count() {
    return 0; 
  }

  // returns true if a list is the same in reverse order
  public boolean isPalindromeList() {
    return true;
  }
}

// to represent a nonempty list of Strings
class ConsLoString implements ILoString {
  String first;
  ILoString rest;

  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
  TEMPLATE
  FIELDS:
  ... this.first ...         -- String
  ... this.rest ...          -- ILoString
  
  METHODS
  ... this.combine() ...                        -- String
  ... this.insert(String other) ...             -- ILoString
  ... this.sort() ...                           -- ILoString
  ... this.beforeOrAfter() ...                  -- boolean 
  ... this.isSorted() ...                       -- boolean
  ... this.interleaveHelper(ILoString list) ... -- ILoString
  ... this.interleave(ILoString list) ...       -- ILoString 
  ... this.mergeHelper(ILoString list) ...      -- ILoString
  ... this.merge(ILoString list) ...            -- ILoString 
  ... this.reverseInsert(String other) ...      -- ILoString 
  ... this.reverse() ...                        -- ILoString 
  ... this.doubledHelper(String other) ...      -- boolean
  ... this.count() ...                          -- int 
  ... this.isDoubledList() ...                  -- boolean
  ... this.isPalindromeList() ...               -- boolean 
  
  METHODS FOR FIELDS
  ... this.first.concat(String) ...                  -- String
  ... this.first.compareTo(String) ...               -- int
  ... this.rest.combine() ...                        -- String
  ... this.rest.insert(String) ...                   -- ILoString 
  ... this.rest.sort() ...                           -- ILoString 
  ... this.rest.beforeOrAfter() ...                  -- boolean
  ... this.rest.isSorted() ...                       -- boolean
  ... this.rest.interleaveHelper(ILoString list) ... -- ILoString
  ... this.rest.interleave(ILoString) ...            -- ILoString 
  ... this.rest.mergeHelper(ILoString) ...           -- ILoString
  ... this.rest.merge(ILoString list) ...            -- ILoString 
  ... this.rest.reverseInsert(String) ...            -- ILoString 
  ... this.rest.reverse() ...                        -- ILoString 
  ... this.rest.doubledHelper(String) ...            -- boolean
  ... this.rest.count() ...                          -- int 
  ... this.rest.isDoubledList() ...                  -- boolean
  ... this.rest.isPalindromeList() ...               -- boolean 
  
  */

  // combine all Strings in this list into one
  public String combine() {
    return this.first.concat(this.rest.combine());
  }

  // inserts a string into a list
  public ILoString insert(String other) {
    if (this.first.compareToIgnoreCase(other) <= 0) {
      return new ConsLoString(this.first, this.rest.insert(other));
    }
    else {
      return new ConsLoString(other, this);
    }
  }

  // sorts a list alphabetically
  public ILoString sort() {
    return (this.rest.sort()).insert(this.first);
  }

  // determines if the first element in the list is sorted
  public boolean beforeOrAfter() {
    String daRest = rest.combine();
    if (daRest.isEmpty()) {
      return true;
    }
    else {
      return this.first.compareToIgnoreCase(daRest) <= 0;
    }
  }

  // determines if a list is sorted
  public boolean isSorted() {
    return this.beforeOrAfter() && this.rest.isSorted();
  }

  // alternates two lists
  public ILoString interleaveHelper(ILoString list) {
    return new ConsLoString(this.first, list.interleave(this.rest));
  }
  
  // alternates two lists
  public ILoString interleave(ILoString list) {
    return this.interleaveHelper(list);
  }

  // sorts a merged list
  public ILoString mergeHelper(ILoString list) {
    return this.rest.mergeHelper(list).insert(this.first);
  }

  // alternates two lists and sorts the returned list
  public ILoString merge(ILoString list) {
    ILoString newList2 = new ConsLoString(this.first, list.merge(this.rest));
    return newList2.mergeHelper(this.rest);
  }

  // sorts a list in reverse order
  public ILoString reverseInsert(String other) {
    return new ConsLoString(this.first, this.rest.reverseInsert(other));
  }

  // reverses a list
  public ILoString reverse() {
    return (this.rest.reverse()).reverseInsert(this.first);
  }

  // determines if a list is doubled
  public boolean doubledHelper(String other) {
    if (this.first.compareTo(other) == 0) {
      return this.rest.isDoubledList();
    }
    else {
      return false;
    }
  }

  //returns count of a list
  public int count() {
    return 1 + this.rest.count();
  }

  // returns true if a list is doubled
  public boolean isDoubledList() {
    if (this.count() % 2 == 0) {
      return this.rest.doubledHelper(this.first);
    }
    else {
      return false;
    }
  }
  
  // returns true if a list is the same in reverse order
  public boolean isPalindromeList() {
    return this.interleave(this.reverse()).isDoubledList();
  }

}


// to represent examples for lists of strings
class ExamplesStrings {
  ILoString empty = new MtLoString();
  ILoString mary = new ConsLoString("Mary ", new ConsLoString("had ", new ConsLoString("a ",
      new ConsLoString("little ", new ConsLoString("lamb.", new MtLoString())))));
  ILoString marySorted = new ConsLoString("a ", new ConsLoString("had ",
      new ConsLoString("Mary ", new ConsLoString("little ", new ConsLoString("lamb.", empty)))));
  ILoString s1 = new ConsLoString("a", new ConsLoString("B", new ConsLoString("c", empty)));
  ILoString s2 = new ConsLoString("1", new ConsLoString("2", empty));
  ILoString s3 = new ConsLoString("1", new ConsLoString("2",
      new ConsLoString("3", new ConsLoString("4", new ConsLoString("5", empty)))));
  ILoString s4 = new ConsLoString("d", new ConsLoString("e", new ConsLoString("f", empty)));
  ILoString s5 = new ConsLoString("z", new ConsLoString("y", new ConsLoString("x", empty)));
  ILoString s6 = new ConsLoString("a",
      new ConsLoString("a", new ConsLoString("a", new ConsLoString("a", empty))));

  // test the method combine for the lists of Strings
  boolean testCombine(Tester t) {
    return t.checkExpect(this.mary.combine(), "Mary had a little lamb.");
  }

  // test the method combine for the lists of Strings
  boolean testCombine2(Tester t) {
    return t.checkExpect(this.s5.combine(), "zyx");
  }

  // test the method insert
  boolean testInsert(Tester t) {
    return t.checkExpect(empty.insert("hi"), new ConsLoString("hi", empty));
  }

  // test the method insert
  boolean testInsert2(Tester t) {
    return t.checkExpect(s1.insert("hi"), new ConsLoString("a",
        new ConsLoString("B", new ConsLoString("c", new ConsLoString("hi", empty)))));
  }

  // test the sort method for a list of strings
  boolean testSort(Tester t) {
    return t.checkExpect(mary.sort(),
        new ConsLoString("a ", new ConsLoString("had ", new ConsLoString("lamb.",
            new ConsLoString("little ", new ConsLoString("Mary ", new MtLoString()))))));
  }

  // test the sort method for a list of strings
  boolean testSort2(Tester t) {
    return t.checkExpect(s5.sort(),
        new ConsLoString("x", new ConsLoString("y", new ConsLoString("z", empty))));
  }

  // test the method beforeOrAfter
  boolean testBeforeOrAfter(Tester t) {
    return t.checkExpect(empty.beforeOrAfter(), true);
  }

  // test the method beforeOrAfter
  boolean testBeforeOrAfter2(Tester t) {
    return t.checkExpect(mary.beforeOrAfter(), false);
  }

  // test the method isSorted
  boolean testIsSorted(Tester t) {
    return t.checkExpect(mary.isSorted(), false);
  }

  // test the method isSorted
  boolean testIsSorted2(Tester t) {
    return t.checkExpect(s1.isSorted(), true);
  }
  
  // test the method mergeHelper
  boolean testInterleaveHelper(Tester t) {
    return t.checkExpect(empty.interleaveHelper(s5), empty);
  }
  
  //test the method mergeHelper
  boolean testInterleaveHelper2(Tester t) {
    return t.checkExpect(s2.interleaveHelper(empty), new ConsLoString("1", new ConsLoString("2",
        new MtLoString())));
  }

  // test the method interleave
  boolean testInterleave(Tester t) {
    return t
        .checkExpect(mary.interleave(s1),
            new ConsLoString("Mary ",
                new ConsLoString("a", new ConsLoString("had ", new ConsLoString("B",
                    new ConsLoString("a ", new ConsLoString("c", new ConsLoString("little ",
                        new ConsLoString("lamb.", new MtLoString())))))))));
  }

  // test the method interleave
  boolean testInterleave2(Tester t) {
    return t.checkExpect(s2.interleave(s3), new ConsLoString("1",
        new ConsLoString("1",
            new ConsLoString("2", new ConsLoString("2", new ConsLoString("3",
                new ConsLoString("4", new ConsLoString("5", new MtLoString()))))))));
  }

  // test the method mergeHelper
  boolean testMergeHelper(Tester t) {
    return t.checkExpect(s2.mergeHelper(empty),
        new ConsLoString("1", new ConsLoString("2", new MtLoString())));
  }

  // test the method mergeHelper
  boolean testMergeHelper2(Tester t) {
    return t.checkExpect(empty.mergeHelper(s5), empty);
  }

  // test the method merge
  boolean testMerge(Tester t) {
    return t.checkExpect(s4.merge(s1), new ConsLoString("a", new ConsLoString("B", new ConsLoString(
        "c",
        new ConsLoString("d", new ConsLoString("e", new ConsLoString("f", new MtLoString())))))));
  }

  // test the method merge
  boolean testMerge2(Tester t) {
    return t.checkExpect(empty.merge(s5),
        new ConsLoString("x", new ConsLoString("y", new ConsLoString("z", empty))));
  }

  // test the method reverseInsert
  boolean testReverseInsert(Tester t) {
    return t.checkExpect(empty.reverseInsert("hi"), new ConsLoString("hi", empty));
  }

  // test the method reverseInsert
  boolean testReverseInsert2(Tester t) {
    return t.checkExpect(s5.reverseInsert("a"), new ConsLoString("z",
        new ConsLoString("y", new ConsLoString("x", new ConsLoString("a", empty)))));
  }

  // test the method reverse
  boolean testReverse(Tester t) {
    return t.checkExpect(s4.reverse(),
        new ConsLoString("f", new ConsLoString("e", new ConsLoString("d", empty))));
  }

  // test the method reverse
  boolean testReverse2(Tester t) {
    return t.checkExpect(empty.reverse(), empty);
  }

  // test the method doubledHelper
  boolean testDoubledHelper(Tester t) {
    return t.checkExpect(s6.doubledHelper("b"), false);
  }

  // test the method doubledHelper
  boolean testDoubledHelper2(Tester t) {
    return t.checkExpect(empty.doubledHelper("b"), true);
  }

  // test the method doubledHelper
  boolean testDoubledHelper3(Tester t) {
    return t.checkExpect(s6.doubledHelper("a"), false);
  }
  
  // test the method count 
  boolean testCount(Tester t) {
    return t.checkExpect(empty.count(), 0);
  }
  
  //test the method count 
  boolean testCount2(Tester t) {
    return t.checkExpect(s5.count(), 3);
  }

  // test the method isDoubledList
  boolean testIsDoubledList(Tester t) {
    return t.checkExpect(s6.isDoubledList(), true);
  }

  // test the method isDoubledList
  boolean testIsDoubledList2(Tester t) {
    return t.checkExpect(s4.isDoubledList(), false);
  }

  // test the method isDoubledList
  boolean testIsDoubledList3(Tester t) {
    return t.checkExpect(empty.isDoubledList(), true);
  }
  
  //test the method isDoubledList
  boolean testIsDoubledList4(Tester t) {
    return t.checkExpect(mary.interleave(mary).isDoubledList(), true);
  }

  // test the method isPalindromeList
  boolean testIsPalindromeList(Tester t) {
    return t.checkExpect(s6.isPalindromeList(), true);
  }

  // test the method isPalindromeList
  boolean testIsPalindromeList2(Tester t) {
    return t.checkExpect(mary.isPalindromeList(), false);
  }

  // test the method isPalindromeList
  boolean testIsPalindromeList3(Tester t) {
    return t.checkExpect(empty.isPalindromeList(), true);
  }
  
}