import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            RemoteControlImpl remoteControl = new RemoteControlImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("RemoteControlService", remoteControl);
            System.out.println("Server is ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
