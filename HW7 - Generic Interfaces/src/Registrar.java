import tester.*;

class Course {
  String name;
  Instructor prof;
  IList<Student> students;

  Course(String name, Instructor prof) {
    this.name = name;
    this.prof = prof;
    this.prof.setCourse(this);
    this.students = new MtList<Student>();
  }

  // adds given student to the student list 
  void enroll(Student s) {

    this.students = new ConsList<Student>(s, this.students);

  }

  // returns true if both course names are the same  
  boolean sameCourseHelper(Course c) {
    return this.name.equals(c.name);
  }

  // calls the exists method on the list of students 
  int checkStudent(String name) {

    return this.students.exists(name);

  }
}

class Instructor {
  String name;
  IList<Course> courses;

  Instructor(String name) {
    this.name = name;
    this.courses = new MtList<Course>();
  }

  // add the given course to the list of courses 
  void setCourse(Course c) {

    this.courses = new ConsList<Course>(c, this.courses);

  }

  // calls the dejavuHelper method on the list of courses 
  boolean dejavu(Student s) {

    return this.courses.dejavuHelper(s.name, 0);

  }
}

class Student {
  String name;
  int id;
  IList<Course> courses;

  Student(String name, int id) {
    this.name = name;
    this.id = id;
    this.courses = new MtList<Course>();
  }

  // adds the given course to the list of courses 
  void enroll(Course c) {

    this.courses = new ConsList<Course>(c, this.courses);

    c.enroll(this);
  }

  // calls the classmatesHelper function on the list of courses 
  boolean classmates(Student c) {

    return this.courses.classmatesHelper(c.courses);

  }

  // returns true if both names are the same 
  boolean existsHelper(String name) {
    return this.name.equals(name);
  }

}

interface IList<T> {
  //calls the sameCourse method on a list of courses 
  boolean classmatesHelper(IList<Course> c);

  // returns true if the given course matches the first in the list 
  boolean sameCourse(Course c);

  // counts how many classes a student takes with the same instructor 
  boolean dejavuHelper(String name, int count);

  // checks if a student exists in a course
  int exists(String name);
}

class MtList<T> implements IList<T> {
  MtList() {
  }
 
  //calls the sameCourse method on a list of courses 
  public boolean classmatesHelper(IList<Course> c) {
    return false;
  }

  // returns true if the given course matches the first in the list 
  public boolean sameCourse(Course c) {
    return false;
  }

  // counts how many classes a student takes with the same instructor 
  public boolean dejavuHelper(String name, int count) {
    return false;
  }

  //checks if a student exists in a course
  public int exists(String name) {
    return 0;
  }

}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  //calls the sameCourse method on a list of courses 
  public boolean classmatesHelper(IList<Course> c) {

    return c.sameCourse((Course) this.first);

  }

  // returns true if the given course matches the first in the list 
  public boolean sameCourse(Course c) {
    if (((Course) this.first).sameCourseHelper(c)) {
      return true;
    }
    else {
      return this.rest.sameCourse(c);
    }
  }

  // counts how many classes a student takes with the same instructor 
  public boolean dejavuHelper(String name, int count) {
    count += ((Course) this.first).checkStudent(name);
    if (count >= 2) {
      return true;
    }
    else {
      return this.rest.dejavuHelper(name, count);
    }
  }

  //checks if a student exists in a course
  public int exists(String name) {
    if (((Student) this.first).existsHelper(name)) {
      return 1;
    }
    else {
      return this.rest.exists(name);
    }

  }

}

class IListExamples {
  IListExamples() {
  }

  Instructor i1;
  Instructor i2;
  Instructor i3;

  Course c1;
  Course c2;
  Course c3;
  Course c4;

  Student s1;
  Student s2;
  Student s3;
  Student s4;
  Student s5;

  void initConditions() {
    this.i1 = new Instructor("Rainy Ramirez");
    this.i2 = new Instructor("Tony Hawks");
    this.i3 = new Instructor("Elias Torres");

    this.c1 = new Course("Painting 101", i2);
    this.c2 = new Course("Linear Algebra", i1);
    this.c3 = new Course("Horseback Riding", i2);
    this.c4 = new Course("Coding 300", i3);

    this.s1 = new Student("Sarah Connor", 00123);
    this.s2 = new Student("Dora", 00125);
    this.s3 = new Student("Pete Davidson", 00420);
    this.s4 = new Student("Lucas Bolufer", 00222);
    this.s5 = new Student("The Rock", 00456);
  }

  void testUpdateCourses(Tester t) {
    initConditions();

    t.checkExpect(i1.courses, new ConsList<Course>(c2, new MtList<Course>()));
    t.checkExpect(i2.courses,
        new ConsList<Course>(c3, new ConsList<Course>(c1, new MtList<Course>())));
    t.checkExpect(i3.courses, new ConsList<Course>(c4, new MtList<Course>()));

  }

  void testEnroll(Tester t) {
    initConditions();
    s1.enroll(c1);
    t.checkExpect(s1.courses, new ConsList<Course>(c1, new MtList<Course>()));
    t.checkExpect(c1.students, new ConsList<Student>(s1, new MtList<Student>()));

    s1.enroll(c2);
    t.checkExpect(s1.courses,
        new ConsList<Course>(c2, new ConsList<Course>(c1, new MtList<Course>())));
    t.checkExpect(c2.students, new ConsList<Student>(s1, new MtList<Student>()));

  }

  void testClassmates(Tester t) {
    initConditions();
    s1.enroll(c1);
    s2.enroll(c1);
    s3.enroll(c2);
    s5.enroll(c4);

    t.checkExpect(s1.classmates(s2), true);
    t.checkExpect(s2.classmates(s1), true);
    t.checkExpect(s1.classmates(s3), false);
    t.checkExpect(s1.classmates(s5), false);
  }

  void testClassmatesHelper(Tester t) {
    initConditions();
    s1.enroll(c1);
    s2.enroll(c1);
    s3.enroll(c2);
    s5.enroll(c4);

    t.checkExpect(s1.courses.classmatesHelper(s2.courses), true);
    t.checkExpect(s2.courses.classmatesHelper(s1.courses), true);
    t.checkExpect(s1.courses.classmatesHelper(s3.courses), false);
    t.checkExpect(s1.courses.classmatesHelper(s5.courses), false);
  }

  void testSameCourse(Tester t) {
    initConditions();
    s1.enroll(c1);
    s2.enroll(c1);
    s3.enroll(c2);
    s5.enroll(c4);

    t.checkExpect(s1.courses.sameCourse(c1), true);
    t.checkExpect(s2.courses.sameCourse(c1), true);
    t.checkExpect(s3.courses.sameCourse(c1), false);
    t.checkExpect(s5.courses.sameCourse(c1), false);

  }

  void testSameCourseHelper(Tester t) {
    initConditions();
    t.checkExpect(c1.sameCourseHelper(c1), true);
    t.checkExpect(c1.sameCourseHelper(c2), false);

  }

  void testDejavu(Tester t) {
    initConditions();
    s1.enroll(c1);
    s1.enroll(c3);

    t.checkExpect(i2.dejavu(s1), true);
    t.checkExpect(i2.dejavu(s2), false);
    t.checkExpect(i1.dejavu(s2), false);

  }

  void testDejavuHelper(Tester t) {
    initConditions();
    s1.enroll(c1);
    s1.enroll(c3);

    t.checkExpect(i2.courses.dejavuHelper("Sarah Connor", 0), true);
    t.checkExpect(i1.courses.dejavuHelper("Sarah Connor", 0), false);

  }

  void testCheckStudent(Tester t) {
    initConditions();
    s1.enroll(c1);
    s1.enroll(c3);

    t.checkExpect(c1.checkStudent("Sarah Connor"), 1);
    t.checkExpect(c3.checkStudent("Sarah Connor"), 1);
    t.checkExpect(c4.checkStudent("Sarah Connor"), 0);

  }

  void testExists(Tester t) {
    initConditions();
    s1.enroll(c1);
    s1.enroll(c3);

    t.checkExpect(c1.students.exists("Sarah Connor"), 1);
    t.checkExpect(c3.students.exists("Sarah Connor"), 1);
    t.checkExpect(c1.students.exists("Random"), 0);

  }

}
