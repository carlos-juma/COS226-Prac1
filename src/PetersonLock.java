public class PetersonLock implements ILock {
    //shared variables (flag array and victim)
    private volatile boolean[] flag = new boolean[2];
    private volatile int victim;
    @Override
    public void lock(int threadId) {
        int i = threadId;
        int j = 1 - i; //the other thread

        flag[i] = true;   //"i want to enter"
        victim = i;       // "you may enter"

        //wait ONLY if the other thread is interested AND it is my turn to yield
        while (flag[j] && victim == i) { 
            //wait
        }
    }

    @Override
    public void unlock(int threadId) {
        // lower the flag to allow the waiting thread to continue
        flag[threadId] = false; 
    }
}