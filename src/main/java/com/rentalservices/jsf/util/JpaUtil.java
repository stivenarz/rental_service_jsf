package com.rentalservices.jsf.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

/**
 * Crea el {@code EntityManagerFactory} único de la aplicación. Lee DB_USERNAME y DB_PASSWORD del entorno.
 *
 * @author Stiven Ariza Rodríguez
 */
public final class JpaUtil {

    private static final String PERSISTENCE_UNIT = "rentalServicesPU";
    private static EntityManagerFactory emf;

    private JpaUtil() {
    }

    public static synchronized void init() {
        if (emf != null) {
            return;
        }

        Map<String, String> overrides = new HashMap<>();
        overrides.put("jakarta.persistence.jdbc.user", env("DB_USERNAME", "root"));
        overrides.put("jakarta.persistence.jdbc.password", env("DB_PASSWORD", ""));

        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, overrides);
    }

    public static EntityManager createEntityManager() {
        if (emf == null) {
            init();
        }
        return emf.createEntityManager();
    }

    public static synchronized void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        emf = null;
    }

    private static String env(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null ? defaultValue : value;
    }
}
