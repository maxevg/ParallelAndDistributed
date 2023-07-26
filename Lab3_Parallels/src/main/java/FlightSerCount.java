import java.io.Serializable;

public class FlightSerCount implements Serializable {
    private float MaxArrDelay;
    private int CountOfFlights;
    private int CountOfDelays;
    private int CountOfCancelled;

    public FlightSerCount() {}

    public FlightSerCount(float MaxArrDelay, int CountOfFlights, int CountOfDelays, int CountOfCancelled){
        this.MaxArrDelay = MaxArrDelay;
        this.CountOfFlights = CountOfFlights;
        this.CountOfDelays = CountOfDelays;
        this.CountOfCancelled = CountOfCancelled;
    }

    public float getMaxArrDelay() {
        return MaxArrDelay;
    }

    public float getCountOfFlights() {
        return CountOfFlights;
    }

    public float getCountOfDelays() {
        return CountOfDelays;
    }

    public float getCountOfCancelled() {
        return CountOfCancelled;
    }

    public static FlightSerCount addValue(FlightSerCount a, float MaxArrDelay, boolean isDelayed, boolean isCancelled) {
        return new FlightSerCount(a.getCountOfFlights() + 1,
                (int) (isDelayed ? a.getCountOfDelays() + 1 : a.getCountOfDelays()),
                (int) Math.max(a.getMaxArrDelay(), MaxArrDelay),
                (int) (isCancelled ? a.getCountOfCancelled() + 1 : a.getCountOfCancelled()));
    }

    public static FlightSerCount add(FlightSerCount a, FlightSerCount b) {
        return new FlightSerCount(a.getCountOfFlights() + b.getCountOfFlights(),
                (int) (a.getCountOfDelays() + b.getCountOfDelays()),
                (int) Math.max(a.getMaxArrDelay(), b.getMaxArrDelay()),
                (int) (a.getCountOfCancelled() + b.getCountOfCancelled()));
    }

    public static String toOutString(FlightSerCount a) {
        float percentOfDelays = (float) a.getCountOfDelays() / a.getCountOfFlights() * 100;
        float percentOfCancelled = (float) a.getCountOfCancelled() / a.getCountOfFlights() * 100;
        return "INFO : { MaxDelay: " + a.getMaxArrDelay() +
                "; Flights : "  + a.getCountOfFlights() +
                "; Delays : " + a.getCountOfDelays() +
                "; Cancelled : " + a.getCountOfCancelled() +
                "}, % of Delays : " + percentOfDelays +
                "%, % of Cancelled : " + percentOfCancelled +
                "%.";
    }

}
