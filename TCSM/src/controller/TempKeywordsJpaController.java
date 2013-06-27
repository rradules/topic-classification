/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Category;
import model.TempKeywords;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class TempKeywordsJpaController implements Serializable {

    public TempKeywordsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TempKeywords tempKeywords) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Category idCategory = tempKeywords.getIdCategory();
            if (idCategory != null) {
                idCategory = em.getReference(idCategory.getClass(), idCategory.getIdCategory());
                tempKeywords.setIdCategory(idCategory);
            }
            em.persist(tempKeywords);
            if (idCategory != null) {
                idCategory.getTempKeywordsCollection().add(tempKeywords);
                idCategory = em.merge(idCategory);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TempKeywords tempKeywords) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TempKeywords persistentTempKeywords = em.find(TempKeywords.class, tempKeywords.getIdTempKeyword());
            Category idCategoryOld = persistentTempKeywords.getIdCategory();
            Category idCategoryNew = tempKeywords.getIdCategory();
            if (idCategoryNew != null) {
                idCategoryNew = em.getReference(idCategoryNew.getClass(), idCategoryNew.getIdCategory());
                tempKeywords.setIdCategory(idCategoryNew);
            }
            tempKeywords = em.merge(tempKeywords);
            if (idCategoryOld != null && !idCategoryOld.equals(idCategoryNew)) {
                idCategoryOld.getTempKeywordsCollection().remove(tempKeywords);
                idCategoryOld = em.merge(idCategoryOld);
            }
            if (idCategoryNew != null && !idCategoryNew.equals(idCategoryOld)) {
                idCategoryNew.getTempKeywordsCollection().add(tempKeywords);
                idCategoryNew = em.merge(idCategoryNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tempKeywords.getIdTempKeyword();
                if (findTempKeywords(id) == null) {
                    throw new NonexistentEntityException("The tempKeywords with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TempKeywords tempKeywords;
            try {
                tempKeywords = em.getReference(TempKeywords.class, id);
                tempKeywords.getIdTempKeyword();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tempKeywords with id " + id + " no longer exists.", enfe);
            }
            Category idCategory = tempKeywords.getIdCategory();
            if (idCategory != null) {
                idCategory.getTempKeywordsCollection().remove(tempKeywords);
                idCategory = em.merge(idCategory);
            }
            em.remove(tempKeywords);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TempKeywords> findTempKeywordsEntities() {
        return findTempKeywordsEntities(true, -1, -1);
    }

    public List<TempKeywords> findTempKeywordsEntities(int maxResults, int firstResult) {
        return findTempKeywordsEntities(false, maxResults, firstResult);
    }

    private List<TempKeywords> findTempKeywordsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TempKeywords.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public TempKeywords findTempKeywords(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TempKeywords.class, id);
        } finally {
            em.close();
        }
    }

    public int getTempKeywordsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TempKeywords> rt = cq.from(TempKeywords.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
