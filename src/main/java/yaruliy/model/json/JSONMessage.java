package yaruliy.model.json;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Table;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.UUID;

@Table(keyspace="Lora", name = "messages")
public class JSONMessage{
    @Column
    private String timestamp;
    @Column
    private String coords;
    @Column
    private int userID;
    @Column
    private UUID id;

    public JSONMessage(){}

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public String getCoords() { return coords; }
    public void setCoords(String coords) { this.coords = coords; }
    public int getUserID() { return userID;  }
    public void setUserID(int userID) { this.userID = userID; }
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public JSONMessage(@JsonProperty("timestamp") String timestamp,
                       @JsonProperty("coords") String coords,
                       @JsonProperty("userID") int userID,
                       @JsonProperty("id") UUID id){
        this.timestamp = timestamp;
        //String[] ss = coords.split(",");
        //int[] array = new int[ss.length];
        //for (int i = 0; i < ss.length; i++){ array[i] = Integer.parseInt(ss[i]); }
        this.coords = coords;
        this.id = id;
        this.userID = userID;
    }

    public JSONMessage(String timestamp, int[] coords, int userID, UUID id){
        this.timestamp = timestamp;
        this.coords = Arrays.toString(coords);
        this.id = id;
        this.userID = userID;
    }
}