import tester.*;
import java.util.function.Predicate;

abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  ANode() {
    this.next = null;
    this.prev = null;
  }

  ANode(ANode<T> next, ANode<T> prev) {
    this.next = next;
    this.prev = prev;
  }

  // updates the current count of nodes in the deque
  public abstract int sizeHelper(int start);

  // returns the removed node data
  public abstract T remove();

  // tests the node with the given predicate
  public abstract ANode<T> findHelper(Predicate<T> pred);

  // EFFECT: modifies the deque to remove the given node
  public abstract void removeNodeHelper(ANode<T> node);

}

class Node<T> extends ANode<T> {
  T data;

  Node(T data) {
    super();
    this.data = data;
  }

  Node(T data, ANode<T> node1, ANode<T> node2) {
    super(node1, node2);
    if (node1 == null || node2 == null) {
      throw new IllegalArgumentException("Node cannot be null");
    }
    this.data = data;
    node1.prev = this;
    node2.next = this;
  }

  // updates the current count of nodes in the deque
  public int sizeHelper(int start) {
    start += 1;
    return this.next.sizeHelper(start);
  }

  // returns the removed node data
  public T remove() {
    this.next.prev = this.prev;
    this.prev.next = this.next;
    return this.data;
  }

  // tests the node with the given predicate
  public ANode<T> findHelper(Predicate<T> pred) {
    if (pred.test(this.data)) {
      return this;
    }
    else {
      return this.next.findHelper(pred);
    }
  }

  // EFFECT: modifies the deque to remove the given node
  public void removeNodeHelper(ANode<T> node) {
    ANode<T> current = this;
    if (current == node) {
      this.remove();
    }
    else {
      this.next.removeNodeHelper(node);
    }
  }

}

class Sentinel<T> extends ANode<T> {
  Sentinel() {
    super();
    this.next = this;
    this.prev = this;
  }

  // updates the current count of nodes in the deque
  public int sizeHelper(int start) {
    return start;
  }

  // returns the removed node data
  public T remove() {
    throw new RuntimeException("Cannot remove from empty list");
  }

  // tests the node with the given predicate
  public ANode<T> findHelper(Predicate<T> pred) {
    return this;
  }

  // EFFECT: modifies the deque to remove the given node
  public void removeNodeHelper(ANode<T> node) {
    return;
  }

}

class Deque<T> {
  Sentinel<T> header;

  Deque() {
    this.header = new Sentinel<T>();
  }

  Deque(Sentinel<T> header) {
    this.header = header;
  }

  // returns the size of the deque
  public int size() {
    return this.header.next.sizeHelper(0);
  }

  // creates a new node at the head of the deque
  public void addAtHead(T data) {
    this.header.next = new Node<T>(data, this.header.next, this.header);

  }

  // creates a new node at the tail of the deque
  public void addAtTail(T data) {
    this.header.prev = new Node<T>(data, this.header, this.header.prev);
  }

  // removes node from head and returns removed node
  public T removeFromHead() {
    return this.header.next.remove();
  }

  // removes node from tail and returns removed node
  public T removeFromTail() {
    return this.header.prev.remove();
  }

  // tests all nodes in deque against given predicate
  public ANode<T> find(Predicate<T> pred) {
    return this.header.next.findHelper(pred);
  }

  // EFFECT: modifies deque by removing given node
  public void removeNode(ANode<T> node) {
    this.header.next.removeNodeHelper(node);
  }

}

class Equals<T> implements Predicate<T> {
  @Override
  public boolean test(T t) {
    return t.equals("bcd");
  }
}

class ExamplesDeque {
  ExamplesDeque() {

  }

  Sentinel<String> s1;
  Sentinel<String> s2;

  Node<String> node1;
  Node<String> node2;
  Node<String> node3;
  Node<String> node4;

  Node<String> node5;
  Node<String> node6;
  Node<String> node7;
  Node<String> node8;

  Deque<String> deque1;
  Deque<String> deque2;
  Deque<String> deque3;

  void initConditions() {
    s1 = new Sentinel<String>();
    s2 = new Sentinel<String>();

    node1 = new Node<String>("abc", s1, s1);
    node2 = new Node<String>("bcd", s1, node1);
    node3 = new Node<String>("cde", s1, node2);
    node4 = new Node<String>("def", s1, node3);

    node5 = new Node<String>("hat", s2, s2);
    node6 = new Node<String>("bag", s2, node5);
    node7 = new Node<String>("was", s2, node6);
    node8 = new Node<String>("cat", s2, node7);

    deque1 = new Deque<String>();
    deque2 = new Deque<String>(s1);
    deque3 = new Deque<String>(s2);
  }

  void testSize(Tester t) {
    initConditions();
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque2.size(), 4);
    t.checkExpect(deque3.size(), 4);
  }

  void testSizeHelper(Tester t) {
    initConditions();
    t.checkExpect(node1.sizeHelper(0), 4);
    t.checkExpect(node5.sizeHelper(0), 4);
    t.checkExpect(s1.sizeHelper(0), 0);
  }

  void testAddAtHead(Tester t) {
    initConditions();
    Sentinel<String> s3 = new Sentinel<String>();

    deque1.addAtHead("abc");
    t.checkExpect(deque1.header.next, new Node<String>("abc", s3, s3));
    deque2.addAtHead("aaa");
    t.checkExpect(deque2.header.next, new Node<String>("aaa", node1, s1));
  }

  void testAddAtTail(Tester t) {
    initConditions();
    Sentinel<String> s5 = new Sentinel<String>();

    deque1.addAtTail("abc");
    t.checkExpect(deque1.header.prev, new Node<String>("abc", s5, s5));
    deque2.addAtTail("xxx");
    t.checkExpect(deque2.header.prev, new Node<String>("xxx", s1, node4));
  }

  void testRemoveFromHead(Tester t) {
    initConditions();

    t.checkException(new RuntimeException("Cannot remove from empty list"), deque1,
        "removeFromHead");
    t.checkExpect(deque2.removeFromHead(), "abc");
    t.checkExpect(deque3.removeFromHead(), "hat");

  }

  void testRemoveFromTail(Tester t) {
    initConditions();

    t.checkException(new RuntimeException("Cannot remove from empty list"), deque1,
        "removeFromTail");
    t.checkExpect(deque2.removeFromTail(), "def");
    t.checkExpect(deque3.removeFromTail(), "cat");

  }

  void testRemove(Tester t) {
    initConditions();
    Sentinel<String> s7 = new Sentinel<String>();

    t.checkException(new RuntimeException("Cannot remove from empty list"), s7, "remove");
    t.checkExpect(node4.remove(), "def");
    t.checkExpect(node8.remove(), "cat");
  }

  void testFind(Tester t) {
    initConditions();
    t.checkExpect(deque1.find(new Equals<String>()), new Sentinel<String>());
    t.checkExpect(deque2.find(new Equals<String>()), node2);
    t.checkExpect(deque3.find(new Equals<String>()), s2);

  }

  void testFindHelper(Tester t) {
    initConditions();
    Sentinel<String> s8 = new Sentinel<String>();
    t.checkExpect(s8.findHelper(new Equals<String>()), s8);
    t.checkExpect(node1.findHelper(new Equals<String>()), node2);
    t.checkExpect(node5.findHelper(new Equals<String>()), s2);

  }

  void testRemoveNode(Tester t) {
    initConditions();
    deque1.removeNode(node1);
    deque2.removeNode(node1);
    deque3.removeNode(node8);
    t.checkExpect(deque1.equals(deque1), true);
    t.checkExpect(deque2.header.next, node2);
    t.checkExpect(deque3.header.prev, node7);

  }

}
