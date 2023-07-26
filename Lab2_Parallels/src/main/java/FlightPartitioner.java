import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class FlightPartitioner extends Partitioner<FlightWritableComparable, Text> {
    public int getPartition(FlightWritableComparable key, Text value, int numReduceTasks) {
        return key.getID() % numReduceTasks;
    }
}
