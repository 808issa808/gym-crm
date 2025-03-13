package org.epam;

import org.epam.config.DataSourceConfig;
import org.epam.config.HibernateConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(DataSourceConfig.class, HibernateConfig.class);
    }
}
