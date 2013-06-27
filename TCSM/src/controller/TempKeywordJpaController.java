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
import model.TempKeyword;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class TempKeywordJpaController implements Serializable {

    public TempKeywordJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TempKeyword tempKeyword) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Category idCategory = tempKeyword.getIdCategory();
            if (idCategory != null) {
                idCategory = em.getReference(idCategory.getClass(), idCategory.getIdCategory());
                tempKeyword.setIdCategory(idCategory);
            }
            em.persist(tempKeyword);
            if (idCategory != null) {
                idCategory.getTempKeywordsCollection().add(tempKeyword);
                idCategory = em.merge(idCategory);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TempKeyword tempKeyword) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TempKeyword persistentTempKeyword = em.find(TempKeyword.class, tempKeyword.getIdTempKeyword());
            Category idCategoryOld = persistentTempKeyword.getIdCategory();
            Category idCategoryNew = tempKeyword.getIdCategory();
            if (idCategoryNew != null) {
                idCategoryNew = em.getReference(idCategoryNew.getClass(), idCategoryNew.getIdCategory());
                tempKeyword.setIdCategory(idCategoryNew);
            }
            tempKeyword = em.merge(tempKeyword);
            if (idCategoryOld != null && !idCategoryOld.equals(idCategoryNew)) {
                idCategoryOld.getTempKeywordsCollection().remove(tempKeyword);
                idCategoryOld = em.merge(idCategoryOld);
            }
            if (idCategoryNew != null && !idCategoryNew.equals(idCategoryOld)) {
                idCategoryNew.getTempKeywordsCollection().add(tempKeyword);
                idCategoryNew = em.merge(idCategoryNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tempKeyword.getIdTempKeyword();
                if (findTempKeyword(id) == null) {
                    throw new NonexistentEntityException("The tempKeyword with id " + id + " no longer exists.");
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
            TempKeyword tempKeyword;
            try {
                tempKeyword = em.getReference(TempKeyword.class, id);
                tempKeyword.getIdTempKeyword();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tempKeyword with id " + id + " no longer exists.", enfe);
            }
            Category idCategory = tempKeyword.getIdCategory();
            if (idCategory != null) {
                idCategory.getTempKeywordsCollection().remove(tempKeyword);
                idCategory = em.merge(idCategory);
            }
            em.remove(tempKeyword);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TempKeyword> findTempKeywordEntities() {
        return findTempKeywordEntities(true, -1, -1);
    }

    public List<TempKeyword> findTempKeywordEntities(int maxResults, int firstResult) {
        return findTempKeywordEntities(false, maxResults, firstResult);
    }

    private List<TempKeyword> findTempKeywordEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TempKeyword.class));
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

    public TempKeyword findTempKeyword(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TempKeyword.class, id);
        } finally {
            em.close();
        }
    }

    public int getTempKeywordCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TempKeyword> rt = cq.from(TempKeyword.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
