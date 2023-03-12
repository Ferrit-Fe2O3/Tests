public class Main {
    public static void main(String[] args) {
        StatsAccumulator statsAccumulator = new StatsAccumulatorImp();
        statsAccumulator.add(1);
        statsAccumulator.add(2);
        statsAccumulator.add(3);
        statsAccumulator.add(4);
        System.out.println("Count: " + statsAccumulator.getCount());
        System.out.println("Max: " + statsAccumulator.getMax());
        System.out.println("Min: " + statsAccumulator.getMin());
        System.out.println("Avg: " + statsAccumulator.getAvg());
    }
}