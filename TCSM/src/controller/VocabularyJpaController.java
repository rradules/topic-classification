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
import model.Stems;
import model.Vocabulary;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class VocabularyJpaController implements Serializable {

    public VocabularyJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Vocabulary vocabulary) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Stems idStem = vocabulary.getIdStem();
            if (idStem != null) {
                idStem = em.getReference(idStem.getClass(), idStem.getIdStem());
                vocabulary.setIdStem(idStem);
            }
            em.persist(vocabulary);
            if (idStem != null) {
                idStem.getVocabularyCollection().add(vocabulary);
                idStem = em.merge(idStem);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findVocabulary(vocabulary.getIdVocabulary()) != null) {
                throw new PreexistingEntityException("Vocabulary " + vocabulary + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Vocabulary vocabulary) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vocabulary persistentVocabulary = em.find(Vocabulary.class, vocabulary.getIdVocabulary());
            Stems idStemOld = persistentVocabulary.getIdStem();
            Stems idStemNew = vocabulary.getIdStem();
            if (idStemNew != null) {
                idStemNew = em.getReference(idStemNew.getClass(), idStemNew.getIdStem());
                vocabulary.setIdStem(idStemNew);
            }
            vocabulary = em.merge(vocabulary);
            if (idStemOld != null && !idStemOld.equals(idStemNew)) {
                idStemOld.getVocabularyCollection().remove(vocabulary);
                idStemOld = em.merge(idStemOld);
            }
            if (idStemNew != null && !idStemNew.equals(idStemOld)) {
                idStemNew.getVocabularyCollection().add(vocabulary);
                idStemNew = em.merge(idStemNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = vocabulary.getIdVocabulary();
                if (findVocabulary(id) == null) {
                    throw new NonexistentEntityException("The vocabulary with id " + id + " no longer exists.");
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
            Vocabulary vocabulary;
            try {
                vocabulary = em.getReference(Vocabulary.class, id);
                vocabulary.getIdVocabulary();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vocabulary with id " + id + " no longer exists.", enfe);
            }
            Stems idStem = vocabulary.getIdStem();
            if (idStem != null) {
                idStem.getVocabularyCollection().remove(vocabulary);
                idStem = em.merge(idStem);
            }
            em.remove(vocabulary);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Vocabulary> findVocabularyEntities() {
        return findVocabularyEntities(true, -1, -1);
    }

    public List<Vocabulary> findVocabularyEntities(int maxResults, int firstResult) {
        return findVocabularyEntities(false, maxResults, firstResult);
    }

    private List<Vocabulary> findVocabularyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vocabulary.class));
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

    public Vocabulary findVocabulary(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Vocabulary.class, id);
        } finally {
            em.close();
        }
    }

    public int getVocabularyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Vocabulary> rt = cq.from(Vocabulary.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
