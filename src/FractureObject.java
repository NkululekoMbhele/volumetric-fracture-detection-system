import java.util.Arrays;
import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.awt.Color;

public class FractureObject {
  private int id;
  private int length;
  private int xLength, yLength, zLength;
  private ArrayList<PVector> fractures;
  private ColourName colour;

  //Constructors
  public FractureObject() {
    this.id = 0;
    this.fractures = null;
  }

  public FractureObject(ArrayList<PVector> fractures) {
    this.id = 0;
    this.fractures = fractures;
    calculateLength();
  }

  public FractureObject(int id, ArrayList<PVector> fractures) {
    this.id = id;
    this.fractures = fractures;
    calculateLength();
  }

  public FractureObject(int id, ArrayList<PVector> fractures, ColourName colour){
    this.id = id;
    this.fractures = fractures;
    this.colour = colour;
    calculateLength();
  }

  public FractureObject(String s){
    //String input must look like:
    //"id, colourName, colourValue, length, xLength, yLength, zLength, [x;y;z], [x1;y1;z1], ... , [xn;yn;zn]"
    String[] strArray = s.split(", ");
    this.id = Integer.parseInt(strArray[0]);
    this.colour = new ColourName(strArray[1], new Color(Integer.parseInt(strArray[2])));
    this.length = Integer.parseInt(strArray[3]);
    this.xLength = Integer.parseInt(strArray[4]);
    this.yLength = Integer.parseInt(strArray[5]);
    this.zLength = Integer.parseInt(strArray[6]);
    this.fractures = new ArrayList<PVector>( (strArray.length - 7) );

    for(int i = 7; i < strArray.length; i++){
      fractures.add( (i-7) , new PVector(strArray[i]) );
    }    
  }

  //Mutator methods
  public void setID(int id) {
    this.id = id;
  }
  public void setColourName(ColourName colour){
    this.colour = colour;
  }

  public void setFracture(ArrayList<PVector> fractures) {
    this.fractures = fractures;
    calculateLength();
  }

  //Accessor methods
  public int getID() {
    return id;
  }

  public ColourName getColourName(){
    return colour;
  }

  public ArrayList<PVector> getFracture() {
    return fractures;
  }

  public int getLength(){
    if(this.length == 0){
      calculateLength();
    }
    return length;
  }

  public int getXLength(){
    if(this.length == 0){
      calculateLength();
    }
    return xLength;
  }

  public int getYLength(){
    if(this.length == 0){
      calculateLength();
    }
    return yLength;
  }

  public int getZLength(){
    if(this.length == 0){
      calculateLength();
    }
    return zLength;
  }
  //Formats the Object information into a neat, user-friendly String 
  public String getInformationString(){
    String s = ("ID: " + String.valueOf(id) + 
                "\nColour: " + colour.getName() + 
                "\nMax Length: " + String.valueOf(length) + 
                "\nLength(x): " + String.valueOf(xLength) + 
                "\nLength(y): " + String.valueOf(yLength) + 
                "\nLength(z): " + String.valueOf(zLength));

    return s;
  }
  //toString method designed for storing String representation of object
  public String toString(){
    String s = (String.valueOf(id) + ", " + 
                colour.getName() + ", " +
                String.valueOf(colour.getColour().getRGB()) + ", " + 
                String.valueOf(length) + ", " + 
                String.valueOf(xLength) + ", " + 
                String.valueOf(yLength) + ", " + 
                String.valueOf(zLength) + ", ");

    for (int i = 0; i < fractures.size(); i++) {
      s += ( i == (fractures.size()-1) ) ? fractures.get(i).toString() : (fractures.get(i).toString() + ", "); 
    }
    //Final return String looks like:
    //"id, colourName, colourValue, length, xLength, yLength, zLength, [x;y;z], [x1;y1;z1], ... , [xn;yn;zn]"
    return s;
  }

  //Calculates the length of the fracture as well as its directional lengths (in the x, y and z directions) 
  private void calculateLength(){
    int minX = 0;
    int minY = 0;
    int minZ = 0;

    int maxX = 0;
    int maxY = 0;
    int maxZ = 0;

    //for each fracture point (3D vector), check each x,y,z to find the min and max x,y,z values in the fracture
    for (int i = 0; i < fractures.size(); i++) {
      PVector fp = fractures.get(i);
    
      if( i == 0 ){
        minX = fp.getX();
        minY = fp.getY();
        minZ = fp.getZ();
        maxX = fp.getX();
        maxY = fp.getY();
        maxZ = fp.getZ();
      }

      minX = (fp.getX() < minX) ? fp.getX() : minX;
      minY = (fp.getY() < minY) ? fp.getY() : minY;
      minZ = (fp.getZ() < minZ) ? fp.getZ() : minZ;

      maxX = (fp.getX() > maxX) ? fp.getX() : maxX;
      maxY = (fp.getY() > maxY) ? fp.getY() : maxY;
      maxZ = (fp.getZ() > maxZ) ? fp.getZ() : maxZ;
    }
    int[] minMaxValues = {
      this.xLength = maxX - minX,
      this.yLength = maxY - minY,
      this.zLength = maxZ - minZ
    };

    IntSummaryStatistics sumStat = Arrays.stream(minMaxValues).summaryStatistics();
    // int min = sumStat.getMin();
    int max = sumStat.getMax();

    this.length = max;
  }

}
