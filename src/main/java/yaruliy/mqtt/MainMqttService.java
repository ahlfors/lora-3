package yaruliy.mqtt;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import yaruliy.mqtt.calls.BasicCallback;
import yaruliy.mqtt.util.MqttServiceBuilder;

@Component
public class MainMqttService {
    @Value("${BROKER_URL}") private String BROKER_URL;
    @Value("${QOS}") private String QOS;

    private BasicCallback mqttCallback;
    @Autowired public void setMqttCallback(BasicCallback mqttCallback){this.mqttCallback = mqttCallback;}

    public Runnable consumer(){
        return () -> {
            try {
                MqttClient client = MqttServiceBuilder.connect(BROKER_URL, "Lora_SUB");
                client.setCallback(this.mqttCallback);
                this.mqttCallback.setDefaultClient(MqttServiceBuilder.connect(BROKER_URL, "Lora_PUB"));
                String[] topics = {"lora/messages","lora/coordinates", "lora/test"};
                client.subscribe(topics);
            }
            catch (MqttException e) { e.printStackTrace(); }
        };
    }
}