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
import model.Header;
import model.Rawdata;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class HeaderJpaController implements Serializable {

    public HeaderJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Header header) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rawdata idRawData = header.getIdRawData();
            if (idRawData != null) {
                idRawData = em.getReference(idRawData.getClass(), idRawData.getIdRawData());
                header.setIdRawData(idRawData);
            }
            em.persist(header);
            if (idRawData != null) {
                idRawData.getHeadersCollection().add(header);
                idRawData = em.merge(idRawData);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHeader(header.getIdHeader()) != null) {
                throw new PreexistingEntityException("Header " + header + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Header header) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Header persistentHeader = em.find(Header.class, header.getIdHeader());
            Rawdata idRawDataOld = persistentHeader.getIdRawData();
            Rawdata idRawDataNew = header.getIdRawData();
            if (idRawDataNew != null) {
                idRawDataNew = em.getReference(idRawDataNew.getClass(), idRawDataNew.getIdRawData());
                header.setIdRawData(idRawDataNew);
            }
            header = em.merge(header);
            if (idRawDataOld != null && !idRawDataOld.equals(idRawDataNew)) {
                idRawDataOld.getHeadersCollection().remove(header);
                idRawDataOld = em.merge(idRawDataOld);
            }
            if (idRawDataNew != null && !idRawDataNew.equals(idRawDataOld)) {
                idRawDataNew.getHeadersCollection().add(header);
                idRawDataNew = em.merge(idRawDataNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = header.getIdHeader();
                if (findHeader(id) == null) {
                    throw new NonexistentEntityException("The header with id " + id + " no longer exists.");
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
            Header header;
            try {
                header = em.getReference(Header.class, id);
                header.getIdHeader();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The header with id " + id + " no longer exists.", enfe);
            }
            Rawdata idRawData = header.getIdRawData();
            if (idRawData != null) {
                idRawData.getHeadersCollection().remove(header);
                idRawData = em.merge(idRawData);
            }
            em.remove(header);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Header> findHeaderEntities() {
        return findHeaderEntities(true, -1, -1);
    }

    public List<Header> findHeaderEntities(int maxResults, int firstResult) {
        return findHeaderEntities(false, maxResults, firstResult);
    }

    private List<Header> findHeaderEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Header.class));
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

    public Header findHeader(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Header.class, id);
        } finally {
            em.close();
        }
    }

    public int getHeaderCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Header> rt = cq.from(Header.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
