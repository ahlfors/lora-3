package yaruliy.model.json;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import yaruliy.sockets.crypto.Crypto;

import java.util.UUID;

@Table(keyspace="Lora", name = "messages")
@JsonIgnoreProperties(value = { "chan","rfch","stat","modu", "datr", "codr", "lsnr","rssi", })
public class JSONLoraMessage{
    @Column
    private String data;
    @Column
    private String tmst;
    @Column
    private String freq;
    @Column
    private String size;
    @Column
    @PartitionKey
    private UUID uuid;
    @Column
    private String userID;
    @Column
    private String source;

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }
    public String getData() { return data; }
    public void setData(String data){ this.data = data; }
    public String getTmst() { return tmst; }
    public void setTmst(String tmst){ this.tmst = tmst; }
    public String getFreq() { return freq; }
    public void setFreq(String freq) { this.freq = freq; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public UUID getUuid() { return uuid; }
    public void setUuid(UUID uuid) { this.uuid = uuid; }

    public JSONLoraMessage(@JsonProperty("data") String data,
                           @JsonProperty("tmst") String tmst,
                           @JsonProperty("freq") String freq,
                           @JsonProperty("size") String size,
                           @JsonProperty("uuid") UUID uuid){
        try { this.data = Crypto.toText(Crypto.decrypt(data)); }
        catch (Exception e) { e.printStackTrace(); }
        this.freq = freq;
        this.tmst = tmst;
        this.size = size;
        this.uuid = uuid;
        this.userID = "login";
    }
    public JSONLoraMessage(){}

    public String toString(){
        return "{" +
                "\n\t" + "data: " + "\"" + this.data + "\"" +"," +
                "\n\t" + "tmst: " + this.tmst + "," +
                "\n\t" + "freq: " + this.freq + "," +
                "\n\t" + "size: " + this.size + "," +
                "\n\t" + "uuid: " + this.uuid + "," +
                "\n\t" + "userID: " + "\"" + this.userID + "\"" +
                "\n" +
                "}";
    }
}