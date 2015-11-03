package sat;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sat.env.*;
import sat.formula.*;


public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();



	
	// TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability
    
	
    public void testSATSolver1(){
    	// (a v b)
    	Environment e = SATSolver.solve(makeFm(makeCl(a,b))	);
/*
    	assertTrue( "one of the literals should be set to true",
    			Bool.TRUE == e.get(a.getVariable())  
    			|| Bool.TRUE == e.get(b.getVariable())	);
    	
*/    	
    }
    
    
    public void testSATSolver2(){
    	// (~a)
    	Environment e = SATSolver.solve(makeFm(makeCl(na)));
/*
    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
*/    	
    }
    
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
            c = c.add(l);
        }
        return c;
    }

    public static void main(String[] args){
        System.out.println("Parse Starts!!!");
        long parseStart = System.nanoTime();
        File file = new File("/Users/liusu/Desktop/largeSat.cnf");
        BufferedReader reader = null;
        List<Clause> clauses = new ArrayList<Clause>();
        List<Variable> variables = new ArrayList<>();
        List<Literal> posLiterals = new ArrayList<>();
        List<Literal> negLiterals = new ArrayList<>();
        try{
            reader = new BufferedReader(new FileReader(file));
            String temp = null;
            int line = 1;
            int numberOfVariables = 0;
            int numberOfClauses = 0;
            while ((temp = reader.readLine()) != null) {
                if(line == 2){
                    String[] info = temp.split(" ");
                    //System.out.println(info[2]);
                    numberOfVariables = Integer.parseInt(info[2]);
//					numberOfClauses = Integer.parseInt(info[3]);
                    for(int i = 1 ; i < numberOfVariables + 1 ; i++){
                        variables.add(new Variable("x"+i));
                        posLiterals.add(PosLiteral.make(variables.get(i - 1)));
                        negLiterals.add(NegLiteral.make(variables.get(i - 1)));
                    }
                }else if(line > 2){
                    if(!temp.equals("\n")){
                        String[] info = temp.split(" ");
                        int temp1;
                        Literal[] literals = new Literal[info.length - 1];
                        for(int i = 0 ; i < info.length - 1 ; i++){
                            temp1 = Integer.parseInt(info[i]);
//                            System.out.print(temp1 + " ");
                            if(temp1 > 0){
                                literals[i] = posLiterals.get(Math.abs(temp1) - 1);
                            }else{
                                literals[i] = negLiterals.get(Math.abs(temp1) - 1);
                            }
                        }
                        clauses.add(makeCl(literals));
                    }
                }
                line++;
            }
            Clause[] newClauses = new Clause[clauses.size()];
            clauses.toArray(newClauses);
            long parseFinish = System.nanoTime();
            long parseTime = parseFinish - parseStart;
            System.out.println("Time:" + parseTime/1000000000.0 + "s\n");
            Formula f = makeFm(newClauses);
            System.out.println("SAT solver starts!!!");
            long started = System.nanoTime();
            Environment env = SATSolver.solve(f);
            long time = System.nanoTime();
            long timeTaken = time - started;
            System.out.println("Time:" + timeTaken / 1000000000.0 + "s");
            reader.close();
            System.out.println(env);
        }catch (IOException e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }
    
}