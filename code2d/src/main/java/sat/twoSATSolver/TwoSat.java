package sat.twoSATSolver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import sat.SATSolverTest;

public final class TwoSat {
    /**
     * Given as input a list of clauses representing a 2-CNF formula, returns
     * whether that formula is satisfiable.
     *
     * @param formula The input 2-CNF formula.
     * @return Whether the formula has a satisfying assignment.
     */
    public static <T> boolean solve(List<Clause<T>> formula) {
        /** Begin by populating a set of all the variables in this formula. */
        Set<T> variables = new HashSet<>();
        for (Clause<T> clause: formula) {
            variables.add(clause.first().value());
            variables.add(clause.second().value());
        }

        /** Construct the directed graph of myGraph.  Begin by creating the
         * nodes.
         */
        DirectedGraph<Literal<T>> myGraph = new DirectedGraph<>();
        for (T variable: variables) {
            /* Add both the variable and its negation. */
            myGraph.addNode(new Literal<>(variable, true));
            myGraph.addNode(new Literal<>(variable, false));
        }

        /** From each clause (A or B), add two clauses - (~A -> B) and (~B -> A)
         * to the graph as edges.
         */
        for (Clause<T> clause: formula) {
            myGraph.addEdge(clause.first().negation(), clause.second());
            myGraph.addEdge(clause.second().negation(), clause.first());
        }

        /** Compute the SCCs of this graph using Kosaraju's algorithm. */
        Map<Literal<T>, Integer> scc = Kosaraju.stronglyConnectedComponents(myGraph);
        Map<Literal<T>, Boolean> assignment = new HashMap<>();
        Map<Integer, Set<Literal<T>>> rScc = new HashMap<>();
        Map<Integer, Boolean> sccTruth = new HashMap<>();
        boolean isSat = true;

        /** Finally, check whether any literal and its negation are in the same
         * strongly connected component.
         */
        for (T variable: variables){
            int index1 = scc.get(new Literal<>(variable,true));
            if (!rScc.containsKey(index1)){
                Set<Literal<T>> sccVars = new HashSet<>();
                rScc.put(index1,sccVars);
            }
            Set<Literal<T>> temp =rScc.get(index1);
            temp.add(new Literal<>(variable, true));
            rScc.put(index1, temp);

            int index2 = scc.get(new Literal<>(variable,false));
            if (!rScc.containsKey(index2)){
                Set<Literal<T>> sccVars = new HashSet<>();
                rScc.put(index2,sccVars);
            }
            temp = rScc.get(index2);
            temp.add(new Literal<>(variable, false));
            rScc.put(index2,temp);

            if (scc.get(new Literal<>(variable, true)).equals(scc.get(new Literal<>(variable, false))))
                isSat = false;
        }

        if(isSat) {

            int i = 0;
            while (!sccTruth.containsKey(i)) {
                for (Literal<T> lit : rScc.get(i)) {
                    assignment.put(lit, true);
                    sccTruth.put(scc.get(lit.negation()), false);
                }
                i++;
            }
            try{
                File output = new File(SATSolverTest.outpDir);
                FileWriter clear = new FileWriter(output);
                clear.write("");
                clear.close();
                FileWriter writer = new FileWriter(output);
                for(T variable: variables){    // all true literals
                    Literal<T> lit = new Literal<>(variable, true);
                    if (assignment.containsKey(lit)){
                        writer.write(lit + ":" + "TRUE\n");
                        System.out.println(lit+":"+assignment.get(lit));
                    }
                    else{
                        writer.write(lit + ":" + "FALSE\n");
                        System.out.println(lit+":"+false);
                    }
                }
                writer.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return isSat;
    }
}