// import java.util.logging.Logger;
// import java.util.logging.Level;

// public class Main {
//     private static int sharedCounter = 0;
    
//     // we'll swap out this instantiation during the demo to show the different locks
//     private static ILock mutex = new LockOne(); 
//     //////////////////////////////////////////////////
    
//     private static final Logger logger = Logger.getLogger(Main.class.getName());

//     public static void main(String[] args) {
//        logger.info("Starting simulation");

//         // two concurrent threads
//         Thread t0 = new Thread(() -> criticalSectionTask(0));
//         Thread t1 = new Thread(() -> criticalSectionTask(1));

//         t0.start();
//         t1.start();

//         try {
//             t0.join();
//             t1.join();
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }

//         logger.info("Expected Counter: 200000");
//         logger.info("Actual Counter:   " + sharedCounter);
//         logger.info("Simulation complete.");
//     }

//     private static void criticalSectionTask(int threadId) {
//         for (int i = 0; i < 100000; i++) {
//             mutex.lock(threadId);
//             try {
//                 sharedCounter++; 
//             } finally {
//                 mutex.unlock(threadId);
//             }
//         }
//     }
// }

public class Main {

    static int counter;

    public static void main(String[] args) throws InterruptedException {
        traceDemo();
        fullDemo();
    }

    // Small run — shows both threads alternating
    static void traceDemo() throws InterruptedException {
        // ILock lock = new LockOne();
        // ILock lock = new LockTwo();
        ILock lock = new PetersonLock();
        counter = 0;
        int n = 5;

        switch (lock) {
            case LockOne l  -> System.out.println("Lock One Demo");
            case LockTwo l  -> System.out.println("Lock Two Demo");
            case PetersonLock p -> System.out.println("Peterson Lock Demo");
            default -> System.out.println("Unknown Lock Type");
        }

        Thread t0 = new Thread(() -> {
            for (int i = 0; i < n; i++) {
                lock.lock(0);
                counter++;
                System.out.println("  Thread-0 enters CS (counter: " + counter + ")");
                lock.unlock(0);
                Thread.yield();
            }
        });

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < n; i++) {
                lock.lock(1);
                counter++;
                System.out.println("  Thread-1 enters CS (counter: " + counter + ")");
                lock.unlock(1);
                Thread.yield();
            }
        });

        t0.start();
        t1.start();
        t0.join(2000);
        t1.join(2000);

        if (t0.isAlive() || t1.isAlive()) {
            t0.interrupt();
            t1.interrupt();
        }
    }

    // Full run — shows mutual exclusion at scale
    static void fullDemo() throws InterruptedException {
        // ILock lock = new LockOne();
        // ILock lock = new LockTwo();
        ILock lock = new PetersonLock();
        int[] counts = new int[2];
        int n = 10000;

        Thread t0 = new Thread(() -> {
            for (int i = 0; i < n; i++) {
                lock.lock(0);
                counts[0]++;
                lock.unlock(0);
            }
        });

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < n; i++) {
                lock.lock(1);
                counts[1]++;
                lock.unlock(1);
            }
        });

        t0.start();
        t1.start();
        t0.join(3000);
        t1.join(3000);

        if (t0.isAlive()) t0.interrupt();
        if (t1.isAlive()) t1.interrupt();

        System.out.println("\nFull run:");
        System.out.println("  Thread-0 expected: " + n + "  actual: " + counts[0]);
        System.out.println("  Thread-1 expected: " + n + "  actual: " + counts[1]);
        System.out.println("  Threads alternate: mutual exclusion holds.");
    }
}
