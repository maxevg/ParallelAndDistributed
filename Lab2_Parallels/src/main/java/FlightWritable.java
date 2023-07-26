import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class FlightWritable implements Writable {
    private int DEST_AIRPORT_ID;
    private float ARR_DELAY;
    private float AIR_TIME;
    private float CANCELLED;

    public FlightWritable() {}

    public FlightWritable(int DEST_AIRPORT_ID, float ARR_DELAY, float AIR_TIME, float CANCELLED) {
        this.DEST_AIRPORT_ID = DEST_AIRPORT_ID;
        this.ARR_DELAY = ARR_DELAY;
        this.AIR_TIME = AIR_TIME;
        this.CANCELLED = CANCELLED;
    }

    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.DEST_AIRPORT_ID);
        dataOutput.writeFloat(this.ARR_DELAY);
        dataOutput.writeFloat(this.AIR_TIME);
        dataOutput.writeFloat(this.CANCELLED);
    }

    public void readFields(DataInput dataInput) throws IOException {
        this.DEST_AIRPORT_ID = dataInput.readInt();
        this.ARR_DELAY = dataInput.readFloat();
        this.AIR_TIME = dataInput.readFloat();
        this.CANCELLED = dataInput.readFloat();
    }

}
