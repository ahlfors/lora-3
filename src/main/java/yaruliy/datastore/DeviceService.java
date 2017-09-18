package yaruliy.datastore;
import yaruliy.model.Device;
import java.util.List;

public interface DeviceService {
    List<Device> getDevices();
    Device getDeviceByID(String id);
    Device getDeviceByLoraID(String id);
    String saveDevice(Device device);
    boolean containsDevice(String loraid);
    long deviceCount();
    void openSession();
    void closeSession();
}