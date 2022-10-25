import java.util.*;

import tester.*; 

/**
 * A class that defines a new permutation code, as well as methods for encoding
 * and decoding of the messages that use this code.
 */
class PermutationCode {
  // The original list of characters to be encoded
  ArrayList<Character> alphabet = new ArrayList<Character>(
      Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
          'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));

  ArrayList<Character> code = new ArrayList<Character>(26);

  // A random number generator
  Random rand = new Random();

  // Create a new instance of the encoder/decoder with a new permutation code
  PermutationCode() {
    this.code = this.initEncoder();
  }

  // Create a new instance of the encoder/decoder with the given code
  PermutationCode(ArrayList<Character> code) {
    this.code = code;
  }

  // Initialize the encoding permutation of the characters
  ArrayList<Character> initEncoder() {
    ArrayList<Character> alphaCopy = new ArrayList<Character>();
    ArrayList<Character> finalList = new ArrayList<Character>();

    for (char x : this.alphabet) {
      alphaCopy.add(x);
    }

    int randomIndex;

    for (int i = alphaCopy.size() - 1; i >= 0; i--) {
      randomIndex = rand.nextInt(alphaCopy.size());
      finalList.add(alphaCopy.get(randomIndex));
      alphaCopy.remove(randomIndex);
    }

    return finalList;
  }

  // produce an encoded String from the given String
  String encode(String source) {
    return ""; // you should complete this method
  }

  // produce a decoded String from the given String
  String decode(String code) {

    String c = code;
    ArrayList<String> cL = new ArrayList<String>(Arrays.asList(c.split("")));
    ArrayList<Character> codeList = new ArrayList<Character>();
    ArrayList<Integer> indexList = new ArrayList<Integer>();
    ArrayList<Character> answerList = new ArrayList<Character>();

    for (String x : cL) {
      codeList.add(x.charAt(0));
    }

    for (char p : codeList) {
      for (int i = 0; i <= this.code.size() - 1; i++) {
        if (this.code.get(i).equals(p)) {
          indexList.add(i);
        }
      }
    }

    for (int i : indexList) {
      for (int j = 0; j <= this.alphabet.size() - 1; i++) {
        if (i == j) {
          answerList.add(this.alphabet.get(j));
        }
      }
    }

    StringBuilder stringBuilder = new StringBuilder();

    for (char r : answerList) {
      stringBuilder.append(r);
    }

    return stringBuilder.toString();

  }
}

class ExamplesPermutation {
  PermutationCode ex1 = new PermutationCode();
  ArrayList<Character> code = new ArrayList<Character>(
      Arrays.asList('m', 'p', 's', 'y', 'j', 'w', 'q', 'i', 'x', 'a', 'f', 'u', 'l', 'o', 'n', 'b',
          'k', 'h', 'v', 'g', 'e', 'r', 'z', 't', 'c', 'd'));

  PermutationCode ex2 = new PermutationCode(code);

  void testDecode(Tester t) {
    t.checkExpect(ex2.decode("vnbixm"), "sophia");
    t.checkExpect(ex2.decode("vnbixm"), "lucas");
  }

}