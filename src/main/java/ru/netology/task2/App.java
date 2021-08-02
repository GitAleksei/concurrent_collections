package ru.netology.task2;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class App {
    private static ConcurrentMap<Integer, Integer> concurrentMap = new ConcurrentHashMap<>();
    private static Map<Integer, Integer> hashMap = Collections.synchronizedMap(new HashMap<>());
    private static final int LENGTH_OF_ARRAY = 20_000_000;
    private static final int BOUND = 1_000;
    private static final int NUMBER_OF_THREAD = 4;
    private static int[] array;

    public static void main(String[] args) {
        array = getRandomArray(LENGTH_OF_ARRAY, BOUND);
        System.out.println("Время в мс. заполнения ConcurrentHashMap = " +
                timeProcessing(App::putToConcurrentMap));

        System.out.println("Время в мс. чтения из ConcurrentHashMap = " +
                timeProcessing(App::getFromConcurrentMap));

        System.out.println("Время в мс. заполнения Collections.synchronizedMap = " +
                timeProcessing(App::putToHashMap));

        System.out.println("Время в мс. чтения из Collections.synchronizedMap = " +
                timeProcessing(App::getFromHashMap));
    }

    private static long timeProcessing(Runnable runnable) {
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_THREAD; i++) {
            threadList.add(new Thread(runnable));
        }

        long startTime = System.currentTimeMillis();
        threadList.forEach(Thread::start);
        threadList.forEach(e -> {
            try {
                e.join();
            } catch (InterruptedException ex) {
                System.out.println(Arrays.toString(ex.getStackTrace()));
            }
        });
        long finishTime = System.currentTimeMillis();

        return finishTime - startTime;
    }

    private static void putToHashMap() {
        for (int i = 0; i < array.length; i++) {
            hashMap.putIfAbsent(i, array[i]);
        }
    }

    private static void getFromHashMap() {
        for (int i = 0; i < concurrentMap.size(); i++) {
            hashMap.get(i);
        }
    }

    private static void putToConcurrentMap() {
        for (int i = 0; i < array.length; i++) {
            concurrentMap.putIfAbsent(i, array[i]);
        }
    }

    private static void getFromConcurrentMap() {
        for (int i = 0; i < concurrentMap.size(); i++) {
            concurrentMap.get(i);
        }
    }

    private static int[] getRandomArray(int length, int bound) {
        int[] array = new int[length];
        Random random = new Random();

        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(bound);
        }

        return array;
    }
}
