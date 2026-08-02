
public interface ILock {
    void lock(int threadId);
    void unlock(int threadId);
}