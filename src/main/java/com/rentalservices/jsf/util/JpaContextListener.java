package com.rentalservices.jsf.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Inicia JPA al desplegar la aplicación y lo cierra al detenerla.
 *
 * @author Stiven Ariza Rodríguez
 */
@WebListener
public class JpaContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        JpaUtil.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JpaUtil.close();
    }
}
