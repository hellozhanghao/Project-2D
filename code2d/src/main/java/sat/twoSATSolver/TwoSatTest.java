package sat.twoSATSolver;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sat.SATSolverTest;


public class TwoSatTest {
    public static void solveTwoSat() throws IOException {
        String filename = SATSolverTest.filename;
        String line;
        int a = 0;
        int b = 0;
        List<Clause<Literal>> clauses = new ArrayList<>();
        try{
            FileReader fileReader = new FileReader(filename);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line=bufferedReader.readLine())!=null){
                if(line.charAt(0)=='c') System.out.println("Comment:"+line);
                else if(line.charAt(0)=='p') System.out.println("Problem:" + line);
                else{
                    String[] info = line.split(" ");
                    for(String s:info){
                        if(a==0) a=Integer.parseInt(s);
                        else if(b==0) b=Integer.parseInt(s);
                        else if(s.equals("0")){
                            Literal la = new Literal(Math.abs(a),a>0);
                            Literal<Integer> lb = new Literal<Integer>(Math.abs(b),b>0);
                            Clause clause = new Clause(la,lb);
                            clauses.add(clause);
                            a = 0;
                            b = 0;

                        }
                    }
                }
            }
            Literal la = new Literal(Math.abs(a),a>0);
            Literal lb = new Literal(Math.abs(b),b>0);
            Clause clause = new Clause(la,lb);
            clauses.add(clause);

        }
        catch (FileNotFoundException ex){
            System.out.println("Unable to open file"+filename);
        }
        catch(IOException ex){
            ex.printStackTrace();
        }

        System.out.println("SAT solver starts!!!");
        long started = System.nanoTime();

        System.out.println(TwoSat.solve(clauses));

        long time = System.nanoTime();
        long timeTaken= time - started;
        System.out.println("Time:" + timeTaken/1000000.0 + "ms");





    }
}
