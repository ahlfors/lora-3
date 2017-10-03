package yaruliy.model;
import com.datastax.driver.core.LocalDate;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.Date;
import java.util.UUID;

@Table(keyspace="Lora", name = "devices")
public class Device {
    @PartitionKey
    @Column private UUID uuid;
    @Column private String loraid;
    @Column private double longitude;
    @Column private double latitude;
    @Column private String lastUsedGateway;
    @Column private LocalDate lastUsedGatewayDate;

    public LocalDate getLastUsedGatewayDate() {
        return lastUsedGatewayDate;
    }
    public void setLastUsedGatewayDate(LocalDate lastUsedGatewayDate) {
        this.lastUsedGatewayDate = lastUsedGatewayDate;
    }
    public String getLastUsedGateway() { return lastUsedGateway; }
    public void setLastUsedGateway(String lastUsedGateway) { this.lastUsedGateway = lastUsedGateway; }
    public UUID getUuid() { return uuid; }
    public void setUuid(UUID uuid) { this.uuid = uuid; }
    public String getLoraid() { return loraid; }
    public void setLoraid(String loraid) { this.loraid = loraid; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public Device(){}

    public String toString(){
        return "uuid: " + this.uuid + "\nloraid: " + this.loraid +
                "\nlongitude: " + this.longitude + "\nlatitude: " + this.latitude + "\n";
    }
}