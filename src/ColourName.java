import java.awt.Color;

public class ColourName {
    private String name;
    private Color colour;

    //Constructors
    public ColourName(){}

    public ColourName(String name, Color colour){
        this.name = name;
        this.colour = colour;
    }
    
    //Mutators
    public void setName(String name){
        this.name = name;
    }
    public void setColour(Color colour){
        this.colour = colour;
    }

    //Accessors
    public Color getColour(){ return colour; }
    public String getName(){ return name; }

    public String toString(){
        String s = name + ", " + String.valueOf(colour.getRGB());
        return s;
    }
}
