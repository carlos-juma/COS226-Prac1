import java.util.logging.Logger;
import java.util.logging.Level;

public class Main {

    // we'll swap out this instantiation during the demo to show the different locks
    private static ILock mutex = new PetersonLock(); 
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    // The target word we want to spell
    private static final char[] TARGET_WORD = "TARGARYEN".toCharArray();

    // Shared resources
    private static int currentIndex = 0;
    private static StringBuilder sharedSpellbook = new StringBuilder();

    public static void main(String[] args) {
       logger.info("Starting simulation");

        //2 concurrent threads
        Thread t0 = new Thread(() -> spellWord(0));
        Thread t1 = new Thread(() -> spellWord(1));
        t0.start();
        t1.start();

        try {
            t0.join();
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("Expected Word: TARGARYEN");
        logger.info("Actual Word:   " + sharedSpellbook.toString());
        logger.info("Simulation complete.");
    }

private static void spellWord(int threadId) {
        while (true) {
            mutex.lock(threadId);     
            try {
                // Check if the word is already finished before proceeding
                if (currentIndex >= TARGET_WORD.length) {
                    break; 
                }

                // CRITICAL SECTION: Read the next character
                char nextChar = TARGET_WORD[currentIndex];

                // enforced delay, this forces the thread to pause while holding the lock.
                // If the lock is broken or missing, the other thread will cause a crash
                try {
                    Thread.sleep(10); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // Append the character and move to the next index
                sharedSpellbook.append(nextChar);
                currentIndex++;
                
                logger.info("Thread " + threadId + " wrote: " + nextChar);

            } finally {
                mutex.unlock(threadId);
            }
        }
    }
}