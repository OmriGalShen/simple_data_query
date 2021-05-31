package api;

import java.util.HashSet;

/**
 * An abstract class which represent a query operator
 * used to perform data retrieving and manipulation
 * on the Database.
 */
public abstract class Operator {
    public enum Command {
        EQUAL,
        GREATER_THAN,
        LESS_THAN,
        AND,
        OR,
        NOT,
        BETWEEN,
        UPDATE,
        DELETE,
        MAX,
        MIN,
        ALL
    }
    Command cmd = null;

    public Operator(Command cmd) {
        this.cmd = cmd;
    }

    public Command getCmd() {
        return cmd;
    }

    public void setCmd(Command cmd) {
        this.cmd = cmd;
    }

    public static boolean isCommand(String test){
        for (Command c : Command.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Given database keys perform data retrieving or manipulation
     * in the case of data retrieving return requested data in
     * the form of database keys (String id)
     * @param keys database keys to perform the operator on
     * @return  requested data in the form of database keys (String id)
     */
    public abstract HashSet<String> performOperator(HashSet<String> keys);
}
