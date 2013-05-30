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
import model.Vocabulary;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Stems;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class StemsJpaController implements Serializable {

    public StemsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Stems stems) throws PreexistingEntityException, Exception {
        if (stems.getVocabularyCollection() == null) {
            stems.setVocabularyCollection(new ArrayList<Vocabulary>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Vocabulary> attachedVocabularyCollection = new ArrayList<Vocabulary>();
            for (Vocabulary vocabularyCollectionVocabularyToAttach : stems.getVocabularyCollection()) {
                vocabularyCollectionVocabularyToAttach = em.getReference(vocabularyCollectionVocabularyToAttach.getClass(), vocabularyCollectionVocabularyToAttach.getIdVocabulary());
                attachedVocabularyCollection.add(vocabularyCollectionVocabularyToAttach);
            }
            stems.setVocabularyCollection(attachedVocabularyCollection);
            em.persist(stems);
            for (Vocabulary vocabularyCollectionVocabulary : stems.getVocabularyCollection()) {
                Stems oldIdStemOfVocabularyCollectionVocabulary = vocabularyCollectionVocabulary.getIdStem();
                vocabularyCollectionVocabulary.setIdStem(stems);
                vocabularyCollectionVocabulary = em.merge(vocabularyCollectionVocabulary);
                if (oldIdStemOfVocabularyCollectionVocabulary != null) {
                    oldIdStemOfVocabularyCollectionVocabulary.getVocabularyCollection().remove(vocabularyCollectionVocabulary);
                    oldIdStemOfVocabularyCollectionVocabulary = em.merge(oldIdStemOfVocabularyCollectionVocabulary);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findStems(stems.getIdStem()) != null) {
                throw new PreexistingEntityException("Stems " + stems + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Stems stems) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Stems persistentStems = em.find(Stems.class, stems.getIdStem());
            Collection<Vocabulary> vocabularyCollectionOld = persistentStems.getVocabularyCollection();
            Collection<Vocabulary> vocabularyCollectionNew = stems.getVocabularyCollection();
            Collection<Vocabulary> attachedVocabularyCollectionNew = new ArrayList<Vocabulary>();
            for (Vocabulary vocabularyCollectionNewVocabularyToAttach : vocabularyCollectionNew) {
                vocabularyCollectionNewVocabularyToAttach = em.getReference(vocabularyCollectionNewVocabularyToAttach.getClass(), vocabularyCollectionNewVocabularyToAttach.getIdVocabulary());
                attachedVocabularyCollectionNew.add(vocabularyCollectionNewVocabularyToAttach);
            }
            vocabularyCollectionNew = attachedVocabularyCollectionNew;
            stems.setVocabularyCollection(vocabularyCollectionNew);
            stems = em.merge(stems);
            for (Vocabulary vocabularyCollectionOldVocabulary : vocabularyCollectionOld) {
                if (!vocabularyCollectionNew.contains(vocabularyCollectionOldVocabulary)) {
                    vocabularyCollectionOldVocabulary.setIdStem(null);
                    vocabularyCollectionOldVocabulary = em.merge(vocabularyCollectionOldVocabulary);
                }
            }
            for (Vocabulary vocabularyCollectionNewVocabulary : vocabularyCollectionNew) {
                if (!vocabularyCollectionOld.contains(vocabularyCollectionNewVocabulary)) {
                    Stems oldIdStemOfVocabularyCollectionNewVocabulary = vocabularyCollectionNewVocabulary.getIdStem();
                    vocabularyCollectionNewVocabulary.setIdStem(stems);
                    vocabularyCollectionNewVocabulary = em.merge(vocabularyCollectionNewVocabulary);
                    if (oldIdStemOfVocabularyCollectionNewVocabulary != null && !oldIdStemOfVocabularyCollectionNewVocabulary.equals(stems)) {
                        oldIdStemOfVocabularyCollectionNewVocabulary.getVocabularyCollection().remove(vocabularyCollectionNewVocabulary);
                        oldIdStemOfVocabularyCollectionNewVocabulary = em.merge(oldIdStemOfVocabularyCollectionNewVocabulary);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = stems.getIdStem();
                if (findStems(id) == null) {
                    throw new NonexistentEntityException("The stems with id " + id + " no longer exists.");
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
            Stems stems;
            try {
                stems = em.getReference(Stems.class, id);
                stems.getIdStem();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The stems with id " + id + " no longer exists.", enfe);
            }
            Collection<Vocabulary> vocabularyCollection = stems.getVocabularyCollection();
            for (Vocabulary vocabularyCollectionVocabulary : vocabularyCollection) {
                vocabularyCollectionVocabulary.setIdStem(null);
                vocabularyCollectionVocabulary = em.merge(vocabularyCollectionVocabulary);
            }
            em.remove(stems);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Stems> findStemsEntities() {
        return findStemsEntities(true, -1, -1);
    }

    public List<Stems> findStemsEntities(int maxResults, int firstResult) {
        return findStemsEntities(false, maxResults, firstResult);
    }

    private List<Stems> findStemsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Stems.class));
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

    public Stems findStems(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Stems.class, id);
        } finally {
            em.close();
        }
    }

    public int getStemsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Stems> rt = cq.from(Stems.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
