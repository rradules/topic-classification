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
import model.Stopword;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class StopwordJpaController implements Serializable {

    public StopwordJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Stopword stopword) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(stopword);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findStopword(stopword.getIdStopWord()) != null) {
                throw new PreexistingEntityException("Stopword " + stopword + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Stopword stopword) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            stopword = em.merge(stopword);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = stopword.getIdStopWord();
                if (findStopword(id) == null) {
                    throw new NonexistentEntityException("The stopword with id " + id + " no longer exists.");
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
            Stopword stopword;
            try {
                stopword = em.getReference(Stopword.class, id);
                stopword.getIdStopWord();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The stopword with id " + id + " no longer exists.", enfe);
            }
            em.remove(stopword);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Stopword> findStopwordEntities() {
        return findStopwordEntities(true, -1, -1);
    }

    public List<Stopword> findStopwordEntities(int maxResults, int firstResult) {
        return findStopwordEntities(false, maxResults, firstResult);
    }

    private List<Stopword> findStopwordEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Stopword.class));
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

    public Stopword findStopword(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Stopword.class, id);
        } finally {
            em.close();
        }
    }

    public int getStopwordCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Stopword> rt = cq.from(Stopword.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
