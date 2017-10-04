package yaruliy.mqtt.util;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttServiceBuilder {
    public static MqttClient connect(String BROKER_URL, String CLIENT_ID) throws MqttException {
        System.out.println("----------------connected----------------" +
                "\n|BROKER_URL=" + BROKER_URL +
                "\n|CLIENT_ID=" + CLIENT_ID);
        System.out.println("-----------------------------------------");

        MqttClient client = new MqttClient(BROKER_URL, CLIENT_ID, new MemoryPersistence());
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);
        client.connect(mqttConnectOptions);
        return client;
    }
}