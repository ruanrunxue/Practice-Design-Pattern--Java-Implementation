package db.transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * 备忘录模式
 */

public class ExecHistory {
    private final List<Command<?,?>> history;

    private ExecHistory() {
        history = new ArrayList<>();
    }

    public static ExecHistory create() {
        return new ExecHistory();
    }

    public void add(Command<?,?> cmd) {
        history.add(cmd);
    }

    public void rollback() {
        for (int i = history.size()-1; i >=0; i--) {
            history.get(i).undo();
        }
    }
}
