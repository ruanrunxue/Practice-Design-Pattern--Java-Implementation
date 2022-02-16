package com.yrunz.designpattern.db.console;

import com.yrunz.designpattern.db.Db;
import com.yrunz.designpattern.db.MemoryDb;
import com.yrunz.designpattern.db.cache.CacheDbProxy;
import com.yrunz.designpattern.db.dsl.Result;

import java.util.Scanner;

// 命令行人机交互，提供dsl执行功能
public class DbConsole {
    private final Db db;
    private DbConsole() {
        db = CacheDbProxy.of(MemoryDb.instance());
    }

    public static DbConsole create() {
        return new DbConsole();
    }

    public void start() {
        System.out.println("enter exit to end.");
        System.out.println("please enter a dsl expression:");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String dsl = scanner.nextLine();
            if (dsl.equals("exit")) {
                break;
            }
            try {
                Result result = db.exec(dsl);
                print(DslResultRender.of(result));
            } catch (RuntimeException exception) {
                print(ErrorRender.of(exception.getMessage()));
            }
            System.out.println("please enter a dsl expression:");
        }
    }

    private void print(DbConsoleRender dbConsoleRender) {
        System.out.println(dbConsoleRender.render());
    }
}
