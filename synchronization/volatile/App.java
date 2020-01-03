import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * In this example we have a generous Santa gifting kids
 * with Christmas presents.
 * 
 * Each kid is a thread that anxiously waits for a present. When
 * Santa arrives (Santa's thread begins executing), he generously
 * gifts all the kids with presents and announces that kid (thread)
 * recieved a present.
 *
 * When a kid receives a present, he/she shouts out (thread prints
 * a message to the screen) and tells everyone about the preseent.
 *
 * ==== Problem
 * Problem that we face in this implementation is with memory consitency.
 * JVM will allow each kid thread to use their local copies (cache) of the
 * variable that holds information about the present. This will make it blind
 * to the changes to present. In order to make sure that this program
 * executes correctly, we need to forbid caching the present variable.
 */

// Present holds a magic number
class Present {
  private int _value = 0;

  public Present(int value) {
    _value = value;
  }

  public int getValue() { return _value; }
}

class Kid extends Thread {

  /** THIS IS THE KEY POINT */
  // volatile keyword tells JVM to forbid threads from making private copies
  // of the variable, thus when the Santa (an external thread) gives
  // a present to the kid thread it will directly change the shared memory,
  // thus allowing kid thread to see the change.
  private volatile Present _present;

  private static int id;
  private int _id;

  public void Kid() {
    _id = id;
  }

  @Override
  public void run() {
    // Wait until you get the present
    while (_present == null) {}

    // Tell everyone you got the present!
    System.out.printf("Just got my present! I found %3d inside.%n", 
        _present.getValue());
  }

  public void give(final Present p) { _present = p; }
}

class Santa extends Thread {

  private final Random _rand = new Random();
  private final List<Kid> _kids;

  public Santa(final List<Kid> kids) {
    _kids = kids;
  }

  @Override
  public void run() {
    // Santa begins his journey, and gifts each
    // of the kids with a present
    for(final Kid k : _kids) {
      Present p = new Present(_rand.nextInt(100));
      k.give(p);
      System.out.printf("Gifted kid %s.%n", k.getName());
    } 
  }
}

// Main app
public class App {

  // Begins the main thread
  public static void main(String[] args) {
    List<Kid> kids = new ArrayList<>();
    for(int i=0; i<10; i++) {
      Kid k = new Kid();
      kids.add(k);
      k.setName("k" + (i+1));
      k.start();
    }

    Santa s = new Santa(kids);
    s.start();
  }
}
