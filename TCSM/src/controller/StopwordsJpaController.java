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
import model.Stopwords;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class StopwordsJpaController implements Serializable {

    public StopwordsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Stopwords stopwords) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(stopwords);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findStopwords(stopwords.getIdStopWord()) != null) {
                throw new PreexistingEntityException("Stopwords " + stopwords + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Stopwords stopwords) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            stopwords = em.merge(stopwords);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = stopwords.getIdStopWord();
                if (findStopwords(id) == null) {
                    throw new NonexistentEntityException("The stopwords with id " + id + " no longer exists.");
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
            Stopwords stopwords;
            try {
                stopwords = em.getReference(Stopwords.class, id);
                stopwords.getIdStopWord();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The stopwords with id " + id + " no longer exists.", enfe);
            }
            em.remove(stopwords);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Stopwords> findStopwordsEntities() {
        return findStopwordsEntities(true, -1, -1);
    }

    public List<Stopwords> findStopwordsEntities(int maxResults, int firstResult) {
        return findStopwordsEntities(false, maxResults, firstResult);
    }

    private List<Stopwords> findStopwordsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Stopwords.class));
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

    public Stopwords findStopwords(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Stopwords.class, id);
        } finally {
            em.close();
        }
    }

    public int getStopwordsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Stopwords> rt = cq.from(Stopwords.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
