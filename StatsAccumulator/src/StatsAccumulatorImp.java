public class StatsAccumulatorImp implements StatsAccumulator {

    private int count = 0;

    private int max;

    private int min;

    private int sum;

    @Override
    public void add(int value) {
        count++;
        if (count == 1) {
            max = value;
            min = value;
            sum = value;
        } else {
            if (max < value) {
                max = value;
            } else if (min > value) {
                min = value;
            }
            sum += value;
        }
    }

    @Override
    public int getMin() {
        return min;
    }

    @Override
    public int getMax() {
        return max;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Double getAvg() {
        return (double) sum / count;
    }
}
