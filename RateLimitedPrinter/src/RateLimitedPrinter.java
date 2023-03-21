public class RateLimitedPrinter {

    private int interval;

    private long lastInvoke = 0;

    public RateLimitedPrinter(int interval) {
        this.interval = interval;
    }

    public void print(String message) {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - lastInvoke) > interval) {
            System.out.println(message);
            lastInvoke = currentTime;
        }
    }

}
