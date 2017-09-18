package yaruliy.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import yaruliy.sockets.LoraUDPSocketServer;
import yaruliy.sockets.SocketServer;

@Configuration
public class BeansConfig {
    final ApplicationContext context;
    @Autowired public BeansConfig(ApplicationContext context) { this.context = context; }
    @Bean public ObjectMapper getObjectMapper() { return new ObjectMapper(); }
}