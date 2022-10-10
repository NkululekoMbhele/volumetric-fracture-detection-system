public class PVector extends Point{
    int z;
    int intensity;

    public PVector(){
        super();
        this.z = 0;
    }
    public PVector(int x, int y){
        super(x, y);
        this.z = 0;
    }
    public PVector(int x, int y, int z){
        super(x, y);
        this.z = z;
    }
    public PVector(int x, int y, int z, int intensity){
        super(x, y);
        this.z = z;
        this.intensity = intensity;
    }
    public PVector(String str){
        // Remove square backets
        str = str.substring(1, str.length() - 1);

        // Set values from string list
        super.setX(Integer.parseInt(str.split(";")[0]));
        super.setY(Integer.parseInt(str.split(";")[1]));
        setZ(Integer.parseInt(str.split(";")[2]));
    }

    // Overriding equals() method to compare two PVectors objects
    @Override
    public boolean equals(Object o) {
 
        // If the object being compared is the object itself return true
        if (o == this) {return true;}
 
        // Return false if object o is not a PVector
        if (!(o instanceof PVector)) {return false;}
         
        // Cast o to PVector and compair fields
        PVector pv = (PVector) o;
        return (pv.getX() == this.getX() &&
                pv.getY() == this.getY() &&
                pv.getZ() == this.getZ() &&
                pv.getIntensity() == this.getIntensity());
    }

    //Mutator method
    public void setZ(int z){ this.z = z; }
    public void setIntensity(int intensity){ this.intensity = intensity; }

    //Accessor method
    public int getZ(){ return z; }
    public int getIntensity(){ return intensity; }

    ///Vector as 1D int array with length 3
    public int[] to1DimensionArray(){
        
        int[] matrix = { super.getX(), super.getY(), this.z };

        return matrix;
    }

    public int[][] to2DimensionArray(){
        int[][] matrix = {
            { super.getX() },
            { super.getY() }, 
            { this.z }
        };

        return matrix;
    }

    // Returns nicely formatted string representation
    public String toString(){
        return "[" + super.getX() + ";" + super.getY() + ";" + this.z + "]";
    }
}
