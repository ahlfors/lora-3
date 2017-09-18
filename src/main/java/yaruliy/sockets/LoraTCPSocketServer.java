package yaruliy.sockets;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import yaruliy.datastore.MessageService;
import yaruliy.model.json.JSONLoraMessage;
import yaruliy.model.json.JSONRxpk;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.UUID;

@Component
public class LoraTCPSocketServer implements SocketServer {
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private Environment env;
    @Autowired public void setEnv(Environment env){ this.env = env;}
    private MessageService messageService;
    @Autowired public void setDataStore(MessageService messageService) { this.messageService = messageService; }
    private ObjectMapper mapper;
    @Autowired public void setMapper(ObjectMapper mapper) {this.mapper = mapper; }

    public LoraTCPSocketServer(){}

    public void start(){
        try{
            while (selector.select() > -1) {
                System.out.println("---------------------------------");
                System.out.println("waiting for new connection...");

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isAcceptable()) {
                        SocketChannel client = serverChannel.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                        System.out.println("Connection Accepted: " + client.socket().getRemoteSocketAddress() + "\n");
                    }
                    else if (selectionKey.isReadable()) {
                        SocketChannel client = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(256);
                        client.read(buffer);
                        String result = new String(buffer.array()).trim();

                        System.out.println("Message received: " + result);

                        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                        JSONRxpk rxpk = mapper.readValue(result, JSONRxpk.class);
                        JSONLoraMessage message = mapper.readValue(rxpk.getLoraMessages()[0].toString(), JSONLoraMessage.class);
                        message.setUuid(UUID.randomUUID());

                        System.out.println("Connection close...");
                        client.close();
                        messageService.writeMessage(message);
                    }
                    iterator.remove();
                }
            }
        }
        catch (IOException e){ e.printStackTrace(); }
    }

    @Override
    public void init() {
        try{
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(env.getProperty("socket.address"),
                    Integer.parseInt(env.getProperty("socket.port"))));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, serverChannel.validOps());
        }
        catch (IOException e) { e.printStackTrace(); }
    }
}