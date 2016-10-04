/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.exporter.vloopback;

import java.awt.image.BufferedImage;
import webcamstudiofx.InfoListener;
import webcamstudiofx.sources.effects.FlipVertical;

/**
 *
 * @author lgs
 */
abstract public class VideoOutput{

    protected static String devicePath = "/dev/video2";
    final public static int RGB24 = 1;
    final public static int UYVY = 2;
    final public static int BGR24 = 3;
    protected static int pixFormat = 2;
    protected static boolean flipImage = false;

    public static void setFlipImage(boolean flip) {
        flipImage = flip;
    }

    public static int getPixFormat() {
        return pixFormat;
    }

    public static String getDevice() {
        return devicePath;
    }
    protected int devFD = 0;
    protected int w = 320;
    protected int h = 240;
    protected FlipVertical flipper = new FlipVertical();
    protected InfoListener listener = null;
    protected byte[] rgbs = null;


    protected byte[] img2rgb24(int[] data) {
        if (rgbs == null || rgbs.length != data.length * 3) {
            rgbs = new byte[data.length * 3];
        }
        int index = 0;
        for (int i = 0; i < data.length; i++) {
            rgbs[index++] = (byte) (data[i] >> 16 & 0xFF);
            rgbs[index++] = (byte) (data[i] >> 8 & 0xFF);
            rgbs[index++] = (byte) (data[i] & 0xFF);
        }
        return rgbs;
    }

    protected byte[] img2bgr24(int[] data) {
        if (rgbs == null || rgbs.length != data.length * 3) {
            rgbs = new byte[data.length * 3];
        }
        int index = 0;
        for (int i = 0; i < data.length; i++) {
            rgbs[index++] = (byte) (data[i] & 0xFF);
            rgbs[index++] = (byte) (data[i] >> 8 & 0xFF);
            rgbs[index++] = (byte) (data[i] >> 16 & 0xFF);
        }
        return rgbs;
    }

    protected byte[] img2yuv420(BufferedImage bi) {
        h = bi.getHeight();
        w = bi.getWidth();
        byte[] yuvs = new byte[(int) ((w * h) * 1.5f)];

        try {
            boolean s = false;

            for (int j = 0; j < h; j++) {
                for (int i = 0; i < w; i++) {
                    int color = bi.getRGB(i, j);

//                    int alpha = color >> 24 & 0xff;
                    int R = color >> 16 & 0xff;
                    int G = color >> 8 & 0xff;
                    int B = color & 0xff;

                    int Y = (int) (R * .299000 + G * .587000 + B * 0.114000);
                    int U = (int) (R * -.168736 + G * -.331264 + B * 0.500000 + 128);
                    int V = (int) (R * .500000 + G * -.418688 + B * -0.081312 + 128);


                    int arraySize = w * h;
                    int yLoc = j * w + i;
                    int uLoc = (j / 2) * (w / 2) + i / 2 + arraySize;
                    int vLoc = (j / 2) * (w / 2) + i / 2 + arraySize + arraySize / 4;

                    yuvs[yLoc] = (byte) Y;
                    yuvs[uLoc] = (byte) U;
                    yuvs[vLoc] = (byte) V;

                    s = !s;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return yuvs;
    }

    protected byte[] img2uyvy(int[] data) {
//        int nbOfPixels = data.length;
        byte[] yuvs = new byte[data.length * 2];
        int index = data.length - 1;
        int yuvIndex = yuvs.length - 1;
        byte y1 = 0;
        byte u = 0;
        byte v = 0;
        byte y0 = 0;
        try {
            while (index > 0) {
                int alpha = data[index] >> 24 & 0xff;
                int R = data[index] >> 16 & 0xff;
                int G = data[index] >> 8 & 0xff;
                int B = data[index] & 0xff;

                y1 = (byte) (R * .299000 + G * .587000 + B * 0.114000);
                u = (byte) (R * -.168736 + G * -.331264 + B * 0.500000 + 128);
                v = (byte) (R * .500000 + G * -.418688 + B * -0.081312 + 128);
                index--;
                alpha = data[index] >> 24 & 0xff;
                R = data[index] >> 16 & 0xff;
                G = data[index] >> 8 & 0xff;
                B = data[index] & 0xff;
                y0 = (byte) (R * .299000 + G * .587000 + B * 0.114000);

                yuvs[yuvIndex--] = y1;
                yuvs[yuvIndex--] = v;
                yuvs[yuvIndex--] = y0;
                yuvs[yuvIndex--] = u;

                index--;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return yuvs;
    }

    public abstract void open(String path, int w, int h, int pixFormat);

    public abstract void close();

    public abstract void write(int[] data);

}
