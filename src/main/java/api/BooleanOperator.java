package api;

/**
 * An abstract class used to group together operators
 * used for boolean requests
 */
public abstract class BooleanOperator extends Operator {

    public enum BooleanCommand {
        AND,
        OR,
        NOT,
        ALL
    }

    public BooleanOperator(Command cmd) {
        super(cmd);
    }

    public static boolean isBooleanCommand(String test){
        for (BooleanCommand c : BooleanCommand.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }
        return false;
    }
}
