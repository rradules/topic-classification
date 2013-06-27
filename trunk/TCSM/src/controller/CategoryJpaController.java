/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.IllegalOrphanException;
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
import model.TempKeyword;

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
        if (category.getTempKeywordsCollection() == null) {
            category.setTempKeywordsCollection(new ArrayList<TempKeyword>());
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
            Collection<TempKeyword> attachedTempKeywordsCollection = new ArrayList<TempKeyword>();
            for (TempKeyword tempKeywordsCollectionTempKeywordToAttach : category.getTempKeywordsCollection()) {
                tempKeywordsCollectionTempKeywordToAttach = em.getReference(tempKeywordsCollectionTempKeywordToAttach.getClass(), tempKeywordsCollectionTempKeywordToAttach.getIdTempKeyword());
                attachedTempKeywordsCollection.add(tempKeywordsCollectionTempKeywordToAttach);
            }
            category.setTempKeywordsCollection(attachedTempKeywordsCollection);
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
            for (TempKeyword tempKeywordsCollectionTempKeyword : category.getTempKeywordsCollection()) {
                Category oldIdCategoryOfTempKeywordsCollectionTempKeyword = tempKeywordsCollectionTempKeyword.getIdCategory();
                tempKeywordsCollectionTempKeyword.setIdCategory(category);
                tempKeywordsCollectionTempKeyword = em.merge(tempKeywordsCollectionTempKeyword);
                if (oldIdCategoryOfTempKeywordsCollectionTempKeyword != null) {
                    oldIdCategoryOfTempKeywordsCollectionTempKeyword.getTempKeywordsCollection().remove(tempKeywordsCollectionTempKeyword);
                    oldIdCategoryOfTempKeywordsCollectionTempKeyword = em.merge(oldIdCategoryOfTempKeywordsCollectionTempKeyword);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Category category) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Category persistentCategory = em.find(Category.class, category.getIdCategory());
            Collection<Domain> domainCollectionOld = persistentCategory.getDomainCollection();
            Collection<Domain> domainCollectionNew = category.getDomainCollection();
            Collection<Keyword> keywordCollectionOld = persistentCategory.getKeywordCollection();
            Collection<Keyword> keywordCollectionNew = category.getKeywordCollection();
            Collection<TempKeyword> tempKeywordsCollectionOld = persistentCategory.getTempKeywordsCollection();
            Collection<TempKeyword> tempKeywordsCollectionNew = category.getTempKeywordsCollection();
            List<String> illegalOrphanMessages = null;
            for (TempKeyword tempKeywordsCollectionOldTempKeyword : tempKeywordsCollectionOld) {
                if (!tempKeywordsCollectionNew.contains(tempKeywordsCollectionOldTempKeyword)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TempKeyword " + tempKeywordsCollectionOldTempKeyword + " since its idCategory field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
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
            Collection<TempKeyword> attachedTempKeywordsCollectionNew = new ArrayList<TempKeyword>();
            for (TempKeyword tempKeywordsCollectionNewTempKeywordToAttach : tempKeywordsCollectionNew) {
                tempKeywordsCollectionNewTempKeywordToAttach = em.getReference(tempKeywordsCollectionNewTempKeywordToAttach.getClass(), tempKeywordsCollectionNewTempKeywordToAttach.getIdTempKeyword());
                attachedTempKeywordsCollectionNew.add(tempKeywordsCollectionNewTempKeywordToAttach);
            }
            tempKeywordsCollectionNew = attachedTempKeywordsCollectionNew;
            category.setTempKeywordsCollection(tempKeywordsCollectionNew);
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
            for (TempKeyword tempKeywordsCollectionNewTempKeyword : tempKeywordsCollectionNew) {
                if (!tempKeywordsCollectionOld.contains(tempKeywordsCollectionNewTempKeyword)) {
                    Category oldIdCategoryOfTempKeywordsCollectionNewTempKeyword = tempKeywordsCollectionNewTempKeyword.getIdCategory();
                    tempKeywordsCollectionNewTempKeyword.setIdCategory(category);
                    tempKeywordsCollectionNewTempKeyword = em.merge(tempKeywordsCollectionNewTempKeyword);
                    if (oldIdCategoryOfTempKeywordsCollectionNewTempKeyword != null && !oldIdCategoryOfTempKeywordsCollectionNewTempKeyword.equals(category)) {
                        oldIdCategoryOfTempKeywordsCollectionNewTempKeyword.getTempKeywordsCollection().remove(tempKeywordsCollectionNewTempKeyword);
                        oldIdCategoryOfTempKeywordsCollectionNewTempKeyword = em.merge(oldIdCategoryOfTempKeywordsCollectionNewTempKeyword);
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            Collection<TempKeyword> tempKeywordsCollectionOrphanCheck = category.getTempKeywordsCollection();
            for (TempKeyword tempKeywordsCollectionOrphanCheckTempKeyword : tempKeywordsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Category (" + category + ") cannot be destroyed since the TempKeyword " + tempKeywordsCollectionOrphanCheckTempKeyword + " in its tempKeywordsCollection field has a non-nullable idCategory field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
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
