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
import model.Blogpost;
import model.Domain;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class BlogpostJpaController implements Serializable {

    public BlogpostJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Blogpost blogpost) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(blogpost);
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

    public void edit(Blogpost blogpost) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            blogpost = em.merge(blogpost);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = blogpost.getIdBlogPost();
                if (findBlogpost(id) == null) {
                    throw new NonexistentEntityException("The blogpost with id " + id + " no longer exists.");
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
            Blogpost blogpost;
            try {
                blogpost = em.getReference(Blogpost.class, id);
                blogpost.getIdBlogPost();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The blogpost with id " + id + " no longer exists.", enfe);
            }
            em.remove(blogpost);
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

    public List<Blogpost> findBlogpostEntities() {
        return findBlogpostEntities(true, -1, -1);
    }

    public List<Blogpost> findBlogpostEntities(int maxResults, int firstResult) {
        return findBlogpostEntities(false, maxResults, firstResult);
    }

    private List<Blogpost> findBlogpostEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Blogpost.class));
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

    public Blogpost findBlogpost(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Blogpost.class, id);
        } finally {
            em.close();
        }
    }

    public int getBlogpostCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Blogpost> rt = cq.from(Blogpost.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Blogpost findByPageAddress(String address) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Blogpost.findByPageAddress");
        q.setParameter("pageAddress", address);

        try {
            return (Blogpost) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Blogpost> findByDomainCategory(Domain domain) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Blogpost.findByDomain");
        q.setParameter("idDomain", domain);

        try {
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Blogpost> findByProcessed(int proc) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Blogpost.findByProcessed");
        q.setParameter("processed", proc);

        try {
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
