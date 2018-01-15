/**
 * Created by Max on 26.02.2017.
 */

import java.io.*;


public class Calc_main {
    public static void main (String[] args) {

        String fileName = "d://lines.txt";
        String lineInput = null;
        boolean nextPr = false;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
/*
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                lineInput = line;
            }
*/
            String line = br.readLine();
            System.out.println(line);
            lineInput = line;
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println();


        int i = 0;
        //double primaryResult = 0;

        class SolvePriority {
            int priority;
            int position;
        }
        SolvePriority[] solvePriority = new SolvePriority[lineInput.length()/3];
        int countPrimaryOperands = 0, numbersCount = 0;
        int numberOfPriority = 0;
        int[] solvePrimaryPosition = new int[lineInput.length()/3];
        double[] number = new double[lineInput.length()/3];

        if (lineInput.charAt(0) != '(') { // || lineInput.charAt(i) != '-'
            number[0] = parseNumber(0, lineInput);
            numbersCount++;
            for (i = 0; i < lineInput.length(); i++) {
                if (lineInput.charAt(i) == ' ' && (lineInput.charAt(i-1) == '*' || lineInput.charAt(i-1) == '+')) { // && lineInput.charAt(i-1) == '/'  && lineInput.charAt(i-1) == '-'
                    number[numbersCount] = parseNumber(i+1, lineInput);
                    numbersCount++;
                }
            }
        }

        for (i = 0; i < lineInput.length(); i++) {
            if (lineInput.charAt(i) == '*' && lineInput.charAt(i-1) == ' ' && lineInput.charAt(i+1) == ' ') {
                solvePrimaryPosition[countPrimaryOperands] = i;
                solvePriority[countPrimaryOperands].priority = numberOfPriority;
                solvePriority[countPrimaryOperands].position = i;
                numberOfPriority++;
                countPrimaryOperands++;
//                prodCount++;
            }
            if (lineInput.charAt(i) == '/' && lineInput.charAt(i-1) == ' ' && lineInput.charAt(i+1) == ' ') {
                solvePrimaryPosition[countPrimaryOperands] = i;
                countPrimaryOperands++;
            }
        }
    /*    int primariyPos = 0 , reverse = solvePrimaryPosition[primariyPos];
   //     if (countPrimaryOperands > 0)
       //     primariyPos = countPrimaryOperands-1;
        for (i = reverse; i > 0; i--) {
            if (lineInput.charAt(i) == '+' && lineInput.charAt(i-1) == ' ' && lineInput.charAt(i+1) == ' ') {
                solvePrimaryPosition[countPrimaryOperands] = i;
                countPrimaryOperands++;
            }
            if (lineInput.charAt(i) == '-' && lineInput.charAt(i-1) == ' ' && lineInput.charAt(i+1) == ' ') {
                solvePrimaryPosition[countPrimaryOperands] = i;
                countPrimaryOperands++;
            }
        }
        int forvard = solvePrimaryPosition[primariyPos];
 */       for (i = 0; i < lineInput.length(); i++) {
            if (lineInput.charAt(i) == '+' && lineInput.charAt(i-1) == ' ' && lineInput.charAt(i+1) == ' ') {
                solvePrimaryPosition[countPrimaryOperands] = i;
                solvePriority[countPrimaryOperands].priority = numberOfPriority;
                solvePriority[countPrimaryOperands].position = i;
                numberOfPriority++;
                countPrimaryOperands++;
            }
            if (lineInput.charAt(i) == '-' && lineInput.charAt(i-1) == ' ' && lineInput.charAt(i+1) == ' ') {
                solvePrimaryPosition[countPrimaryOperands] = i;
                countPrimaryOperands++;
            }
        }

        //primaryResult = parseNumber(0, lineInput);

        double[] primaryResult = new double[countPrimaryOperands];


        int j = 0;
        //primaryResult[0] = parseNumber(solvePrimaryPosition[0],lineInput);
        for (i = 0; i < countPrimaryOperands; i++) {

            if (lineInput.charAt(solvePrimaryPosition[i]) == '*')
                if (i == 0) {
                    primaryResult[i] = parseNumber(backNumberSeek(solvePrimaryPosition[i], lineInput), lineInput) * parseNumber(solvePrimaryPosition[i] + 2, lineInput);
                } else {
                    if (nextPr(i, countPrimaryOperands, solvePrimaryPosition) == true) { //
                        primaryResult[i] = primaryResult[i - 1] * parseNumber(solvePrimaryPosition[i] + 2, lineInput);
                    } else {
                        primaryResult[i] = parseNumber(backNumberSeek(solvePrimaryPosition[i], lineInput), lineInput) * parseNumber(solvePrimaryPosition[i] + 2, lineInput);
                    }
                }


            if (lineInput.charAt(solvePrimaryPosition[i]) == '/')
                primaryResult[i] /= parseNumber(solvePrimaryPosition[i] + 2, lineInput);


            System.out.print(solvePrimaryPosition[i] + " ");
        }

        for (i = 0; i < countPrimaryOperands; i++) {

            if (lineInput.charAt(solvePrimaryPosition[i]) == '+')

                if (i == 0)
                    primaryResult[i] = parseNumber(solvePrimaryPosition[i] + 2,lineInput) + parseNumber( backNumberSeek(solvePrimaryPosition[i],lineInput), lineInput);
                else {

                    if (nextPr(i, countPrimaryOperands, solvePrimaryPosition) == true) //
                        primaryResult[i] = primaryResult[i - 1] + parseNumber(solvePrimaryPosition[i] + 2, lineInput);

                    else
                        primaryResult[i] = primaryResult[i - 1] + parseNumber(backNumberSeek(solvePrimaryPosition[i], lineInput), lineInput);
                }

            if (lineInput.charAt(solvePrimaryPosition[i]) == '-' && lineInput.charAt(solvePrimaryPosition[i] + 1) == ' ')
                primaryResult[i] -= parseNumber(solvePrimaryPosition[i]+2, lineInput);


            System.out.print(solvePrimaryPosition[i] + " ");
        }

        System.out.println();
        System.out.println(primaryResult[countPrimaryOperands-1]);

    }

    public static double solver (char sign, int pos, String inputLine) {
        if (inputLine.charAt(pos) == '+')
            return parseNumber(pos+2, inputLine);

        if (inputLine.charAt(pos) == '-' && inputLine.charAt(pos+1) == ' ')
            return parseNumber(pos+2, inputLine);

        if (inputLine.charAt(pos) == '*')
            return parseNumber(pos+2, inputLine);

        if (inputLine.charAt(pos) == '/')
            return parseNumber(pos+2, inputLine);
        return 0;
    }

    public static double parseNumber (int count, String inputLine) {
        String bufferNumber = "";
        int i = count, j = count;
        while (inputLine.charAt(j) != ' ')
            if (j + 1 < inputLine.length())
                j++;
            else {
                i--;
                break;
            }
        while (i < j) {
            if (j + 1 < inputLine.length())
                bufferNumber += inputLine.charAt(i);
            else
                bufferNumber += inputLine.charAt(i + 1);
            i++;
        }

        try {
            return Integer.parseInt(bufferNumber);
        } catch (NumberFormatException e) {
            System.out.println("Неправильно набрано число - " + bufferNumber );

            return 0;
        }

    }

    public static boolean nextPr (int i, int countOfOperands, int[] solvePriorityPosition) { // int countOfOperands,
        boolean t = false;
        int max = 0;
        for (int j = i; j < countOfOperands; j++)
            if (solvePriorityPosition[i] > solvePriorityPosition[j])
                max = solvePriorityPosition[i];
            else max = solvePriorityPosition[j];
          //  if (solvePriorityPosition[i] < solvePriorityPosition[j] || solvePriorityPosition[i-1] > solvePriorityPosition[j] || (j == i && solvePriorityPosition[i] == solvePriorityPosition[j]))  //|| solvePriorityPosition[i-1] <= solvePriorityPosition[j] && j != i

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
