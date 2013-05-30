/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Categories;
import model.Keywords;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class KeywordsJpaController implements Serializable {

    public KeywordsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Keywords keywords) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categories idCategory = keywords.getIdCategory();
            if (idCategory != null) {
                idCategory = em.getReference(idCategory.getClass(), idCategory.getIdCategory());
                keywords.setIdCategory(idCategory);
            }
            em.persist(keywords);
            if (idCategory != null) {
                idCategory.getKeywordsCollection().add(keywords);
                idCategory = em.merge(idCategory);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findKeywords(keywords.getIdKeyword()) != null) {
                throw new PreexistingEntityException("Keywords " + keywords + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Keywords keywords) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Keywords persistentKeywords = em.find(Keywords.class, keywords.getIdKeyword());
            Categories idCategoryOld = persistentKeywords.getIdCategory();
            Categories idCategoryNew = keywords.getIdCategory();
            if (idCategoryNew != null) {
                idCategoryNew = em.getReference(idCategoryNew.getClass(), idCategoryNew.getIdCategory());
                keywords.setIdCategory(idCategoryNew);
            }
            keywords = em.merge(keywords);
            if (idCategoryOld != null && !idCategoryOld.equals(idCategoryNew)) {
                idCategoryOld.getKeywordsCollection().remove(keywords);
                idCategoryOld = em.merge(idCategoryOld);
            }
            if (idCategoryNew != null && !idCategoryNew.equals(idCategoryOld)) {
                idCategoryNew.getKeywordsCollection().add(keywords);
                idCategoryNew = em.merge(idCategoryNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = keywords.getIdKeyword();
                if (findKeywords(id) == null) {
                    throw new NonexistentEntityException("The keywords with id " + id + " no longer exists.");
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
            Keywords keywords;
            try {
                keywords = em.getReference(Keywords.class, id);
                keywords.getIdKeyword();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The keywords with id " + id + " no longer exists.", enfe);
            }
            Categories idCategory = keywords.getIdCategory();
            if (idCategory != null) {
                idCategory.getKeywordsCollection().remove(keywords);
                idCategory = em.merge(idCategory);
            }
            em.remove(keywords);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Keywords> findKeywordsEntities() {
        return findKeywordsEntities(true, -1, -1);
    }

    public List<Keywords> findKeywordsEntities(int maxResults, int firstResult) {
        return findKeywordsEntities(false, maxResults, firstResult);
    }

    private List<Keywords> findKeywordsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Keywords.class));
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

    public Keywords findKeywords(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Keywords.class, id);
        } finally {
            em.close();
        }
    }

    public int getKeywordsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Keywords> rt = cq.from(Keywords.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
