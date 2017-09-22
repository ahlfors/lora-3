package yaruliy.controllers.rest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import yaruliy.datastore.DeviceService;
import yaruliy.datastore.MessageService;
import yaruliy.model.Device;
import yaruliy.model.json.JSONLoraMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/lora")
public class DataController {
    private MessageService messageService;
    @Autowired public void setMessageService(MessageService messageService) { this.messageService = messageService; }
    private DeviceService deviceService;
    @Autowired public void setDeviceService(DeviceService deviceService) { this.deviceService = deviceService; }

    @RequestMapping(value = "/messages", method = RequestMethod.GET, produces = "application/json")
    public List<JSONLoraMessage> messageList(HttpServletRequest request) throws JsonProcessingException {
        return messageService.getMessagesByUserLogin(request.getRemoteUser());
    }

    @RequestMapping(value = "/devices", method = RequestMethod.GET, produces = "application/json")
    public List<Device> deviceList(HttpServletRequest request) { return deviceService.getDevices(); }

    @RequestMapping(value = "/device/{id}", method = RequestMethod.GET, produces = "application/json")
    public Device deviceInfo(@PathVariable(value = "id") String id){ return deviceService.getDeviceByID(id); }
}