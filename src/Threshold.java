import java.awt.image.BufferedImage;

public class Threshold {

  // return a thresholded Buffered image according to the given intensity value
  public static BufferedImage ThresholdImage(
    BufferedImage img,
    int requiredThresholdValue
  ) {
    // get the height and with of the picture
    int height = img.getHeight();
    int width = img.getWidth();
    // new BufferedImage to store the threshholded image
    BufferedImage finalThresholdImage = new BufferedImage(
      width,
      height,
      BufferedImage.TYPE_INT_RGB
    );

    // initiate RGB modes to zero
    int red = 0;
    int green = 0;
    int blue = 0;

    // iterate through all the image pixels and set the pixel to white if it is close to white (255)
    // and black (0) if close to black

    for (int x = 0; x < width; x++) {
      try {
        for (int y = 0; y < height; y++) {
          // get pixel color in position x, y
          int color = img.getRGB(x, y);

          red = getRed(color);
          green = getGreen(color);
          blue = getBlue(color);

          if ((red + green + blue) / 3 < (int) (requiredThresholdValue)) {
            finalThresholdImage.setRGB(x, y, mixColor(0, 0, 0));
          } else {
            finalThresholdImage.setRGB(x, y, mixColor(255, 255, 255));
          }
        }
      } catch (Exception e) {
        e.getMessage();
      }
    }

    return finalThresholdImage;
  }

  private static int mixColor(int red, int green, int blue) {
    return red << 16 | green << 8 | blue;
  }

  // get Red pixel color
  public static int getRed(int color) {
    return (color & 0x00ff0000) >> 16;
  }

  // get Green pixel color
  public static int getGreen(int color) {
    return (color & 0x0000ff00) >> 8;
  }

  // get Blue pixel color
  public static int getBlue(int color) {
    return (color & 0x000000ff) >> 0;
  }
}
