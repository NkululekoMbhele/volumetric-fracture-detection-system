import java.awt.image.BufferedImage;
import java.awt.Color;

public class MorphologicalProcessor{

    private Slice[] images;
    private Slice[] closed_Images;
    private Slice[] opened_Images;

    //Constructors
    public MorphologicalProcessor(Slice[] dataset){
        this.images = dataset;
        this.closed_Images = null;
        this.opened_Images = null;
    }
    public MorphologicalProcessor(){}
    
    //Mutator
    public void setDataset(Slice[] dataset){
        this.images = dataset;
    }

    //Accessors
    public Slice[] getDataset(){ return this.images; }
    public Slice[] getClosedDataset(){ return this.closed_Images; }
    public Slice[] getOpenedDataset(){ return this.opened_Images; }


    //DATASET LEVEL OPERATIONS:
    
    //performs dilation() then erosion() on dataset
    public void closeDataset(){
        this.closed_Images = new Slice[this.images.length];
        for(int i = 0; i < images.length; i++){
            closed_Images[i] = new Slice(erode(new Slice(dilate(images[i]))));
        }
    }
    //performs erosion() then dilation() on dataset
    public void openDataset(){
        this.opened_Images = new Slice[this.images.length];
        for(int i = 0; i < images.length; i++){
            opened_Images[i] = new Slice(dilate(new Slice(erode(images[i]))));
        }
    }

    //SLICE LEVEL OPERATIONS:

    //performs dilation() then erosion() on input Slice
    public Slice closeImage(Slice slice){
        return new Slice(erode(new Slice(dilate(slice))));
    }

    //performs erosion() then dilation() on input Slice
    public Slice openImage(Slice slice){
        return new Slice(dilate(new Slice(erode(slice))));
    }

    //erosdes object
    private BufferedImage erode(Slice slice){
        
        BufferedImage image = slice.getBImage();
        //Create copy of slice BI
        BufferedImage outputBI = Slice.clone(image);
        
        int width = image.getWidth();
        int height = image.getHeight();
        
        //loop through all points in image performing erosion
        for(int y = 1; y < height-1; y++){
            for(int x = 1; x < width-1; x++){
                //Create Structuring Element in cross shape
                int p01 = (image.getRGB(x-1, y) & 0xFF);    //S.E
                int p10 = (image.getRGB(x, y-1) & 0xFF);    //S.E
                int p11 = (image.getRGB(x, y) & 0xFF);      //S.E
                int p12 = (image.getRGB(x, y+1) & 0xFF);    //S.E
                int p21 = (image.getRGB(x+1, y) & 0xFF);    //S.E

                // | 0  p10  0 |        | 0 1 0 |
                // |p01 p11 p21|   =>   | 1 1 1 |
                // | 0  p21  0 |        | 0 1 0 | 
                
                //if structuring element overlaps with object, set pixel (x,y) to black
                if( p01==255 || p10==255 || p11==255 || p12==255 || p21==255 ){
                    outputBI.setRGB(x, y, Color.BLACK.getRGB());
                }
                //if structuring element fits inside object completely, set pixel to white
                if( p01==255 && p10==255 && p11==255 && p12==255 && p21==255 ){
                    outputBI.setRGB(x, y, Color.WHITE.getRGB());
                }
                
            }
        }
        return outputBI;
    }
    
    //dilates / expands object 
    private BufferedImage dilate(Slice slice){
       
       BufferedImage image = slice.getBImage();
       //Create copy of slice BI
       BufferedImage outputBI = Slice.clone(image);
       
       int width = image.getWidth();
       int height = image.getHeight();
       
       //loop through all points in image performing erosion
       for(int y = 1; y < height-1; y++){
           for(int x = 1; x < width-1; x++){
               //Create Structuring Element in cross shape
               int p01 = (image.getRGB(x-1, y) & 0xFF);    //S.E
               int p10 = (image.getRGB(x, y-1) & 0xFF);    //S.E
               int p11 = (image.getRGB(x, y) & 0xFF);      //S.E
               int p12 = (image.getRGB(x, y+1) & 0xFF);    //S.E
               int p21 = (image.getRGB(x+1, y) & 0xFF);    //S.E

               // | 0  p10  0 |        | 0 1 0 |
               // |p01 p11 p21|   =>   | 1 1 1 |
               // | 0  p21  0 |        | 0 1 0 | 
               
               //if structuring element overlaps with object, set pixel (x,y) to white / create object pixel
               if( p01==255 || p10==255 || p11==255 || p12==255 || p21==255 ){
                   outputBI.setRGB(x, y, Color.WHITE.getRGB());
               }
               
           }
       }
       return outputBI;
    }

}
