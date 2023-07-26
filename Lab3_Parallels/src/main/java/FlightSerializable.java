import java.io.Serializable;

public class FlightSerializable implements Serializable {
    private int DEST_AIRPORT_ID;
    private float ORIGIN_AIRPORT_ID;
    private float ARR_DELAY;
    private float CANCELLED;

    public FlightSerializable() {}

    public FlightSerializable(int DEST_AIRPORT_ID, float ORIGIN_AIRPORT_ID, float ARR_DELAY, float CANCELLED) {
        this.DEST_AIRPORT_ID = DEST_AIRPORT_ID;
        this.ARR_DELAY = ARR_DELAY;
        this.ORIGIN_AIRPORT_ID = ORIGIN_AIRPORT_ID;
        this.CANCELLED = CANCELLED;
    }

    public float getARR_DELAY() {
        return ARR_DELAY;
    }

    public float getCANCELLED() {
        return ARR_DELAY;
    }

    public float getDEST_AIRPORT_ID() {
        return ARR_DELAY;
    }

    public float getORIGIN_AIRPORT_ID() {
        return ORIGIN_AIRPORT_ID;
    }

}
