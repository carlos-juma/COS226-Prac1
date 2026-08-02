// ==========================================
//[name]
// ==========================================
public class LockOne implements ILock {
    //  Declare shared state variables here (e.g., flag array)

    private boolean[] flag = new boolean[2];


    @Override
    public void lock(int threadId) {

        int i = threadId;
        int j = 1 - i;

        flag[i] = true;

        while(flag[j]){
         // wait
        }
    }

    @Override
    public void unlock(int threadId) {
        int i = threadId;
        flag[i] = false;
    }
}