package com.yrunz.designpattern.db.cmd;

import com.yrunz.designpattern.db.MemoryDb;
import com.yrunz.designpattern.db.dsl.Result;

import java.util.Scanner;

// 命令行人机交互，提供dsl执行功能
public class Cmd {
    private Cmd() {}

    public static Cmd create() {
        return new Cmd();
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
                Result result = MemoryDb.instance().exec(dsl);
                print(DslResultRender.of(result));
            } catch (RuntimeException exception) {
                print(ErrorRender.of(exception.getMessage()));
            }
            System.out.println("please enter a dsl expression:");
        }
    }

    private void print(CmdRender cmdRender) {
        System.out.println(cmdRender.render());
    }
}
