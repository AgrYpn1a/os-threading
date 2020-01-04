/** Crytical Section Manager class */
// This will work for two parallel processes
class CSManager {

  // id ::= { 1, 2 }
  // Current id of the process inside CS
  private volatile int processNumber = 1;

  public void criticalSection(Process p) {

    System.out.printf("%s entered CS.%n", p.getName());

    // Do work
    try {
      Thread.sleep(200);
    } catch(Exception e) {}

    System.out.printf("%s finished CS.%n", p.getName());

    // Allow other to enter
    processNumber = p.getProcessId() == 1 ? 2 : 1;
  }

  public int getId() { return processNumber; }
}

abstract class Process extends Thread {
  public abstract int getProcessId();
}

class ProcessA extends Process {
  public final int id = 1;

  private CSManager _cs;
  
  public ProcessA(CSManager csManager) {
    _cs = csManager;
  }

  @Override
  public void run() {

    while(true) {
      // Wait for other process to finish
      while(_cs.getId() != id) {}
    
      _cs.criticalSection(this);
      doWork();
    }

  }

  @Override
  public int getProcessId() { return id; }

  public void doWork() {
    try {
      // System.out.println("*** " + getName() + " working...");
      Thread.sleep(100); 
    } catch(Exception e) {}

    System.out.println("## " + getName() + " waiting...");
  }
}

class ProcessB extends Process {
  public final int id = 2;

  private CSManager _cs;
  
  public ProcessB(CSManager csManager) {
    _cs = csManager;
  }

  @Override
  public void run() {

    while(true) {
      // Wait for other process to finish
      while(_cs.getId() != id) {}
    
      _cs.criticalSection(this);
      doWork();
    }
  }

  @Override
  public int getProcessId() { return id; }

  public void doWork() {
    try {
      System.out.println("*** " + getName() + " working...");
      Thread.sleep(5000); 
    } catch(Exception e) {}

    System.out.println("## " + getName() + " waiting...");
  }
}

public class App {

  public static void main(String[] args) {
    CSManager cs = new CSManager();

    ProcessA a = new ProcessA(cs);
    ProcessB b = new ProcessB(cs);

    a.setName("Process1");
    b.setName("Process2");

    a.start();
    b.start();
  }
}
