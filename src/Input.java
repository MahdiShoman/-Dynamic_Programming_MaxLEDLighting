import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;

public class Input {

    Alert alertWarning = new Alert(Alert.AlertType.WARNING);
    Alert alertInformation = new Alert(Alert.AlertType.INFORMATION);
    private   int numberOfLeds ;
    private   int[] ledsOrder ;
    private Stage primaryStage;

    private TextField numLedTextField ;
    private TextField orderTextField;

    public Input(Stage primaryStage, TextField numLedTextField, TextField orderTextField) {
        this.primaryStage = primaryStage;
        this.numLedTextField = numLedTextField;
        this.orderTextField = orderTextField;
        this.numberOfLeds=0;
    }

    public Input(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public int getNumberOfLeds() {
        return numberOfLeds;
    }

    public void setNumberOfLeds(int numberOfLeds) {
        this.numberOfLeds = numberOfLeds;
    }

    public int[] getLedsOrder() {
        return ledsOrder;
    }

    public void setLedsOrder(int[] ledsOrder) {
        this.ledsOrder = ledsOrder;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public TextField getNumLedTextField() {
        return numLedTextField;
    }

    public void setNumLedTextField(TextField numLedTextField) {
        this.numLedTextField = numLedTextField;
    }

    public TextField getOrderTextField() {
        return orderTextField;
    }

    public void setOrderTextField(TextField orderTextField) {
        this.orderTextField = orderTextField;
    }

    public void loadFile() {
        if (!hasError) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            loadFileChecker(selectedFile);
            try {
                String content = new String(Files.readAllBytes(selectedFile.toPath()));
                //inputTextArea.setText(content);
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }

  public static   Boolean hasError = false;
    private void loadFileChecker(File file) { //to check all cases it can come on file
        if (!hasError) {
            try {
                hasError =false;
                String[] lines = Files.readAllLines(file.toPath()).toArray(String[]::new);

                if (lines.length >= 2) {
                    // Parse the number of LEDs
                    numberOfLeds = Integer.parseInt(lines[0]); // convert from string to int

                    // Check if the number of LEDs is valid
                   checkLedsNumber();

                    // Parse the LED order
                    ledsOrder = Arrays.stream(lines[1].split(" "))
                            .mapToInt(Integer::parseInt)
                            .toArray(); // change the line from string to int array

                    checkLEDsOrder();
                    if (!hasError) {
                        alertInformation.setContentText(" The File has been Read.");
                        alertInformation.show();
                    }
                } else {
                    showError("Error: Invalid file format. (it should be 2 lines ,first has num of LED's and second has order of LED's )");
                }
            } catch (Exception e) {
                showError("Error loading file: " + e.getMessage());
            }
        }

    }

    private void showError(String message) { // Display error message in the UI
        hasError = true;
        alertWarning.setContentText(message);
        alertWarning.showAndWait();
        // send hem to another this to stop code continuing
//        System.out.println(message);
    }

    // the actual reading to let him fix the problem
    //some comments
    public void  readTextArea(){
        if (!hasError) {
            String numLedText = numLedTextField.getText();
            String orderText = orderTextField.getText();
            try {
                hasError = false;
                // Parse the number of LEDs
              //  numberOfLeds = Integer.parseInt(numLedText);

                // Check if the number of LEDs is valid
               // checkLedsNumber();
                // Parse the LED order
                ledsOrder = Arrays.stream(orderText.split(" "))
                        .mapToInt(Integer::parseInt)
                        .toArray();
               // checkLedsOrder();
                System.out.println(ledsOrder.length + "check");

            } catch (NumberFormatException e) {
                showError("Error: Invalid input format. Please enter valid integers.");
            }

        }
    }
// minas , char
    public void checkLedsNumber(){
        if (numberOfLeds < 0) {
            showError("Error: The number of LEDs must be a positive integer.");

        }
        try {
           Integer.parseInt(numberOfLeds+"");
        } catch (NumberFormatException e) {
            showError("Invalid input. Please enter an integer.");
        }
    }
    // range , duplicate , count of num on ordered leds , (spaces)
    public void checkLEDsOrder() {
        // Check if the number of LEDs matches the count in the LED order
       // if (ledsOrder != null) { // need it for listener on field

            // Check for repetition in LED order
            boolean[] seen = new boolean[numberOfLeds +1 ];  // Depend on range number
            //[false, false, false, false,...]
            boolean hasRepetition = false;
        System.out.println(ledsOrder.length);
            for (int i = 0; i < ledsOrder.length-1; i++) {
                int currentLED = ledsOrder[i];
                if (currentLED < 1 || currentLED > ledsOrder.length) {
                    showError("Error: The data on the  LEDs Order aren't  on the range ! ");
                }
                // Check if this LED has  seen before
                if (seen[currentLED]) {
                    hasRepetition = true;
                    break;
                }
                // Mark this LED as seen
                seen[currentLED] = true;
            }

            if (hasRepetition) {
                showError("Error: LED order contains repetition.");
            }
        System.out.println(ledsOrder.length +"testtt");
        if (numberOfLeds != ledsOrder.length) {
            showError("Error: The number of LEDs does not match the count in the LED order.");
        }

      //  }

    }
    static String lastInput ="E";
    int checkInputs =0;
    byte [] checkRep ;// to start from 1
    void checkLEDsOrderFiled (String keyPressed){ // keyPressed will be last idx from split string
       keyPressed = keyPressed.trim();
        String keyPressedLastIdx=keyPressed.charAt(keyPressed.length()-1)+"";
        //do split
        //if TAB the it's a first number
        //if its number then check(minus , range ,rep ,char ,count of num on ordered leds)
        //do array of 4 length has
        //// if any thiung expect space and from 1 to 9 dont tack it
        if (checkInputs == 0) {
            definitionValues();
        }
        if (checkInputs+1 > numberOfLeds) {
            showError("Error: The number of LEDs does not match the count in the LED order.");
        }else {
            try {
                if (!keyPressedLastIdx.equals("E") && (Integer.parseInt(keyPressedLastIdx) >= 1 && Integer.parseInt(keyPressedLastIdx) <= 9)) {//keyPressed is last idx
                    if (!lastInput.equals("E")) {// To add another digit
                        lastInput = lastInput + keyPressedLastIdx;// to merge last inputs
                        if (Integer.parseInt(lastInput) <= numberOfLeds) {
                            //ledsOrder[checkInputs] = Integer.parseInt(lastInput);// insert new number
                            if (checkRep[Integer.parseInt(lastInput)] == 0) {
                                checkRep[Integer.parseInt(lastInput)] = 1;// to avoid rep
                            }else {
                                showError("Error: LED order contains repetition.");
                            }

                        }
                    } else {

                        //ledsOrder[checkInputs] = Integer.parseInt(keyPressedLastIdx);// last idx
                        checkInputs++;
                        if (checkRep[Integer.parseInt(keyPressedLastIdx)] == 0) {
                            checkRep[Integer.parseInt(keyPressedLastIdx)] = 1;// to avoid rep
                        }else {
                            showError("Error: LED order contains repetition.");
                        }
                        lastInput = keyPressedLastIdx;//last idx
                /*if (lastInput .equals("E")&&) {
                    checkInputs++;
                }else {
                    showError("");
                }*/

                    }

                } else { // keyPressed is all the string
                    if (!(keyPressed.equals("BACK_SPACE") || keyPressed.equals("SPACE"))) {
                        showError(" Error: Invalid input. Please enter an positive integer.");
                    } else {
                        if(keyPressed.equals("SPACE")){
                            lastInput = keyPressedLastIdx;// for next step
                        }

                    }

                }
            }catch(NumberFormatException e){

            }
        }
    }
    void definitionValues(){
        numberOfLeds = Integer.parseInt(numLedTextField.getText());
        checkRep =new byte[numberOfLeds+1];
    }

     void  resetDataInput(){
        checkRep=null;
        checkInputs=0;
    }
}
