package com.yrunz.designpattern.db.transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * 备忘录模式
 */

public class CmdHistory {
    private final List<Command> history;

    private CmdHistory() {
        history = new ArrayList<>();
    }

    public static CmdHistory create() {
        return new CmdHistory();
    }

    public void add(Command cmd) {
        history.add(cmd);
    }

    public void rollback() {
        for (int i = history.size()-1; i >=0; i--) {
            history.get(i).undo();
        }
    }
}
