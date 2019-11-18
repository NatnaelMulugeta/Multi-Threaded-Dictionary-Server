package app.code;

import java.util.concurrent.LinkedBlockingQueue;

public class DictionaryThreadPool {
    private  int threadNumbers;
    private  PoolDictionaryThreads[] threads;
    private  LinkedBlockingQueue queue;

    public DictionaryThreadPool(int threadNumbers) {
        this.threadNumbers = threadNumbers;
        queue = new LinkedBlockingQueue();
        threads = new PoolDictionaryThreads[threadNumbers];

        for (int i = 0; i < threadNumbers; i++) {
            threads[i] = new PoolDictionaryThreads();
            threads[i].start();
        }
    }

    public void execute(Runnable task) {
        synchronized (queue) {
            queue.add(task);
            queue.notify();
        }
    }

    private class PoolDictionaryThreads extends Thread {
        public void run() {
            Runnable task;

            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            System.out.println( e.getMessage());
                        }
                    }
                    task = (Runnable) queue.poll();
                }
                try {
                    task.run();
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
