import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;


public class MaxLedLightingApp extends Application {
 //   private int[] selectedLeds;
    public static TextArea outputTextArea = new TextArea(); // to use it in DPTable Class
    public static Button DPTableButton = new Button("DP Table");
    public static Button designButton = new Button("Design");
    public static TextField numOfLedsField = new TextField();
    public static TextField orderOfLedsField = new TextField();


    // Obj from LCS
    LCS lcsFile = new LCS(); //it global  to use it in design class
    LCS lcsField = new LCS();

    AtomicBoolean fileOrNot = new AtomicBoolean(false); // to  know what to use  on DPTable (print)
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Max LED Lighting");

        // UI components


        numOfLedsField.setPromptText("Number Of LEDs ... ");
        orderOfLedsField.setPromptText("Enter LEDs order (with space between of num)");

        numOfLedsField.setMaxWidth(120);
        orderOfLedsField.setMinWidth(300);
        orderOfLedsField.setMaxWidth(350);
        Button loadButton = new Button("Load File");
        Button calculateButton = new Button("Calculate");


        // Layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(numOfLedsField, 0, 0);
        grid.add(orderOfLedsField, 0, 1);
        grid.add(loadButton, 0, 3);
        grid.add(calculateButton, 1, 3);
        grid.add(DPTableButton, 2, 3);
        grid.add(designButton, 3, 3);
        grid.add(outputTextArea, 0, 2, 4, 1);

        // Event handlers
        Input inputField = new Input(primaryStage,numOfLedsField,orderOfLedsField); // for fields
        Input inputFile = new Input(primaryStage); // for files

        numOfLedsField.textProperty().addListener((observable, oldValue, newValue) -> {
            // This block of code will be executed whenever the text changes
            inputField.checkLedsNumber();
            System.out.println("Text changed: " + newValue);
        });

/*
        orderOfLedsField.textProperty().addListener((observable, oldValue, newValue) -> {
            // This block of code will be executed whenever the text changes
            String orderText = orderOfLedsField.getText();
            int [] checkInput= Arrays.stream(orderText.split(" "))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            inputField.setLedsOrder(checkInput);
            inputField.checkLEDsOrder();
            System.out.println("Text changed: " + newValue);
        });*/
        orderOfLedsField.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            // This block of code will be executed whenever a key is released
            inputField.setLedsOrder(new int[Integer.parseInt(numOfLedsField.getText())+1]);
            inputField.checkLEDsOrderFiled(event.getCode()+"");
            System.out.println("Key Released: " + event.getCode());
        });



        if (!Input.hasError) {
            calculateButton.setOnAction(actionEvent -> { // check the calculation  is for the file or not
                        if (!Input.hasError) {

                            if (numOfLedsField.getText().length() != 0 && orderOfLedsField.getText().length() != 0) { // is not file

                                fileOrNot.set(false);
                                inputField.readTextArea();
                                System.out.println(inputField.getNumberOfLeds());
                                System.out.println(Arrays.toString(inputField.getLedsOrder()));

                                lcsField.setNumberOfLeds(inputField.getNumberOfLeds()); //
                                lcsField.setLedsOrder(inputField.getLedsOrder());
                                lcsField.DoLCS();

                                numOfLedsField.clear(); //  if user want to enter more data
                                orderOfLedsField.clear();
                                inputField.resetDataInput();

                            } else {// is a file

                                if (inputFile.getNumberOfLeds() != 0) { // means there is no file loaded
                                    fileOrNot.set(true);
                                    lcsFile.setNumberOfLeds(inputFile.getNumberOfLeds());
                                    lcsFile.setLedsOrder(inputFile.getLedsOrder());
                                    lcsFile.DoLCS();

                                    //  LCS(sourceArray(inputFile.getNumberOfLeds()), inputFile.getLedsOrder(), inputFile.getNumberOfLeds());
                                } else {
                                    System.out.println("load a file or enter data in the text field ");
                                }
                            }
                        }

            });

            try {
                loadButton.setOnAction(actionEvent1 -> inputFile.loadFile());
            }catch (NumberFormatException e){
                System.out.println("please choose a file " +"\n" + e.getMessage());
            }
            DPTableButton.setOnAction(actionEvent -> {
                printDPTable();
            });
            designButton.setOnAction(actionEvent -> {
                circuitDesign();
            });
        }else {
            System.out.println("weee");
        }



        Scene scene = new Scene(grid, 620, 320);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public TextArea getOutputTextArea() {
        return outputTextArea;
    }

    public void setOutputTextArea(TextArea outputTextArea) {
        this.outputTextArea = outputTextArea;
    }

    Stage TableStage = new Stage() ;
    public  void printDPTable (){
        DPTable dpTable;
        if(fileOrNot.get()){
            System.out.println(lcsFile.getTableValues().length);
            dpTable = new DPTable(lcsFile.getTableValues());
            System.out.println(dpTable.getValues().length);
        }else {
            System.out.println(lcsField.getTableValues().length);
            dpTable = new DPTable(lcsField.getTableValues());
            System.out.println(dpTable.getValues().length);
        }
        TableStage.setTitle(" DP Table ");
        TableStage.setScene(dpTable.printDPTable());
        TableStage.show();
    }

    Stage designStage = new Stage() ;
    public void circuitDesign(){
        CircuitFullDesign fullDesign = new CircuitFullDesign();
        if(fileOrNot.get()){
            fullDesign.setNumberOfLEDs(lcsFile.getNumberOfLeds());
            fullDesign.setLedsOrder(lcsFile.getLedsOrder());
            fullDesign.setLedsCanLight(lcsFile.getLedsCanLight());
        }else {
            fullDesign.setNumberOfLEDs(lcsField.getNumberOfLeds());
            fullDesign.setLedsOrder(lcsField.getLedsOrder());
            fullDesign.setLedsCanLight(lcsField.getLedsCanLight());
        }
        designStage.setTitle(" Circuit Design ");
        designStage.setScene(fullDesign.createLEDsAndSources());
        designStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
