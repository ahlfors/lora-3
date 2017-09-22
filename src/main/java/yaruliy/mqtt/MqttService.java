package yaruliy.mqtt;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MqttService {
    @Value("${BROKER_URL}") private String BROKER_URL;
    @Value("${CLIENT_ID}") private String CLIENT_ID;
    @Value("${TOPIC_NAME}") private String TOPIC_NAME;
    @Value("${QOS}") private String QOS;

    private MqttCallback mqttCallback;
    @Autowired public void setMqttCallback(MqttCallback mqttCallback){this.mqttCallback = mqttCallback;}

    private MqttClient connect(){
        try {
            System.out.println("----------------connected----------------" +
                    "\n|BROKER_URL=" + BROKER_URL +
                    "\n|CLIENT_ID=" + CLIENT_ID + "\n|");

            MqttClient client = new MqttClient(BROKER_URL, CLIENT_ID, new MemoryPersistence());
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(true);
            client.connect(mqttConnectOptions);
            return client;
        }
        catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("except " + me);
            me.printStackTrace();
        }
        return null;
    }

    /*public void publish(String data) {
        try {
            MqttClient client = connect();
            System.out.println("Publish message: " + data);
            MqttMessage message = new MqttMessage(data.getBytes(Charset.forName("UTF-8")));
            message.setQos(Integer.parseInt(QOS));
            client.setCallback(new BasicCallback());
            client.publish(TOPIC_NAME, message);
            client.disconnect();
            System.out.println("Successfully disconnected from MQTT Broker...");
        }
        catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("except " + me);
            me.printStackTrace();
        }
    }*/

    public Runnable consumer(){
        return () -> {
            try {
                MqttClient client = connect();
                client.setCallback(this.mqttCallback);
                client.subscribe(TOPIC_NAME, 1);
            }
            catch (MqttException e) { e.printStackTrace(); }
        };
    }
}