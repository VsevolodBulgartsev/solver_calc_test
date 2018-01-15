/**
 * Created by Max on 26.02.2017.
 */

        import java.io.*;
        import java.util.Arrays;
        import java.util.HashSet;

public class Calc_main {
    public static void main (String[] args) {

        String fileNameInput = "d://input.in";
        String fileNameOutput = "d://output.out";
        String lineInput = null;

        try (BufferedReader br = new BufferedReader(new FileReader(fileNameInput))) {
            String line = br.readLine();
            System.out.println(line);
            lineInput = line;
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println();

        int i = 0;
        int exponent = 0;
        HashSet<Character> opForLowPrior = new HashSet<Character> (Arrays.asList('^', '*', '/', '+', '-'));
        HashSet<Character> opForMedPrior = new HashSet<Character> (Arrays.asList('^', '*', '/'));

        int stackStringCount = 1, mainStringCount = 0;
        String[] stringPolska = new String[lineInput.length()];
       // String[] stringPolskaOutput = new String[lineInput.length()];
        String[] stringStack = new String[lineInput.length() / 2];
        stringStack[0] = " ";

// Приведение строки в постфиксную запись (модифицированную)

        for (i = 0; i < lineInput.length(); i++) {
            if (lineInput.charAt(i) == ' ')
                i = i;
            else {
                if (Character.isDigit(lineInput.charAt(i)) == true)
                    if (i < lineInput.length() - 1)
                        if (lineInput.charAt(i + 1) == ' ' || lineInput.charAt(i + 1) == ')' || Character.isDigit(lineInput.charAt(i + 1)) == true) {  // считывание и запись положительного числа
                            stringPolska[mainStringCount] = Integer.toString(parseNumber(i, lineInput));
                            i = parseNumberPos(i, lineInput);
                            mainStringCount++;
                        } else
                            expressionError(fileNameOutput);
                    else {
                        stringPolska[mainStringCount] = Integer.toString(parseNumber(i, lineInput));
                        i = parseNumberPos(i, lineInput);
                        mainStringCount++;
                    }
                if (i < lineInput.length() - 1)  // считывание и запись отрицательного числа    i > 0 &&
                    if (Character.isDigit(lineInput.charAt(i + 1)) == true && lineInput.charAt(i) == '-') {
                        stringPolska[mainStringCount] = Integer.toString(parseNumber(i, lineInput));
                        i = parseNumberPos(i + 1, lineInput);
                        mainStringCount++;
                    }
                if (lineInput.charAt(i) == '*' && i < lineInput.length()-1)
                    if (trueOperand(i, lineInput) == true) {
                        if (i > 0)
                            if (stackStringCount > 1 && lineInput.charAt(i + 1) == ' ')
                                while (opForMedPrior.contains(stringStack[stackStringCount - 1].charAt(0))) {
                                    stringPolska[mainStringCount] = stringStack[stackStringCount - 1];
                                    stringStack[stackStringCount - 1] = null;
                                    stackStringCount--;
                                    mainStringCount++;
                                }
                        stringStack[stackStringCount] = "*";
                        i++;
                        stackStringCount++;
                    }
                    else expressionError(fileNameOutput);

                if (lineInput.charAt(i) == '/' && i < lineInput.length()-1)
                    if (trueOperand(i, lineInput) == true) {
                        if (i > 0)
                            if (stackStringCount > 1 && lineInput.charAt(i + 1) == ' ')
                                while (opForMedPrior.contains(stringStack[stackStringCount - 1].charAt(0))) {
                                    stringPolska[mainStringCount] = stringStack[stackStringCount - 1];
                                    stringStack[stackStringCount - 1] = null;
                                    stackStringCount--;
                                    mainStringCount++;
                                }
                        stringStack[stackStringCount] = "/";
                        i++;
                        stackStringCount++;
                    }
                    else expressionError(fileNameOutput);

                if (lineInput.charAt(i) == '+' && i < lineInput.length()-1)
                    if (trueOperand(i, lineInput) == true) {
                        if (i > 0)
                            if (stackStringCount > 1 && lineInput.charAt(i + 1) == ' ')
                                while (opForLowPrior.contains(stringStack[stackStringCount - 1].charAt(0))) {
                                    stringPolska[mainStringCount] = stringStack[stackStringCount - 1];
                                    stringStack[stackStringCount - 1] = null;
                                    stackStringCount--;
                                    mainStringCount++;
                                }
                        stringStack[stackStringCount] = "+";
                        i++;
                        stackStringCount++;
                    }
                    else expressionError(fileNameOutput);

                if (lineInput.charAt(i) == '-' && i < lineInput.length()-1)
                    if (trueOperand(i, lineInput) == true) {
                        if (i > 0)
                        if (stackStringCount > 1)
                            while (opForLowPrior.contains(stringStack[stackStringCount - 1].charAt(0))) {
                                stringPolska[mainStringCount] = stringStack[stackStringCount - 1];
                                stringStack[stackStringCount - 1] = null;
                                stackStringCount--;
                                mainStringCount++;
                            }
                        stringStack[stackStringCount] = "-";
                        i++;
                        stackStringCount++;
                    }
                    else expressionError(fileNameOutput);

                if (lineInput.charAt(i) == '^')
                    if (trueOperand(i, lineInput) == true) {
                        if (stackStringCount > 1)
                            while (stringStack[stackStringCount - 1] == "^") {
                                stringPolska[mainStringCount] = stringStack[stackStringCount - 1];
                                stringStack[stackStringCount - 1] = null;
                                stackStringCount--;
                                mainStringCount++;
                            }
                        stringStack[stackStringCount] = "^";
                        i++;
                        stackStringCount++;
                    }
                    else expressionError(fileNameOutput);

                if (lineInput.charAt(i) == '(') {
                    stringStack[stackStringCount] = "(";
                    stackStringCount++;
                }
                if (lineInput.charAt(i) == ')') {
                    if (stackStringCount > 1)
                        while (stringStack[stackStringCount - 1] != "(") {
                            stringPolska[mainStringCount] = stringStack[stackStringCount - 1];
                            stringStack[stackStringCount - 1] = null;
                            stackStringCount--;
                            mainStringCount++;
                        }
                    stringStack[stackStringCount - 1] = null;
                    stackStringCount--;
                }
                if (Character.isLetter(lineInput.charAt(i)) == true)
                    expressionError(fileNameOutput);

            }

        }
        int j = mainStringCount + (stackStringCount - 1);
        for (i = mainStringCount; i < j; i++) {
            stringPolska[i] = stringStack[stackStringCount - 1];
            stringStack[stackStringCount - 1] = null;
            stackStringCount--;
            mainStringCount++;
        }

        for (i = 0; i < mainStringCount; i++)
            System.out.print(stringPolska[i] + " ");

        System.out.println();

        // Вычесления по постфиксной записи

        for (i = 0; i < mainStringCount; i++) {
            if (stringPolska[i] == "^") {
                exponent = Integer.parseInt(stringPolska[i-2]);
                for (j = 1; j < Integer.parseInt(stringPolska[i-1]); j++)
                    exponent *=  Integer.parseInt(stringPolska[i-2]);
                stringPolska[i-2] = Integer.toString(exponent);
                stringPolska[i] = stringPolska[i-1] = null;
                for (j = i-1; j < mainStringCount - 2; j++) {
                    stringPolska[j] = stringPolska[j+2];
                }
                stringPolska[mainStringCount - 1] = stringPolska[mainStringCount - 2] = null;
                mainStringCount--; mainStringCount--;
                i = i - 2;
            }
            if (stringPolska[i] == "*") {
                stringPolska[i-2] = Integer.toString(Integer.parseInt(stringPolska[i-2]) * Integer.parseInt(stringPolska[i-1]));
                stringPolska[i] = stringPolska[i-1] = null;
                for (j = i-1; j < mainStringCount - 2; j++) {
                    stringPolska[j] = stringPolska[j+2];
                }
                stringPolska[mainStringCount - 1] = stringPolska[mainStringCount - 2] = null;
                mainStringCount--; mainStringCount--;
                i = i - 2;
            }
            if (stringPolska[i] == "/") {
                stringPolska[i-2] = Integer.toString(Integer.parseInt(stringPolska[i-2]) / Integer.parseInt(stringPolska[i-1]));
                stringPolska[i] = stringPolska[i-1] = null;
                for (j = i-1; j < mainStringCount - 2; j++) {
                    stringPolska[j] = stringPolska[j+2];
                }
                stringPolska[mainStringCount - 1] = stringPolska[mainStringCount - 2] = null;
                mainStringCount--; mainStringCount--;
                i = i - 2;
            }
            if (stringPolska[i] == "+") {
                stringPolska[i-2] = Integer.toString(Integer.parseInt(stringPolska[i-2]) + Integer.parseInt(stringPolska[i-1]));
                stringPolska[i] = stringPolska[i-1] = null;
                for (j = i-1; j < mainStringCount - 2; j++) {
                    stringPolska[j] = stringPolska[j+2];
                }
                stringPolska[mainStringCount - 1] = stringPolska[mainStringCount - 2] = null;
                mainStringCount--; mainStringCount--;
                i = i - 2;
            }
            if (stringPolska[i] == "-") {
                stringPolska[i-2] = Integer.toString(Integer.parseInt(stringPolska[i-2]) - Integer.parseInt(stringPolska[i-1]));
                stringPolska[i] = stringPolska[i-1] = null;
                for (j = i-1; j < mainStringCount - 2; j++) {
                    stringPolska[j] = stringPolska[j+2];
                }
                stringPolska[mainStringCount - 1] = stringPolska[mainStringCount - 2] = null;
                mainStringCount--; mainStringCount--;
                i = i - 2;
            }
        }

        System.out.println();
        System.out.println("Ответ - " + stringPolska[0]);

        try(FileWriter writer = new FileWriter(fileNameOutput, false))
        {
            writer.write(stringPolska[0]);
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

    }

    public static boolean trueOperand (int i, String inputLine) {
        boolean oper = false;
        if ((Character.isDigit(inputLine.charAt(i - 2)) == true && Character.isDigit(inputLine.charAt(i + 2)) == true) ||
                (Character.isDigit(inputLine.charAt(i - 2)) == true && inputLine.charAt(i + 2) == '(') ||
                (Character.isDigit(inputLine.charAt(i - 2)) == true && inputLine.charAt(i + 2) == ')') ||
                (Character.isDigit(inputLine.charAt(i - 2)) == true && inputLine.charAt(i + 2) == '-') ||
                (inputLine.charAt(i - 2) == '(' && Character.isDigit(inputLine.charAt(i + 2)) == true) ||
                (inputLine.charAt(i - 2) == '(' && inputLine.charAt(i + 2) == '(') ||
                (inputLine.charAt(i - 2) == '(' && inputLine.charAt(i + 2) == ')') ||
                (inputLine.charAt(i - 2) == '(' && inputLine.charAt(i + 2) == '-') ||
                (inputLine.charAt(i - 2) == ')' && Character.isDigit(inputLine.charAt(i + 2)) == true) ||
                (inputLine.charAt(i - 2) == ')' && inputLine.charAt(i + 2) == '(') ||
                (inputLine.charAt(i - 2) == ')' && inputLine.charAt(i + 2) == ')') ||
                (inputLine.charAt(i - 2) == ')' && inputLine.charAt(i + 2) == '-')
                )
            oper = true;
        return oper;
    }

    public static void expressionError(String fileNameOutput) {
        System.out.println("Ошибка в записи уравнения");

        try(FileWriter writer = new FileWriter(fileNameOutput, false))
        {
            writer.write("Ошибка в записи уравнения");
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        System.exit(-1);
    }

    public static int parseNumber (int count, String inputLine) {
        String bufferNumber = "";
        int i = count, j = count;
        while (inputLine.charAt(j) != ' ' && inputLine.charAt(j) != ')')
            if (j + 1 < inputLine.length() || inputLine.charAt(j) == ')')
                j++;
            else {
                i--;
                break;
            }
        while (i < j) {
            if (j + 1 < inputLine.length() || inputLine.charAt(j) == ')')
                bufferNumber += inputLine.charAt(i);
            else
                bufferNumber += inputLine.charAt(i + 1);
            i++;
        }
        try {
            return Integer.parseInt(bufferNumber);
        } catch (NumberFormatException e) {
            System.out.println("Неправильно набрано число - " + bufferNumber );
            System.exit(-1);
            return 0;
        }
    }

    public static int parseNumberPos (int count, String inputLine) {
        int i = count, j = count;
        while (inputLine.charAt(j) != ' ' && inputLine.charAt(j) != ')')
            if (j + 1 < inputLine.length() || inputLine.charAt(j) == ')')
                j++;
            else {
                j++;
                break;
            }
        return j-1;
    }

    public static boolean nextPr (int i, int countOfOperands, int[] solvePriorityPosition) { // int countOfOperands,
        boolean t = false;
        int max = 0;
        for (int j = i; j < countOfOperands; j++)
            if (solvePriorityPosition[i] > solvePriorityPosition[j])
                max = solvePriorityPosition[i];
            else max = solvePriorityPosition[j];

        if (max >= solvePriorityPosition[i])
            t = false;
        else
            t = true;

        return t;
    }

    public static int backNumberSeek (int j, String s) {
        char c = s.charAt(j - 2);
        while (c != ' ' && j > 0) { //
            j--;
            if (j > 2) {
                c = s.charAt(j - 1);
            }
        }
        return j;
    }
}