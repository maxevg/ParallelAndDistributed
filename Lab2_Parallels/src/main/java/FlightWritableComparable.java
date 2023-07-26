import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class FlightWritableComparable implements WritableComparable {
    private int AEROPORT_ID;
    private int INDENTITY;

    public FlightWritableComparable() {}

    public FlightWritableComparable(int AEROPORT_ID, int INDENTITY) {
        this.AEROPORT_ID = AEROPORT_ID;
        this.INDENTITY = INDENTITY;
    }

    public int getID() {
        return this.AEROPORT_ID;
    }

    public int compareID(Object O) {
        FlightWritableComparable Second = (FlightWritableComparable) O;
        return Integer.compare(this.AEROPORT_ID, Second.AEROPORT_ID);
    }

    public int compareTo(Object O) {
        FlightWritableComparable Second = (FlightWritableComparable) O;
        if (this.AEROPORT_ID > Second.AEROPORT_ID) {
            return 1;
        } else if (this.AEROPORT_ID < Second.AEROPORT_ID) {
            return  -1;
        } else if (this.INDENTITY > Second.INDENTITY) {
            return 1;
        } else {
            return -1;
        }
    }

    public void write(DataOutput DataOutput) throws IOException {
        DataOutput.writeInt(this.AEROPORT_ID);
        DataOutput.writeInt(this.INDENTITY);
    }

    public void readFields(DataInput DataInput) throws IOException {
        this.AEROPORT_ID = DataInput.readInt();
        this.INDENTITY = DataInput.readInt();
    }
}
