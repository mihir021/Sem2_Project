package DS;

public class Action {
    String tableName;
    String actionType;
    Object data;

    public Action(String actionType, Object data) {
        this.actionType = actionType;
        this.data = data;
    }
    public String getActionType() {
        return actionType;
    }
    public Object getData() {
        return data;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
