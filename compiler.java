import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class compiler {
    public static void main(String[] args) {
        ArrayList<line> lines = new ArrayList<>();
        ArrayList<words> Words = new ArrayList<>();
        ArrayList<numbers> Numbers = new ArrayList<>();
        try {
            //reads the file and puts all the lines in an arraylist
            File myObj = new File("/Users/atharvdesai/IdeaProjects/compilerproject/src/stuff.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                line temp = new line(data);
                lines.add(temp);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
            //iterates through all the lines of the code
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).getLine();
                linerunner(1, line,Numbers,Words,lines, i);

        }


    }

    public static void linerunner(int a, String line,ArrayList<numbers> Numbers, ArrayList<words> Words, ArrayList<line> lines, int i) {
        int count = 0;
        Scanner in = new Scanner(System.in);
        //checks if it is a comment
        if(lines.get(i).getLine().length()>2&&lines.get(i).getLine().substring(0,2).equals("//")&&i!=lines.size()-1)
        {
            i++;
            count++;
        }
        //checks if it is a start of a curly brace or an and of a curly brace
        else if(line.startsWith("{")||line.startsWith("}"))
        {
            count++;
        }

        //checks if the line starts with a loop
        else if (lines.get(i).getLine().length()>5&&line.substring(0, 5).equals("loop:") && (line.charAt(6)=='{')) {
            count++;
            int b = Character.getNumericValue(line.charAt(5));
            //finds the number that the code needs to loop
            String content = "";
            boolean h1 = true;
            int j = i+1;
            int f = j;
            //count how many lines of code are in the loop
            while(h1)
            {
                if(lines.get(f).getLine().equals("}"))
                {
                    h1 = false;
                }
                else
                    f++;

            }
            for(int l = 0; l<b-1;l++)
            {
                //runs them the intended amount of times

                while(j<f)
                {
                    linerunner(1, lines.get(j).getLine(), Numbers,Words,lines,i);
                    j++;
                }
                j=i+1;
            }
        }
        else if (line.startsWith("if(") && line.endsWith(")")) {
            //checks if the line starts with an if
            count++;
            boolean d = false;
            String condition = line.substring(3, line.length() - 1);
            //splits all the things to compare into different parts
            String[] parts = condition.split(",");
            String firstValue = parts[0].trim();
            String comparison = parts[1].trim();
            String secondValue = parts[2].trim();
            boolean isString1 = firstValue instanceof String;
            boolean isString2 = secondValue instanceof String;

            //takes it and if it is math then it puts it in the math function and gets the values
            if(firstValue.startsWith("math(")&&firstValue.endsWith(")"))
            {
                firstValue=String.valueOf(math(firstValue,Numbers));
            }
            else if(secondValue.startsWith("math(")&&secondValue.endsWith(")"))
            {
                secondValue=String.valueOf(math(secondValue,Numbers));
            }
            else if(!isString1&&!isString2)
            {
                //checks if it is a variable
                boolean isVariable1 = firstValue.startsWith(":") && firstValue.endsWith(":");
                boolean isVariable2 = secondValue.startsWith(":") && secondValue.endsWith(":");
                int x1 =0;
                int x2 = 0;
                if(isVariable1)
                {
                    x1 = getValueOf(firstValue.substring(1, firstValue.length() - 1),Numbers);
                }
                else
                    x1 = Integer.parseInt(firstValue);
                if(isVariable2)
                {
                    x2 = getValueOf(secondValue.substring(1, secondValue.length() - 1),Numbers);
                }
                else
                    x2 = Integer.parseInt(secondValue);

                d = compare("string",x1,x2,firstValue,secondValue,Numbers,Words,comparison);
            }

            else if(isString1&&isString2)
            {
                //uses the boolean do either int functions or string functions
                boolean isVariable1 = firstValue.startsWith(":") && firstValue.endsWith(":");
                boolean isVariable2 = secondValue.startsWith(":") && secondValue.endsWith(":");
                String s1 ="";
                String s2 = "";
                if(isVariable1)
                {
                    s1 += getWordOf(firstValue.substring(1, firstValue.length() - 1),Words);
                }
                else
                    s1 +=firstValue.substring(1, firstValue.length() - 1);
                if(isVariable2)
                {
                    s2 = getWordOf(secondValue.substring(1, secondValue.length() - 1),Words);
                }
                else
                    s2+=secondValue.substring(1, secondValue.length() - 1);


                d = compare("string",0,0,firstValue,secondValue,Numbers,Words,comparison);
            }
            else
                System.out.println("you cannot compare those things");
            if(d)
            {
                //runs the lines that are within the curly braces by counting the number of lines and running it
                boolean h1 = true;
                int j = i+1;
                int f = j;
                while(h1)
                {
                    if(lines.get(f).getLine().equals("}"))
                    {
                        h1 = false;
                    }
                    else
                        f++;

                }
                while(j<f)
                {
                    linerunner(1, lines.get(j).getLine(), Numbers,Words,lines,i);
                    j++;
                }
            }


            }


            else if (line.length() > 6 && line.substring(0, 6).equals("print(") && line.charAt(line.length() - 1) == ')') {
                //checks if it has print in the line
            count++;
                String stuffinline = line.substring(6, line.length() - 1);
                if(stuffinline.startsWith(":") && stuffinline.endsWith(":"))
                {
                    if((stuffinline instanceof String))
                    {
                        int x = getValueOf(stuffinline.substring(1, stuffinline.length() - 1),Numbers);
                        if(x==0)
                        {
                            System.out.println(getWordOf(stuffinline.substring(1, stuffinline.length() - 1),Words));
                        }
                        else
                            System.out.println(x);
                    }


                }
                else
                    System.out.println(stuffinline);
            }
            else if(line.length() > 7 && line.substring(0,7).equals("String(")&& line.charAt(line.length() - 1) == ')')
            {
                //checks if they want to create a string
                count++;
                boolean hello = true;
                String b= "";
                String c = "";
                for(int j = 7; j<line.length()&&hello;j++)
                {
                    //manually splits the string
                    if(line.charAt(j) == ',')
                    {
                        hello = false;
                        int k=j+1;
                        while(k<line.length()-1)
                        {
                            c+=line.charAt(k);
                            k++;
                        }
                    }
                    if(hello)
                        b+=line.charAt(j);
                }
                String f1 = "";
                words temp = new words(b,c);
                if(c.equals(":in:"))
                {
                    f1=in.nextLine();
                    temp.setString(f1);
                }
                Words.add(temp);

            }
            else if(line.length() > 4 && line.substring(0,4).equals("int(")&&line.charAt(line.length()-1)==')')
            {
                //chekks if the start of the line initializes a int and then splits it into different parts so and adds it so the arraylist of all ints.
                count++;
                boolean hello = true;
                String d = "";
                String e="";
                for(int j = 4;j<line.length();j++)
                {
                    if(line.charAt(j)==',')
                    {
                        hello = false;
                        int k=j+1;
                        while(k<line.length()-1)
                        {
                            e+=line.charAt(k);
                            k++;
                            j++;
                        }
                    }
                    if(hello)
                        d+=line.charAt(j);
                }
                int f=0;
                numbers temp1 = new numbers(d,f);
                if(e.equals(":in:"))
                {
                    f = in.nextInt();

                }
                if(e.startsWith("math(")&&e.charAt(e.length()-1)==')')
                {
                    f = math(e,Numbers);
                }
                else
                    f = Integer.parseInt(e);
                temp1.setValue(f);
                Numbers.add(temp1);
            }

        }

    public static boolean compare(String type, int a, int b, String c, String d, ArrayList<numbers> Numbers, ArrayList<words> Words, String operator)

            //takes in everything and then just compares the two number of strings based on the comparison that is inputted.
    {
        if(type.equals("int"))
        {
            if(operator.equals(">")) {
                return a > b;
            }
            if(operator.equals("<"))
            {
                return a<b;
            }
            if(operator.equals("="))
            {
                return a==b;
            }
            else
                System.out.println("that is not an operator");

        }
        int i = 0;
        if(type.equals("string"))
        {
            if(operator.equals(">"))
            {
                if(c.compareTo(d)>0)
                {
                    return true;
                }
                if(c.compareTo(d)<0)
                {
                    return false;
                }
            }
            if(operator.equals("<"))
            {
                if(d.compareTo(c)>0)
                {
                    return true;
                }
                if(d.compareTo(c)<0)
                {
                    return false;
                }
            }
            if(operator.equals("="))
            {
                return c.equals(d);
            }
            else
                System.out.println("that is not an operator");

        }
        return true;
    }

    public static int getValueOf(String name, ArrayList<numbers> Numbers)
    {
        //traverses the arraylist and then finds the value of the integer.
        for(int i = 0; i<Numbers.size(); i++)
        {
            if(Numbers.get(i).name.equals(name)){
                return Numbers.get(i).getValue();
            }
        }
        return 0;
    }
    public static String getWordOf(String name, ArrayList<words> Words)
    {
        //traverses the arraylist and then finds the word assiciated with the string
        for(int i = 0; i<Words.size(); i++)
        {
            if(Words.get(i).getName().equals(name))
                return Words.get(i).getString();
        }
        return "";
    }
    public static int math(String e, ArrayList<numbers> Numbers)
            //takes in 1 string then splits it into the 3 parameters and then
    {
        String a1 = e.substring(e.indexOf("(") + 1, e.indexOf(",")).trim();
        String b1 = e.substring(e.indexOf(",") + 3, e.indexOf(")")).trim();
        String op = e.substring(e.indexOf(",")+1, e.lastIndexOf(",")).trim();

        boolean isVariable1 = a1.startsWith(":") && a1.endsWith(":");
        boolean isVariable2 = b1.startsWith(":") && b1.endsWith(":");
        int a =0;
        int b = 0;
        if(isVariable1)
        {
            a = getValueOf(a1.substring(1, a1.length() - 1),Numbers);
        }
        else
            a = Integer.parseInt(a1);
        if(isVariable2)
        {
            b = getValueOf(b1.substring(1, b1.length() - 1),Numbers);
        }
        else
            b = Integer.parseInt(b1);

        if(op.equals("+"))
        {
            return a+b;
        }
        if(op.equals("-"))
        {
            return a-b;
        }
        if(op.equals("/"))
        {
            return a/b;
        }
        if(op.equals("*"))
        {
            return a*b;
        }
        if(op.equals("%"))
        {
            return a%b;
        }
        else
            return 0;
    }
}