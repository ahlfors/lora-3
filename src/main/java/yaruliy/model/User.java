package yaruliy.model;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Table;
import java.util.UUID;

@Table(keyspace="Lora", name = "users")
public class User{
    @Column
    private String login;
    @Column
    private String password;
    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private UUID uuid;
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public UUID getUuid() { return uuid; }
    public void setUuid(UUID uuid) { this.uuid = uuid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public User(){}
    public User(String login, String password, UUID uuid){
        this.login = login;
        this.password = password;
        this.uuid = uuid;
    }

    public User(String login, String password, String name, String surname){
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.uuid = UUID.randomUUID();
    }

    public String toString(){ return "[" + this.login + "; " + this.password + "]"; }
}