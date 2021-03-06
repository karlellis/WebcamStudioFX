/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.util;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author patrick
 */
public class Tools {

    final static String userHome = System.getProperty("user.home");

    public static OS getOS() {
        OS os = OS.LINUX;
        String value = System.getProperty("os.name").toLowerCase().trim();
        if (value.contains("linux")) {
            os = OS.LINUX;
        } else if (value.contains("windows")) {
            os = OS.WINDOWS;
        } else if (value.contains("os x")) {
            os = OS.OSX;
        }
        return os;
    }

    public static String getOSName() {
        String name = "linux";
        OS os = getOS();
        switch (os) {
            case LINUX:
                name = "linux";
                break;
            case WINDOWS:
                name = "windows";
                break;
            case OSX:
                name = "osx";
                break;
        }

        return name;
    }

    public static String getUserHome() {
        return userHome;
    }

    public static void sleep(long millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void waitUntil(long nanoEndTime) {
        long delta = nanoEndTime - System.nanoTime();
        if (delta > 0) {
            try {
                if (delta > 999999) {
                    long milli = delta / 1000000;
                    long nano = delta % 1000000;
                    Thread.sleep(milli, (int) nano);
                } else {
                    Thread.sleep(0, (int) delta);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static BufferedImage toCompatibleImage(BufferedImage image) {
        GraphicsConfiguration gfx_config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        if (image.getColorModel().equals(gfx_config.getColorModel())) {
            return image;
        }
        BufferedImage new_image = gfx_config.createCompatibleImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) new_image.getGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return new_image;
    }

    private Tools() {
    }

    public enum OS {

        WINDOWS, LINUX, OSX
    }
}
