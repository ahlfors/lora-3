package yaruliy.mqtt;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yaruliy.datastore.DeviceService;
import yaruliy.datastore.MessageService;
import yaruliy.model.Device;
import yaruliy.model.json.JSONData;
import yaruliy.model.json.JSONLoraMessage;
import yaruliy.model.json.JSONRxpk;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

@Component
class BasicCallback implements MqttCallback {
    private MessageService messageService;
    @Autowired public void setMessageService(MessageService messageService) { this.messageService = messageService; }
    private DeviceService deviceService;
    @Autowired public void setDeviceService(DeviceService deviceService) { this.deviceService = deviceService; }
    private ObjectMapper mapper;
    @Autowired public void setMapper(ObjectMapper mapper) {this.mapper = mapper; }

    public void connectionLost(Throwable arg0) {
        System.out.println("-------------------------------------------------");
        System.out.println("Connection Lost");
    }

    public void messageArrived(String topic, MqttMessage arrivedMessage) throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Kyiv"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        System.out.println("-------------------------------------------------");
        System.out.println("| Topic:" + topic);
        System.out.println("| Message: " + new String(arrivedMessage.getPayload()));
        System.out.println("-------------------------------------------------");

        String data = new String(arrivedMessage.getPayload());
        if(data.contains("{")){
            System.out.println("\norigin data: " + data);

            data = data.substring(data.indexOf('{'), data.lastIndexOf('}') + 1);
            System.out.println("parser data:" + data + "\n");

            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            JSONLoraMessage message = mapper.readValue(data, JSONRxpk.class).getLoraMessages()[0];
            System.out.println("loramessage: \n" + message.toString());

            message.setUuid(UUID.randomUUID());
            message.setTmst(dateFormat.format(new Date()));
            message.setSource("MQTT");
            messageService.writeMessage(message);

            JSONData jsonData = mapper.readValue(message.getData(), JSONData.class);

            Device device = deviceService.getDeviceByLoraID(jsonData.getId());
            device.setLatitude(jsonData.getLat());
            device.setLongitude(jsonData.getLon());
            System.out.println("\n" + device);
            deviceService.saveDevice(device);
        }
        else {
            JSONLoraMessage message = new JSONLoraMessage();
            message.setSource("MQTT");
            message.setUserID("login");
            message.setData(data);
            message.setSize("1");
            message.setTmst(dateFormat.format(new Date()));
            message.setUuid(UUID.randomUUID());
            messageService.writeMessage(message);
        }
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("-------------------------------------------------");
        System.out.println("Delivery is Complete");
    }
}