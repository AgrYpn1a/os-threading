class CONST {
  public static final long SIZE = 99900000;
}

class Counter {

  private long _counterA;
  private long _counterB;

  private long _counterAEff;
  private long _counterBEff;

  public synchronized void addOneToA() {
    ++_counterA;
  }

  public synchronized void addOneToB() {
    ++_counterB;
  }

  private Object _a = new Object();
  private Object _b = new Object();

  public void addOneToAEff() {
    synchronized (_a) {
      ++_counterAEff;
    }
  }

  public void addOneToBEff() {
    synchronized (_b) {
      ++_counterBEff;
    }
  }

  public long getCounterA() { return _counterA; }
  public long getCounterB() { return _counterB; }
  public long getCounterAEff() { return _counterAEff; }
  public long getCounterBEff() { return _counterBEff; }
}

class ProcessA extends Thread {

  private Counter _c;

  public ProcessA(Counter c) {
    _c = c;
  }

  @Override
  public void run() {
    for(int i=0; i<CONST.SIZE; i++)
      _c.addOneToA();
  }
}

class ProcessAEfficient extends Thread {

  private Counter _c;

  public ProcessAEfficient(Counter c) {
    _c = c;
  }

  @Override
  public void run() {
    for(int i=0; i<CONST.SIZE; i++)
      _c.addOneToAEff();
  }
}

class ProcessB extends Thread {

  private Counter _c;

  public ProcessB(Counter c) {
    _c = c;
  }

  @Override
  public void run() {
    for(int i=0; i<CONST.SIZE; i++)
      _c.addOneToB();
  }
}

class ProcessBEfficient extends Thread {

  private Counter _c;

  public ProcessBEfficient(Counter c) {
    _c = c;
  }

  @Override
  public void run() {
    for(int i=0; i<CONST.SIZE; i++)
      _c.addOneToBEff();
  }
}

public class App {

  public static void main(String[] args) {

    System.out.println("==== Standard Sync =====");
    doWork();
    System.out.printf("%n%n");
    System.out.println("==== Efficient Sync =====");
    doWorkEfficient();

  }

  private static void doWork() {
    Counter c = new Counter();

    ProcessA pA = new ProcessA(c);
    ProcessB pB = new ProcessB(c);

    double startTime = System.currentTimeMillis();

    pA.start();
    pB.start();

    try {
      pA.join();
      pB.join();
    } catch(Exception e) {}

    double endTime = System.currentTimeMillis();

    System.out.printf("Counter A is %d%n", c.getCounterA());
    System.out.printf("Counter B is %d%n", c.getCounterB());

    System.out.printf("Time to complete: %f ms%n", (endTime - startTime));
  }

  private static void doWorkEfficient() {
    Counter c = new Counter();

    ProcessAEfficient pA = new ProcessAEfficient(c);
    ProcessBEfficient pB = new ProcessBEfficient(c);

    double startTime = System.currentTimeMillis();

    pA.start();
    pB.start();

    try {
      pA.join();
      pB.join();
    } catch(Exception e) {}

    double endTime = System.currentTimeMillis();

    System.out.printf("Counter A is %d%n", c.getCounterAEff());
    System.out.printf("Counter B is %d%n", c.getCounterBEff());

    System.out.printf("Time to complete: %f ms%n", (endTime - startTime));
  }
}
