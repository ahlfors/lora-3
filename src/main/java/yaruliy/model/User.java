package yaruliy.model;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(keyspace="Lora", name = "users")
public class User{
    @PartitionKey
    @Column private UUID uuid;
    @Column private String login;
    @Column private String password;
    @Column private String name;
    @Column private String surname;
    @Column private List<String> emails;

    public List<String> getEmails() { return emails; }
    public void setEmails(List<String> emails) { this.emails = emails; }
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

    public User(String login, String password, String name, String surname, String email){
        this.emails = new ArrayList<>();
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.uuid = UUID.randomUUID();
        this.emails.add(email);
    }

    public String toString(){ return "[" + this.login + "; " + this.password + "]"; }
}