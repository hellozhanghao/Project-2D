package sat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sat.env.*;
import sat.formula.*;

import static sat.twoSATSolver.TwoSatTest.solveTwoSat;

public class SATSolverTest {
    public static final String outpDir = "/Users/liusu/Desktop/BoolAssignment.txt";
    public static final String filename = "/Users/liusu/Desktop/largeUnsat.cnf";

    // Helper function for constructing a formula.  Takes
    // a variable number of arguments, e.g.
    // makeFm(a, b, c) will make the formula (a and b and c)
    // @param e,...   clause in the formula
    // @return formula containing e,...
    
    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }
    
    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            try{
                c = c.add(l);
            }catch(NullPointerException ex){
                return null;
            }
        }
        return c;
    }

    /**
     ******* Code to test the execution time *******
     *
     * First Half:
     * System.out.println("Process Starts!!!");
     * long processStart = System.nanoTime();
     *
     * the SATSolver.solve() method
     *
     * Second Half:
     * long processFinish = System.nanoTime();
     * long processTime = processFinish - processStart;
     * System.out.println("Time:" + processTime/1000000000.0 + "s\n");
     */

    public static void main(String[] args){
        /********** Step 1: Open File **********
         */
        File file = new File(filename);
        BufferedReader reader;
        /********** Step 2: Initialize Containers **********
         */
        List<Clause> clauses = new ArrayList<>();       //Store all the clauses
        Literal[] literals;                             //Store the literal parsed from the file temporarily
        List<Variable> variables = new ArrayList<>();   //Store all the variables
        List<Literal> posLiterals = new ArrayList<>();  //Store all the positive literals
        List<Literal> negLiterals = new ArrayList<>();  //Store all the negative literals
        Boolean isTwoSat = true;
        Boolean startParsing = false;
        /********** Step 3: Parse the file **********
         */
        System.out.println("Parsing Starts!!!");
        long processStart = System.nanoTime();
        try{
            reader = new BufferedReader(new FileReader(file));
            String tempString;                 //Store the line parsed temporarily
            int numberOfVariables;          //Record the number of variables
            List<Literal> oneClause = new ArrayList<>();    //Store the literals parsed so far (before meet a 0)
            while ((tempString = reader.readLine()) != null) {
                if(tempString.length() > 0){
                    if(tempString.charAt(0) == 'p'){
                        startParsing = true;
                        String[] info = tempString.split(" ");
                        numberOfVariables = Integer.parseInt(info[2]);  //Get Number of Variables
                        for(int i = 1 ; i < numberOfVariables + 1 ; i++){
                            variables.add(new Variable(""+i));                     //Create variable list
                            posLiterals.add(PosLiteral.make(variables.get(i - 1))); //Create positive literal list
                            negLiterals.add(NegLiteral.make(variables.get(i - 1))); //Create negative literal list
                        }
                    }else if(startParsing){
                        String[] info = tempString.split(" ");    //Split the line by space
                        isTwoSat = info.length == 3;
                        int currentLiteral;                       //Store the current literal temporarily
                        for(String i : info) {
                            currentLiteral = Integer.parseInt(i);
                            if (currentLiteral != 0) {
                                // Add current literal to the current clause
                                if (currentLiteral > 0) {
                                    oneClause.add(posLiterals.get(Math.abs(currentLiteral) - 1));
                                } else {
                                    oneClause.add(negLiterals.get(Math.abs(currentLiteral) - 1));
                                }
                            } else{
                                // Make a new clause if "0" is met
                                literals = new Literal[oneClause.size()];
                                oneClause.toArray(literals);
                                if(makeCl(literals) != null){
                                    clauses.add(makeCl(literals));
                                }
                                oneClause.clear();
                            }
                        }

                    }
                }

            }
            long processFinish = System.nanoTime();
            long processTime = processFinish - processStart;
            System.out.println("Time:" + processTime/1000000000.0 + "s\n");

            /********** Step 4: Solve the formula **********
             */
            if(isTwoSat){
                solveTwoSat();
            }else{
                Clause[] newClauses = new Clause[clauses.size()];
                clauses.toArray(newClauses);
                Formula f = makeFm(newClauses);
                System.out.println("Process Starts!!!");
                long processStart1 = System.nanoTime();
                Environment env = SATSolver.solve(f);
                long processFinish1 = System.nanoTime();
                long processTime1 = processFinish1 - processStart1;
                System.out.println("Time:" + processTime1 / 1000000000.0 + "s\n");
                reader.close();

                /********** Step 5: Output result **********
                  */
                File output = new File(outpDir);
                FileWriter clear = new FileWriter(output);
                clear.write("");
                clear.close();
                if(env != null){
                    String set1 = env.toString().substring(13, env.toString().length() - 1);
                    String set2[] = set1.split(", ");
                    FileWriter writer = new FileWriter(output,true);
                    for(String i : set2){
                        String[] singleAssignment = i.split("->");
                        writer.write(singleAssignment[0] + ":" + singleAssignment[1] + "\n");
                    }
                    writer.close();
                }
                if(env != null) {
                    System.out.println("Satisfiable");
                    System.out.println(env);
                }else{
                    System.out.println("Not Satisfiable");
                }
            }
        }catch (IOException e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }
    
}