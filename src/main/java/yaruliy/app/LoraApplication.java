package yaruliy.app;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import yaruliy.mqtt.MainMqttService;
import yaruliy.sockets.SocketServer;

@SpringBootApplication(scanBasePackages = "yaruliy")
public class LoraApplication {
    private static SocketServer socketServer;
    @Autowired public void setSocketServer(SocketServer server){ this.socketServer = server; }
    private static MainMqttService mainMqttService;
    @Autowired public void setMqttService(MainMqttService mainMqttService){this.mainMqttService = mainMqttService; }

	public static void main(String[] args) {
	    SpringApplication.run(LoraApplication.class, args);
	    mainMqttService.consumer().run();
	    socketServer.init();
	    socketServer.start();
	}
}