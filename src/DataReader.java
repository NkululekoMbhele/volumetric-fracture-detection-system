import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class DataReader{

    private static int numSlices;

    public static void getHeaderInfo(String fileName) throws IOException{
        
        FileInputStream stream = new FileInputStream(new File("../data/" + fileName + "/" + fileName + ".hdr"));
        Scanner scan = new Scanner(stream);
        
        scan.nextLine();            // Discard comment line

        numSlices = scan.nextInt(); // Get number of images in file

        scan.close();
    }   
    
    public static BufferedImage readImage(String fileName, int imgNum) throws IOException {
        
        BufferedImage img = null;   //Image to return

        img = ImageIO.read(new File("../data/" + fileName + "/" + fileName + imgNum + ".png")); //Attemp to load files
        img = convertToRGB(img);
        return img;
    }
    
    public static Slice[] readData(String fileName){

        Slice[] slices = null;    //Create the return array

        //try read in all images
        try{
            // Set the number of image to be read in
            getHeaderInfo(fileName);

            slices = new Slice[numSlices];
            for (int i = 0; i < numSlices; i++){
                slices[i] = new Slice(readImage(fileName, i));
            }
            if (slices[0] == null){
                slices = null;
            }
        }
        catch(IOException e){
            //Error handeled in Application class
        }
        return slices;
    }

    public static int getNumSlices(){
        return numSlices;
    }

    private static BufferedImage convertToRGB(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();

        //new image
        BufferedImage image_cp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //loop through all image pixels, find average of neighbours and set pixel
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){

                Color c = new Color(image.getRGB(x, y));

                //get rgb components
                int r = c.getRed();

                int g = c.getGreen();
                
                int b = c.getBlue();
                
                //set new color
                c = new Color(r, g, b);
                
                image_cp.setRGB(x, y, c.getRGB());
            }
        }
        return image_cp;
    }
}
