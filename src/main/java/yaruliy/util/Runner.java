package yaruliy.util;
import java.util.List;

public class Runner {
    public static void main(String[] args) {
        Sender sender = new Sender();
        List<String> l = sender.send();
        for (String s: l) {
            System.out.println(s);
        }
    }
}