/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Location;
import model.Blogroll;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Blogpost;
import model.Domain;
import model.Rawdata;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class DomainJpaController implements Serializable {

    public DomainJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Domain domain) throws PreexistingEntityException, Exception {
        if (domain.getBlogrollCollection() == null) {
            domain.setBlogrollCollection(new ArrayList<Blogroll>());
        }
        if (domain.getBlogpostCollection() == null) {
            domain.setBlogpostCollection(new ArrayList<Blogpost>());
        }
        if (domain.getRawdataCollection() == null) {
            domain.setRawdataCollection(new ArrayList<Rawdata>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Location idLocation = domain.getIdLocation();
            if (idLocation != null) {
                idLocation = em.getReference(idLocation.getClass(), idLocation.getIdLocation());
                domain.setIdLocation(idLocation);
            }
            Collection<Blogroll> attachedBlogrollCollection = new ArrayList<Blogroll>();
            for (Blogroll blogrollCollectionBlogrollToAttach : domain.getBlogrollCollection()) {
                blogrollCollectionBlogrollToAttach = em.getReference(blogrollCollectionBlogrollToAttach.getClass(), blogrollCollectionBlogrollToAttach.getIdBlogRoll());
                attachedBlogrollCollection.add(blogrollCollectionBlogrollToAttach);
            }
            domain.setBlogrollCollection(attachedBlogrollCollection);
            Collection<Blogpost> attachedBlogpostCollection = new ArrayList<Blogpost>();
            for (Blogpost blogpostCollectionBlogpostToAttach : domain.getBlogpostCollection()) {
                blogpostCollectionBlogpostToAttach = em.getReference(blogpostCollectionBlogpostToAttach.getClass(), blogpostCollectionBlogpostToAttach.getIdBlogPost());
                attachedBlogpostCollection.add(blogpostCollectionBlogpostToAttach);
            }
            domain.setBlogpostCollection(attachedBlogpostCollection);
            Collection<Rawdata> attachedRawdataCollection = new ArrayList<Rawdata>();
            for (Rawdata rawdataCollectionRawdataToAttach : domain.getRawdataCollection()) {
                rawdataCollectionRawdataToAttach = em.getReference(rawdataCollectionRawdataToAttach.getClass(), rawdataCollectionRawdataToAttach.getIdRawData());
                attachedRawdataCollection.add(rawdataCollectionRawdataToAttach);
            }
            domain.setRawdataCollection(attachedRawdataCollection);
            em.persist(domain);
            if (idLocation != null) {
                idLocation.getDomainCollection().add(domain);
                idLocation = em.merge(idLocation);
            }
            for (Blogroll blogrollCollectionBlogroll : domain.getBlogrollCollection()) {
                Domain oldIdDomainOfBlogrollCollectionBlogroll = blogrollCollectionBlogroll.getIdDomain();
                blogrollCollectionBlogroll.setIdDomain(domain);
                blogrollCollectionBlogroll = em.merge(blogrollCollectionBlogroll);
                if (oldIdDomainOfBlogrollCollectionBlogroll != null) {
                    oldIdDomainOfBlogrollCollectionBlogroll.getBlogrollCollection().remove(blogrollCollectionBlogroll);
                    oldIdDomainOfBlogrollCollectionBlogroll = em.merge(oldIdDomainOfBlogrollCollectionBlogroll);
                }
            }
            for (Blogpost blogpostCollectionBlogpost : domain.getBlogpostCollection()) {
                Domain oldIdDomainOfBlogpostCollectionBlogpost = blogpostCollectionBlogpost.getIdDomain();
                blogpostCollectionBlogpost.setIdDomain(domain);
                blogpostCollectionBlogpost = em.merge(blogpostCollectionBlogpost);
                if (oldIdDomainOfBlogpostCollectionBlogpost != null) {
                    oldIdDomainOfBlogpostCollectionBlogpost.getBlogpostCollection().remove(blogpostCollectionBlogpost);
                    oldIdDomainOfBlogpostCollectionBlogpost = em.merge(oldIdDomainOfBlogpostCollectionBlogpost);
                }
            }
            for (Rawdata rawdataCollectionRawdata : domain.getRawdataCollection()) {
                Domain oldIdDomainOfRawdataCollectionRawdata = rawdataCollectionRawdata.getIdDomain();
                rawdataCollectionRawdata.setIdDomain(domain);
                rawdataCollectionRawdata = em.merge(rawdataCollectionRawdata);
                if (oldIdDomainOfRawdataCollectionRawdata != null) {
                    oldIdDomainOfRawdataCollectionRawdata.getRawdataCollection().remove(rawdataCollectionRawdata);
                    oldIdDomainOfRawdataCollectionRawdata = em.merge(oldIdDomainOfRawdataCollectionRawdata);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDomain(domain.getIdDomain()) != null) {
                throw new PreexistingEntityException("Domain " + domain + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Domain domain) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Domain persistentDomain = em.find(Domain.class, domain.getIdDomain());
            Location idLocationOld = persistentDomain.getIdLocation();
            Location idLocationNew = domain.getIdLocation();
            Collection<Blogroll> blogrollCollectionOld = persistentDomain.getBlogrollCollection();
            Collection<Blogroll> blogrollCollectionNew = domain.getBlogrollCollection();
            Collection<Blogpost> blogpostCollectionOld = persistentDomain.getBlogpostCollection();
            Collection<Blogpost> blogpostCollectionNew = domain.getBlogpostCollection();
            Collection<Rawdata> rawdataCollectionOld = persistentDomain.getRawdataCollection();
            Collection<Rawdata> rawdataCollectionNew = domain.getRawdataCollection();
            if (idLocationNew != null) {
                idLocationNew = em.getReference(idLocationNew.getClass(), idLocationNew.getIdLocation());
                domain.setIdLocation(idLocationNew);
            }
            Collection<Blogroll> attachedBlogrollCollectionNew = new ArrayList<Blogroll>();
            for (Blogroll blogrollCollectionNewBlogrollToAttach : blogrollCollectionNew) {
                blogrollCollectionNewBlogrollToAttach = em.getReference(blogrollCollectionNewBlogrollToAttach.getClass(), blogrollCollectionNewBlogrollToAttach.getIdBlogRoll());
                attachedBlogrollCollectionNew.add(blogrollCollectionNewBlogrollToAttach);
            }
            blogrollCollectionNew = attachedBlogrollCollectionNew;
            domain.setBlogrollCollection(blogrollCollectionNew);
            Collection<Blogpost> attachedBlogpostCollectionNew = new ArrayList<Blogpost>();
            for (Blogpost blogpostCollectionNewBlogpostToAttach : blogpostCollectionNew) {
                blogpostCollectionNewBlogpostToAttach = em.getReference(blogpostCollectionNewBlogpostToAttach.getClass(), blogpostCollectionNewBlogpostToAttach.getIdBlogPost());
                attachedBlogpostCollectionNew.add(blogpostCollectionNewBlogpostToAttach);
            }
            blogpostCollectionNew = attachedBlogpostCollectionNew;
            domain.setBlogpostCollection(blogpostCollectionNew);
            Collection<Rawdata> attachedRawdataCollectionNew = new ArrayList<Rawdata>();
            for (Rawdata rawdataCollectionNewRawdataToAttach : rawdataCollectionNew) {
                rawdataCollectionNewRawdataToAttach = em.getReference(rawdataCollectionNewRawdataToAttach.getClass(), rawdataCollectionNewRawdataToAttach.getIdRawData());
                attachedRawdataCollectionNew.add(rawdataCollectionNewRawdataToAttach);
            }
            rawdataCollectionNew = attachedRawdataCollectionNew;
            domain.setRawdataCollection(rawdataCollectionNew);
            domain = em.merge(domain);
            if (idLocationOld != null && !idLocationOld.equals(idLocationNew)) {
                idLocationOld.getDomainCollection().remove(domain);
                idLocationOld = em.merge(idLocationOld);
            }
            if (idLocationNew != null && !idLocationNew.equals(idLocationOld)) {
                idLocationNew.getDomainCollection().add(domain);
                idLocationNew = em.merge(idLocationNew);
            }
            for (Blogroll blogrollCollectionOldBlogroll : blogrollCollectionOld) {
                if (!blogrollCollectionNew.contains(blogrollCollectionOldBlogroll)) {
                    blogrollCollectionOldBlogroll.setIdDomain(null);
                    blogrollCollectionOldBlogroll = em.merge(blogrollCollectionOldBlogroll);
                }
            }
            for (Blogroll blogrollCollectionNewBlogroll : blogrollCollectionNew) {
                if (!blogrollCollectionOld.contains(blogrollCollectionNewBlogroll)) {
                    Domain oldIdDomainOfBlogrollCollectionNewBlogroll = blogrollCollectionNewBlogroll.getIdDomain();
                    blogrollCollectionNewBlogroll.setIdDomain(domain);
                    blogrollCollectionNewBlogroll = em.merge(blogrollCollectionNewBlogroll);
                    if (oldIdDomainOfBlogrollCollectionNewBlogroll != null && !oldIdDomainOfBlogrollCollectionNewBlogroll.equals(domain)) {
                        oldIdDomainOfBlogrollCollectionNewBlogroll.getBlogrollCollection().remove(blogrollCollectionNewBlogroll);
                        oldIdDomainOfBlogrollCollectionNewBlogroll = em.merge(oldIdDomainOfBlogrollCollectionNewBlogroll);
                    }
                }
            }
            for (Blogpost blogpostCollectionOldBlogpost : blogpostCollectionOld) {
                if (!blogpostCollectionNew.contains(blogpostCollectionOldBlogpost)) {
                    blogpostCollectionOldBlogpost.setIdDomain(null);
                    blogpostCollectionOldBlogpost = em.merge(blogpostCollectionOldBlogpost);
                }
            }
            for (Blogpost blogpostCollectionNewBlogpost : blogpostCollectionNew) {
                if (!blogpostCollectionOld.contains(blogpostCollectionNewBlogpost)) {
                    Domain oldIdDomainOfBlogpostCollectionNewBlogpost = blogpostCollectionNewBlogpost.getIdDomain();
                    blogpostCollectionNewBlogpost.setIdDomain(domain);
                    blogpostCollectionNewBlogpost = em.merge(blogpostCollectionNewBlogpost);
                    if (oldIdDomainOfBlogpostCollectionNewBlogpost != null && !oldIdDomainOfBlogpostCollectionNewBlogpost.equals(domain)) {
                        oldIdDomainOfBlogpostCollectionNewBlogpost.getBlogpostCollection().remove(blogpostCollectionNewBlogpost);
                        oldIdDomainOfBlogpostCollectionNewBlogpost = em.merge(oldIdDomainOfBlogpostCollectionNewBlogpost);
                    }
                }
            }
            for (Rawdata rawdataCollectionOldRawdata : rawdataCollectionOld) {
                if (!rawdataCollectionNew.contains(rawdataCollectionOldRawdata)) {
                    rawdataCollectionOldRawdata.setIdDomain(null);
                    rawdataCollectionOldRawdata = em.merge(rawdataCollectionOldRawdata);
                }
            }
            for (Rawdata rawdataCollectionNewRawdata : rawdataCollectionNew) {
                if (!rawdataCollectionOld.contains(rawdataCollectionNewRawdata)) {
                    Domain oldIdDomainOfRawdataCollectionNewRawdata = rawdataCollectionNewRawdata.getIdDomain();
                    rawdataCollectionNewRawdata.setIdDomain(domain);
                    rawdataCollectionNewRawdata = em.merge(rawdataCollectionNewRawdata);
                    if (oldIdDomainOfRawdataCollectionNewRawdata != null && !oldIdDomainOfRawdataCollectionNewRawdata.equals(domain)) {
                        oldIdDomainOfRawdataCollectionNewRawdata.getRawdataCollection().remove(rawdataCollectionNewRawdata);
                        oldIdDomainOfRawdataCollectionNewRawdata = em.merge(oldIdDomainOfRawdataCollectionNewRawdata);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = domain.getIdDomain();
                if (findDomain(id) == null) {
                    throw new NonexistentEntityException("The domain with id " + id + " no longer exists.");
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
            Domain domain;
            try {
                domain = em.getReference(Domain.class, id);
                domain.getIdDomain();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The domain with id " + id + " no longer exists.", enfe);
            }
            Location idLocation = domain.getIdLocation();
            if (idLocation != null) {
                idLocation.getDomainCollection().remove(domain);
                idLocation = em.merge(idLocation);
            }
            Collection<Blogroll> blogrollCollection = domain.getBlogrollCollection();
            for (Blogroll blogrollCollectionBlogroll : blogrollCollection) {
                blogrollCollectionBlogroll.setIdDomain(null);
                blogrollCollectionBlogroll = em.merge(blogrollCollectionBlogroll);
            }
            Collection<Blogpost> blogpostCollection = domain.getBlogpostCollection();
            for (Blogpost blogpostCollectionBlogpost : blogpostCollection) {
                blogpostCollectionBlogpost.setIdDomain(null);
                blogpostCollectionBlogpost = em.merge(blogpostCollectionBlogpost);
            }
            Collection<Rawdata> rawdataCollection = domain.getRawdataCollection();
            for (Rawdata rawdataCollectionRawdata : rawdataCollection) {
                rawdataCollectionRawdata.setIdDomain(null);
                rawdataCollectionRawdata = em.merge(rawdataCollectionRawdata);
            }
            em.remove(domain);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Domain> findDomainEntities() {
        return findDomainEntities(true, -1, -1);
    }

    public List<Domain> findDomainEntities(int maxResults, int firstResult) {
        return findDomainEntities(false, maxResults, firstResult);
    }

    private List<Domain> findDomainEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Domain.class));
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

    public Domain findDomain(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Domain.class, id);
        } finally {
            em.close();
        }
    }

    public int getDomainCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Domain> rt = cq.from(Domain.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
