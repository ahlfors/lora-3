package yaruliy.util;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Sender implements ISender{
    public Sender(){}

    @Override
    public List<String> send() {
        List<String> list  = new ArrayList<>();
        Resource resource = new ClassPathResource("python/sender.py");
        try{
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(Runtime.getRuntime()
                    .exec("python3 " + resource.getFile().getAbsoluteFile())
                    .getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(Runtime.getRuntime()
                    .exec("python3 " + resource.getFile().getAbsoluteFile())
                    .getErrorStream()));
            String s;
            while ((s = stdInput.readLine()) != null) { list.add(s); }
            while ((s = stdError.readLine()) != null) { list.add(s); }
            return list;
        }
        catch (IOException e) { e.printStackTrace(); }
        return list;
    }
}