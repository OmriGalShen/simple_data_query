package api;

/**
 * An abstract class used to group together operators
 * used to modify database data
 */
public abstract class ModifyOperator extends Operator{

    public enum ModifyCommand {
        UPDATE,
        DELETE
    }

    public ModifyOperator(Command cmd) {
        super(cmd);
    }

    public static boolean isModifyCommand(String test){
        for (ModifyCommand c : ModifyCommand.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }
        return false;
    }
}
