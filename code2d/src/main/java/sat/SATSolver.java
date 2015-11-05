package sat;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Bool;
import sat.env.Environment;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.PosLiteral;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     * 
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        ImList<Clause> clauses = formula.getClauses();
        Environment result = new Environment();
        return solve(clauses,result);
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     * 
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        if(clauses.size() == 0){
            return env;
        }
        Clause smallest = clauses.first();
        //TODO: improve the efficiency of search algorithm
        for(Clause i : clauses){
            int thisSize = i.size();
            smallest = smallest.size() > thisSize ? i : smallest;
            //sort instead of search
            if(thisSize == 0){
                return null;
            }
        }
        //TODO: improve the efficiency of recursion, simplify the process
        if(smallest.size() == 1){
            Literal l = smallest.chooseLiteral();
            env = l instanceof PosLiteral ? env.put(l.getVariable(), Bool.TRUE) : env.put(l.getVariable(), Bool.FALSE);
            return solve(substitute(clauses,l),env);
        }else{
            Literal l = smallest.chooseLiteral();
            ImList reducedClauses = substitute(clauses, l);
            Environment setTrue = solve(reducedClauses,env.put(l.getVariable(),Bool.TRUE));
            return setTrue != null ? setTrue : solve(substitute(clauses,l.getNegation()),env.put(l.getVariable(),Bool.FALSE));
        }
    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     * 
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses, Literal l) {
        // TODO: improve the efficiency of substitute since it will be called multiple times
        ImList<Clause> newClauses = new EmptyImList<Clause>();
        for(Clause i : clauses){
            Clause newOne = i.reduce(l);
            newClauses = newOne != null ? newClauses.add(newOne) : newClauses;
        }
        return newClauses;
    }

}
