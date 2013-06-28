/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Category;
import model.LearningTable;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class LearningTableJpaController implements Serializable {

    public LearningTableJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(LearningTable learningTable) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Category idCategory = learningTable.getIdCategory();
            if (idCategory != null) {
                idCategory = em.getReference(idCategory.getClass(), idCategory.getIdCategory());
                learningTable.setIdCategory(idCategory);
            }
            em.persist(learningTable);
            if (idCategory != null) {
                idCategory.getLearningTableCollection().add(learningTable);
                idCategory = em.merge(idCategory);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(LearningTable learningTable) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LearningTable persistentLearningTable = em.find(LearningTable.class, learningTable.getIdLearnTable());
            Category idCategoryOld = persistentLearningTable.getIdCategory();
            Category idCategoryNew = learningTable.getIdCategory();
            if (idCategoryNew != null) {
                idCategoryNew = em.getReference(idCategoryNew.getClass(), idCategoryNew.getIdCategory());
                learningTable.setIdCategory(idCategoryNew);
            }
            learningTable = em.merge(learningTable);
            if (idCategoryOld != null && !idCategoryOld.equals(idCategoryNew)) {
                idCategoryOld.getLearningTableCollection().remove(learningTable);
                idCategoryOld = em.merge(idCategoryOld);
            }
            if (idCategoryNew != null && !idCategoryNew.equals(idCategoryOld)) {
                idCategoryNew.getLearningTableCollection().add(learningTable);
                idCategoryNew = em.merge(idCategoryNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = learningTable.getIdLearnTable();
                if (findLearningTable(id) == null) {
                    throw new NonexistentEntityException("The learningTable with id " + id + " no longer exists.");
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
            LearningTable learningTable;
            try {
                learningTable = em.getReference(LearningTable.class, id);
                learningTable.getIdLearnTable();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The learningTable with id " + id + " no longer exists.", enfe);
            }
            Category idCategory = learningTable.getIdCategory();
            if (idCategory != null) {
                idCategory.getLearningTableCollection().remove(learningTable);
                idCategory = em.merge(idCategory);
            }
            em.remove(learningTable);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<LearningTable> findLearningTableEntities() {
        return findLearningTableEntities(true, -1, -1);
    }

    public List<LearningTable> findLearningTableEntities(int maxResults, int firstResult) {
        return findLearningTableEntities(false, maxResults, firstResult);
    }

    private List<LearningTable> findLearningTableEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LearningTable.class));
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

    public LearningTable findLearningTable(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LearningTable.class, id);
        } finally {
            em.close();
        }
    }

    public int getLearningTableCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LearningTable> rt = cq.from(LearningTable.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<LearningTable> findByKeyword(String keyword) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("LearningTable.findByKeyword");
        q.setParameter("keyword", keyword);

        try {
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<LearningTable> findByCategory(Category categ) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("LearningTable.findByCategory");
        q.setParameter("idCategory", categ);

        try {
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
