package yaruliy.mqtt.calls;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yaruliy.datastore.CassandraGatewayService;
import yaruliy.datastore.DeviceService;
import yaruliy.datastore.MessageService;
import yaruliy.email.Sender;
import yaruliy.model.Device;
import yaruliy.model.Gateway;
import yaruliy.model.json.JSONData;
import yaruliy.model.json.JSONLoraMessage;
import yaruliy.model.json.JSONRxpk;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

@Component
public class BasicCallback implements MqttCallback {
    private int count = 0;
    private SimpleDateFormat dateFormat;
    private Calendar cal;
    private MqttClient client;

    private CassandraGatewayService gatewayService;
    @Autowired public void setGatewayService(CassandraGatewayService service){ this.gatewayService = service; }
    private MessageService messageService;
    @Autowired public void setMessageService(MessageService messageService){ this.messageService = messageService; }
    private DeviceService deviceService;
    @Autowired public void setDeviceService(DeviceService deviceService){ this.deviceService = deviceService; }
    private ObjectMapper mapper;
    @Autowired public void setMapper(ObjectMapper mapper){ this.mapper = mapper; }
    private Sender sender;
    @Autowired public void setSender(Sender sender){ this.sender = sender; }

    public BasicCallback(){
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Kyiv"));
        dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        cal = Calendar.getInstance();
    }

    public void connectionLost(Throwable arg0) {
        System.out.println("---------------------\n" + arg0.toString() + "\n----------------------");
        System.out.println("Connection Lost");
        arg0.printStackTrace();
    }

    public void messageArrived(String topic, MqttMessage arrivedMessage) throws Exception {
        String data = new String(arrivedMessage.getPayload());
        System.out.println("-------------------------------------------------");
        System.out.println("| Topic:" + topic);
        System.out.println("| Message: " + data);
        System.out.println("-------------------------------------------------");

        String currentTime = getCurrentTime();

        switch(topic){
            case "lora/coordinates": {
                data = data.substring(data.indexOf('{'), data.lastIndexOf('}') + 1);
                System.out.println("parser data:" + data + "\n");

                mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                JSONLoraMessage message = mapper.readValue(data, JSONRxpk.class).getLoraMessages()[0];
                System.out.println("loramessage: \n" + message.toString());

                message.setUuid(UUID.randomUUID());
                message.setTmst(currentTime);
                message.setSource("MQTT");
                message.setTopic(topic);
                messageService.writeMessage(message);

                JSONData jsonData = mapper.readValue(message.getData(), JSONData.class);

                Device device = deviceService.getDeviceByLoraID(jsonData.getId());
                device.setLatitude(jsonData.getLat());
                device.setLongitude(jsonData.getLon());
                System.out.println("\n" + device);
                deviceService.saveDevice(device);

                sender.sendHTMLMessage("New Message [" + count + "]", "" +
                        "Message: " + message.getData() +
                        "<br>Source: MQTT" +
                        "<br>Time: " + message.getTmst() +
                        "<br>Device: " +
                        "<a href=\"http://ec2-34-210-69-19.us-west-2.compute.amazonaws.com:7001/" +
                        "device/" + device.getUuid() + "\">link</a>"
                );
                count++;
            }
            case "lora/messages": {
                JSONLoraMessage message = new JSONLoraMessage();
                message.setSource("MQTT");
                message.setUserID("login");
                message.setData(data);
                message.setSize("1");
                message.setTmst(currentTime);
                message.setUuid(UUID.randomUUID());
                message.setTopic(topic);
                messageService.writeMessage(message);

                String lastmessage = data.split(",")[1];
                String clientID = data.split(",")[0];
                System.out.println("cliend ID: " + clientID);

                Device device = deviceService.getDeviceByLoraID(clientID);
                device.setLastUsedGatewayDate(new Timestamp(System.currentTimeMillis()));
                device.setLastUsedGateway("06-00-00-00-00-00");
                deviceService.saveDevice(device);

                System.out.println("device.getLastUsedGateway(): " + device.getLastUsedGateway());
                Gateway gateway = gatewayService.getGatewayByMac(device.getLastUsedGateway());
                System.out.println("gateway MAC: " + gateway.getMac());

                System.out.println("messages [" + lastmessage.trim() + "] will be sended to device/" + gateway.getMac());
                client.publish("device/"+ gateway.getMac() , new MqttMessage(lastmessage.getBytes()));
            }
        }
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("-----------------\n" + token.toString() + "---------------------\n");
        System.out.println("Delivery is Complete");
    }

    private String getCurrentTime(){
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, 3);
        return dateFormat.format(cal.getTime());
    }

    public void setDefaultClient(MqttClient client){ this.client = client; }
}