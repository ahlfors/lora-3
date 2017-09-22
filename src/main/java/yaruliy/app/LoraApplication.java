package yaruliy.app;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import yaruliy.mqtt.MqttService;
import yaruliy.sockets.SocketServer;

@SpringBootApplication(scanBasePackages = "yaruliy")
public class LoraApplication {
    private static SocketServer socketServer;
    @Autowired public void setSocketServer(@Qualifier("loraUDPSocketServer") SocketServer server){
    	socketServer = server;
    }
    private static MqttService mqttService;
    @Autowired public void setMqttService(MqttService mqttService){this.mqttService = mqttService; }

	public static void main(String[] args) {
	    SpringApplication.run(LoraApplication.class, args);
	    mqttService.consumer().run();
	    socketServer.init();
	    socketServer.start();
	}
}