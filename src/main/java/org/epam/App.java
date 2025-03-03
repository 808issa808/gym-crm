package org.epam;

import org.epam.data.Storage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        System.out.println("Starting Spring application...");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            Storage dataLoader = context.getBean(Storage.class);
            System.out.println("DataLoader initialized.");
        }
    }
}