package com.oleksiykovtun.qshot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Application residence in the system tray
 */
public class TrayApp {
    private static final String TRAY_ICON_IMAGE_PATH = "resources/trayIcon32.png";

    public static void main(String[] args) throws Exception {
        ScreenShotTakingWindow.startHidden();
        setupTray();
        setupHotKey();
    }

    private static void setupTray() throws Exception {
        final PopupMenu trayMenu = new PopupMenu();

        MenuItem screenShotMenuItem = new MenuItem("Take Screenshot");
        screenShotMenuItem.addActionListener(ActionEvent -> ScreenShotTakingWindow.show());
        trayMenu.add(screenShotMenuItem);

        MenuItem openScreenShotsFolderMenuItem = new MenuItem("Open Screenshots Folder");
        openScreenShotsFolderMenuItem.addActionListener(ActionEvent -> ScreenShotsFolderManager.openFolder());
        trayMenu.add(openScreenShotsFolderMenuItem);

        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.addActionListener(ActionEvent -> System.exit(0));
        trayMenu.add(exitMenuItem);

        final TrayIcon trayIcon = getTrayIcon(TRAY_ICON_IMAGE_PATH);
        trayIcon.setPopupMenu(trayMenu);
        trayIcon.addMouseListener(
                new MouseListener() {
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            ScreenShotTakingWindow.show();
                        }
                    }
                    public void mouseEntered(MouseEvent e) { }
                    public void mouseExited(MouseEvent e) { }
                    public void mousePressed(MouseEvent e) { }
                    public void mouseReleased(MouseEvent e) { }
                }
        );

        final SystemTray tray = SystemTray.getSystemTray();
        tray.add(trayIcon);
    }

    private static TrayIcon getTrayIcon(String imageFilePath) throws Exception {
        int iconSize = SystemTray.getSystemTray().getTrayIconSize().height;
        BufferedImage iconImage = new BufferedImage(iconSize, iconSize, BufferedImage.TYPE_INT_ARGB);

        BufferedImage iconSourceImage = ImageIO.read(new File(imageFilePath));
        int iconSourceSize = iconSourceImage.getHeight();

        double iconResizeScaleFactor = (double)iconSize / iconSourceSize;
        AffineTransform transform = AffineTransform.getScaleInstance(iconResizeScaleFactor, iconResizeScaleFactor);

        iconImage.createGraphics().drawRenderedImage(iconSourceImage, transform);
        return new TrayIcon(iconImage);
    }

    private static void setupHotKey() throws Exception {
        // TODO
    }
}
