/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
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
import model.Category;
import model.Keyword;

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

    public void create(Category category) {
        if (category.getDomainCollection() == null) {
            category.setDomainCollection(new ArrayList<Domain>());
        }
        if (category.getKeywordCollection() == null) {
            category.setKeywordCollection(new ArrayList<Keyword>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Domain> attachedDomainCollection = new ArrayList<Domain>();
            for (Domain domainCollectionDomainToAttach : category.getDomainCollection()) {
                domainCollectionDomainToAttach = em.getReference(domainCollectionDomainToAttach.getClass(), domainCollectionDomainToAttach.getIdDomain());
                attachedDomainCollection.add(domainCollectionDomainToAttach);
            }
            category.setDomainCollection(attachedDomainCollection);
            Collection<Keyword> attachedKeywordCollection = new ArrayList<Keyword>();
            for (Keyword keywordCollectionKeywordToAttach : category.getKeywordCollection()) {
                keywordCollectionKeywordToAttach = em.getReference(keywordCollectionKeywordToAttach.getClass(), keywordCollectionKeywordToAttach.getIdKeyword());
                attachedKeywordCollection.add(keywordCollectionKeywordToAttach);
            }
            category.setKeywordCollection(attachedKeywordCollection);
            em.persist(category);
            for (Domain domainCollectionDomain : category.getDomainCollection()) {
                Category oldIdCategoryOfDomainCollectionDomain = domainCollectionDomain.getIdCategory();
                domainCollectionDomain.setIdCategory(category);
                domainCollectionDomain = em.merge(domainCollectionDomain);
                if (oldIdCategoryOfDomainCollectionDomain != null) {
                    oldIdCategoryOfDomainCollectionDomain.getDomainCollection().remove(domainCollectionDomain);
                    oldIdCategoryOfDomainCollectionDomain = em.merge(oldIdCategoryOfDomainCollectionDomain);
                }
            }
            for (Keyword keywordCollectionKeyword : category.getKeywordCollection()) {
                Category oldIdCategoryOfKeywordCollectionKeyword = keywordCollectionKeyword.getIdCategory();
                keywordCollectionKeyword.setIdCategory(category);
                keywordCollectionKeyword = em.merge(keywordCollectionKeyword);
                if (oldIdCategoryOfKeywordCollectionKeyword != null) {
                    oldIdCategoryOfKeywordCollectionKeyword.getKeywordCollection().remove(keywordCollectionKeyword);
                    oldIdCategoryOfKeywordCollectionKeyword = em.merge(oldIdCategoryOfKeywordCollectionKeyword);
                }
            }
            em.getTransaction().commit();
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
            Collection<Domain> domainCollectionOld = persistentCategory.getDomainCollection();
            Collection<Domain> domainCollectionNew = category.getDomainCollection();
            Collection<Keyword> keywordCollectionOld = persistentCategory.getKeywordCollection();
            Collection<Keyword> keywordCollectionNew = category.getKeywordCollection();
            Collection<Domain> attachedDomainCollectionNew = new ArrayList<Domain>();
            for (Domain domainCollectionNewDomainToAttach : domainCollectionNew) {
                domainCollectionNewDomainToAttach = em.getReference(domainCollectionNewDomainToAttach.getClass(), domainCollectionNewDomainToAttach.getIdDomain());
                attachedDomainCollectionNew.add(domainCollectionNewDomainToAttach);
            }
            domainCollectionNew = attachedDomainCollectionNew;
            category.setDomainCollection(domainCollectionNew);
            Collection<Keyword> attachedKeywordCollectionNew = new ArrayList<Keyword>();
            for (Keyword keywordCollectionNewKeywordToAttach : keywordCollectionNew) {
                keywordCollectionNewKeywordToAttach = em.getReference(keywordCollectionNewKeywordToAttach.getClass(), keywordCollectionNewKeywordToAttach.getIdKeyword());
                attachedKeywordCollectionNew.add(keywordCollectionNewKeywordToAttach);
            }
            keywordCollectionNew = attachedKeywordCollectionNew;
            category.setKeywordCollection(keywordCollectionNew);
            category = em.merge(category);
            for (Domain domainCollectionOldDomain : domainCollectionOld) {
                if (!domainCollectionNew.contains(domainCollectionOldDomain)) {
                    domainCollectionOldDomain.setIdCategory(null);
                    domainCollectionOldDomain = em.merge(domainCollectionOldDomain);
                }
            }
            for (Domain domainCollectionNewDomain : domainCollectionNew) {
                if (!domainCollectionOld.contains(domainCollectionNewDomain)) {
                    Category oldIdCategoryOfDomainCollectionNewDomain = domainCollectionNewDomain.getIdCategory();
                    domainCollectionNewDomain.setIdCategory(category);
                    domainCollectionNewDomain = em.merge(domainCollectionNewDomain);
                    if (oldIdCategoryOfDomainCollectionNewDomain != null && !oldIdCategoryOfDomainCollectionNewDomain.equals(category)) {
                        oldIdCategoryOfDomainCollectionNewDomain.getDomainCollection().remove(domainCollectionNewDomain);
                        oldIdCategoryOfDomainCollectionNewDomain = em.merge(oldIdCategoryOfDomainCollectionNewDomain);
                    }
                }
            }
            for (Keyword keywordCollectionOldKeyword : keywordCollectionOld) {
                if (!keywordCollectionNew.contains(keywordCollectionOldKeyword)) {
                    keywordCollectionOldKeyword.setIdCategory(null);
                    keywordCollectionOldKeyword = em.merge(keywordCollectionOldKeyword);
                }
            }
            for (Keyword keywordCollectionNewKeyword : keywordCollectionNew) {
                if (!keywordCollectionOld.contains(keywordCollectionNewKeyword)) {
                    Category oldIdCategoryOfKeywordCollectionNewKeyword = keywordCollectionNewKeyword.getIdCategory();
                    keywordCollectionNewKeyword.setIdCategory(category);
                    keywordCollectionNewKeyword = em.merge(keywordCollectionNewKeyword);
                    if (oldIdCategoryOfKeywordCollectionNewKeyword != null && !oldIdCategoryOfKeywordCollectionNewKeyword.equals(category)) {
                        oldIdCategoryOfKeywordCollectionNewKeyword.getKeywordCollection().remove(keywordCollectionNewKeyword);
                        oldIdCategoryOfKeywordCollectionNewKeyword = em.merge(oldIdCategoryOfKeywordCollectionNewKeyword);
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
            Collection<Domain> domainCollection = category.getDomainCollection();
            for (Domain domainCollectionDomain : domainCollection) {
                domainCollectionDomain.setIdCategory(null);
                domainCollectionDomain = em.merge(domainCollectionDomain);
            }
            Collection<Keyword> keywordCollection = category.getKeywordCollection();
            for (Keyword keywordCollectionKeyword : keywordCollection) {
                keywordCollectionKeyword.setIdCategory(null);
                keywordCollectionKeyword = em.merge(keywordCollectionKeyword);
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

    public Category findByCategory(String name) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Category.findByCategory");
        q.setParameter("category", name);

        try {
            return (Category) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
