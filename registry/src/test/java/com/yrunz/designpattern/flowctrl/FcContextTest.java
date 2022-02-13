package com.yrunz.designpattern.flowctrl;

import org.junit.Test;

import static org.junit.Assert.*;

public class FcContextTest {

    @Test
    public void testTryAccept() throws InterruptedException {
        FcContext context = FcContext.create();
        assertTrue((context.curState instanceof NormalState));
        for (int i = 0; i < 20; i++) {
            context.tryAccept();
        }
        Thread.sleep(1000);
        context.tryAccept();
        assertTrue((context.curState instanceof MinorState));
        for (int i = 0; i < 100; i++) {
            context.tryAccept();
        }
        Thread.sleep(1000);
        context.tryAccept();
        assertTrue((context.curState instanceof MajorState));
        for (int i = 0; i < 6; i++) {
            context.tryAccept();
        }
        Thread.sleep(1000);
        context.tryAccept();
        assertTrue((context.curState instanceof NormalState));
    }

}