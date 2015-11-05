package sat.twoSATSolver;

public final class Clause<T> {
    private final Literal<T> firstLiteral; // The two literals this clause is made of.
    private final Literal<T> secondLiteral;

    /**
     * Constructs a new clause out of two literals.
     *
     * @param first The first literal
     * @param second The second literal
     */
    public Clause(Literal<T> first, Literal<T> second) {
        firstLiteral = first;
        secondLiteral = second;
    }

    /**
     * Returns the first literal in this clause.
     *
     * @return The first literal in this clause.
     */
    public Literal<T> first() {
        return firstLiteral;
    }

    /**
     * Return the second literal in this clause.
     *
     * @return The second literal in this clause.
     */
    public Literal<T> second() {
        return secondLiteral;
    }

    /**
     * Returns a string representation of this clause.
     *
     * @return A string representation of this clause
     */
    @Override
    public String toString() {
        return "(" + first() + " or " + second() + ")";
    }
}