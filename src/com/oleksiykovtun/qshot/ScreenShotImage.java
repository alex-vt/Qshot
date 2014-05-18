package com.oleksiykovtun.qshot;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Screen shot image data and common operations with it
 */
class ScreenShotImage {
    private WritableImage image;

    public ScreenShotImage() throws Exception {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage capture = new Robot().createScreenCapture(screenRect);
        image = SwingFXUtils.toFXImage(capture, null);
    }

    public ScreenShotImage(WritableImage image) throws Exception {
        this.image = image;
    }

    public ScreenShotImage crop(int x1, int y1, int x2, int y2) throws Exception {
        return new ScreenShotImage(new WritableImage(image.getPixelReader(),
                Math.min(x1, x2), Math.min(y1, y2),
                Math.abs(x1 - x2), Math.abs(y1 - y2)));
    }

    public WritableImage getWritableImage() {
        return image;
    }

    public void writeToFilePath(String filePath) throws Exception {
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File(filePath));
    }

    public void copyToClipboard() {
        final ClipboardContent content = new ClipboardContent();
        content.putImage(image);
        Clipboard.getSystemClipboard().setContent(content);
    }
}
