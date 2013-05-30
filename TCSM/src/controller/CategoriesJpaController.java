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
import model.Keywords;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Categories;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class CategoriesJpaController implements Serializable {

    public CategoriesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Categories categories) throws PreexistingEntityException, Exception {
        if (categories.getKeywordsCollection() == null) {
            categories.setKeywordsCollection(new ArrayList<Keywords>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Keywords> attachedKeywordsCollection = new ArrayList<Keywords>();
            for (Keywords keywordsCollectionKeywordsToAttach : categories.getKeywordsCollection()) {
                keywordsCollectionKeywordsToAttach = em.getReference(keywordsCollectionKeywordsToAttach.getClass(), keywordsCollectionKeywordsToAttach.getIdKeyword());
                attachedKeywordsCollection.add(keywordsCollectionKeywordsToAttach);
            }
            categories.setKeywordsCollection(attachedKeywordsCollection);
            em.persist(categories);
            for (Keywords keywordsCollectionKeywords : categories.getKeywordsCollection()) {
                Categories oldIdCategoryOfKeywordsCollectionKeywords = keywordsCollectionKeywords.getIdCategory();
                keywordsCollectionKeywords.setIdCategory(categories);
                keywordsCollectionKeywords = em.merge(keywordsCollectionKeywords);
                if (oldIdCategoryOfKeywordsCollectionKeywords != null) {
                    oldIdCategoryOfKeywordsCollectionKeywords.getKeywordsCollection().remove(keywordsCollectionKeywords);
                    oldIdCategoryOfKeywordsCollectionKeywords = em.merge(oldIdCategoryOfKeywordsCollectionKeywords);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCategories(categories.getIdCategory()) != null) {
                throw new PreexistingEntityException("Categories " + categories + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Categories categories) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categories persistentCategories = em.find(Categories.class, categories.getIdCategory());
            Collection<Keywords> keywordsCollectionOld = persistentCategories.getKeywordsCollection();
            Collection<Keywords> keywordsCollectionNew = categories.getKeywordsCollection();
            Collection<Keywords> attachedKeywordsCollectionNew = new ArrayList<Keywords>();
            for (Keywords keywordsCollectionNewKeywordsToAttach : keywordsCollectionNew) {
                keywordsCollectionNewKeywordsToAttach = em.getReference(keywordsCollectionNewKeywordsToAttach.getClass(), keywordsCollectionNewKeywordsToAttach.getIdKeyword());
                attachedKeywordsCollectionNew.add(keywordsCollectionNewKeywordsToAttach);
            }
            keywordsCollectionNew = attachedKeywordsCollectionNew;
            categories.setKeywordsCollection(keywordsCollectionNew);
            categories = em.merge(categories);
            for (Keywords keywordsCollectionOldKeywords : keywordsCollectionOld) {
                if (!keywordsCollectionNew.contains(keywordsCollectionOldKeywords)) {
                    keywordsCollectionOldKeywords.setIdCategory(null);
                    keywordsCollectionOldKeywords = em.merge(keywordsCollectionOldKeywords);
                }
            }
            for (Keywords keywordsCollectionNewKeywords : keywordsCollectionNew) {
                if (!keywordsCollectionOld.contains(keywordsCollectionNewKeywords)) {
                    Categories oldIdCategoryOfKeywordsCollectionNewKeywords = keywordsCollectionNewKeywords.getIdCategory();
                    keywordsCollectionNewKeywords.setIdCategory(categories);
                    keywordsCollectionNewKeywords = em.merge(keywordsCollectionNewKeywords);
                    if (oldIdCategoryOfKeywordsCollectionNewKeywords != null && !oldIdCategoryOfKeywordsCollectionNewKeywords.equals(categories)) {
                        oldIdCategoryOfKeywordsCollectionNewKeywords.getKeywordsCollection().remove(keywordsCollectionNewKeywords);
                        oldIdCategoryOfKeywordsCollectionNewKeywords = em.merge(oldIdCategoryOfKeywordsCollectionNewKeywords);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = categories.getIdCategory();
                if (findCategories(id) == null) {
                    throw new NonexistentEntityException("The categories with id " + id + " no longer exists.");
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
            Categories categories;
            try {
                categories = em.getReference(Categories.class, id);
                categories.getIdCategory();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categories with id " + id + " no longer exists.", enfe);
            }
            Collection<Keywords> keywordsCollection = categories.getKeywordsCollection();
            for (Keywords keywordsCollectionKeywords : keywordsCollection) {
                keywordsCollectionKeywords.setIdCategory(null);
                keywordsCollectionKeywords = em.merge(keywordsCollectionKeywords);
            }
            em.remove(categories);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Categories> findCategoriesEntities() {
        return findCategoriesEntities(true, -1, -1);
    }

    public List<Categories> findCategoriesEntities(int maxResults, int firstResult) {
        return findCategoriesEntities(false, maxResults, firstResult);
    }

    private List<Categories> findCategoriesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Categories.class));
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

    public Categories findCategories(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Categories.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoriesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Categories> rt = cq.from(Categories.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
