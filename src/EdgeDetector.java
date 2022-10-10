import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EdgeDetector {

    //Constructor
    public EdgeDetector(){}


    //Detects edges in a thresholded (white / black) image, storing the white/object edge points in an ArrayList
    //of 3D points (PVectors)
    public ArrayList<PVector> detectEdges(BufferedImage image, int image_index){
         
         ArrayList<PVector> edges = new ArrayList<PVector>();

         int width = image.getWidth();
         int height = image.getHeight();
         
         //loop through all points in the image and detect edge points (local changes in intensity)
         for(int y = 1; y < height-1; y++){
             for(int x = 1; x < width-1; x++){
                 
                int p00 = (image.getRGB(x-1, y-1) & 0xFF);
                int p01 = (image.getRGB(x-1, y) & 0xFF);
                int p02 = (image.getRGB(x-1, y+1) & 0xFF);

                int p10 = (image.getRGB(x, y-1) & 0xFF);
                int p11 = (image.getRGB(x, y) & 0xFF);
                int p12 = (image.getRGB(x, y+1) & 0xFF);

                int p20 = (image.getRGB(x+1, y-1) & 0xFF);
                int p21 = (image.getRGB(x+1, y) & 0xFF);
                int p22 = (image.getRGB(x+1, y+1) & 0xFF);
 
                 // |p00 p10 p20|
                 // |p01 p11 p21|
                 // |p02 p12 p22|
                
                 float sum = (float)(p00+p01+p02+p10+p11+p12+p20+p21+p22);
                
                 //check if edges exist in the current kernel by checking if the 
                 //average of the kernel is between 0<sum<1, if so it's an edge. 
                 //i.e are all pixels white(255) or black(0) if not, its an edge
                 if( p11 == 255 ){
                    if( ( ( 0f < sum/(255*9)) ) && ( (sum/(255*9)) < 1f ) ){
                        edges.add(new PVector(x, y, image_index, p11));
                    }
                 }
            }
        }
         return edges;
    }

}
