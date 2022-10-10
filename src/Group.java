import java.util.ArrayList;

public class Group {

    static int globalID;
    private int id;
    private ArrayList<PVector> currentFractureVectors;
    private ArrayList<PVector> nextFractureVectors;

    // Contructors
    public Group(ArrayList<PVector> fractureComponentArrayList){
        id = globalID;
        globalID++;
        currentFractureVectors = fractureComponentArrayList;
        nextFractureVectors = new ArrayList<PVector>();
    }
    public Group(){
        id = globalID;
        globalID++;
        currentFractureVectors = new ArrayList<PVector>();
        nextFractureVectors = new ArrayList<PVector>();
    }

    //Mutators
    public void setComponent(ArrayList<PVector> componentsPVectors){
        this.currentFractureVectors = componentsPVectors;
    }
    
    public void addComponent(ArrayList<PVector> newComponent){
        this.currentFractureVectors.addAll(newComponent);
    }

    public void emptyNextComponent(){
        this.nextFractureVectors = new ArrayList<PVector>();
    }
    
    public void addNextComponent(ArrayList<PVector> newComponent){
        this.nextFractureVectors.addAll(newComponent);
    }

    //Accessors
    public int getID(){
        return this.id;
    }

    public ArrayList<PVector> getNextFractureVectors(){
        return nextFractureVectors;
    }

    public ArrayList<PVector> getArrList(){
        return this.currentFractureVectors;
    }
}
