import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        int [] source = {1,2,3,4,5,6};
        int [] lights ={2,6,3,5,4,1};
        //System.out.println(maximum(source,lights));
       // byte [] f = {};
        LCS(source,lights);

    }
    public static int maximum (int []source , int [] lights){
        int max = 0 ;
        int maximumLights = 0 ;
        if (source.length==0 || lights.length==0){
            return -1;
        }
     //   int k =-1;//index for last open light
        for (int i = 0 ;i< source.length-1;i++){// for source array
           /* if (k==lights.length-1){//means
                max=1;
            }*/
            for (int j = 0 ;j< lights.length-1;j++){ // for lights array

                if (source[i] == lights[j]) {
                    if (j!=lights.length-1){
                        i++;   // to go to the next source to match it
                    }
                    max++;
                  //  k=j;
                }

            }
            if (max>maximumLights){
                maximumLights=max;
            }
        }

        return maximumLights;
    }
    static int l = 1;
    static int no = 0;
    static int [][] c = new int[7][7];
    static String [][] b = new String[7][7];
    public static void LCS (int []source , int [] lights){ // source == y- axis (x), light == x axis  (y)
        int m = source.length;
        int n = lights.length;
        for (int i = 0; i <= m; i++) {
            c[i][0] = 0;
            b[i][0]="*";
           // System.out.println(c[i][0]);
        }

        for (int j = 0; j <= n; j++) {
            c[0][j] = 0;
            b[0][j]="*";
        }

        for ( int i = 1; i <= m; i++){
            for ( int j = 1; j <= n; j++) {
                if (source[i-1] == lights[j-1]) {
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
        for ( int i = 0; i <= m; i++){
            for ( int j = 0; j <= n; j++) {
                System.out.print(c[i][j]+" ");
                if (l==7){
                    System.out.print("\n");
                    l=1;
                }else
                    l++;
            }
        }

        System.out.println(" ");
        for ( int i = 0; i <= m; i++){
            for ( int j = 0; j <= n; j++) {
                System.out.print(b[i][j]+" ");
                if (l==7){
                    System.out.print("\n");
                    l=1;
                }else
                    l++;
            }
        }
        System.out.println(" ");
        System.out.println(maximum(source,lights));

        print_LCS(b,source,6,6);
        System.out.println("\n"+no);
    }

    public static void print_LCS ( String [][] b, int [] x, int i, int j ){
        if ( ( i == 0 ) || ( j == 0 ) ){
            return;
        }
            else{
                if (b[i][j].equals("\\") ){
                    print_LCS(b, x, i - 1, j - 1);
                    System.out.print(x[i-1] +" ");
                    no++;
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
    }
}

