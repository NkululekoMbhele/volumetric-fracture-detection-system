import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

class GUI extends JFrame {

  // Components for Object Name Area of GUI
  private JLabel lblObjName = new JLabel("Welcome");
  JPanel pnlName = new JPanel();

  //Components for Image Area of GUI
  private ImageIcon imageIcon = new ImageIcon(new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB));
  private JLabel lblImgHolder;
  private JLabel lblSliceNumDescription = new JLabel("Showing image number:");
  private JTextField txtfldSliceNum = new JTextField();
  private JButton btnNext = new JButton("Next");
  private JButton btnPrev = new JButton("Previous");
  private JPanel pnlImgControls;
  private JPanel pnlImgArea;

  //Components for Button Area of GUI
  private JButton btnLoadNew = new JButton("Load New Object");
  private JButton btnLoadSaved = new JButton("Load Fractures");
  private JButton btnSave = new JButton("Save Fractures");
  private JLabel lblRotateObj = new JLabel("Rotate Object Options");
  private JPanel pnlLeftRight = new JPanel();
  private JButton btnLeft = new JButton("Left");
  private JButton btnRight = new JButton("Right");
  private JPanel pnlUpDown = new JPanel();
  private JButton btnDown = new JButton("Down");
  private JButton btnUp = new JButton("Up");
  private JPanel pnlRotate = new JPanel();
  private JButton btnClock = new JButton("Clockwise"); 
  private JButton btnAntiClock = new JButton("Anticlockwise");
  private JButton btnFracturesOnly = new JButton("Show Fractures Only");
  private JButton btnFilter = new JButton("Filter Object");
  private JPanel pnlButtons;
  private JPanel pnlAllButtons;

  //Components for TextArea Area of GUI
  private JTextArea txtArea = new JTextArea("Information Display Area:");
  private JLabel lblSouth = new JLabel("");
  private JLabel lblEast = new JLabel("");
  private JLabel lblWest = new JLabel("");
  private JPanel pnlTxtArea;

  public GUI(){

    // Basic Frame settings
    super("Volumetric Fracture Detection Application");
    setSize(700,750);
    setLocationRelativeTo(null); // Center the window   
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    

  // 1. Configuring the Object Name Area of GUI


    // Set font of label
    lblObjName.setFont(new Font("", Font.BOLD, 18));
    
    // Add label to panel and panel to frame
    pnlName.add(lblObjName);
    add(pnlName, BorderLayout.NORTH);


  // 2. Configuring the Image Area of GUI


    // Set image controls panel layout
    pnlImgControls = new JPanel(new FlowLayout());

    // Set size of textfield for image number
    txtfldSliceNum.setColumns(3);

    // Fill the image contols panel
    pnlImgControls.add(lblSliceNumDescription);
    pnlImgControls.add(txtfldSliceNum);
    pnlImgControls.add(btnPrev);
    pnlImgControls.add(btnNext);

    // Set image area panel layout
    pnlImgArea = new JPanel();
    pnlImgArea.setLayout(new BorderLayout());   

    // Set label to hold imageIcon
    lblImgHolder = new JLabel(imageIcon);

    // Fill the image area panel
    pnlImgArea.add(lblImgHolder, BorderLayout.WEST);
    pnlImgArea.add(pnlImgControls, BorderLayout.SOUTH);

    // Add Image Area panel to frame
    add(pnlImgArea, BorderLayout.WEST);


  // 3. Configuring the Button Area of GUI
    

    // Set Buttons panel layout
    pnlButtons = new JPanel(new BorderLayout());

    //Set AllButtons panel layout
    pnlAllButtons = new JPanel();
    pnlAllButtons.setLayout(new GridLayout(0,1));

    // Set View Control panels
    pnlLeftRight.setLayout(new GridLayout(1, 2));
    pnlUpDown.setLayout(new GridLayout(1, 2));
    pnlRotate.setLayout(new GridLayout(1, 2));

    // Remove all View Control buttons's margins (for formating)
    btnLeft.setBorder(null);
    btnRight.setBorder(null);
    btnUp.setBorder(null);
    btnDown.setBorder(null);
    btnClock.setBorder(null);
    btnAntiClock.setBorder(null);

    // Fill View Contol panels
    pnlLeftRight.add(btnLeft);
    pnlLeftRight.add(btnRight);
    pnlUpDown.add(btnUp);
    pnlUpDown.add(btnDown);
    pnlRotate.add(btnClock);
    pnlRotate.add(btnAntiClock);

    // Fill All Buttons Panel
    pnlAllButtons.add(btnLoadNew);
    pnlAllButtons.add(btnLoadSaved);
    pnlAllButtons.add(btnSave);
    pnlAllButtons.add(btnFracturesOnly);
    pnlAllButtons.add(btnFilter);
    pnlAllButtons.add(lblRotateObj);
    pnlAllButtons.add(pnlLeftRight);
    pnlAllButtons.add(pnlUpDown);
    pnlAllButtons.add(pnlRotate);

    // Add AllButtons panel to Button panel and Button panel to frame
    pnlButtons.add(pnlAllButtons, BorderLayout.CENTER);
    add(pnlButtons, BorderLayout.EAST);  


  // 4. Configuring the TextArea Area of GUI


    // Set Text Area Panel layout
    pnlTxtArea = new JPanel();
    pnlTxtArea.setLayout(new BorderLayout());
    
    // Set Text Area font and disable users from editing this field
    txtArea.setEditable(false);
    txtArea.setFont(new Font("", Font.BOLD, 12));
    
    // Add Text Area to Text Area Panel
    pnlTxtArea.add(txtArea, BorderLayout.CENTER);

    // Set sizes of dummy labels for formatting and add to Text Area Panel
    lblSouth.setPreferredSize(new Dimension(0, 10));
    lblEast.setPreferredSize(new Dimension(178, 100));
    lblWest.setPreferredSize(new Dimension(10, 0));
    pnlTxtArea.add(lblSouth, BorderLayout.SOUTH);
    pnlTxtArea.add(lblEast, BorderLayout.EAST);
    pnlTxtArea.add(lblWest, BorderLayout.WEST);

    // Add Text Area Panel to frame
    add(pnlTxtArea, BorderLayout.SOUTH);

    // Create and assign necessary listeners to components
    setUpListeners();
  }

  public void showWelcome(){
    // Welcome message
    JOptionPane.showMessageDialog(this, "Welcome. To get started, load a new or saved object.");
  }

  // Method to update image after making changes
  public void updateImg(){
    imageIcon.setImage(VFDSModel.getCurrImg(VFDSModel.getCurrImgNum()));
    lblImgHolder.repaint();
  }

  // Rotates the 
  public void rotate(String direction){
    Rotation r = new Rotation(VFDSModel.getDisplayObject());

    switch (direction){
      case ("Left"):
        VFDSModel.setDisplayObj(r.rotateAboutYaxis(VFDSModel.getDisplayObject()));
        break;

      case ("Right"):
      // Rotating 3 times is the same as rotating opposite way once
        VFDSModel.setDisplayObj(r.rotateAboutYaxis(VFDSModel.getDisplayObject()));
        VFDSModel.setDisplayObj(r.rotateAboutYaxis(VFDSModel.getDisplayObject()));
        VFDSModel.setDisplayObj(r.rotateAboutYaxis(VFDSModel.getDisplayObject()));
        break;

      case ("Up"):
        VFDSModel.setDisplayObj(r.rotateAboutXaxis(VFDSModel.getDisplayObject()));
        break;

      case ("Down"):
      // Rotating 3 times is the same as rotating opposite way once
        VFDSModel.setDisplayObj(r.rotateAboutXaxis(VFDSModel.getDisplayObject()));
        VFDSModel.setDisplayObj(r.rotateAboutXaxis(VFDSModel.getDisplayObject()));
        VFDSModel.setDisplayObj(r.rotateAboutXaxis(VFDSModel.getDisplayObject()));
        break;

      case ("Clockwise"):
        VFDSModel.setDisplayObj(r.rotateAboutZaxis(VFDSModel.getDisplayObject()));
        break;

      case ("Anticlockwise"):
      // Rotating 3 times is the same as rotating opposite way once
      VFDSModel.setDisplayObj(r.rotateAboutZaxis(VFDSModel.getDisplayObject()));
      VFDSModel.setDisplayObj(r.rotateAboutZaxis(VFDSModel.getDisplayObject()));
      VFDSModel.setDisplayObj(r.rotateAboutZaxis(VFDSModel.getDisplayObject()));
      break;
     
    }
    updateImg();
  }

  // Creates listeners for all components that can be interacted with
  public void setUpListeners(){

      // -----------------------------------
      // 1. Action listeners for buttons panel
      // -----------------------------------

    // Create listener for "Load New Object" button
    ActionListener btnLoadNewListener = new ActionListener(){
      public void actionPerformed(ActionEvent ae){
        // Ask user for file name
        String fileName = JOptionPane.showInputDialog(null, "Please enter the file name:", "Load Object", JOptionPane.QUESTION_MESSAGE);
        // If user pressed cancel
        if (fileName == null){
          return;
        }
        // Show loading
        JOptionPane.showMessageDialog(null, "Trying to load object, please wait.", "Information Message", JOptionPane.INFORMATION_MESSAGE);

        // If loading file is successful
        if (VFDSModel.setObj(fileName)){
          
          // Detect fractures in obj
          VFDSModel.detectFractures();

          // Show the object with coloured fractures
          VFDSModel.showPaintedObj();

          JOptionPane.showMessageDialog(null, "Object loaded", "Information Message", JOptionPane.INFORMATION_MESSAGE);
          lblObjName.setText("Object: " + fileName.substring(0, 1).toUpperCase() + fileName.substring(1));
          VFDSModel.setCurrImgNum(0);
          txtfldSliceNum.setText(Integer.toString(VFDSModel.getCurrImgNum()));
          updateImg();

          txtArea.setText(VFDSModel.getFractures().get(0).getInformationString());
        }
        else{
          JOptionPane.showMessageDialog(null, "Failed to load file \"" + fileName + "\".\n" +
          "Please ensure that both the header and the images belonging to the object you are trying to load " +
          "are of type PNG and are located in a folder named \"" + fileName + "\".\n" +
          "This folder should itself be located within the \"data\" folder.\n", "Information Message", JOptionPane.ERROR_MESSAGE);
        }
      }
    };
    // Assign listener
    btnLoadNew.addActionListener(btnLoadNewListener);

    //Create listener for "Load Fractures" button
    ActionListener btnLoadSavedListener = new ActionListener(){
      public void actionPerformed(ActionEvent ae){
        
        String fileName = JOptionPane.showInputDialog(null, "Please enter the file name:", "Load Object", JOptionPane.QUESTION_MESSAGE);

        if (fileName == null){
          return;
        }
        if (VFDSModel.loadFractures(fileName) == 0){
          JOptionPane.showMessageDialog(null, "Fractures successfully loaded.", "Information Message", JOptionPane.INFORMATION_MESSAGE);
          lblObjName.setText("Object: " + fileName.substring(0, 1).toUpperCase() + fileName.substring(1));
          VFDSModel.setCurrImgNum(0);
          txtfldSliceNum.setText(Integer.toString(VFDSModel.getCurrImgNum()));
          updateImg();
        }
        else{
          JOptionPane.showMessageDialog(null, "Failed to load Fractures. Please check the file " + fileName +
          " exists in the saved_files folder.", "Error Message", JOptionPane.ERROR_MESSAGE);
        }
      }
    };
    // Assign listener
    btnLoadSaved.addActionListener(btnLoadSavedListener);
  
    //Create listener for "Save Fractures" button
    ActionListener btnSaveListener = new ActionListener(){
      public void actionPerformed(ActionEvent ae){

        if (VFDSModel.saveFractures() == 0){
          JOptionPane.showMessageDialog(null, "Fractures successfully saved", "Information Message", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
          JOptionPane.showMessageDialog(null, "Failed to Fractures", "Error Message", JOptionPane.ERROR_MESSAGE);
        }
      }
    };
    // Assign listener
    btnSave.addActionListener(btnSaveListener);

    //Create listener for "Show Fractures Only" button
    ActionListener btnFracturesOnlyListener = new ActionListener(){
      public void actionPerformed(ActionEvent ae){
        //Toggle showing filtered/original (coloured) object
        if (btnFracturesOnly.getText() == "Show Fractures Only"){
          VFDSModel.showOnlyFracsObj();
          updateImg();
          btnFracturesOnly.setText("Show original");
        }
        else if (btnFracturesOnly.getText() == "Show original"){
          VFDSModel.showPaintedObj();
          updateImg();
          btnFracturesOnly.setText("Show Fractures Only");
        }
      }
    };
    // Assign listener
    btnFracturesOnly.addActionListener(btnFracturesOnlyListener);

    //Create listener for "Filter Object" button
    ActionListener btnFilterListener = new ActionListener(){
      public void actionPerformed(ActionEvent ae){

        //Toggle showing filtered/original (coloured) object
        if (btnFilter.getText() == "Filter Object"){
          VFDSModel.showDenoisedObj();
          updateImg();
          btnFilter.setText("Show original");
        }
        else if (btnFilter.getText() == "Show original"){
          VFDSModel.showPaintedObj();
          updateImg();
          btnFilter.setText("Filter Object");
        }
      }
    };
    // Assign listener
    btnFilter.addActionListener(btnFilterListener);

      // ---------------------------------------
      // 2. Action listeners for rotation panels
      // ---------------------------------------

    //Create listener for "Down" button
    btnDown.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){
        rotate(btnDown.getText());
      }
    });

    //Create listener for "Up" button
    btnUp.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){

        rotate(btnUp.getText());
      }
    });

    //Create listener for "Left" button
    btnLeft.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){

        rotate(btnLeft.getText());
      }
    });

    //Create listener for "Right" button
    btnRight.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){

        rotate(btnRight.getText());
      }
    });

    //Create listener for "Clockwise" button
    btnClock.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){

        rotate(btnClock.getText());
      }
    });

    //Create listener for "Anticlockwise" button
    btnAntiClock.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){

        rotate(btnAntiClock.getText());
      }
    });

      // ---------------------------------------
      // 3. Action listeners for image area panel
      // ---------------------------------------

    //Create listener for "Up Arrow" button
    ActionListener btnNextListener = new ActionListener(){
      public void actionPerformed(ActionEvent ae){

        int currImgNum = VFDSModel.getCurrImgNum();

        // If not last image in object
        if (currImgNum < VFDSModel.getNumSlices() - 1){
          // Increment currImgNum and the field representing it by 1
          VFDSModel.setCurrImgNum(currImgNum + 1);
          txtfldSliceNum.setText(Integer.toString(currImgNum + 1));

          // Update Image
          updateImg();
        }
      }
    };
    // Assign listener
    btnNext.addActionListener(btnNextListener);

    //Create listener for "Down Arrow" button
    ActionListener btnPrevListener = new ActionListener(){
      public void actionPerformed(ActionEvent ae){
        
        int currImgNum = VFDSModel.getCurrImgNum();

        // If not first image in object
        if (currImgNum != 0){
          // Decrement currImgNum and the field representing it by 1
          VFDSModel.setCurrImgNum(currImgNum - 1);
          txtfldSliceNum.setText(Integer.toString(currImgNum - 1));
        }

        // Update Image
        updateImg();
      }
    };
    // Assign listener
    btnPrev.addActionListener(btnPrevListener);

    // Create listener for users inputing int in slice number field
    ActionListener txtfldSliceNumListener = new ActionListener(){ 
      public void actionPerformed(java.awt.event.ActionEvent e) {
        try{
          int input = Integer.parseInt(txtfldSliceNum.getText());
          if (input < 0 || input > (VFDSModel.getNumSlices()-1)){
            JOptionPane.showMessageDialog(null, "Please enter number between 0 and " + (VFDSModel.getNumSlices() -1),
            "Error Message", JOptionPane.ERROR_MESSAGE);
            txtfldSliceNum.setText(Integer.toString(VFDSModel.getCurrImgNum()));
          }
          else{
            VFDSModel.setCurrImgNum(input);
            updateImg();
            
          }
        }
        catch(Exception ex){
          JOptionPane.showMessageDialog(null, "Please ensure you only input integer values", "Error Message",
          JOptionPane.ERROR_MESSAGE);
          txtfldSliceNum.setText(Integer.toString(VFDSModel.getCurrImgNum()));
        }
          
      }
    };
    // Assign listener
    txtfldSliceNum.addActionListener(txtfldSliceNumListener);
  }
}
