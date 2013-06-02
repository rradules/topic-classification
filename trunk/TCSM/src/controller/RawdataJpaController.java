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
import model.Header;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Rawdata;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class RawdataJpaController implements Serializable {

    public RawdataJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rawdata rawdata) throws PreexistingEntityException, Exception {
        if (rawdata.getHeadersCollection() == null) {
            rawdata.setHeadersCollection(new ArrayList<Header>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Domain idDomain = rawdata.getIdDomain();
            if (idDomain != null) {
                idDomain = em.getReference(idDomain.getClass(), idDomain.getIdDomain());
                rawdata.setIdDomain(idDomain);
            }
            Collection<Header> attachedHeadersCollection = new ArrayList<Header>();
            for (Header headersCollectionHeaderToAttach : rawdata.getHeadersCollection()) {
                headersCollectionHeaderToAttach = em.getReference(headersCollectionHeaderToAttach.getClass(), headersCollectionHeaderToAttach.getIdHeader());
                attachedHeadersCollection.add(headersCollectionHeaderToAttach);
            }
            rawdata.setHeadersCollection(attachedHeadersCollection);
            em.persist(rawdata);
            if (idDomain != null) {
                idDomain.getRawdataCollection().add(rawdata);
                idDomain = em.merge(idDomain);
            }
            for (Header headersCollectionHeader : rawdata.getHeadersCollection()) {
                Rawdata oldIdRawDataOfHeadersCollectionHeader = headersCollectionHeader.getIdRawData();
                headersCollectionHeader.setIdRawData(rawdata);
                headersCollectionHeader = em.merge(headersCollectionHeader);
                if (oldIdRawDataOfHeadersCollectionHeader != null) {
                    oldIdRawDataOfHeadersCollectionHeader.getHeadersCollection().remove(headersCollectionHeader);
                    oldIdRawDataOfHeadersCollectionHeader = em.merge(oldIdRawDataOfHeadersCollectionHeader);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRawdata(rawdata.getIdRawData()) != null) {
                throw new PreexistingEntityException("Rawdata " + rawdata + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rawdata rawdata) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rawdata persistentRawdata = em.find(Rawdata.class, rawdata.getIdRawData());
            Domain idDomainOld = persistentRawdata.getIdDomain();
            Domain idDomainNew = rawdata.getIdDomain();
            Collection<Header> headersCollectionOld = persistentRawdata.getHeadersCollection();
            Collection<Header> headersCollectionNew = rawdata.getHeadersCollection();
            if (idDomainNew != null) {
                idDomainNew = em.getReference(idDomainNew.getClass(), idDomainNew.getIdDomain());
                rawdata.setIdDomain(idDomainNew);
            }
            Collection<Header> attachedHeadersCollectionNew = new ArrayList<Header>();
            for (Header headersCollectionNewHeaderToAttach : headersCollectionNew) {
                headersCollectionNewHeaderToAttach = em.getReference(headersCollectionNewHeaderToAttach.getClass(), headersCollectionNewHeaderToAttach.getIdHeader());
                attachedHeadersCollectionNew.add(headersCollectionNewHeaderToAttach);
            }
            headersCollectionNew = attachedHeadersCollectionNew;
            rawdata.setHeadersCollection(headersCollectionNew);
            rawdata = em.merge(rawdata);
            if (idDomainOld != null && !idDomainOld.equals(idDomainNew)) {
                idDomainOld.getRawdataCollection().remove(rawdata);
                idDomainOld = em.merge(idDomainOld);
            }
            if (idDomainNew != null && !idDomainNew.equals(idDomainOld)) {
                idDomainNew.getRawdataCollection().add(rawdata);
                idDomainNew = em.merge(idDomainNew);
            }
            for (Header headersCollectionOldHeader : headersCollectionOld) {
                if (!headersCollectionNew.contains(headersCollectionOldHeader)) {
                    headersCollectionOldHeader.setIdRawData(null);
                    headersCollectionOldHeader = em.merge(headersCollectionOldHeader);
                }
            }
            for (Header headersCollectionNewHeader : headersCollectionNew) {
                if (!headersCollectionOld.contains(headersCollectionNewHeader)) {
                    Rawdata oldIdRawDataOfHeadersCollectionNewHeader = headersCollectionNewHeader.getIdRawData();
                    headersCollectionNewHeader.setIdRawData(rawdata);
                    headersCollectionNewHeader = em.merge(headersCollectionNewHeader);
                    if (oldIdRawDataOfHeadersCollectionNewHeader != null && !oldIdRawDataOfHeadersCollectionNewHeader.equals(rawdata)) {
                        oldIdRawDataOfHeadersCollectionNewHeader.getHeadersCollection().remove(headersCollectionNewHeader);
                        oldIdRawDataOfHeadersCollectionNewHeader = em.merge(oldIdRawDataOfHeadersCollectionNewHeader);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = rawdata.getIdRawData();
                if (findRawdata(id) == null) {
                    throw new NonexistentEntityException("The rawdata with id " + id + " no longer exists.");
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
            Rawdata rawdata;
            try {
                rawdata = em.getReference(Rawdata.class, id);
                rawdata.getIdRawData();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rawdata with id " + id + " no longer exists.", enfe);
            }
            Domain idDomain = rawdata.getIdDomain();
            if (idDomain != null) {
                idDomain.getRawdataCollection().remove(rawdata);
                idDomain = em.merge(idDomain);
            }
            Collection<Header> headersCollection = rawdata.getHeadersCollection();
            for (Header headersCollectionHeader : headersCollection) {
                headersCollectionHeader.setIdRawData(null);
                headersCollectionHeader = em.merge(headersCollectionHeader);
            }
            em.remove(rawdata);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Rawdata> findRawdataEntities() {
        return findRawdataEntities(true, -1, -1);
    }

    public List<Rawdata> findRawdataEntities(int maxResults, int firstResult) {
        return findRawdataEntities(false, maxResults, firstResult);
    }

    private List<Rawdata> findRawdataEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rawdata.class));
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

    public Rawdata findRawdata(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rawdata.class, id);
        } finally {
            em.close();
        }
    }

    public int getRawdataCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rawdata> rt = cq.from(Rawdata.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Rawdata findByPageAddress(String address) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Rawdata.findByPageAddress");
        q.setParameter("pageAddress", address);

        try {
            return (Rawdata) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
