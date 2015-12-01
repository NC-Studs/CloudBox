/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.File;
import entity.User;
import entity.UserFiles;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import jpa.exceptions.NonexistentEntityException;
import jpa.exceptions.PreexistingEntityException;
import jpa.exceptions.RollbackFailureException;

/**
 *
 * @author victori
 */
public class UserFilesJpaController implements Serializable {

    public UserFilesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserFiles userFiles) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            File idFile = userFiles.getIdFile();
            if (idFile != null) {
                idFile = em.getReference(idFile.getClass(), idFile.getIdFile());
                userFiles.setIdFile(idFile);
            }
            User idUser = userFiles.getIdUser();
            if (idUser != null) {
                idUser = em.getReference(idUser.getClass(), idUser.getIdUser());
                userFiles.setIdUser(idUser);
            }
            em.persist(userFiles);
            if (idFile != null) {
                idFile.getUserFilesCollection().add(userFiles);
                idFile = em.merge(idFile);
            }
            if (idUser != null) {
                idUser.getUserFilesCollection().add(userFiles);
                idUser = em.merge(idUser);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUserFiles(userFiles.getId()) != null) {
                throw new PreexistingEntityException("UserFiles " + userFiles + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserFiles userFiles) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UserFiles persistentUserFiles = em.find(UserFiles.class, userFiles.getId());
            File idFileOld = persistentUserFiles.getIdFile();
            File idFileNew = userFiles.getIdFile();
            User idUserOld = persistentUserFiles.getIdUser();
            User idUserNew = userFiles.getIdUser();
            if (idFileNew != null) {
                idFileNew = em.getReference(idFileNew.getClass(), idFileNew.getIdFile());
                userFiles.setIdFile(idFileNew);
            }
            if (idUserNew != null) {
                idUserNew = em.getReference(idUserNew.getClass(), idUserNew.getIdUser());
                userFiles.setIdUser(idUserNew);
            }
            userFiles = em.merge(userFiles);
            if (idFileOld != null && !idFileOld.equals(idFileNew)) {
                idFileOld.getUserFilesCollection().remove(userFiles);
                idFileOld = em.merge(idFileOld);
            }
            if (idFileNew != null && !idFileNew.equals(idFileOld)) {
                idFileNew.getUserFilesCollection().add(userFiles);
                idFileNew = em.merge(idFileNew);
            }
            if (idUserOld != null && !idUserOld.equals(idUserNew)) {
                idUserOld.getUserFilesCollection().remove(userFiles);
                idUserOld = em.merge(idUserOld);
            }
            if (idUserNew != null && !idUserNew.equals(idUserOld)) {
                idUserNew.getUserFilesCollection().add(userFiles);
                idUserNew = em.merge(idUserNew);
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
                BigDecimal id = userFiles.getId();
                if (findUserFiles(id) == null) {
                    throw new NonexistentEntityException("The userFiles with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UserFiles userFiles;
            try {
                userFiles = em.getReference(UserFiles.class, id);
                userFiles.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userFiles with id " + id + " no longer exists.", enfe);
            }
            File idFile = userFiles.getIdFile();
            if (idFile != null) {
                idFile.getUserFilesCollection().remove(userFiles);
                idFile = em.merge(idFile);
            }
            User idUser = userFiles.getIdUser();
            if (idUser != null) {
                idUser.getUserFilesCollection().remove(userFiles);
                idUser = em.merge(idUser);
            }
            em.remove(userFiles);
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

    public List<UserFiles> findUserFilesEntities() {
        return findUserFilesEntities(true, -1, -1);
    }

    public List<UserFiles> findUserFilesEntities(int maxResults, int firstResult) {
        return findUserFilesEntities(false, maxResults, firstResult);
    }

    private List<UserFiles> findUserFilesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserFiles.class));
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

    public UserFiles findUserFiles(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserFiles.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserFilesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserFiles> rt = cq.from(UserFiles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
