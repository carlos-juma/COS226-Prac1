public class LockTwo {
    private volatile int victim;

    public LockTwo() {
        victim = -1;
    }

    public void lock(int i) {
        victim = i;
        while (victim == i) {
            Thread.yield();
        }
    }

    public void unlock(int i) {
    }
}
