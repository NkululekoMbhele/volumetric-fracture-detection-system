import java.awt.Color;

public class ColourAllocater {
    
    private static int count = 0;

    ColourName[] colorList = new ColourName[]{
        // 13 Standard Colours
        new ColourName("RED", Color.RED), 
        new ColourName("BLUE", Color.BLUE), 
        new ColourName("GREEN", Color.GREEN), 
        new ColourName("YELLOW", Color.YELLOW), 
        new ColourName("ORANGE", Color.ORANGE), 
        new ColourName("PINK", Color.PINK), 
        new ColourName("CYAN", Color.CYAN), 
        new ColourName("MAGENTA", Color.MAGENTA),
        new ColourName("PURPLE", new Color(0x80, 0x00, 0x80)),
        new ColourName("VIOLET", new Color(0xEE, 0x82, 0xEE)),
        new ColourName("MINT CREAM", new Color(0xF5, 0xFF, 0xFA)),
        new ColourName("ROSY BROWN", new Color(0xBC, 0x8F, 0x8F)),
        new ColourName("AQUA", new Color(0x00, 0xFF, 0xFF)),
        // 13 Darker Colours
        new ColourName("DARK RED", Color.RED.darker()), 
        new ColourName("DARK BLUE", Color.BLUE.darker()), 
        new ColourName("DARK GREEN", Color.GREEN.darker()), 
        new ColourName("DARK YELLOW", Color.YELLOW.darker()), 
        new ColourName("DARK ORANGE", Color.ORANGE.darker()), 
        new ColourName("DARK PINK", Color.PINK.darker()), 
        new ColourName("DARK CYAN", Color.CYAN.darker()), 
        new ColourName("DARK MAGENTA", Color.MAGENTA.darker()),
        new ColourName("DARK PURPLE", new Color(0x80, 0x00, 0x08).darker()),
        new ColourName("DARK VIOLET", new Color(0xEE, 0x82, 0xEE).darker()),
        new ColourName("DARK MINT CREAM", new Color(0xF5, 0xFF, 0xFA).darker()),
        new ColourName("DARK ROSY BROWN", new Color(0xBC, 0x8F, 0x8F).darker()),
        new ColourName("DARK AQUA", new Color(0x00, 0xFF, 0xFF).darker())
    };

    public ColourAllocater(){}

    public void resetCount(){
        count = 0;
    }

    // Assign the next colour in the list
    public ColourName getNextColour(){
        ColourName returnColour = colorList[count];
        if (count == 25)
            count = 0;
        else
            count++;
        return returnColour;
    }
}
