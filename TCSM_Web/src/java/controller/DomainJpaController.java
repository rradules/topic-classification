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
import model.Location;
import model.Category;
import model.Domain;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class DomainJpaController implements Serializable {

    public DomainJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Domain domain) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Location idLocation = domain.getIdLocation();
            if (idLocation != null) {
                idLocation = em.getReference(idLocation.getClass(), idLocation.getIdLocation());
                domain.setIdLocation(idLocation);
            }
            Category idCategory = domain.getIdCategory();
            if (idCategory != null) {
                idCategory = em.getReference(idCategory.getClass(), idCategory.getIdCategory());
                domain.setIdCategory(idCategory);
            }
            em.persist(domain);
            if (idLocation != null) {
                idLocation.getDomainCollection().add(domain);
                idLocation = em.merge(idLocation);
            }
            if (idCategory != null) {
                idCategory.getDomainCollection().add(domain);
                idCategory = em.merge(idCategory);
            }
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

    public void edit(Domain domain) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Domain persistentDomain = em.find(Domain.class, domain.getIdDomain());
            Location idLocationOld = persistentDomain.getIdLocation();
            Location idLocationNew = domain.getIdLocation();
            Category idCategoryOld = persistentDomain.getIdCategory();
            Category idCategoryNew = domain.getIdCategory();
            if (idLocationNew != null) {
                idLocationNew = em.getReference(idLocationNew.getClass(), idLocationNew.getIdLocation());
                domain.setIdLocation(idLocationNew);
            }
            if (idCategoryNew != null) {
                idCategoryNew = em.getReference(idCategoryNew.getClass(), idCategoryNew.getIdCategory());
                domain.setIdCategory(idCategoryNew);
            }
            domain = em.merge(domain);
            if (idLocationOld != null && !idLocationOld.equals(idLocationNew)) {
                idLocationOld.getDomainCollection().remove(domain);
                idLocationOld = em.merge(idLocationOld);
            }
            if (idLocationNew != null && !idLocationNew.equals(idLocationOld)) {
                idLocationNew.getDomainCollection().add(domain);
                idLocationNew = em.merge(idLocationNew);
            }
            if (idCategoryOld != null && !idCategoryOld.equals(idCategoryNew)) {
                idCategoryOld.getDomainCollection().remove(domain);
                idCategoryOld = em.merge(idCategoryOld);
            }
            if (idCategoryNew != null && !idCategoryNew.equals(idCategoryOld)) {
                idCategoryNew.getDomainCollection().add(domain);
                idCategoryNew = em.merge(idCategoryNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
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

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
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
            Category idCategory = domain.getIdCategory();
            if (idCategory != null) {
                idCategory.getDomainCollection().remove(domain);
                idCategory = em.merge(idCategory);
            }
            em.remove(domain);
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
        public Domain findByDomainName(String name) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Domain.findByDomainName");
        q.setParameter("domainName", name);

        try {
            return (Domain) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Domain> findByDomainCategory(Category categ) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Domain.findByCategory");
        q.setParameter("idCategory", categ);

        try {
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
}
