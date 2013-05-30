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
import model.Stem;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class StemJpaController implements Serializable {

    public StemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Stem stem) throws PreexistingEntityException, Exception {
        if (stem.getVocabularyCollection() == null) {
            stem.setVocabularyCollection(new ArrayList<Vocabulary>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Vocabulary> attachedVocabularyCollection = new ArrayList<Vocabulary>();
            for (Vocabulary vocabularyCollectionVocabularyToAttach : stem.getVocabularyCollection()) {
                vocabularyCollectionVocabularyToAttach = em.getReference(vocabularyCollectionVocabularyToAttach.getClass(), vocabularyCollectionVocabularyToAttach.getIdVocabulary());
                attachedVocabularyCollection.add(vocabularyCollectionVocabularyToAttach);
            }
            stem.setVocabularyCollection(attachedVocabularyCollection);
            em.persist(stem);
            for (Vocabulary vocabularyCollectionVocabulary : stem.getVocabularyCollection()) {
                Stem oldIdStemOfVocabularyCollectionVocabulary = vocabularyCollectionVocabulary.getIdStem();
                vocabularyCollectionVocabulary.setIdStem(stem);
                vocabularyCollectionVocabulary = em.merge(vocabularyCollectionVocabulary);
                if (oldIdStemOfVocabularyCollectionVocabulary != null) {
                    oldIdStemOfVocabularyCollectionVocabulary.getVocabularyCollection().remove(vocabularyCollectionVocabulary);
                    oldIdStemOfVocabularyCollectionVocabulary = em.merge(oldIdStemOfVocabularyCollectionVocabulary);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findStem(stem.getIdStem()) != null) {
                throw new PreexistingEntityException("Stem " + stem + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Stem stem) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Stem persistentStem = em.find(Stem.class, stem.getIdStem());
            Collection<Vocabulary> vocabularyCollectionOld = persistentStem.getVocabularyCollection();
            Collection<Vocabulary> vocabularyCollectionNew = stem.getVocabularyCollection();
            Collection<Vocabulary> attachedVocabularyCollectionNew = new ArrayList<Vocabulary>();
            for (Vocabulary vocabularyCollectionNewVocabularyToAttach : vocabularyCollectionNew) {
                vocabularyCollectionNewVocabularyToAttach = em.getReference(vocabularyCollectionNewVocabularyToAttach.getClass(), vocabularyCollectionNewVocabularyToAttach.getIdVocabulary());
                attachedVocabularyCollectionNew.add(vocabularyCollectionNewVocabularyToAttach);
            }
            vocabularyCollectionNew = attachedVocabularyCollectionNew;
            stem.setVocabularyCollection(vocabularyCollectionNew);
            stem = em.merge(stem);
            for (Vocabulary vocabularyCollectionOldVocabulary : vocabularyCollectionOld) {
                if (!vocabularyCollectionNew.contains(vocabularyCollectionOldVocabulary)) {
                    vocabularyCollectionOldVocabulary.setIdStem(null);
                    vocabularyCollectionOldVocabulary = em.merge(vocabularyCollectionOldVocabulary);
                }
            }
            for (Vocabulary vocabularyCollectionNewVocabulary : vocabularyCollectionNew) {
                if (!vocabularyCollectionOld.contains(vocabularyCollectionNewVocabulary)) {
                    Stem oldIdStemOfVocabularyCollectionNewVocabulary = vocabularyCollectionNewVocabulary.getIdStem();
                    vocabularyCollectionNewVocabulary.setIdStem(stem);
                    vocabularyCollectionNewVocabulary = em.merge(vocabularyCollectionNewVocabulary);
                    if (oldIdStemOfVocabularyCollectionNewVocabulary != null && !oldIdStemOfVocabularyCollectionNewVocabulary.equals(stem)) {
                        oldIdStemOfVocabularyCollectionNewVocabulary.getVocabularyCollection().remove(vocabularyCollectionNewVocabulary);
                        oldIdStemOfVocabularyCollectionNewVocabulary = em.merge(oldIdStemOfVocabularyCollectionNewVocabulary);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = stem.getIdStem();
                if (findStem(id) == null) {
                    throw new NonexistentEntityException("The stem with id " + id + " no longer exists.");
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
            Stem stem;
            try {
                stem = em.getReference(Stem.class, id);
                stem.getIdStem();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The stem with id " + id + " no longer exists.", enfe);
            }
            Collection<Vocabulary> vocabularyCollection = stem.getVocabularyCollection();
            for (Vocabulary vocabularyCollectionVocabulary : vocabularyCollection) {
                vocabularyCollectionVocabulary.setIdStem(null);
                vocabularyCollectionVocabulary = em.merge(vocabularyCollectionVocabulary);
            }
            em.remove(stem);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Stem> findStemEntities() {
        return findStemEntities(true, -1, -1);
    }

    public List<Stem> findStemEntities(int maxResults, int firstResult) {
        return findStemEntities(false, maxResults, firstResult);
    }

    private List<Stem> findStemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Stem.class));
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

    public Stem findStem(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Stem.class, id);
        } finally {
            em.close();
        }
    }

    public int getStemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Stem> rt = cq.from(Stem.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
