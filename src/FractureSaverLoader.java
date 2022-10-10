import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FractureSaverLoader {

    public static int saveFractureInfo(ArrayList<FractureObject> fractures, String fileName){

    // Assign path for system
    String outputFilePathSystemSave = "../saved_files/" + fileName + "_fracture_data.txt";

    // Assign path for user
    String outputFilePathUserSave = "../output/" + fileName + "fracture_output.txt";
    
    // File for system
    File outputFileSystem = new File(outputFilePathSystemSave);

    // File for user
    File outputFileUser = new File(outputFilePathUserSave);
 

        BufferedWriter bf1 = null;
        BufferedWriter bf2 = null;

        try {
            // Assign bf1 to write to its outputFile
            bf1 = new BufferedWriter(new FileWriter(outputFileSystem));

            // Assign bf2 to write to its outputFile 
            bf2 = new BufferedWriter(new FileWriter(outputFileUser));

            // For each saved fracture
            for (FractureObject frac : fractures) {
  
                // Write string represenation of a fracture
                bf1.write(frac.toString());
                bf2.write(frac.getInformationString());
  
                // Return
                bf1.newLine();
                bf2.newLine();
                bf2.newLine();
            }
  
            bf1.flush();
            bf1.close();
            bf2.flush();
            bf2.close();
            return 0;
        }
        catch (IOException e) {
            try {  
                e.printStackTrace();
                bf1.close();
                bf2.close();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            return -1;
        }
    }

    public static ArrayList<FractureObject> loadFractureInfo(String fileName){
        try{
            // FileInputSteam and scanner to read each line of the file
            FileInputStream stream = new FileInputStream(new File("../saved_files/" + fileName + "_fracture_data.txt"));
            Scanner scanner = new Scanner(stream);

            // List to collect fracutres  
            ArrayList<FractureObject> fractureList = new ArrayList<FractureObject>();

            while (scanner.hasNextLine()){
                
                // Create fractureObject from nextLine
                FractureObject f = new FractureObject(scanner.nextLine());

                // Add fracture to List
                fractureList.add(f);
            }
            scanner.close();
            return fractureList;
        }
        catch(Exception e){ //Includes IO errors such as invalid object name
            return null;
        }
    }
}
