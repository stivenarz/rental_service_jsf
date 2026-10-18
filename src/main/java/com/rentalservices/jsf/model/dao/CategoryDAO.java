package com.rentalservices.jsf.model.dao;

import com.rentalservices.jsf.model.entity.Category;
import com.rentalservices.jsf.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

/**
 * Gestiona el acceso a la tabla {@code categories} mediante JPA.
 *
 * @author Stiven Ariza Rodríguez
 */
public class CategoryDAO {

    public List<Category> findAll() {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Category c ORDER BY c.name", Category.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Category findById(Long id) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }

    /** Indica si otra categoría usa el nombre. La colación de MySQL ignora mayúsculas y acentos. */
    public boolean existsByNameExcludingId(String name, Long excludedId) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            Long total = em.createQuery(
                            "SELECT COUNT(c) FROM Category c WHERE c.name = :name AND (:id IS NULL OR c.id <> :id)",
                            Long.class)
                    .setParameter("name", name)
                    .setParameter("id", excludedId)
                    .getSingleResult();
            return total > 0;
        } finally {
            em.close();
        }
    }

    /** Inserta o actualiza una categoría dentro de una transacción. */
    public Category save(Category category) {
        EntityManager em = JpaUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Category saved;
            if (category.getId() == null) {
                em.persist(category);
                saved = category;
            } else {
                saved = em.merge(category);
            }
            tx.commit();
            return saved;
        } catch (RuntimeException e) {
            rollback(tx);
            throw e;
        } finally {
            em.close();
        }
    }

    public void deleteById(Long id) {
        EntityManager em = JpaUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Category category = em.find(Category.class, id);
            if (category != null) {
                em.remove(category);
            }
            tx.commit();
        } catch (RuntimeException e) {
            rollback(tx);
            throw e;
        } finally {
            em.close();
        }
    }

    private void rollback(EntityTransaction tx) {
        if (tx.isActive()) {
            tx.rollback();
        }
    }
}
