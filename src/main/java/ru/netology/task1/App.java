package ru.netology.task1;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Call center
 *
 */
public class App {
    private static final int CALLS_PER_PERIOD = 30;
    private static final int TIMEOUT_CALLS = 1_000;
    private static final int NUMBER_OF_PERIODS = 2;
    private static final int TIME_ANSWER = 1_000;
    private static final int NUMBER_OF_SPECIALISTS = 5;
    private static final Queue<Call> calls = new ConcurrentLinkedQueue<>();
    private static volatile boolean endCalls;


    public static void main( String[] args ) {
        Thread treadATE = new Thread(App::ATE, "Поток-АТС");
        ThreadGroup groupSpecialists = new ThreadGroup("группа специалистов");
        treadATE.start();

        for (int i = 0; i < NUMBER_OF_SPECIALISTS; i++) {
            new Thread(groupSpecialists, App::specialist, "Поток-специалист" + i).start();
        }
    }

    private static void ATE() {
        for (int i = 0; i < NUMBER_OF_PERIODS; i++) {
            for (int j = 0; j < CALLS_PER_PERIOD; j++) {
                calls.offer(new Call("+7 924 143 " + (j + i * CALLS_PER_PERIOD),
                        "User" + (j + i * CALLS_PER_PERIOD)));
            }
            System.out.println(Thread.currentThread().getName() + " отправил " + CALLS_PER_PERIOD
                    + " звонков");

            try {
                Thread.sleep(TIMEOUT_CALLS);
            } catch (InterruptedException e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }
        endCalls = true;
    }

    private static void specialist() {
        Call call;
        while (!endCalls) {
            while ((call = calls.poll()) != null) {
                System.out.println(call.getName() + " соединен с " +
                        Thread.currentThread().getName());
                try {
                    Thread.sleep(TIME_ANSWER);
                } catch (InterruptedException e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            }
        }
        System.out.println(Thread.currentThread().getName() + " завершил работу");
    }
}
