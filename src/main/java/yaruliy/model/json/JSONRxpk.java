package yaruliy.model.json;

public class JSONRxpk {
    private JSONLoraMessage[] rxpk;
    public JSONLoraMessage[] getLoraMessages() { return rxpk; }
    public void setRxpk(JSONLoraMessage[] rxpk) { this.rxpk = rxpk; }
    public JSONRxpk(){}
}