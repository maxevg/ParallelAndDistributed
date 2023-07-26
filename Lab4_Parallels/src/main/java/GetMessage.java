import akka.actor.dsl.Inbox;

public class GetMessage {
    private Integer packageId;

    public GetMessage(Integer packageId) {
        this.packageId = packageId;
    }

    public Integer getPackageId() {
        return  this.packageId;
    }
}
