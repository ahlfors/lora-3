package yaruliy.app;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import yaruliy.sockets.SocketServer;

@SpringBootApplication(scanBasePackages = "yaruliy")
public class LoraApplication {
    private static SocketServer socketServer;
    @Autowired public void setSocketServer(@Qualifier("loraUDPSocketServer") SocketServer server){ socketServer = server; }

	public static void main(String[] args) {
	    SpringApplication.run(LoraApplication.class, args);
	    socketServer.init();
        socketServer.start();
	}
}