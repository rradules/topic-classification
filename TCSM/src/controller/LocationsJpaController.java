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
import model.Domain;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Locations;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class LocationsJpaController implements Serializable {

    public LocationsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Locations locations) throws PreexistingEntityException, Exception {
        if (locations.getDomainCollection() == null) {
            locations.setDomainCollection(new ArrayList<Domain>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Domain> attachedDomainCollection = new ArrayList<Domain>();
            for (Domain domainCollectionDomainToAttach : locations.getDomainCollection()) {
                domainCollectionDomainToAttach = em.getReference(domainCollectionDomainToAttach.getClass(), domainCollectionDomainToAttach.getIdDomain());
                attachedDomainCollection.add(domainCollectionDomainToAttach);
            }
            locations.setDomainCollection(attachedDomainCollection);
            em.persist(locations);
            for (Domain domainCollectionDomain : locations.getDomainCollection()) {
                Locations oldIdLocationOfDomainCollectionDomain = domainCollectionDomain.getIdLocation();
                domainCollectionDomain.setIdLocation(locations);
                domainCollectionDomain = em.merge(domainCollectionDomain);
                if (oldIdLocationOfDomainCollectionDomain != null) {
                    oldIdLocationOfDomainCollectionDomain.getDomainCollection().remove(domainCollectionDomain);
                    oldIdLocationOfDomainCollectionDomain = em.merge(oldIdLocationOfDomainCollectionDomain);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLocations(locations.getIdLocation()) != null) {
                throw new PreexistingEntityException("Locations " + locations + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Locations locations) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Locations persistentLocations = em.find(Locations.class, locations.getIdLocation());
            Collection<Domain> domainCollectionOld = persistentLocations.getDomainCollection();
            Collection<Domain> domainCollectionNew = locations.getDomainCollection();
            Collection<Domain> attachedDomainCollectionNew = new ArrayList<Domain>();
            for (Domain domainCollectionNewDomainToAttach : domainCollectionNew) {
                domainCollectionNewDomainToAttach = em.getReference(domainCollectionNewDomainToAttach.getClass(), domainCollectionNewDomainToAttach.getIdDomain());
                attachedDomainCollectionNew.add(domainCollectionNewDomainToAttach);
            }
            domainCollectionNew = attachedDomainCollectionNew;
            locations.setDomainCollection(domainCollectionNew);
            locations = em.merge(locations);
            for (Domain domainCollectionOldDomain : domainCollectionOld) {
                if (!domainCollectionNew.contains(domainCollectionOldDomain)) {
                    domainCollectionOldDomain.setIdLocation(null);
                    domainCollectionOldDomain = em.merge(domainCollectionOldDomain);
                }
            }
            for (Domain domainCollectionNewDomain : domainCollectionNew) {
                if (!domainCollectionOld.contains(domainCollectionNewDomain)) {
                    Locations oldIdLocationOfDomainCollectionNewDomain = domainCollectionNewDomain.getIdLocation();
                    domainCollectionNewDomain.setIdLocation(locations);
                    domainCollectionNewDomain = em.merge(domainCollectionNewDomain);
                    if (oldIdLocationOfDomainCollectionNewDomain != null && !oldIdLocationOfDomainCollectionNewDomain.equals(locations)) {
                        oldIdLocationOfDomainCollectionNewDomain.getDomainCollection().remove(domainCollectionNewDomain);
                        oldIdLocationOfDomainCollectionNewDomain = em.merge(oldIdLocationOfDomainCollectionNewDomain);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = locations.getIdLocation();
                if (findLocations(id) == null) {
                    throw new NonexistentEntityException("The locations with id " + id + " no longer exists.");
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
            Locations locations;
            try {
                locations = em.getReference(Locations.class, id);
                locations.getIdLocation();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The locations with id " + id + " no longer exists.", enfe);
            }
            Collection<Domain> domainCollection = locations.getDomainCollection();
            for (Domain domainCollectionDomain : domainCollection) {
                domainCollectionDomain.setIdLocation(null);
                domainCollectionDomain = em.merge(domainCollectionDomain);
            }
            em.remove(locations);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Locations> findLocationsEntities() {
        return findLocationsEntities(true, -1, -1);
    }

    public List<Locations> findLocationsEntities(int maxResults, int firstResult) {
        return findLocationsEntities(false, maxResults, firstResult);
    }

    private List<Locations> findLocationsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Locations.class));
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

    public Locations findLocations(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Locations.class, id);
        } finally {
            em.close();
        }
    }

    public int getLocationsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Locations> rt = cq.from(Locations.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
