import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlightMapper extends Mapper<LongWritable, Text, FlightWritableComparable, Text> {
    private static float CheckNullDelay(String current) {
        if (current.equals("")) {
            return 0.0F;
        } else {
            return Float.parseFloat(current);
        }
    }

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        if (key.get() > 0) {
            String[] Table = value.toString().split(",");
            int DestAeroportID = Integer.parseInt(Table[14]);
            float ArrDelay = CheckNullDelay(Table[17]);
            if (ArrDelay > 0.0F) {
                FlightWritableComparable CurrentKey = new FlightWritableComparable(DestAeroportID, 1);
                context.write(CurrentKey, new Text(Table[17]));
            }
        }
    }
}