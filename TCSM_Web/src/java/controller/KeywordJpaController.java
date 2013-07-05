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
import model.Keyword;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class KeywordJpaController implements Serializable {

    public KeywordJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Keyword keyword) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Category idCategory = keyword.getIdCategory();
            if (idCategory != null) {
                idCategory = em.getReference(idCategory.getClass(), idCategory.getIdCategory());
                keyword.setIdCategory(idCategory);
            }
            em.persist(keyword);
            if (idCategory != null) {
                idCategory.getKeywordCollection().add(keyword);
                idCategory = em.merge(idCategory);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Keyword keyword) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Keyword persistentKeyword = em.find(Keyword.class, keyword.getIdKeyword());
            Category idCategoryOld = persistentKeyword.getIdCategory();
            Category idCategoryNew = keyword.getIdCategory();
            if (idCategoryNew != null) {
                idCategoryNew = em.getReference(idCategoryNew.getClass(), idCategoryNew.getIdCategory());
                keyword.setIdCategory(idCategoryNew);
            }
            keyword = em.merge(keyword);
            if (idCategoryOld != null && !idCategoryOld.equals(idCategoryNew)) {
                idCategoryOld.getKeywordCollection().remove(keyword);
                idCategoryOld = em.merge(idCategoryOld);
            }
            if (idCategoryNew != null && !idCategoryNew.equals(idCategoryOld)) {
                idCategoryNew.getKeywordCollection().add(keyword);
                idCategoryNew = em.merge(idCategoryNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = keyword.getIdKeyword();
                if (findKeyword(id) == null) {
                    throw new NonexistentEntityException("The keyword with id " + id + " no longer exists.");
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
            Keyword keyword;
            try {
                keyword = em.getReference(Keyword.class, id);
                keyword.getIdKeyword();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The keyword with id " + id + " no longer exists.", enfe);
            }
            Category idCategory = keyword.getIdCategory();
            if (idCategory != null) {
                idCategory.getKeywordCollection().remove(keyword);
                idCategory = em.merge(idCategory);
            }
            em.remove(keyword);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Keyword> findKeywordEntities() {
        return findKeywordEntities(true, -1, -1);
    }

    public List<Keyword> findKeywordEntities(int maxResults, int firstResult) {
        return findKeywordEntities(false, maxResults, firstResult);
    }

    private List<Keyword> findKeywordEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Keyword.class));
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

    public Keyword findKeyword(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Keyword.class, id);
        } finally {
            em.close();
        }
    }

    public int getKeywordCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Keyword> rt = cq.from(Keyword.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Keyword> findByKeyword(String keyword) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Keyword.findByKeyword");
        q.setParameter("keyword", keyword);

        try {
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Keyword> findByCategory(Category categ) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Keyword.findByCategory");
        q.setParameter("idCategory", categ);

        try {
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
