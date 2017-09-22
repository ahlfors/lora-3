package yaruliy.sockets;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import yaruliy.datastore.DeviceService;
import yaruliy.datastore.MessageService;
import yaruliy.model.Device;
import yaruliy.model.json.JSONData;
import yaruliy.model.json.JSONLoraMessage;
import yaruliy.model.json.JSONRxpk;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

@Component
public class LoraUDPSocketServer implements SocketServer{
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];
    private MessageService messageService;
    @Autowired public void setMessageService(MessageService messageService) { this.messageService = messageService; }
    private DeviceService deviceService;
    @Autowired public void setDeviceService(DeviceService deviceService) { this.deviceService = deviceService; }
    private ObjectMapper mapper;
    @Autowired public void setMapper(ObjectMapper mapper) {this.mapper = mapper; }
    @Value("${socket.address}")
    private String address;

    @Override
    public void start() {
        Runnable runnable = () -> {
            System.out.println("UPD Address: " + address);
            running = true;
            while (running) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                try {
                    socket.receive(packet);
                    packet = new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort());

                    String data = new String(packet.getData(), 0, packet.getLength());
                    System.out.println("\norigin data: " + data);

                    data = data.substring(data.indexOf('{'), data.lastIndexOf('}') + 1);
                    System.out.println("parser data:" + data + "\n");

                    socket.send(packet);

                    mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                    JSONLoraMessage message = mapper.readValue(data, JSONRxpk.class).getLoraMessages()[0];
                    System.out.println("loramessage: \n" + message.toString());

                    message.setUuid(UUID.randomUUID());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                    TimeZone.setDefault(TimeZone.getTimeZone("Europe/Kyiv"));
                    message.setTmst(dateFormat.format(new Date()));
                    message.setSource("SOCKETS");
                    messageService.writeMessage(message);

                    JSONData jsonData = mapper.readValue(message.getData(), JSONData.class);
                    Device device;

                    if(deviceService.containsDevice(jsonData.getId()))
                        device = deviceService.getDeviceByLoraID(jsonData.getId());
                    else {
                        device = new Device();
                        device.setLoraid(jsonData.getId());
                        device.setUuid(UUID.randomUUID());
                    }

                    device.setLatitude(jsonData.getLat());
                    device.setLongitude(jsonData.getLon());
                    System.out.println("\n" + device);
                    deviceService.saveDevice(device);
                }
                catch (Exception e){ e.printStackTrace(); }
            }
            socket.close();
        };
        runnable.run();
    }

    @Override
    public void init() {
        try { socket = new DatagramSocket(new InetSocketAddress(address, 40000)); }
        catch (SocketException e) { e.printStackTrace(); }
    }
}
