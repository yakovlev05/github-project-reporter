package ru.yakovlev05.infr;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class Main {
    static void main() {
        new App(Main.class).run();
    }
}
