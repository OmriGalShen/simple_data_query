package api;

import java.util.HashSet;

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
        BIGGEST,
        SMALLEST
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

    public abstract HashSet<String> performOperator(HashSet<String> keys);
}
