package com.rentalservices.jsf.util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

/**
 * Registra notificaciones para la vista. Cada una tiene título y detalle, y se muestra como toast.
 *
 * @author Stiven Ariza Rodríguez
 */
public final class FacesUtil {

    private FacesUtil() {
    }

    public static void success(String title, String detail) {
        add(FacesMessage.SEVERITY_INFO, title, detail);
    }

    public static void warn(String title, String detail) {
        add(FacesMessage.SEVERITY_WARN, title, detail);
    }

    public static void error(String title, String detail) {
        add(FacesMessage.SEVERITY_ERROR, title, detail);
    }

    private static void add(FacesMessage.Severity severity, String title, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, title, detail));
    }
}
