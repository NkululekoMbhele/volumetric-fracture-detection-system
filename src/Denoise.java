import java.util.*;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class Denoise{

  private BufferedImage image;
  private BufferedImage image_cp;

  //Constructors
  public Denoise(){
  
  }

  public Denoise(BufferedImage image){
    this.image = image;
  }

  //performs denoise using average neighbour denoising filter
  private void performDenoiseAverage(){
    
    int width = image.getWidth();
    int height = image.getHeight();

    //new BufferedImage to store denoised data
    this.image_cp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    //current position of image target pixel to analyse
    int x = 0;
    int y = 0;

    //loop through all image pixels, find average of neighbours and set pixel
    for(y = 1; y < height -1; y++){
      for(x = 1; x < width -1; x++){

        //get neighbouring pixels (variables of form pxy)
        Color p00 = new Color(image.getRGB(x-1, y-1));
        Color p01 = new Color(image.getRGB(x-1, y));
        Color p02 = new Color(image.getRGB(x-1, y+1));

        Color p10 = new Color(image.getRGB(x, y-1));
        Color p11 = new Color(image.getRGB(x, y));
        Color p12 = new Color(image.getRGB(x, y+1));

        Color p20 = new Color(image.getRGB(x+1, y-1));
        Color p21 = new Color(image.getRGB(x+1, y));
        Color p22 = new Color(image.getRGB(x+1, y+1));
        //[p00 p10 p20]     Array of points with target p11 in centre
        //[p01 p11 p21]
        //[p02 p12 p22]

        //apply average denoising kernel for red range
        int r = p00.getRed() / 9 +
                p01.getRed() / 9 +
                p02.getRed() / 9 +
                p10.getRed() / 9 +
                p11.getRed() / 9 +
                p12.getRed() / 9 + 
                p20.getRed() / 9 +
                p21.getRed() / 9 +
                p22.getRed() / 9 ;

        //apply average denoising kernel for green range
        int g = p00.getGreen() / 9 +
                p01.getGreen() / 9 +
                p02.getGreen() / 9 +
                p10.getGreen() / 9 +
                p11.getGreen() / 9 +
                p12.getGreen() / 9 + 
                p20.getGreen() / 9 +
                p21.getGreen() / 9 +
                p22.getGreen() / 9 ;

        //apply average denoising kernel for blue range
        int b = p00.getBlue() / 9 +
                p01.getBlue() / 9 +
                p02.getBlue() / 9 +
                p10.getBlue() / 9 +
                p11.getBlue() / 9 +
                p12.getBlue() / 9 + 
                p20.getBlue() / 9 +
                p21.getBlue() / 9 +
                p22.getBlue() / 9 ;

        //set new RGB pixel values in denoised image
        r = Math.min(255, Math.max(0, r));
        g = Math.min(255, Math.max(0, g));
        b = Math.min(255, Math.max(0, b));
        
        Color c = new Color(r, g, b);

        image_cp.setRGB(x, y, c.getRGB());
      }
    }

    // image_cp contains a complete BufferedImage of denoised image data
  }

  //Perform denoise using laplace filter
  private void performDenoiseLaplace(){
    
    int width = image.getWidth();
    int height = image.getHeight();

    //new BufferedImage to store laplace map
    this.image_cp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    //current position of image target pixel to analyse
    int x = 0;
    int y = 0;

    //loop through all image pixels, run laplace filter on kernel and set pixel
    for(y = 1; y < height -1; y++){
      for(x = 1; x < width -1; x++){
        
        //get neighbouring pixels (variables of form pxy)
        Color p00 = new Color(image.getRGB(x-1, y-1));
        Color p01 = new Color(image.getRGB(x-1, y));
        Color p02 = new Color(image.getRGB(x-1, y+1));

        Color p10 = new Color(image.getRGB(x, y-1));
        Color p11 = new Color(image.getRGB(x, y));
        Color p12 = new Color(image.getRGB(x, y+1));

        Color p20 = new Color(image.getRGB(x+1, y-1));
        Color p21 = new Color(image.getRGB(x+1, y));
        Color p22 = new Color(image.getRGB(x+1, y+1));
        //[p00 p10 p20]     Array of points with target p11 in centre
        //[p01 p11 p21]
        //[p02 p12 p22]
        // Laplace denoising kernel 
        //[-1 -1 -1]
        //[-1 +8 -1]
        //[-1 -1 -1]
        //apply laplace denoising kernel for red range
        int r = -p00.getRed() 
                -p01.getRed()
                -p02.getRed()
                -p10.getRed() 
                + 8*p11.getRed() 
                -p12.getRed()
                -p20.getRed() 
                -p21.getRed() 
                -p22.getRed(); 

        //apply laplace denoising kernel for green range
        int g = -p00.getGreen() 
                -p01.getGreen() 
                -p02.getGreen()
                -p10.getGreen() 
                + 8*p11.getGreen() 
                -p12.getGreen()
                -p20.getGreen()
                -p21.getGreen()
                -p22.getGreen();

        //apply laplace denoising kernel for blue range
        int b = -p00.getBlue() 
                -p01.getBlue() 
                -p02.getBlue()
                -p10.getBlue() 
                + 8*p11.getBlue() 
                -p12.getBlue()
                -p20.getBlue() 
                -p21.getBlue()
                -p22.getBlue();

        //set new RGB pixel values in denoised image
        r = Math.min(255, Math.max(0, r));
        g = Math.min(255, Math.max(0, g));
        b = Math.min(255, Math.max(0, b));
        
        Color c = new Color(r, g, b);
        
        image_cp.setRGB(x, y, c.getRGB());
      }
    }

    // image_cp contains the laplace map of the image where noise is made white

    // runs the compare function that uses the laplace map to identify noise pixels and averages the pixels
    // on the original image, then updates the image_cp value to the new denoised image
    compareLaplaceMapAndOriginalImage();
  }

  //Performs denoise using median filter
  private void performDenoiseMedian(){
    
    int width = image.getWidth();
    int height = image.getHeight();

    //new BufferedImage to store denoised data
    this.image_cp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 

    //current position of image target pixel to analyse
    int x = 0;
    int y = 0;

    //loop through all image pixels, find average of neighbours and set pixel
    for(y = 1; y < height -1; y++){
      for(x = 1; x < width -1; x++){

        //Int array to store and sort kernel of pixel values
        int[] k = new int[9];
        
        k[0] = image.getRGB(x-1, y-1);
        k[1] = image.getRGB(x-1, y);
        k[2] = image.getRGB(x-1, y+1);

        k[3] = image.getRGB(x, y-1);
        k[4] = image.getRGB(x, y);
        k[5] = image.getRGB(x, y+1);

        k[6] = image.getRGB(x+1, y-1);
        k[7] = image.getRGB(x+1, y);
        k[8] = image.getRGB(x+1, y+1);

        Arrays.sort(k);

        //Color object created with median int RGB value
        Color c = new Color(k[4]);

        //Set pixel colour in denoised image
        image_cp.setRGB(x, y, c.getRGB());
      }
    }

    // image_cp contains a complete BufferedImage of denoised image data
  }

  //Applies filter on input dataset and returns filtered data. (Average denoising filter => default)
  public BufferedImage applyDenoiseFilter(DenoiseFilterType filter_type, BufferedImage image){

    //set image
    setImage(image);

    //Check which filter to use  
    switch(filter_type) {
        case AVERAGE:
            performDenoiseAverage();
            break;
        case LAPLACE:
            performDenoiseLaplace();
            break;
        case MEDIAN:
            performDenoiseMedian();
            break;
        default:
            performDenoiseAverage();
    }
    //return denoised image 2D integer array 
    return this.image_cp;
  }

  //Set image
  private void setImage(BufferedImage image_to_set){
    this.image = image_to_set;
  }

  //Get original image
  public BufferedImage getImage(){
    return this.image;
  }

  //Get Last denoised image
  public BufferedImage getLastDenoisedImage(){
    return this.image_cp;
  }

  private void compareLaplaceMapAndOriginalImage(){

    //red, green and blue integer values of the target pixel (x,y)
    int rl = 0;
    int gl = 0;
    int bl = 0;

    //loop through all pixels in image, finding the the color of each on laplace map
    //and if it corresponds to noise (is white), averaging the pixel on the original image
    for(int y = 0; y < image.getHeight(); y++){
      for(int x = 0; x < image.getWidth(); x++){

        //set laplace map RGB values for pixel at (x, y)
        rl = new Color(image_cp.getRGB(x, y)).getRed();
        gl = new Color(image_cp.getRGB(x, y)).getGreen();
        bl = new Color(image_cp.getRGB(x, y)).getBlue();

        //check if laplace map pixel is white and if so, average pixel
        if(rl == 255 && gl == 255 & bl == 255){
          //get neighbouring pixels (variables of form pxy)
          Color p00 = new Color(image.getRGB(x-1, y-1));
          Color p01 = new Color(image.getRGB(x-1, y));
          Color p02 = new Color(image.getRGB(x-1, y+1));

          Color p10 = new Color(image.getRGB(x, y-1));
          Color p11 = new Color(image.getRGB(x, y));
          Color p12 = new Color(image.getRGB(x, y+1));

          Color p20 = new Color(image.getRGB(x+1, y-1));
          Color p21 = new Color(image.getRGB(x+1, y));
          Color p22 = new Color(image.getRGB(x+1, y+1));
          //[p00 p10 p20]     Array of points with target p11 in centre
          //[p01 p11 p21]
          //[p02 p12 p22]

          //apply average denoising kernel for red range
          int r = p00.getRed() / 9 +
                  p01.getRed() / 9 +
                  p02.getRed() / 9 +
                  p10.getRed() / 9 +
                  p11.getRed() / 9 +
                  p12.getRed() / 9 + 
                  p20.getRed() / 9 +
                  p21.getRed() / 9 +
                  p22.getRed() / 9 ;

          //apply average denoising kernel for green range
          int g = p00.getGreen() / 9 +
                  p01.getGreen() / 9 +
                  p02.getGreen() / 9 +
                  p10.getGreen() / 9 +
                  p11.getGreen() / 9 +
                  p12.getGreen() / 9 + 
                  p20.getGreen() / 9 +
                  p21.getGreen() / 9 +
                  p22.getGreen() / 9 ;

          //apply average denoising kernel for blue range
          int b = p00.getBlue() / 9 +
                  p01.getBlue() / 9 +
                  p02.getBlue() / 9 +
                  p10.getBlue() / 9 +
                  p11.getBlue() / 9 +
                  p12.getBlue() / 9 + 
                  p20.getBlue() / 9 +
                  p21.getBlue() / 9 +
                  p22.getBlue() / 9 ;

          //set new RGB pixel values in denoised image
          r = Math.min(255, Math.max(0, r));
          g = Math.min(255, Math.max(0, g));
          b = Math.min(255, Math.max(0, b));
          
          Color c = new Color(r, g, b);

          //set pixel colour on original image to the average value
          image.setRGB(x, y, c.getRGB());
        }
      }
    }
    //update the image_cp variable to be the new denoised image
    image_cp = image;
  }
}
