
package com.hhcx.aoptestframe.util;


public class BackManager {

    public static final Object object = new Object();
    
    public static final long TIME_WAIT = 10000;
    private static boolean isRun = false;

    public static void waitForBack() {
        new WaitThread().start();
    }
    
    public static void notifyBack() {
        synchronized (object) {
            object.notifyAll();
        }
    }

    public static void destroy() {
        isRun = false;
        notifyBack();
    }

    public static void waitForNotify(long millis) {
        try {
            synchronized (object) {
                object.wait(millis);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class WaitThread extends Thread {
        @Override
        public void run() {
            isRun = true;
            try {
                while (isRun) {
                    synchronized (object) {
                        object.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                run();
            }
        }
    }
}
