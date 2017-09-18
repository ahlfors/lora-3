package yaruliy.datastore;
import yaruliy.model.json.JSONLoraMessage;
import java.util.List;

public interface MessageService {
    void writeMessage(JSONLoraMessage message);
    List<JSONLoraMessage> getMessagesByUserLogin(String login);
    JSONLoraMessage getMessageByID(String id);
    void deleteMessage(int id);
    void openSession();
    void closeSession();
}