/**
 * Watch the output of the program. See how only one CS is done 
 * at the time, even though they are independant of each other in
 * a sense that they work on different things that need not be in sync
 * with each other.
 */

class LockTarget {
  private int _value = 153;

  // CS1 is independant of CS2
  public synchronized void cs1() {
    try {
      System.out.println("CS 1");
      Thread.sleep(3000);
    } catch(Exception e) {} 
  }

  // CS2 is independant of CS1
  public synchronized void cs2() {
    try {
      System.out.println("CS 2");
      Thread.sleep(3000);
    } catch(Exception e) {} 
  }

  public int getValue() {
    return _value;
  }
}

class Process extends Thread {

  private LockTarget _target;
  private int _id;

  public Process(final LockTarget target, int id) {
    _target = target;
    _id = id;
  }

  @Override
  public void run() {
    while(!isInterrupted()) {
      if(_id == 1) {
        _target.cs1();
      } else if(_id == 2) {
        _target.cs2();
      }
    }
  }
}

public class App {

  public static void main(String[] args) {
    LockTarget target = new LockTarget();

    Process p1 = new Process(target, 1);
    Process p2 = new Process(target, 2);

    p1.start();
    p2.start();
  
  }
}
