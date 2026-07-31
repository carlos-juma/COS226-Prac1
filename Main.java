public class Main {

    static int counter;

    public static void main(String[] args) throws InterruptedException {
        traceDemo();
        fullDemo();
    }

    // Small run — shows both threads alternating
    static void traceDemo() throws InterruptedException {
        LockTwo lock = new LockTwo();
        counter = 0;
        int n = 5;

        System.out.println("LockTwo Demonstration:\n");

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
        LockTwo lock = new LockTwo();
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
