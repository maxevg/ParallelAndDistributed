import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;

import java.io.IOException;


public class FlightNameMapper extends Mapper<LongWritable, Text, FlightWritableComparable, Text> {
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        if (key.get() > 0) {
            String[] Table = value.toString().split("\",");
            int DestAeroportID = Integer.parseInt(Table[0].replaceAll("\"", ""));
            FlightWritableComparable CurrentKey = new FlightWritableComparable(DestAeroportID, 0);
            context.write(CurrentKey, new Text(Table[1]));
        }
    }
}
