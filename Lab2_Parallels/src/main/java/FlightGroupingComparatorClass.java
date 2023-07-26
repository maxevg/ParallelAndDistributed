import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class FlightGroupingComparatorClass extends WritableComparator {
    protected  FlightGroupingComparatorClass() {
        super(FlightWritableComparable.class, true);
    }

    public int compare(WritableComparable a, WritableComparable b) {
        FlightWritableComparable FirstFlight = (FlightWritableComparable) a;
        FlightWritableComparable SecondFlight = (FlightWritableComparable) b;
        return FirstFlight.compareID(SecondFlight);
    }
}
