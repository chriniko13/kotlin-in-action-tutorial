package chapter5;

public class PostponeUtil {


    void postponeComputation(int delay, Runnable computation) {

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            if (!Thread.currentThread().isInterrupted()) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException(e);
        }
        computation.run();
    }
}
