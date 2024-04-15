import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class LCS { // get max num of LED who lights & values for DP table & values of LEDs light
    private int numberOfLeds;
    private int[] ledsOrder;
    private int [][] tableValues;// to use it on DP table
    private int [] ledsCanLight;// values of LEDs light
    private final TextField numOfLedsField ;
    private final TextField orderOfLedsField ;

    private final TextArea outputTextArea ;
    LCS(){
        this.outputTextArea=MaxLedLightingApp.outputTextArea;
        this.numOfLedsField=MaxLedLightingApp.numOfLedsField;
        this.orderOfLedsField=MaxLedLightingApp.orderOfLedsField;
    }

    LCS(int  numberOfLeds,int[] ledsOrder){
        this.ledsOrder=ledsOrder;
        this.numberOfLeds=numberOfLeds;
        this.outputTextArea=MaxLedLightingApp.outputTextArea;
        this.numOfLedsField=MaxLedLightingApp.numOfLedsField;
        this.orderOfLedsField=MaxLedLightingApp.orderOfLedsField;
    }

    public int[][] getTableValues() {
        return tableValues;
    }

    public void setTableValues(int[][] tableValues) {
        this.tableValues = tableValues;
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

    public int[] getLedsCanLight() {
        return ledsCanLight;
    }

    public void setLedsCanLight(int[] ledsCanLight) {
        this.ledsCanLight = ledsCanLight;
    }

    int l = 1;// to organize table
    //static int maximumLights = 0; // to max number of LED's


    // get max num of LED who lights & values for DP table
    public  void DoLCS ( ){
        if (ledsOrder != null) {
            // source == y- axis (x), light == x axis  (y)
            int []source = sourceArray(numberOfLeds);
            int [][] c = new int[numberOfLeds +1][numberOfLeds +1]; //size must be the number of LED's +1 cause first row & column must be zeros
            String [][] b = new String[numberOfLeds +1][numberOfLeds +1]; //size must be the number of LED's +1 cause first row & column must be *
            int lengthOfSourceArray = source.length;
            int lengthOfLedsOrder = ledsOrder.length;
            ledsCanLight = new int[numberOfLeds];
            for (int i = 0; i <= lengthOfSourceArray; i++) {//first row & column
                c[i][0] = 0;
                b[i][0]="*";
            }

            for (int j = 0; j <= lengthOfLedsOrder; j++) {//first row & column
                c[0][j] = 0;
                b[0][j]="*";
            }

            for ( int i = 1; i <= lengthOfSourceArray; i++){
                for ( int j = 1; j <= lengthOfLedsOrder; j++) {
                    if (source[i-1] == ledsOrder[j-1]) { // i-1 & j-1 is to reach the first idx in array
                        c[i][j] = c[i - 1][j - 1] + 1;
                        b[i][j] = "\\";
                    } else if (c[i][j - 1] > c[i - 1][j]) {
                        c[i][j] = c[i][j - 1];
                        b[i][j] = "-";
                    } else {
                        c[i][j] = c[i - 1][j];
                        b[i][j] = "|";
                    }


                }
            }
            setTableValues(c);// to use this value  on DP table
            outputTextArea.setText("number of LED's : " +numOfLedsField.getText() +"\n");
            outputTextArea.appendText(" order of LED's :" + orderOfLedsField.getText() +"\n");
            outputTextArea.appendText("the maximum number of LEDs that you can light is : "+ c[numberOfLeds][numberOfLeds]+"\n");
            // to print DP table
            for ( int i = 0; i <= lengthOfSourceArray; i++){
                for ( int j = 0; j <= lengthOfLedsOrder; j++) {
                    outputTextArea.appendText(c[i][j]+"  ");
                    if (l==lengthOfLedsOrder+1){
                        outputTextArea.appendText("\n");
                        l=1;
                    }else
                        l++;
                }
            }

            //to print guide table
            System.out.println(" ");
            for ( int i = 0; i <= lengthOfSourceArray; i++){
                for ( int j = 0; j <= lengthOfLedsOrder; j++) {
                    System.out.print(b[i][j]+" ");
                    if (l==lengthOfLedsOrder+1){
                        System.out.print("\n");
                        l=1;
                    }else
                        l++;
                }
            }
            outputTextArea.appendText("\n The LED's can light up are :");
            print_LCS(b,source, numberOfLeds, numberOfLeds);// i,j must be like number of LED's
        }


    }
    private int [] sourceArray (int n){ // to create the source array to use it in maximum method
        int [] source = new int[n];
        for (int i = 1; i <= n; i++) {
            source[i-1]=i;
        }

        return source;
    }
    int activeIdx=0; // to have an active idx from  ledsCanLight array
    public  void print_LCS ( String [][] b, int [] x, int i, int j ){// print LED's can lights
       try {
           if ( ( i == 0 ) || ( j == 0 ) ){
               return;
           }
           else{
               if (b[i][j].equals("\\") ){
                   activeIdx++;
                   print_LCS(b, x, i - 1, j - 1);
                   outputTextArea.appendText(x[i-1] +" "); // i-1 is to reach to correct idx
                   ledsCanLight[activeIdx-1]= x[i-1];
                   activeIdx--;
               }
               else{
                   if (b[i][j].equals("|")){
                       print_LCS(b, x, i - 1, j);
                   }
                   else{
                       print_LCS(b, x, i, j - 1);
                   }
               }
           }
       }catch (Exception e){

       }
    }

}
