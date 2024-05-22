import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;

public class Client1 {
    private RemoteControl remoteControl;
    private JLabel screenLabel;
    private int screenWidth;
    private int screenHeight;

    public Client1() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            remoteControl = (RemoteControl) registry.lookup("RemoteControlService");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Controller");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.width;
        screenHeight = screenSize.height;

        screenLabel = new JLabel();
        screenLabel.setPreferredSize(new Dimension(screenWidth, screenHeight));
        screenLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    remoteControl.clickMouse(e.getButton());
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });

        screenLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                try {
                    remoteControl.moveMouse(e.getX(), e.getY());
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });

        frame.add(new JScrollPane(screenLabel), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateScreen();
            }
        }, 0, 100);
    }

    private void updateScreen() {
        try {
            byte[] screenBytes = remoteControl.captureScreen();
            if (screenBytes != null) {
                ByteArrayInputStream bais = new ByteArrayInputStream(screenBytes);
                BufferedImage screen = ImageIO.read(bais);
                Image scaledImage = screen.getScaledInstance(screenWidth, screenHeight, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(scaledImage);
                screenLabel.setIcon(icon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Client1::new);
    }
}