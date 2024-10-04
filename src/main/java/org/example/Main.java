package org.example;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        AtomicBoolean found = new AtomicBoolean(false);

        char character = JOptionPane.showInputDialog("Ingresa una letra").charAt(0);
        Runnable thread1 = getThread1(character, found);

        int number = Integer.parseInt(JOptionPane.showInputDialog("Ingresa un numero"));
        Runnable thread2 = getThread2(number, found);

        executor.submit(thread1);
        executor.submit(thread2);
        executor.awaitTermination(30, TimeUnit.SECONDS);
        executor.shutdown();
    }

    private static Runnable getThread1(char character, AtomicBoolean found) {
        return () -> {
            try {
                Thread.currentThread().setName("Thread 1");
                for (char i = 'A'; i <= character ; i++) {
                    Thread.sleep(500);
                    System.out.println(i);
                    if(i == character){
                        found.set(true);
                        System.out.println("Letra " + i + " encontrada");
                        System.out.println("Trabajo del hilo " + Thread.currentThread().getName() + " terminado");
                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        };
    }
    private static Runnable getThread2(int number, AtomicBoolean found) {
        AtomicInteger atomicInteger = new AtomicInteger(number);
        return () -> {
            try {
                Thread.currentThread().setName("Thread 2");
                while(!found.get()){
                    Thread.sleep(600);
                    System.out.println(atomicInteger.getAndDecrement());
                }
                System.out.println("Trabajo del hilo " + Thread.currentThread().getName() + " terminado");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        };
    }
}