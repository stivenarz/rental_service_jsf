package com.rentalservices.jsf.model.service;

import com.rentalservices.jsf.exception.DuplicateCategoryException;
import com.rentalservices.jsf.model.dao.CategoryDAO;
import com.rentalservices.jsf.model.entity.Category;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.Serializable;
import java.util.List;

/**
 * Aplica las reglas de negocio del módulo de categorías.
 *
 * @author Stiven Ariza Rodríguez
 */
@ApplicationScoped
public class CategoryService implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int NAME_MAX = 100;
    private static final int DESCRIPTION_MAX = 500;

    private final CategoryDAO categoryDAO = new CategoryDAO();

    public List<Category> listar() {
        return categoryDAO.findAll();
    }

    public Category buscarPorId(Long id) {
        Category category = categoryDAO.findById(id);
        if (category == null) {
            throw new IllegalArgumentException("La categoría no existe.");
        }
        return category;
    }

    /** Valida y crea una categoría. Lanza DuplicateCategoryException si el nombre ya existe. */
    public Category crear(String name, String description) {
        String cleanName = validarNombre(name);
        String cleanDescription = validarDescripcion(description);

        if (categoryDAO.existsByNameExcludingId(cleanName, null)) {
            throw new DuplicateCategoryException(cleanName);
        }

        Category category = new Category(cleanName, cleanDescription);
        return categoryDAO.save(category);
    }

    /** Valida y actualiza una categoría existente. */
    public Category actualizar(Long id, String name, String description, boolean active) {
        Category category = buscarPorId(id);

        String cleanName = validarNombre(name);
        String cleanDescription = validarDescripcion(description);

        if (categoryDAO.existsByNameExcludingId(cleanName, id)) {
            throw new DuplicateCategoryException(cleanName);
        }

        category.setName(cleanName);
        category.setDescription(cleanDescription);
        category.setActive(active);
        return categoryDAO.save(category);
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        categoryDAO.deleteById(id);
    }

    /** Recorta el nombre y verifica que no esté vacío ni supere 100 caracteres. */
    private String validarNombre(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        String clean = name.trim();
        if (clean.length() > NAME_MAX) {
            throw new IllegalArgumentException("El nombre no puede superar los " + NAME_MAX + " caracteres.");
        }
        return clean;
    }

    private String validarDescripcion(String description) {
        if (description == null) {
            return null;
        }
        String clean = description.trim();
        if (clean.length() > DESCRIPTION_MAX) {
            throw new IllegalArgumentException("La descripción no puede superar los " + DESCRIPTION_MAX + " caracteres.");
        }
        return clean.isEmpty() ? null : clean;
    }
}
