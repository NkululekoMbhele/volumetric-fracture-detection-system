import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.image.BufferedImage;

public class VFDSModel{

    private static String objName;  //Holds object name

    private static int numSlices = 0;   //Holds number of images in stack

    private static int curImgNum = 0;      //Holds current image number being displayed

    private static ArrayList<FractureObject> fractures; //Holds co-ordinates of each fracture voxel in each fracture

    private static Slice[] obj;     //Holds all raw object data

    private static Slice[] paintedObj; // Holds all object data with fractures coloured in

    private static Slice[] denoisedObj; // Holds all denoised object data with fractures coloured in

    private static Slice[] onlyFracsObj; // Holds stack of images with only fractures showing

    private static Slice[] displayObj;   //Points at object being displayed


    // Populate object by loading file
    public static boolean setObj(String fileName){

        // Try to read data, returning false if failed
        obj = DataReader.readData(fileName);
        if (obj == null){
            return false;
        }
        
        // Save name and number of slices in stack
        numSlices = DataReader.getNumSlices();  
        objName = fileName;

        // Reset all fields
        paintedObj = null;
        displayObj = null;
        denoisedObj = null;
        onlyFracsObj = null;
        fractures = new ArrayList<FractureObject>();

        return true;
    }

    // Detect fractures within object loaded
    public static void detectFractures(){

        // Detect fracture points in each slice of the object and run detection alrogithm over them
        HashMap<Integer, ArrayList<PVector>> allFracPoints = getFracPoints();
        FractureDetector fd = new FractureDetector(allFracPoints);
        fd.detectFractures();

        // Retreive fractures from Fracture Detect
        HashMap<Integer, ArrayList<PVector>> fracMap = fd.getFractures();

        // Save all !null fracture information to the fracture list, asigning new fracture ID's and designating unique colours
        ColourAllocater ca = new ColourAllocater();
        ca.resetCount();
        int newID = 0;
        for (int i = 0; i < fracMap.size(); i++) {
            if (fracMap.get(i) != null){
                fractures.add(new FractureObject(newID, fracMap.get(i), ca.getNextColour()));
                newID++;
            }
        }
    }

    // Returns all fracture points in each slice of the object
    public static HashMap<Integer, ArrayList<PVector>> getFracPoints(){

        HashMap<Integer, ArrayList<PVector>> fracPointsPerSlice = new HashMap<Integer, ArrayList<PVector>>();
        for (int i = 0; i < obj.length; i++) {
            
            ArrayList<PVector> list = new ArrayList<PVector>();
    
            // Threshold a copy of img
            BufferedImage b1 = Threshold.ThresholdImage(Slice.clone(obj[i].getBImage()), 200);
        
            // Close the holes in b1 and save as b2
            MorphologicalProcessor mp = new MorphologicalProcessor();
            BufferedImage b2 = mp.closeImage(new Slice(b1)).getBImage();
        
            // Compare the two images to find where fractures lie
            int[][] b1Arr = new Slice(b1).toIntArray();
            int[][] b2Arr = new Slice(b2).toIntArray();
        
            // Find the list of white PVectors (Intensity = 255) that make up the slice image's border
            EdgeDetector ed = new EdgeDetector();
            ArrayList<PVector> edgePixels = ed.detectEdges(b2, i);
        
            // If a black pixel in b1 becomes a white pixel in b2 and pixel is not on the border of the object
            for (int x = 0; x < numSlices; x++) {
              for (int y = 0; y < numSlices; y++) {
        
                if (b1Arr[x][y] == 0 && b2Arr[x][y] == 255 && !edgePixels.contains(new PVector(x, y, i, 255))){
                  list.add(new PVector(x, y, i));
                }
              }
            }
    
            // If no fractures found, set list of fractures to null
            if (list.size() == 0){
                list = null;
            }
            fracPointsPerSlice.put(i, list);
        }
        return fracPointsPerSlice;
    }

    // For each voxel in each fracture, paint the object with the fracture's unique colour
    public static void overlayFractures(Slice[] Obj){
        for (FractureObject frac : fractures) {
            for (PVector pv : frac.getFracture()){
                Obj[pv.getZ()].paintPixel(pv.getX(), pv.getY(), frac.getColourName().getColour().getRGB());
            }
        }
    }

    // Makes a deep copy of an object
    public static Slice[] copy(Slice[] obj){
        Slice[] newObj = new Slice[numSlices];
        for (int i = 0; i < obj.length; i++) {
            newObj[i] = new Slice(obj[i].copy(obj[i].getBImage()));
        }
        return newObj;
    }

    // Sets display object to provided object
    public static void pointTo(Slice[] obj){
        displayObj = obj;
    }

    // Paint fractures onto replica of raw object and point to paintObj
    public static void setPaintedObj(){
        paintedObj = copy(obj);
        overlayFractures(paintedObj);
    }

    // Set display object to display painted object
    public static void showPaintedObj(){
        if (paintedObj == null){
            setPaintedObj();
        }
        pointTo(paintedObj);
    }

    // Paint fractures onto denoised raw object deep copy
    public static void setDenoisedObj(){
        denoisedObj = copy(obj);
        for (int i = 0; i < denoisedObj.length; i++) {
            denoisedObj[i].setBImage(denoisedObj[i].filterImg());
        }
        overlayFractures(denoisedObj);
    }

    // Set display object to denoised object
    public static void showDenoisedObj(){
        if (denoisedObj == null){
            setDenoisedObj();
        }
        pointTo(denoisedObj);
    }

    // Paint fractures onto a black canvas to show only fracutres
    public static void setOnlyFracturesObejct(){

        onlyFracsObj = new Slice[numSlices];

        // Instantiate each slice in onyl fractures object
        for (int i = 0; i < onlyFracsObj.length; i++) {
            onlyFracsObj[i] = new Slice(new BufferedImage(numSlices, numSlices, BufferedImage.TYPE_INT_RGB));
        }

        // Set each pixel in the object to black
        for (int i = 0; i < onlyFracsObj.length; i++) {
            for (int x = 0; x < numSlices; x++) {
                for (int y = 0; y < numSlices; y++) {
                    onlyFracsObj[i].getBImage().setRGB(x, y, 0);
                }
            }                
        }
        overlayFractures(onlyFracsObj);
    }

    // Set display object to show only fractures object
    public static void showOnlyFracsObj(){
        if (onlyFracsObj == null){
            setOnlyFracturesObejct();
        }
        pointTo(onlyFracsObj);
    }

    // Try to save raw object's fracture information to a text file
    public static int saveFractures(){
        return FractureSaverLoader.saveFractureInfo(fractures, objName);
    }

    // Try to load raw object's fracture information from a text file
    public static int loadFractures(String input){
        ArrayList<FractureObject> newFractures = FractureSaverLoader.loadFractureInfo(input);
        if (newFractures == null)
            return 1 ;
        else{
            reset();                    // Clear data fields
            fractures = newFractures;
                                    
            showOnlyFracsObj();

            return 0;
        }
    }

    // Reset method
    public static void reset(){
        obj = null;
        onlyFracsObj = null;
    }

    // Mutator methods
    public static void setCurrImgNum(int i) {
        curImgNum = i;
    }

    public static void setObj(Slice[] newObj){
        obj = newObj;
    }

    public static void setDisplayObj(Slice[] newObj){
        displayObj = newObj;
    }

    // Accessor methods
    public static int getCurrImgNum(){
        return curImgNum;
    }

    public static String getObjName(){
        return objName;
    }
    
    public static Slice[] getObj(){
        return obj;
    }

    public static Image getCurrImg(int i) {
        return Slice.resize(displayObj[i].getBImage());
    }

    public static int getNumSlices(){
        return numSlices;
    }

    public static ArrayList<FractureObject> getFractures(){
        return fractures;
    }

    public static Slice[] getDisplayObject(){
        return displayObj;
    }

}
