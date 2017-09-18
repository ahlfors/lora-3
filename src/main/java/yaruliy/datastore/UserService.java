package yaruliy.datastore;
import yaruliy.model.User;

public interface UserService {
    User findByLogin(String login);
    String saveUser(User user);
    void openSession();
    void closeSession();
}