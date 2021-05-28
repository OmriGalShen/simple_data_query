package api;

public abstract class BooleanOperator extends Operator {

    public enum BooleanCommand {
        AND,
        OR,
        NOT,
        DELETE
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
