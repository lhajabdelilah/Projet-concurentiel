import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.imageio.ImageIO;

public class RemoteControlImpl extends UnicastRemoteObject implements RemoteControl {
    private Robot robot;

    protected RemoteControlImpl() throws RemoteException {
        super();
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] captureScreen() throws RemoteException {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenCapture = robot.createScreenCapture(screenRect);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(screenCapture, "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void moveMouse(int x, int y) throws RemoteException {
        robot.mouseMove(x, y);
    }

    @Override
    public void clickMouse(int button) throws RemoteException {
        robot.mousePress(button);
        robot.mouseRelease(button);
    }

    @Override
    public void sendKeyPress(int keyCode) throws RemoteException {
        robot.keyPress(keyCode);
        robot.keyRelease(keyCode);
    }
}