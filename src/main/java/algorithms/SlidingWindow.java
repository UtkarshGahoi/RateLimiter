package algorithms;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SlidingWindow implements RateLimiter {
    Queue<Long> queue;
    long timeWindowInSeconds;
    long bucketSize;

    public SlidingWindow(long bucketSize, long timeWindowInSeconds) {
        this.timeWindowInSeconds = timeWindowInSeconds;
        this.bucketSize = bucketSize;
        this.queue = new ConcurrentLinkedQueue<Long>();
    }

    @Override
    public synchronized boolean allowRequest() {
        Long currentTime = System.currentTimeMillis();
        checkAndUpdateWindow(currentTime);
        if(queue.size() < bucketSize){
            queue.offer(currentTime);
            return true;
        }
        return false;
    }
    // Will remove all timestamps which fall behind greater than 1 sec
    public void checkAndUpdateWindow(Long currentTime){
        if(queue.isEmpty()) return;

        long time = (currentTime - queue.peek())/1000;
        System.out.println("Time "+time);
        while (!queue.isEmpty() && time >= timeWindowInSeconds){
            queue.poll();
            System.out.println("QueuePeekTime "+queue.peek());
            if(!queue.isEmpty()) time = (currentTime - queue.peek())/1000;
        }
    }
}