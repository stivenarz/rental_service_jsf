package com.rentalservices.jsf.controller;

import com.rentalservices.jsf.exception.DuplicateCategoryException;
import com.rentalservices.jsf.model.entity.Category;
import com.rentalservices.jsf.model.service.CategoryService;
import com.rentalservices.jsf.util.FacesUtil;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlador JSF del módulo de categorías. Atiende las acciones de categorias/list.xhtml.
 *
 * @author Stiven Ariza Rodríguez
 */
@Named("categoryController")
@ViewScoped
public class CategoryController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Inject
    private CategoryService categoryService;

    private List<Category> categories;
    private Category categoriaAEliminar;
    private boolean modalAbierto;

    private Long editingId;
    private String name;
    private String description;
    private boolean active = true;

    public void init() {
        if (categories == null) {
            cargarLista();
        }
    }

    /** Limpia el formulario y abre el modal de creación. */
    public void nuevo() {
        limpiarFormulario();
        modalAbierto = true;
    }

    /** Carga la categoría en el formulario y abre el modal de edición. */
    public void editar(Category category) {
        this.editingId = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.active = category.isActive();
        this.modalAbierto = true;
    }

    /** Crea o actualiza la categoría y notifica el resultado. */
    public void guardar() {
        try {
            if (editingId == null) {
                Category creada = categoryService.crear(name, description);
                FacesUtil.success("Categoría creada",
                        "«" + creada.getName() + "» se guardó correctamente.");
            } else {
                Category actualizada = categoryService.actualizar(editingId, name, description, active);
                FacesUtil.success("Categoría actualizada",
                        "Los cambios de «" + actualizada.getName() + "» se guardaron.");
            }
            limpiarFormulario();
            cargarLista();
        } catch (DuplicateCategoryException e) {
            FacesUtil.warn("Nombre duplicado",
                    "Ya existe una categoría con ese nombre. Usa un nombre diferente.");
        } catch (IllegalArgumentException e) {
            FacesUtil.error("No se pudo guardar", e.getMessage());
        }
    }

    public void eliminar(Category category) {
        try {
            categoryService.eliminar(category.getId());
            FacesUtil.success("Categoría eliminada",
                    "«" + category.getName() + "» se eliminó de la lista.");
            if (category.getId().equals(editingId)) {
                limpiarFormulario();
            }
            cargarLista();
        } catch (IllegalArgumentException e) {
            FacesUtil.error("No se pudo eliminar", e.getMessage());
        }
    }

    public void cancelar() {
        limpiarFormulario();
    }

    /** Marca la categoría para eliminar y abre la confirmación. */
    public void solicitarEliminar(Category category) {
        this.categoriaAEliminar = category;
    }

    /** Elimina la categoría marcada. */
    public void confirmarEliminar() {
        if (categoriaAEliminar != null) {
            Category category = categoriaAEliminar;
            categoriaAEliminar = null;
            eliminar(category);
        }
    }

    public void cancelarEliminar() {
        categoriaAEliminar = null;
    }

    public boolean isConfirmandoEliminar() {
        return categoriaAEliminar != null;
    }

    public String getNombreAEliminar() {
        return categoriaAEliminar == null ? "" : categoriaAEliminar.getName();
    }

    private void cargarLista() {
        categories = categoryService.listar();
    }

    private void limpiarFormulario() {
        editingId = null;
        name = null;
        description = null;
        active = true;
        modalAbierto = false;
    }

    public boolean isModalAbierto() {
        return modalAbierto;
    }

    public boolean isEditing() {
        return editingId != null;
    }

    public String formatearFecha(LocalDateTime fecha) {
        return fecha == null ? "" : fecha.format(FORMATO_FECHA);
    }

    public String getTitulo() {
        return isEditing() ? "Editar categoría" : "Nueva categoría";
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Long getEditingId() {
        return editingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
