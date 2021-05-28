package api;

public abstract class CompareOperator<T> extends Operator {
    protected final CompareProperty property;
    protected final T value;

    public enum CompareCommand {
        EQUAL,
        GREATER_THAN,
        LESS_THAN,
        BETWEEN,
        UPDATE,
        MAX,
        MIN
    }

    public enum CompareProperty {
        id,
        title,
        content,
        views,
        timestamp
    }

    public CompareOperator(Command cmd, CompareProperty property, T value) {
        super(cmd);
        this.property = property;
        this.value = value;
    }

    public CompareProperty getProperty() {
        return property;
    }

    public T getValue() {
        return value;
    }

    public static boolean isCompareCommand(String test){
        for (CompareCommand c : CompareCommand.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }
        return false;
    }

}
