package ru.yakovlev05.infr;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {


    private final Class<?> clazz;

    public App(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void run() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(clazz);

        //todo: логирование
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
