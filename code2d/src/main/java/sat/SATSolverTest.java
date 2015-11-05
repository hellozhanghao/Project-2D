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

//import static sat.twoSATSolver.TwoSatTest.solveTwoSat;

public class SATSolverTest {
    public static final String outpDir = "/Users/liusu/Desktop/BoolAssignment.txt";
    public static final String filename = "/Users/liusu/Desktop/largeSat.cnf";
//    public final String filename = "/Users/liusu/Desktop/largeUnsat.cnf";
//    public final String filename = "/Users/liusu/Desktop/s8Sat.cnf";
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();


    // TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability


    public void testSATSolver1() {
        // (a v b)
        Environment e = SATSolver.solve(makeFm(makeCl(a, b)));
/*
    	assertTrue( "one of the literals should be set to true",
    			Bool.TRUE == e.get(a.getVariable())  
    			|| Bool.TRUE == e.get(b.getVariable())	);
    	
*/
    }


    public void testSATSolver2() {
        // (~a)
        Environment e = SATSolver.solve(makeFm(makeCl(na)));
/*
    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
*/
    }


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
//                ex.printStackTrace();
//                System.out.println("null pointer");
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
        BufferedReader reader = null;
        /********** Step 2: Initialize Containers **********
         */
        List<Clause> clauses = new ArrayList<>();       //Store all the clauses
        Literal[] literals;                             //Store the literal parsed from the file temporarily
        List<Variable> variables = new ArrayList<>();   //Store all the variables
        List<Literal> posLiterals = new ArrayList<>();  //Store all the positive literals
        List<Literal> negLiterals = new ArrayList<>();  //Store all the negative literals
        Boolean isTwoSat = true;
        /********** Step 3: Parse the file **********
         */
        System.out.println("Parsing Starts!!!");
        long processStart = System.nanoTime();
        try{
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;                 //Store the line parsed temporarily
            int line = 1;                       //Record the line number
            int numberOfVariables = 0;          //Record the number of variables
            List<Literal> oneClause = new ArrayList<>();    //Store the literals parsed so far (before meet a 0)
            while ((tempString = reader.readLine()) != null) {
                if(line == 2){
                    String[] info = tempString.split(" ");
                    numberOfVariables = Integer.parseInt(info[2]);  //Get Number of Variables
                    for(int i = 1 ; i < numberOfVariables + 1 ; i++){
                        variables.add(new Variable(""+i));                     //Create variable list
                        posLiterals.add(PosLiteral.make(variables.get(i - 1))); //Create positive literal list
                        negLiterals.add(NegLiteral.make(variables.get(i - 1))); //Create negative literal list
                    }
                }else if(line > 2){
                    if(tempString.length() > 0){
                        String[] info = tempString.split(" ");    //Split the line by space
                        isTwoSat = info.length <= 3 ? true : false;
                        int currentLiteral;                       //Store the current literal temporarily
                        for(int i = 0 ; i < info.length ; i++) {
                            currentLiteral = Integer.parseInt(info[i]);
                            if (currentLiteral != 0) {
                                // Add current literal to the current clause
                                if (currentLiteral > 0) {
//                                    oneClause.add(PosLiteral.make(Math.abs(currentLiteral)+""));
                                    oneClause.add(posLiterals.get(Math.abs(currentLiteral) - 1));
                                } else {
//                                    oneClause.add(NegLiteral.make(Math.abs(currentLiteral)+""));
                                    oneClause.add(negLiterals.get(Math.abs(currentLiteral) - 1));
                                }
                            } else{
                                // Make a new clause if "0" is met
//                                System.out.println("Check point 1");
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
                line++;
            }
            long processFinish = System.nanoTime();
            long processTime = processFinish - processStart;
            System.out.println("Time:" + processTime/1000000000.0 + "s\n");

            /********** Step 4: Solve the formula **********
             */
            if(!isTwoSat){
//                solveTwoSat();
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
                if(!output.exists()) {
                    try{
                        output.createNewFile();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
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
                System.out.println(env);
            }
        }catch (IOException e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }
    
}