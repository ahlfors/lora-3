package yaruliy.datastore;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import yaruliy.model.json.JSONLoraMessage;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CassandraMessageService implements MessageService {
    private Cluster cluster = null;
    private Session session = null;
    @Value("${cassandra.address}")
    private String address;

    public CassandraMessageService() {}

    @Override
    public void writeMessage(JSONLoraMessage message){
        openSession();
        MappingManager manager = new MappingManager(session);
        Mapper<JSONLoraMessage> mapper = manager.mapper(JSONLoraMessage.class);
        mapper.save(message);
    }

    @Override
    public List<JSONLoraMessage> getMessagesByUserLogin(String login) {
        openSession();
        String query = "SELECT * FROM Lora.messages;";// WHERE userid = " + "'" + login + "'" + " ALLOW FILTERING ;";
        MappingManager manager = new MappingManager(session);
        Mapper<JSONLoraMessage> mapper = manager.mapper(JSONLoraMessage.class);
        Result<JSONLoraMessage> messages = mapper.map(session.execute(query));

        List<JSONLoraMessage> result = new ArrayList<>();
        while (messages.iterator().hasNext()){ result.add(messages.iterator().next()); }
        return result;
    }

    @Override
    public JSONLoraMessage getMessageByID(String id) {
        UUID uuid = UUID.fromString(id);
        openSession();
        MappingManager manager = new MappingManager(session);
        Mapper<JSONLoraMessage> mapper = manager.mapper(JSONLoraMessage.class);
        return mapper.get(uuid);
    }

    @Override
    public void deleteMessage(int id) {}

    private void prepareDataBase() {
        final String createKeySpace =
                "CREATE KEYSPACE IF NOT EXISTS Lora WITH replication = {'class':'SimpleStrategy', 'replication_factor':3};";

        final String createTable =
                "CREATE TABLE IF NOT EXISTS Lora.messages ("
                        + "uuid uuid PRIMARY KEY,"
                        + "data text,"
                        + "tmst text,"
                        + "freq text,"
                        + "size text,"
                        + "userID text,"
                        + "source text,"
                        + "topic text"
                        + ");";
        Cluster.builder().addContactPoint(address).build().connect().execute(createKeySpace);
        Cluster.builder().addContactPoint(address).build().connect().execute(createTable);
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

    @PostConstruct
    public void postConstruct() {
        prepareDataBase();
    }
}