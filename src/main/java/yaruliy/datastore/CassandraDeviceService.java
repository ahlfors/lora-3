package yaruliy.datastore;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import yaruliy.model.Device;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CassandraDeviceService implements DeviceService{
    private Cluster cluster = null;
    private Session session = null;
    @Value("${cassandra.address}")
    private String address;
    public CassandraDeviceService(){}

    @Override
    public List<Device> getDevices() {
        openSession();
        String query = "SELECT * FROM Lora.devices;";
        MappingManager manager = new MappingManager(session);
        Mapper<Device> mapper = manager.mapper(Device.class);
        ResultSet results = session.execute(query);
        List<Device> devices = new ArrayList<>();
        while (mapper.map(results).iterator().hasNext()){
            devices.add(mapper.map(results).iterator().next());
        }
        return devices;
    }

    @Override
    public Device getDeviceByID(String id) {
        openSession();
        UUID uuid = UUID.fromString(id);
        openSession();
        MappingManager manager = new MappingManager(session);
        Mapper<Device> mapper = manager.mapper(Device.class);
        return mapper.get(uuid);
    }

    @Override
    public Device getDeviceByLoraID(String id) {
        openSession();

        if(containsDevice(id)){
            String query = "SELECT * FROM Lora.devices WHERE loraid = " + "'" + id + "'" + " ALLOW FILTERING ;";
            MappingManager manager = new MappingManager(session);
            Mapper<Device> mapper = manager.mapper(Device.class);
            ResultSet results = session.execute(query);
            Device result = mapper.map(results).iterator().next();
            if(result == null) return new Device();
            return result;
        }
        else {
            Device device = new Device();
            device.setLoraid(id);
            device.setUuid(UUID.randomUUID());
            device.setLastUsedGateway("06-00-00-00-00-00");
            device.setLastUsedGatewayDate(new Timestamp(System.currentTimeMillis()));
            return device;
        }
    }

    @Override
    public String saveDevice(Device device) {
        openSession();
        MappingManager manager = new MappingManager(session);
        Mapper<Device> mapper = manager.mapper(Device.class);
        mapper.save(device);
        return "Device successfully recorded";
    }

    @Override
    public long deviceCount() {
        openSession();
        String q = "SELECT count(*) FROM Lora.devices;";
        MappingManager manager = new MappingManager(session);
        Mapper<Device> mapper = manager.mapper(Device.class);
        ResultSet results = session.execute(q);
        return results.one().getLong("count");
    }

    @Override
    public void openSession() {
        prepareDataBase();
        cluster = Cluster.builder().addContactPoint(address).build();
        session = cluster.connect("Lora");
    }

    @Override
    public void closeSession() {
        if (session != null) session.close();
        if (cluster != null) cluster.close();
    }

    private void prepareDataBase() {
        final String createKeySpace =
                "CREATE KEYSPACE IF NOT EXISTS Lora WITH replication = {'class':'SimpleStrategy', 'replication_factor':3};";

        final String createTable =
                "CREATE TABLE IF NOT EXISTS Lora.devices ("
                        + "uuid uuid PRIMARY KEY,"
                        + "loraid text,"
                        + "longitude double,"
                        + "latitude double,"
                        + "lastUsedGateway text,"
                        + "lastUsedGatewayDate timestamp"
                        + ");";

        Cluster.builder().addContactPoint(address).build().connect().execute(createKeySpace);
        Cluster.builder().addContactPoint(address).build().connect().execute(createTable);
    }

    @Override
    public boolean containsDevice(String loraid){
        String q = "select count( * ) from devices where loraid = '" + loraid + "' ALLOW FILTERING;";
        openSession();
        MappingManager manager = new MappingManager(session);
        Mapper<Device> mapper = manager.mapper(Device.class);
        ResultSet results = session.execute(q);
        return results.one().getLong("count") > 0;
    }

    @PostConstruct
    public void postConstruct() {
        prepareDataBase();
    }
}