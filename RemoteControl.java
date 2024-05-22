import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteControl extends Remote {
    byte[] captureScreen() throws RemoteException;
    void moveMouse(int x, int y) throws RemoteException;
    void clickMouse(int button) throws RemoteException;
    void sendKeyPress(int keyCode) throws RemoteException;
}