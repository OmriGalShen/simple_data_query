package api;

public abstract class ModifyOperator extends Operator{
    protected final String id;

    public enum ModifyCommand {
        UPDATE,
        DELETE
    }

    public ModifyOperator(Command cmd, String id) {
        super(cmd);
        this.id = id;
    }

    public String getId() {
        return id;
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
