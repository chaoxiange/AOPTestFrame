package com.hhcx.aoptestframe.action;


import com.hhcx.frame.impl.ActionCallbackImpl;

import java.util.Map;

/**
 * Created by gecx on 17-11-29.
 */

public class DemoAction extends ConstantAction {

    boolean testResult = false;

    @Override
    public void autoTest(Map<String, Object> param, ActionCallbackImpl callback) {
        if (isRun) {
            return;
        }
        isRun = true;
        testResult = false;
        setParameter(param, callback);
        TestThread ac5Thread = new TestThread();
        ac5Thread.start();
    }

    class TestThread extends Thread {

        @Override
        public void run() {

        }
    }

    @Override
    public void close(Map<String, Object> param, ActionCallbackImpl callback) {

    }
}
