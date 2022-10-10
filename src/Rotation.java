import java.lang.Math;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.Color;

public class Rotation extends MatrixMath{

    private Slice[] slice_dataset;
    private BufferedImage[] dataset;
    private ArrayList<PVector> points;

    private int[][] rotationAboutX = {
        {1,0,0},
        {0,0,-1},
        {0,1,0}
    };

    private int[][] rotationAboutY = {
        {0,0,1},
        {0,1,0},
        {-1,0,0}
    };

    private int[][] rotationAboutZ = {
        {0,-1,0},
        {1,0,0},
        {0,0,1}
    };
  

    //Constructors
    public Rotation(BufferedImage[] dataset){
        this.dataset = dataset;
        createPVectorArrayList();
    }
    public Rotation(Slice[] slice_dataset){
        this.slice_dataset = slice_dataset;
        BufferedImage[] BI_dataset = new BufferedImage[slice_dataset.length];

        int counter = 0;
        for(Slice slice : slice_dataset){
            BI_dataset[counter] = slice.getBImage();
            counter++;
        }
        this.dataset = BI_dataset;
        createPVectorArrayList();
    }


    //Mutator methods
    public void setSliceDataset(Slice[] slice_dataset){
        this.slice_dataset = slice_dataset;
    }
    public void setDataset(BufferedImage[] dataset){
        this.dataset = dataset;
        //createPVectorArrayList();
    }

    //Accessor methods
    public ArrayList<PVector> getPoints(){ return points; }

    //create array of vectors
    private void createPVectorArrayList(){

        this.points = new ArrayList<PVector>(this.dataset.length * dataset[0].getWidth() * dataset[0].getHeight());

        //Creates a PVector object (3D point with intensity value) for each pixel in the dataset
        for (int z = 0; z < dataset.length; z++) {
            for(int x = 0; x < dataset[z].getWidth(); x++){
                for(int y = 0; y < dataset[z].getHeight(); y++){
                    points.add(new PVector(x, y, z, dataset[z].getRGB(x, y)));
                }
            }
        }
    }

    //rotate about x-axis ---> returns new rotated dataset BufferedImage[]
    public BufferedImage[] rotateAboutXaxis(BufferedImage[] dataset){
        //Check if the supplied dataset matches that supplied to the instance of the class.
        //If not, change the dataset that the class is working with and create a new PVector ArrayList
        if(!(dataset.equals(this.dataset))){
            this.dataset = dataset;
        }
        
        BufferedImage[] output = new BufferedImage[dataset.length];
        
        //Reset points ArrayList
        points.clear();

        //Loop through all the pixels in the dataset, create vectors, rotate vectors and store new rotated vectors in points ArrayList
        for (int z = 0; z < dataset.length; z++) {
            
            output[z] = new BufferedImage(dataset[z].getWidth(), dataset[z].getHeight(), BufferedImage.TYPE_INT_RGB);

            for(int x = 0; x < dataset[z].getWidth(); x++){
                for(int y = 0; y < dataset[z].getHeight(); y++){
                    //Offsets (dimension midpoints) used to translate positive array x,y,z indicies to center at origin
                    int offsetX = (int)Math.ceil((double)dataset[z].getWidth() / 2);
                    int offsetY = (int)Math.ceil((double)dataset[z].getHeight() / 2);
                    int offsetZ = (int)Math.ceil((double)dataset.length / 2);
                    //Centers dataset at the origin then rotates points about x-axis
                    PVector rotatedVector = matrixMultiply(rotationAboutX, new PVector(x - offsetX, y - offsetY, z - offsetZ, dataset[z].getRGB(x, y)));
                    //Translate rotated point to strictly positive dimension coordinates
                    rotatedVector.setX(rotatedVector.getX() + offsetX);
                    rotatedVector.setY(rotatedVector.getY() + offsetY);
                    rotatedVector.setZ(rotatedVector.getZ() + offsetZ);
                    //Add rotated vector with positive coordinates to points ArrayList
                    points.add(rotatedVector);
                }
            }
        }

        //Iterate through points and map each point to the correct BufferedImage in output[] and set pixel intensity value
        for(PVector vector : points){
            int x = vector.getX();
            int y = vector.getY();
            int z = vector.getZ();
            int colour = vector.getIntensity();

            output[z].setRGB(x, y-1, colour);
        }

        return output;
    } 


    //rotate about y-axis ---> returns new rotated dataset BufferedImage[]
    public BufferedImage[] rotateAboutYaxis(BufferedImage[] dataset){
        //Check if the supplied dataset matches that supplied to the instance of the class.
        //If not, change the dataset that the class is working with and create a new PVector ArrayList
        if(!(dataset.equals(this.dataset))){
            this.dataset = dataset;
        }
        
        BufferedImage[] output = new BufferedImage[dataset.length];

        //Reset points ArrayList
        points.clear();

        //Loop through all the pixels in the dataset, create vectors, rotate vectors and store new rotated vectors in points ArrayList
        for (int z = 0; z < dataset.length; z++) {
            
            output[z] = new BufferedImage(dataset[z].getWidth(), dataset[z].getHeight(), BufferedImage.TYPE_INT_RGB);

            for(int x = 0; x < dataset[z].getWidth(); x++){
                for(int y = 0; y < dataset[z].getHeight(); y++){
                    //Offsets (dimension midpoints) used to translate positive array x,y,z indicies to center at origin
                    int offsetX = (int)Math.ceil((double)dataset[z].getWidth() / 2);
                    int offsetY = (int)Math.ceil((double)dataset[z].getHeight() / 2);
                    int offsetZ = (int)Math.ceil((double)dataset.length / 2);
                    //Centers dataset at the origin then rotates points about y-axis
                    PVector rotatedVector = matrixMultiply(rotationAboutY, new PVector(x - offsetX, y - offsetY, z - offsetZ, dataset[z].getRGB(x, y)));
                    //Translate rotated point to strictly positive dimension coordinates
                    rotatedVector.setX(rotatedVector.getX() + offsetX);
                    rotatedVector.setY(rotatedVector.getY() + offsetY);
                    rotatedVector.setZ(rotatedVector.getZ() + offsetZ);
                    //Add rotated vector with positive coordinates to points ArrayList
                    points.add(rotatedVector);
                }
            }
        }

        //Iterate through points and map each point to the correct BufferedImage in output[] and set pixel intensity value
        for(PVector vector : points){
            int x = vector.getX();
            int y = vector.getY();
            int z = vector.getZ();
            int colour = vector.getIntensity();

            output[z-1].setRGB(x, y, colour);
        }

        return output;
    }


    //rotate about z-axis ---> returns new rotated dataset BufferedImage[]
    public BufferedImage[] rotateAboutZaxis(BufferedImage[] dataset){
        //Check if the supplied dataset matches that supplied to the instance of the class.
        //If not, change the dataset that the class is working with and create a new PVector ArrayList
        if(!(dataset.equals(this.dataset))){
            this.dataset = dataset;
        }
        
        BufferedImage[] output = new BufferedImage[dataset.length];
        
        //Reset points ArrayList
        points.clear();

        //Loop through all the pixels in the dataset, create vectors, rotate vectors and store new rotated vectors in points ArrayList
        for (int z = 0; z < dataset.length; z++) {
            
            output[z] = new BufferedImage(dataset[z].getWidth(), dataset[z].getHeight(), BufferedImage.TYPE_INT_RGB);

            for(int x = 0; x < dataset[z].getWidth(); x++){
                for(int y = 0; y < dataset[z].getHeight(); y++){
                    //Offsets (dimension midpoints) used to translate positive array x,y,z indicies to center at origin
                    int offsetX = (int)Math.ceil((double)dataset[z].getWidth() / 2);
                    int offsetY = (int)Math.ceil((double)dataset[z].getHeight() / 2);
                    int offsetZ = (int)Math.ceil((double)dataset.length / 2);
                    //Centers dataset at the origin then rotates points about z-axis
                    PVector rotatedVector = matrixMultiply(rotationAboutZ, new PVector(x - offsetX, y - offsetY, z - offsetZ, dataset[z].getRGB(x, y)));
                    //Translate rotated point to strictly positive dimension coordinates
                    rotatedVector.setX(rotatedVector.getX() + offsetX);
                    rotatedVector.setY(rotatedVector.getY() + offsetY);
                    rotatedVector.setZ(rotatedVector.getZ() + offsetZ);
                    //Add rotated vector with positive coordinates to points ArrayList
                    points.add(rotatedVector);
                }
            }
        }

        //Iterate through points and map each point to the correct BufferedImage in output[] and set pixel intensity value
        for(PVector vector : points){
            int x = vector.getX();
            int y = vector.getY();
            int z = vector.getZ();
            int colour = vector.getIntensity();

            output[z].setRGB(x-1, y, colour);
        }

        return output;
    }

    
    //Overload to perform MatrixMath.matrixMultiply(int[][] a, PVector b)
    private PVector matrixMultiply(int[][] a, PVector v){
        
        PVector r = new PVector();
        
        int[][] product = matrixMultiply(a, v.to2DimensionArray());

        //Check if multiplication was successful (i.e. columns of A = rows of B = 3)
        if(product == null) return null;

        //Set new x, y, z values of resultant vector
        r.setX(product[0][0]);
        r.setY(product[1][0]);
        r.setZ(product[2][0]);
        //Set resultant vector intensity value to match that of input vector v
        r.setIntensity(v.getIntensity());

        return r;
    }


    //Rotate functions using Slice objects rather than BufferedImage objects

    //rotate about x-axis ---> returns new rotated dataset Slice[]
    public Slice[] rotateAboutXaxis(Slice[] dataset){
        //Check if the supplied dataset matches that supplied to the instance of the class.
        //If not, change the dataset that the class is working with and create a new PVector ArrayList
        if(!(dataset.equals(this.slice_dataset))){
            this.slice_dataset = dataset;
        }
        
        Slice[] output = new Slice[dataset.length];

        //Reset points ArrayList
        points.clear();

        //Loop through all the pixels in the dataset, create vectors, rotate vectors and store new rotated vectors in points ArrayList
        for (int z = 0; z < dataset.length; z++) {
            
            output[z] = new Slice(new BufferedImage(dataset[z].getBImage().getWidth(), dataset[z].getBImage().getHeight(), BufferedImage.TYPE_INT_RGB));

            for(int x = 0; x < dataset[z].getBImage().getWidth(); x++){
                for(int y = 0; y < dataset[z].getBImage().getHeight(); y++){
                    //Offsets (dimension midpoints) used to translate positive array x,y,z indicies to center at origin
                    int offsetX = (int)Math.ceil((double)dataset[z].getBImage().getWidth() / 2);
                    int offsetY = (int)Math.ceil((double)dataset[z].getBImage().getHeight() / 2);
                    int offsetZ = (int)Math.ceil((double)dataset.length / 2);
                    //Centers dataset at the origin then rotates points about y-axis
                    PVector rotatedVector = matrixMultiply(rotationAboutX, new PVector((x - offsetX), (y - offsetY), (z - offsetZ), dataset[z].getBImage().getRGB(x, y)));
                    //Translate rotated point to strictly positive dimension coordinates
                    rotatedVector.setX(rotatedVector.getX() + offsetX);
                    rotatedVector.setY(rotatedVector.getY() + offsetY);
                    rotatedVector.setZ(rotatedVector.getZ() + offsetZ);
                    //Add rotated vector with positive coordinates to points ArrayList
                    points.add(rotatedVector);
                }
            }
        }
        
        //Iterate through points and map each point to the correct Slice in output[] and set pixel intensity value
        for(PVector vector : points){
            int x = vector.getX();
            int y = vector.getY();
            int z = vector.getZ();
            Color colour = new Color(vector.getIntensity());
            
            output[z].getBImage().setRGB(x, y-1, colour.getRGB());
        }

        return output;
    }
    

    //rotate about y-axis ---> returns new rotated dataset Slice[]
    public Slice[] rotateAboutYaxis(Slice[] dataset){
        //Check if the supplied dataset matches that supplied to the instance of the class.
        //If not, change the dataset that the class is working with and create a new PVector ArrayList
        if(!(dataset.equals(this.slice_dataset))){
            this.slice_dataset = dataset;
        }
        
        Slice[] output = new Slice[dataset.length];

        //Reset points ArrayList
        points.clear();

        //Loop through all the pixels in the dataset, create vectors, rotate vectors and store new rotated vectors in points ArrayList
        for (int z = 0; z < dataset.length; z++) {
            
            output[z] = new Slice(new BufferedImage(dataset[z].getBImage().getWidth(), dataset[z].getBImage().getHeight(), BufferedImage.TYPE_INT_RGB));

            for(int x = 0; x < dataset[z].getBImage().getWidth(); x++){
                for(int y = 0; y < dataset[z].getBImage().getHeight(); y++){
                    //Offsets (dimension midpoints) used to translate positive array x,y,z indicies to center at origin
                    int offsetX = (int)Math.ceil((double)dataset[z].getBImage().getWidth() / 2);
                    int offsetY = (int)Math.ceil((double)dataset[z].getBImage().getHeight() / 2);
                    int offsetZ = (int)Math.ceil((double)dataset.length / 2);
                    //Centers dataset at the origin then rotates points about y-axis
                    PVector rotatedVector = matrixMultiply(rotationAboutY, new PVector(x - offsetX, y - offsetY, z - offsetZ, dataset[z].getBImage().getRGB(x, y)));
                    //Translate rotated point to strictly positive dimension coordinates
                    rotatedVector.setX(rotatedVector.getX() + offsetX);
                    rotatedVector.setY(rotatedVector.getY() + offsetY);
                    rotatedVector.setZ(rotatedVector.getZ() + offsetZ);
                    //Add rotated vector with positive coordinates to points ArrayList
                    points.add(rotatedVector);
                }
            }
        }

        //Iterate through points and map each point to the correct Slice in output[] and set pixel intensity value
        for(PVector vector : points){
            int x = vector.getX();
            int y = vector.getY();
            int z = vector.getZ();
            int colour = vector.getIntensity();

            output[z-1].getBImage().setRGB(x, y, colour);
        }

        return output;
    }


    //rotate about z-axis ---> returns new rotated dataset Slice[]
    public Slice[] rotateAboutZaxis(Slice[] dataset){
        //Check if the supplied dataset matches that supplied to the instance of the class.
        //If not, change the dataset that the class is working with and create a new PVector ArrayList
        if(!(dataset.equals(this.slice_dataset))){
            this.slice_dataset = dataset;
        }
        
        Slice[] output = new Slice[dataset.length];

        //Reset points ArrayList
        points.clear();

        //Loop through all the pixels in the dataset, create vectors, rotate vectors and store new rotated vectors in points ArrayList
        for (int z = 0; z < dataset.length; z++) {
            
            output[z] = new Slice(new BufferedImage(dataset[z].getBImage().getWidth(), dataset[z].getBImage().getHeight(), BufferedImage.TYPE_INT_RGB));

            for(int x = 0; x < dataset[z].getBImage().getWidth(); x++){
                for(int y = 0; y < dataset[z].getBImage().getHeight(); y++){
                    //Offsets (dimension midpoints) used to translate positive array x,y,z indicies to center at origin
                    int offsetX = (int)Math.ceil((double)dataset[z].getBImage().getWidth() / 2);
                    int offsetY = (int)Math.ceil((double)dataset[z].getBImage().getHeight() / 2);
                    int offsetZ = (int)Math.ceil((double)dataset.length / 2);
                    //Centers dataset at the origin then rotates points about y-axis
                    PVector rotatedVector = matrixMultiply(rotationAboutZ, new PVector(x - offsetX, y - offsetY, z - offsetZ, dataset[z].getBImage().getRGB(x, y)));
                    //Translate rotated point to strictly positive dimension coordinates
                    rotatedVector.setX(rotatedVector.getX() + offsetX);
                    rotatedVector.setY(rotatedVector.getY() + offsetY);
                    rotatedVector.setZ(rotatedVector.getZ() + offsetZ);
                    //Add rotated vector with positive coordinates to points ArrayList
                    points.add(rotatedVector);
                }
            }
        }

        //Iterate through points and map each point to the correct Slice in output[] and set pixel intensity value
        for(PVector vector : points){
            int x = vector.getX();
            int y = vector.getY();
            int z = vector.getZ();
            int colour = vector.getIntensity();

            output[z].getBImage().setRGB(x-1, y, colour);
        }

        return output;
    }

}
