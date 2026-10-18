package com.rentalservices.jsf.exception;

/**
 * Indica que ya existe una categoría con el mismo nombre.
 *
 * @author Stiven Ariza Rodríguez
 */
public class DuplicateCategoryException extends RuntimeException {

    public DuplicateCategoryException(String name) {
        super("Ya existe una categoría con el nombre: " + name);
    }
}
