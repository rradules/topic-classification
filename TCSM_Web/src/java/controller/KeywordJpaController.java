/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import model.Category;
import model.Keyword;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class KeywordJpaController implements Serializable {

    public KeywordJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Keyword keyword) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
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
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Keyword keyword) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
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
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
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

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
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
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
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
