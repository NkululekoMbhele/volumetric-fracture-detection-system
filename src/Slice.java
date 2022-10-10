import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Slice{

  private BufferedImage img;

  // Basic contructor
  public Slice(BufferedImage img){
    this.img = img;
  }

  //Statically clones/creates a copy of a BufferedImage
  public static BufferedImage clone(BufferedImage image) {
    BufferedImage clone = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
    Graphics2D g2d = clone.createGraphics();
    g2d.drawImage(image, 0, 0, null);
    g2d.dispose();
  return clone;
}

  // Non-statically clones/creates a copy of a BufferedImage
  public BufferedImage copy(BufferedImage image) {
    BufferedImage clone = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
    Graphics2D g2d = clone.createGraphics();
    g2d.drawImage(image, 0, 0, null);
    g2d.dispose();
  return clone;
}

  // Resizes images to a desired size (for display)
  public static BufferedImage resize(BufferedImage img) { 
    int rescaleFactor = 512/img.getWidth();
    BufferedImage bigger = new BufferedImage(img.getWidth()*rescaleFactor, img.getHeight()*rescaleFactor, img.getType());
    Graphics2D g = bigger.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
    g.drawImage(img, 0, 0, bigger.getWidth(), bigger.getHeight(), 0, 0, img.getWidth(), img.getHeight(), null);

    return bigger;
}

  // Paints pixels at x and y in slice image
  public void paintPixel(int x, int y, int rgb){
   img.setRGB(x, y, rgb); 
  }

  // Overlays coloured fractures onto slice (for display)
  public static BufferedImage overlayFractures(BufferedImage img){
    // To Do
    // Change each fracture pixel to it's designated colour
    return img;
  }

  // Filter img
  public BufferedImage filterImg(){
    Denoise denoise = new Denoise();
    BufferedImage filteredImg = denoise.applyDenoiseFilter(DenoiseFilterType.LAPLACE , img);
    return filteredImg;
  }

  // Returns the greyscale intensity map of the bufferedImage in the slice
  public int[][] toIntArray(){
      
    int[][] array = new int[img.getHeight()][img.getWidth()];

    int red = 0;
    int green = 0;
    int blue = 0;

    for (int y = 0; y < img.getWidth(); y++){
        for (int x = 0; x < img.getHeight(); x++){

          int color = img.getRGB(x, y);

          red = getRed(color);
          green = getGreen(color);
          blue = getBlue(color);

          array[x][y] = (red + green + blue)/3;
        }
    }
    return array;
  }

  // Getting RGB components
  public static int getRed(int color) {
    return (color & 0x00ff0000) >> 16;
  }
  public static int getGreen(int color) {
    return (color & 0x0000ff00) >> 8;
  }
  public static int getBlue(int color) {
    return (color & 0x000000ff) >> 0;
  }

  // Converts greyscale intensity map of a bufferedImage to a bufferedImage
  public BufferedImage toBufferedImage(int[][] array){

    BufferedImage img = new BufferedImage(array.length, array.length, BufferedImage.TYPE_INT_RGB);

    // Set each pixel in img
    for (int x = 0; x < array.length; x++){
      for (int y = 0; y < array.length; y++){
        img.setRGB(x, y, array[x][y]);
      }
    }
    return img;
  }

  // Accessor methods
  public BufferedImage getBImage(){
    return img;
  }

  public void setBImage(BufferedImage img){
    this.img = img;
  }

}
