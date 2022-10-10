import java.util.ArrayList;
import java.util.HashMap;

public class FractureDetector {
    
    HashMap<Integer, ArrayList<PVector>> master_HashMap;    //HashMap<fractureID, PVectors that make up the fracture>
    HashMap<Integer, ArrayList<PVector>> imageFP;           //HashMap of image fracture points
    ConnectComponents detector = new ConnectComponents();     //for 2D connectedComponents method : Waiting on Nkululeko

    //Constructor
    public FractureDetector(HashMap<Integer, ArrayList<PVector>> imageFP){
        this.imageFP = imageFP;
        master_HashMap = new HashMap<Integer, ArrayList<PVector>>();
    }

    //Iterate through ImageFP[] and uniquely identify all 3D fractures to populate master_HashMap
    public void detectFractures(){

        HashMap<Integer, Group> groups = new HashMap<Integer, Group>();
        HashMap<Integer, ArrayList<PVector>> nextComponents = new HashMap<Integer, ArrayList<PVector>>();

        // iterate through imageFP (dataset)
        for(int i = 0; i < imageFP.size()-1; i++){

            //store arraylist of the fracture points that relate to the current image
            ArrayList<PVector> currImgVectors = imageFP.get(Integer.valueOf(i));    //List of fracture points in curr img
            ArrayList<PVector> nextImgVectors = imageFP.get(Integer.valueOf(i+1));  //List of fracure points in next img
            
            // If current and next image have no fracture pixels, skip
            if (currImgVectors == null && nextImgVectors == null){
                continue;
            }

            // If current image has no fracture pixels but next does, create new groups for components in the next image
            if(currImgVectors == null && nextImgVectors != null){
                nextComponents = detector.ConnectedComponent(nextImgVectors, imageFP.size());       // Get List of connected fracture pixels in next image

                for (int j = 0; j < nextComponents.size(); j++) {

                    Group newGroup = new Group(nextComponents.get(j));              // Make a new group
                    groups.put(newGroup.getID(), newGroup);                         // Add that new group to the groups hashmap at newGroup ID

                    master_HashMap.put(newGroup.getID(), newGroup.getArrList());    // Add new fracture to the master_HashMap at newGroup ID
                    continue;
                }
            }

            // If current image has fracture pixles but next doesn't, this is the end of all currently running fractures. Hense empty the groups list
            if(currImgVectors != null && nextImgVectors == null){
                groups.clear();
            }

            // If current and next image have fracure pixels
            if(currImgVectors != null && nextImgVectors != null){
                nextComponents = detector.ConnectedComponent(nextImgVectors, imageFP.size()); // Get components in next image

                // Store the list of key values from nextComponents
                Integer[] unaddedComponents = new Integer[nextComponents.size()];
                for (int j = 0; j < unaddedComponents.length; j++) {
                    unaddedComponents[j] = j;
                }

                // HashMap to record if more than one group connect to a component on the next image
                HashMap<Integer, ArrayList<Integer>> whoConnectsToWho = new HashMap<Integer, ArrayList<Integer>>();

                boolean fracturesMerge = false; // Checks if fractures merge in next image

                // List of groups that have finished and should be removed at the end of the cycle
                ArrayList<Integer> groupsToRemove = new ArrayList<Integer>();

                for (int currentGroupID : groups.keySet()) {                    

                    boolean continues = false;  //Check for if a fracture continues into next image
                    
                    groups.get(currentGroupID).emptyNextComponent(); // Empty current group's nextComponent field

                    for (int nextComponentID = 0; nextComponentID < nextComponents.size(); nextComponentID++) {
                        
                        ArrayList<PVector> currentGroup = groups.get(Integer.valueOf(currentGroupID)).getArrList(); //current group in current image
                        ArrayList<PVector> nextComponent = nextComponents.get(Integer.valueOf(nextComponentID));    //current component in next image
                        
                        if (isTouching(currentGroup, nextComponent)){
                            continues = true;

                            // Add group ID to list of groups that tried to join to this component
                            ArrayList<Integer> list = whoConnectsToWho.get(nextComponentID);                            
                            if (list== null){               // Component hasn't been shared
                                list = new ArrayList<Integer>();
                            }
                            list.add(currentGroupID);
                            whoConnectsToWho.put(nextComponentID, list);
                            if (unaddedComponents[nextComponentID] == null){
                                // If this catches, two groups tried to join to the component
                                // unaddedComponetns is empty
                                fracturesMerge = true;
                            }
                            else{
                                unaddedComponents[nextComponentID] = null;
                            }
                            groups.get(currentGroupID).addNextComponent(nextComponents.get(nextComponentID));   // Set group PVector list to nextComponent PVector list                                
                            master_HashMap.get(currentGroupID).addAll(nextComponents.get(nextComponentID)); // Adds all pixels in next component to fracture with id currentGroupID
                        }
                    }
                    // If a group doens't continue, add it to a list of groups to be removed
                    if (continues == false){
                        groupsToRemove.add(currentGroupID);
                    }

                    // Set currentFractureVectors to nextFractureVectors
                    groups.get(currentGroupID).setComponent(groups.get(currentGroupID).getNextFractureVectors());
                }

                // Delete any groups that didn't connect to components above
                for (Integer groupID : groupsToRemove){
                    groups.remove(groupID);
                }

                // Dealing with if fractures merge in next slice
                if (fracturesMerge){
                    // Loop through all next components
                    for (int j = 0; j < nextComponents.size(); j++) {

                        // If added by multiple groups
                        if (whoConnectsToWho.get(j) != null){

                            if (whoConnectsToWho.get(j).size()>1){
                                
                                // Find which group is the largest (of the assumed 2 only)
    
                                ArrayList<Integer> groupIDs = whoConnectsToWho.get(j);
                                int firstID = groupIDs.get(0);
                                int secondID = groupIDs.get(1);
    
                                if (groups.get(secondID).getArrList().size() > groups.get(firstID).getArrList().size()){
                                    master_HashMap.get(secondID).addAll(master_HashMap.get(firstID)); // Add fracture points in first ID to fracure points in second ID
                                    master_HashMap.put(firstID, null);  // Remove fracure points in first ID
                                    groups.remove(firstID);             // Close group with ID first ID
                                }
                                else{
                                    master_HashMap.get(firstID).addAll(master_HashMap.get(secondID)); // Add fracture points in second ID to fracure points in first ID
                                    master_HashMap.put(secondID, null);  // Remove fracure points in second ID
                                    groups.remove(secondID);             // Close group with ID second ID
                                    
                                }
                            }
                        }
                    }
                    
                }
                
                // Dealing with components in the next slice not yet added to a current group. Checks if componentID != null
                for (int j = 0; j < unaddedComponents.length; j++) {
                    if (unaddedComponents[j] != null){
                        Group newGroup = new Group(nextComponents.get(j));              // Make a new group
                        groups.put(newGroup.getID(), newGroup);                         // Add that new group to the groups hashmap at newGroup ID
    
                        master_HashMap.put(newGroup.getID(), newGroup.getArrList());    // Add new fracture to the master_HashMap at newGroup ID
                    }
                }
            }
            
        }    
            
    }

    // Compares two PVectors
    private boolean comparePoints(PVector a, PVector b){
        return ( (a.getX() == b.getX()) && (a.getY() == b.getY()) ) ? true : false;
    }

    // Checks if two PVectors are within each other's neighbourhoods
    private boolean isTouching(ArrayList<PVector> C0, ArrayList<PVector> C1){
        boolean hit = false;
        
        //Check if any of the points in C0 exist in C1
        for (PVector t : C0) {
            //make grid
            PVector t00 = new PVector(t.getX()-1, t.getY()-1);
            PVector t01 = new PVector(t.getX()-1, t.getY());
            PVector t02 = new PVector(t.getX()-1, t.getY()+1);

            PVector t10 = new PVector(t.getX(), t.getY()-1);
            //PVector t11 = new PVector(t.getX(), t.getY()); //this is just t itself
            PVector t12 = new PVector(t.getX(), t.getY()+1);

            PVector t20 = new PVector(t.getX()+1, t.getY()-1);
            PVector t21 = new PVector(t.getX()+1, t.getY());
            PVector t22 = new PVector(t.getX()+1, t.getY()+1);
            //[t00 t10 t20]     Grid of points with target t in centre
            //[t01  t  t21]
            //[t02 t12 t22]


            for (PVector p : C1) {
                //compare to grid
                if(
                    comparePoints(p, t00) ||
                    comparePoints(p, t01) ||
                    comparePoints(p, t02) ||

                    comparePoints(p, t10) ||
                    comparePoints(p, t)   ||
                    comparePoints(p, t12) ||

                    comparePoints(p, t20) ||
                    comparePoints(p, t21) ||
                    comparePoints(p, t22)
                    ){
                        hit = true;
                        break;
                }
            }
        }
        return hit;
    }

    // Accessor method
    public HashMap<Integer, ArrayList<PVector>> getFractures(){
        return master_HashMap;
    }
    
}
