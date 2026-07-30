import java.util.logging.Logger;
import java.util.logging.Level;

public class Main {
    private static int sharedCounter = 0;
    
    // we'll swap out this instantiation during the demo to show the different locks
    private static ILock mutex = new PetersonLock(); 
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
       logger.info("Starting simulation with two threads...");
        // FEATURE: Demonstrate using exactly two concurrent threads
        Thread t0 = new Thread(() -> criticalSectionTask(0));
        Thread t1 = new Thread(() -> criticalSectionTask(1));

        t0.start();
        t1.start();

        try {
            t0.join();
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("Expected Counter: 200000");
        logger.info("Actual Counter:   " + sharedCounter);
        logger.info("Simulation complete.");
    }

    private static void criticalSectionTask(int threadId) {
        for (int i = 0; i < 100000; i++) {
            mutex.lock(threadId);
            try {
                sharedCounter++; 
            } finally {
                mutex.unlock(threadId);
            }
        }
    }
}