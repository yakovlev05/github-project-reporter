package ru.yakovlev05.infr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class App {
    private final Class<?> clazz;

    public App(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void run() {
        log.info("Application starting...");
        try {
            new AnnotationConfigApplicationContext(clazz);
        } catch (Exception e) {
            log.error("Application failed to start", e);
            System.exit(1);
        }
        log.info("Application started");
        lock();
    }

    private void lock() {
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
