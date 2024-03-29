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
import model.Blogroll;
import model.Domain;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class BlogrollJpaController implements Serializable {

    public BlogrollJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Blogroll blogroll) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(blogroll);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBlogroll(blogroll.getIdBlogRoll()) != null) {
                throw new PreexistingEntityException("Blogroll " + blogroll + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Blogroll blogroll) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            blogroll = em.merge(blogroll);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = blogroll.getIdBlogRoll();
                if (findBlogroll(id) == null) {
                    throw new NonexistentEntityException("The blogroll with id " + id + " no longer exists.");
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
            Blogroll blogroll;
            try {
                blogroll = em.getReference(Blogroll.class, id);
                blogroll.getIdBlogRoll();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The blogroll with id " + id + " no longer exists.", enfe);
            }
            em.remove(blogroll);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Blogroll> findBlogrollEntities() {
        return findBlogrollEntities(true, -1, -1);
    }

    public List<Blogroll> findBlogrollEntities(int maxResults, int firstResult) {
        return findBlogrollEntities(false, maxResults, firstResult);
    }

    private List<Blogroll> findBlogrollEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Blogroll.class));
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

    public Blogroll findBlogroll(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Blogroll.class, id);
        } finally {
            em.close();
        }
    }

    public int getBlogrollCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Blogroll> rt = cq.from(Blogroll.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Blogroll> findByIdDomain(Domain domain) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Blogroll.findByIddomain");
        q.setParameter("idDomain", domain);

        try {
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public Blogroll findByDomainAndName(Domain dom, String blog) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Blogroll.findByDomainBlog");
        q.setParameter("idDomain", dom);
        q.setParameter("blog", blog);

        try {
            return (Blogroll) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
