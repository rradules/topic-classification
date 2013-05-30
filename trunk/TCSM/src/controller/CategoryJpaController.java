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
import model.Keyword;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Category;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class CategoryJpaController implements Serializable {

    public CategoryJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Category category) throws PreexistingEntityException, Exception {
        if (category.getKeywordsCollection() == null) {
            category.setKeywordsCollection(new ArrayList<Keyword>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Keyword> attachedKeywordsCollection = new ArrayList<Keyword>();
            for (Keyword keywordsCollectionKeywordToAttach : category.getKeywordsCollection()) {
                keywordsCollectionKeywordToAttach = em.getReference(keywordsCollectionKeywordToAttach.getClass(), keywordsCollectionKeywordToAttach.getIdKeyword());
                attachedKeywordsCollection.add(keywordsCollectionKeywordToAttach);
            }
            category.setKeywordsCollection(attachedKeywordsCollection);
            em.persist(category);
            for (Keyword keywordsCollectionKeyword : category.getKeywordsCollection()) {
                Category oldIdCategoryOfKeywordsCollectionKeyword = keywordsCollectionKeyword.getIdCategory();
                keywordsCollectionKeyword.setIdCategory(category);
                keywordsCollectionKeyword = em.merge(keywordsCollectionKeyword);
                if (oldIdCategoryOfKeywordsCollectionKeyword != null) {
                    oldIdCategoryOfKeywordsCollectionKeyword.getKeywordsCollection().remove(keywordsCollectionKeyword);
                    oldIdCategoryOfKeywordsCollectionKeyword = em.merge(oldIdCategoryOfKeywordsCollectionKeyword);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCategory(category.getIdCategory()) != null) {
                throw new PreexistingEntityException("Category " + category + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Category category) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Category persistentCategory = em.find(Category.class, category.getIdCategory());
            Collection<Keyword> keywordsCollectionOld = persistentCategory.getKeywordsCollection();
            Collection<Keyword> keywordsCollectionNew = category.getKeywordsCollection();
            Collection<Keyword> attachedKeywordsCollectionNew = new ArrayList<Keyword>();
            for (Keyword keywordsCollectionNewKeywordToAttach : keywordsCollectionNew) {
                keywordsCollectionNewKeywordToAttach = em.getReference(keywordsCollectionNewKeywordToAttach.getClass(), keywordsCollectionNewKeywordToAttach.getIdKeyword());
                attachedKeywordsCollectionNew.add(keywordsCollectionNewKeywordToAttach);
            }
            keywordsCollectionNew = attachedKeywordsCollectionNew;
            category.setKeywordsCollection(keywordsCollectionNew);
            category = em.merge(category);
            for (Keyword keywordsCollectionOldKeyword : keywordsCollectionOld) {
                if (!keywordsCollectionNew.contains(keywordsCollectionOldKeyword)) {
                    keywordsCollectionOldKeyword.setIdCategory(null);
                    keywordsCollectionOldKeyword = em.merge(keywordsCollectionOldKeyword);
                }
            }
            for (Keyword keywordsCollectionNewKeyword : keywordsCollectionNew) {
                if (!keywordsCollectionOld.contains(keywordsCollectionNewKeyword)) {
                    Category oldIdCategoryOfKeywordsCollectionNewKeyword = keywordsCollectionNewKeyword.getIdCategory();
                    keywordsCollectionNewKeyword.setIdCategory(category);
                    keywordsCollectionNewKeyword = em.merge(keywordsCollectionNewKeyword);
                    if (oldIdCategoryOfKeywordsCollectionNewKeyword != null && !oldIdCategoryOfKeywordsCollectionNewKeyword.equals(category)) {
                        oldIdCategoryOfKeywordsCollectionNewKeyword.getKeywordsCollection().remove(keywordsCollectionNewKeyword);
                        oldIdCategoryOfKeywordsCollectionNewKeyword = em.merge(oldIdCategoryOfKeywordsCollectionNewKeyword);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = category.getIdCategory();
                if (findCategory(id) == null) {
                    throw new NonexistentEntityException("The category with id " + id + " no longer exists.");
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
            Category category;
            try {
                category = em.getReference(Category.class, id);
                category.getIdCategory();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The category with id " + id + " no longer exists.", enfe);
            }
            Collection<Keyword> keywordsCollection = category.getKeywordsCollection();
            for (Keyword keywordsCollectionKeyword : keywordsCollection) {
                keywordsCollectionKeyword.setIdCategory(null);
                keywordsCollectionKeyword = em.merge(keywordsCollectionKeyword);
            }
            em.remove(category);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Category> findCategoryEntities() {
        return findCategoryEntities(true, -1, -1);
    }

    public List<Category> findCategoryEntities(int maxResults, int firstResult) {
        return findCategoryEntities(false, maxResults, firstResult);
    }

    private List<Category> findCategoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Category.class));
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

    public Category findCategory(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Category> rt = cq.from(Category.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
