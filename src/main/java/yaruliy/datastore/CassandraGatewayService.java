package yaruliy.datastore;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import yaruliy.model.Gateway;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CassandraGatewayService {
    private Cluster cluster = null;
    private Session session = null;
    @Value("${cassandra.address}")
    private String address;

    public CassandraGatewayService() {}

    public void saveGateway(Gateway gateway){
        openSession();
        MappingManager manager = new MappingManager(session);
        Mapper<Gateway> mapper = manager.mapper(Gateway.class);
        mapper.save(gateway);
    }

    public List<Gateway> getDevices() {
        openSession();
        String query = "SELECT * FROM Lora.gateways;";
        MappingManager manager = new MappingManager(session);
        Mapper<Gateway> mapper = manager.mapper(Gateway.class);
        ResultSet results = session.execute(query);
        List<Gateway> devices = new ArrayList<>();
        while (mapper.map(results).iterator().hasNext()){
            devices.add(mapper.map(results).iterator().next());
        }
        return devices;
    }

    public Gateway getGatewayByMac(String mac){
        openSession();
        String query = "SELECT * FROM Lora.gateways WHERE mac = " + "'" + mac + "'" + " ALLOW FILTERING ;";
        MappingManager manager = new MappingManager(session);
        Mapper<Gateway> mapper = manager.mapper(Gateway.class);
        ResultSet results = session.execute(query);
        Gateway result = mapper.map(results).iterator().next();
        if(result == null) return new Gateway();
        return result;
    }

    public void openSession() {
        prepareDataBase();
        cluster = Cluster.builder().addContactPoint(address).build();
        session = cluster.connect("Lora");
    }

    public void closeSession() {
        if (session != null) session.close();
        if (cluster != null) cluster.close();
    }

    private void prepareDataBase() {
        final String createKeySpace =
                "CREATE KEYSPACE IF NOT EXISTS Lora WITH replication = {'class':'SimpleStrategy', 'replication_factor':3};";

        final String createTable =
                "CREATE TABLE IF NOT EXISTS Lora.gateways ("
                        + "uuid uuid PRIMARY KEY,"
                        + "mac text"
                        + ");";

        Cluster.builder().addContactPoint(address).build().connect().execute(createKeySpace);
        Cluster.builder().addContactPoint(address).build().connect().execute(createTable);
    }

    @PostConstruct
    public void postConstruct() { prepareDataBase(); }
}