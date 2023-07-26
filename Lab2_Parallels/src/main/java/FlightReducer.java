import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class FlightReducer extends Reducer<FlightWritableComparable, Text, Text, Text> {
    protected void reduce(FlightWritableComparable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Iterator<Text> iter = values.iterator();
        Text NameInfo = new Text(iter.next());
        int Count = 0;
        float Min = 0.0F;
        float Max = 0.0F;
        float Average = 0.0F;
        while (iter.hasNext()) {
            float CurrentValue = Float.parseFloat(iter.next().toString());
            if (Count == 0) {
                Min = CurrentValue;
            }
            if (CurrentValue < Min) {
                Min = CurrentValue;
            } else if (CurrentValue > Max) {
                Max = CurrentValue;
            }
            Average += CurrentValue;
            Count++;
        }
        if (Count != 0) {
            Average /= Count;
            context.write(NameInfo, new Text("MINDELA: " + Min + "; MAXDELAY: " + Max + "; AVERAGE: " + Average + ";"));
        }
    }
}
