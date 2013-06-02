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
import model.Blogpost;
import model.Domain;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class BlogpostJpaController implements Serializable {

    public BlogpostJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Blogpost blogpost) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Domain idDomain = blogpost.getIdDomain();
            if (idDomain != null) {
                idDomain = em.getReference(idDomain.getClass(), idDomain.getIdDomain());
                blogpost.setIdDomain(idDomain);
            }
            em.persist(blogpost);
            if (idDomain != null) {
                idDomain.getBlogpostCollection().add(blogpost);
                idDomain = em.merge(idDomain);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBlogpost(blogpost.getIdBlogPost()) != null) {
                throw new PreexistingEntityException("Blogpost " + blogpost + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Blogpost blogpost) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Blogpost persistentBlogpost = em.find(Blogpost.class, blogpost.getIdBlogPost());
            Domain idDomainOld = persistentBlogpost.getIdDomain();
            Domain idDomainNew = blogpost.getIdDomain();
            if (idDomainNew != null) {
                idDomainNew = em.getReference(idDomainNew.getClass(), idDomainNew.getIdDomain());
                blogpost.setIdDomain(idDomainNew);
            }
            blogpost = em.merge(blogpost);
            if (idDomainOld != null && !idDomainOld.equals(idDomainNew)) {
                idDomainOld.getBlogpostCollection().remove(blogpost);
                idDomainOld = em.merge(idDomainOld);
            }
            if (idDomainNew != null && !idDomainNew.equals(idDomainOld)) {
                idDomainNew.getBlogpostCollection().add(blogpost);
                idDomainNew = em.merge(idDomainNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
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

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Blogpost blogpost;
            try {
                blogpost = em.getReference(Blogpost.class, id);
                blogpost.getIdBlogPost();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The blogpost with id " + id + " no longer exists.", enfe);
            }
            Domain idDomain = blogpost.getIdDomain();
            if (idDomain != null) {
                idDomain.getBlogpostCollection().remove(blogpost);
                idDomain = em.merge(idDomain);
            }
            em.remove(blogpost);
            em.getTransaction().commit();
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
}
