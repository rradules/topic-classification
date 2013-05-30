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
import model.Headers;
import model.Rawdata;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class HeadersJpaController implements Serializable {

    public HeadersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Headers headers) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rawdata idRawData = headers.getIdRawData();
            if (idRawData != null) {
                idRawData = em.getReference(idRawData.getClass(), idRawData.getIdRawData());
                headers.setIdRawData(idRawData);
            }
            em.persist(headers);
            if (idRawData != null) {
                idRawData.getHeadersCollection().add(headers);
                idRawData = em.merge(idRawData);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHeaders(headers.getIdHeader()) != null) {
                throw new PreexistingEntityException("Headers " + headers + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Headers headers) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Headers persistentHeaders = em.find(Headers.class, headers.getIdHeader());
            Rawdata idRawDataOld = persistentHeaders.getIdRawData();
            Rawdata idRawDataNew = headers.getIdRawData();
            if (idRawDataNew != null) {
                idRawDataNew = em.getReference(idRawDataNew.getClass(), idRawDataNew.getIdRawData());
                headers.setIdRawData(idRawDataNew);
            }
            headers = em.merge(headers);
            if (idRawDataOld != null && !idRawDataOld.equals(idRawDataNew)) {
                idRawDataOld.getHeadersCollection().remove(headers);
                idRawDataOld = em.merge(idRawDataOld);
            }
            if (idRawDataNew != null && !idRawDataNew.equals(idRawDataOld)) {
                idRawDataNew.getHeadersCollection().add(headers);
                idRawDataNew = em.merge(idRawDataNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = headers.getIdHeader();
                if (findHeaders(id) == null) {
                    throw new NonexistentEntityException("The headers with id " + id + " no longer exists.");
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
            Headers headers;
            try {
                headers = em.getReference(Headers.class, id);
                headers.getIdHeader();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The headers with id " + id + " no longer exists.", enfe);
            }
            Rawdata idRawData = headers.getIdRawData();
            if (idRawData != null) {
                idRawData.getHeadersCollection().remove(headers);
                idRawData = em.merge(idRawData);
            }
            em.remove(headers);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Headers> findHeadersEntities() {
        return findHeadersEntities(true, -1, -1);
    }

    public List<Headers> findHeadersEntities(int maxResults, int firstResult) {
        return findHeadersEntities(false, maxResults, firstResult);
    }

    private List<Headers> findHeadersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Headers.class));
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

    public Headers findHeaders(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Headers.class, id);
        } finally {
            em.close();
        }
    }

    public int getHeadersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Headers> rt = cq.from(Headers.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
