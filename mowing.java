//Atharv Desai
//Period 4
import java.util.*;
public class mowing{
    public static void main(String[] args) {
        //Get input for the number of lawns and run the loop the n amount of times
        Scanner in = new Scanner(System.in);
        System.out.println("Number of Lawns: ");
        int a = in.nextInt();
        while(a>0){
            //get all the inputs
            Scanner in1 = new Scanner(System.in);
            System.out.println("Enter lawn width: ");
            int w = in1.nextInt();
            System.out.println("Enter lawn height: ");
            int h = in1.nextInt();
            String[][] lawn = createlawn(h,w);
            printlawn(lawn);
            boolean hello = true;
            int x = 0;
            int y = 0;
            while(hello){
                System.out.println("Enter intitial pos(X): ");
                y = in1.nextInt();
                System.out.println("Enter initial pos(Y): ");
                x = in1.nextInt();
                if(!check(lawn, (x-1),(y-1)))
                {
                    System.out.println("Invalid starting position try again");
                }
                else
                    hello = false;
            }
            cutter(lawn,(x-1),(y-1));
            //print the lawn out at the end
            printlawn(lawn);
            String he = in.nextLine();
        }
    }
    //run a recursive function that will cut a 3x3 square
    public static void cutter(String [][]a,int h, int w) {
        if(check(a,h+1,w)) {
            cut(a,h+1,w);
            cutter(a,h+1,w);
        }
        if(check(a,h,w+1)) {
            cut(a,h,w+1);
            cutter(a,h,w+1);
        }
        if(check(a,h-1,w)) {
            cut(a,h-1,w);
            cutter(a,h-1,w);
        }
        if(check(a,h,w-1)) {
            cut(a,h,w-1);
            cutter(a,h,w-1);
        }

    }
    //initializes a lawn with the specified parameters and randomizes some trees
    public static String[][] createlawn(int h, int w) {
        String[][]b = new String[h][w];
        for(int i = 0; i<h; i++) {
            for(int j = 0; j<w; j++) {
                int rand = (int) (Math.random() * (10));
                if(rand!=(1))
                    b[i][j]=("⚫");
                else
                    b[i][j]=("\uD83C\uDF32");
            }
        }
        return b;
    }
    //cut function will just set the given values to "c" based on the center position
    public static void cut(String [][]a,int h, int w) {
        a[h-1][w-1]="✂️";
        a[h][w-1]="✂️";
        a[h+1][w-1]="✂️";
        a[h-1][w]="✂️";
        a[h][w]="✂️";
        a[h+1][w]="✂️";
        a[h-1][w+1]="✂️";
        a[h][w+1]="✂️";
        a[h+1][w+1]="✂️";
    }
    //Function will traverse through the 2D array and print out the whole lawn
    public static void printlawn(String[][]a) {
        for(int i = 0; i<a.length; i++) {
            for(int j = 0; j<a[i].length; j++) {
                System.out.print(a[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    //checks if the given 3x3 is cuttable, checks whether it is within the lawn and if it contains a tree
    public static boolean check(String[][]a, int h , int w){
        if(h>0 && h<a.length-1 && w>0 && w<a[1].length-1) {
            String[] b = new String[9];
            b[0] = a[h - 1][w - 1];
            b[1] = a[h][w - 1];
            b[2] = a[h + 1][w - 1];
            b[3] = a[h - 1][w];
            b[4] = a[h][w];
            b[5] = a[h + 1][w];
            b[6] = a[h - 1][w + 1];
            b[7] = a[h][w + 1];
            b[8] = a[h + 1][w + 1];
            for (int i = 0; i < b.length; i++) {
                if (b[i].equals("\uD83C\uDF32")) {
                    return false;
                }
            }
            //this string is to avoid the program from osciallating between the two same squares, if this part was not
            // there then a stackoverflowerror would occur and the program would not run correctly
            String hi = "";
            for(int j = 0; j<b.length;j++) {
                hi+=b[j];
            }
            if(hi.equals("✂️✂️✂️✂️✂️✂️✂️✂️✂️")) {
                return false;
            }
            return true;
        }
        else
            return false;
    }
}