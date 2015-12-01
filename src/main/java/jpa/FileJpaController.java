/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import entity.File;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.User;
import entity.UserFiles;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import jpa.exceptions.IllegalOrphanException;
import jpa.exceptions.NonexistentEntityException;
import jpa.exceptions.PreexistingEntityException;
import jpa.exceptions.RollbackFailureException;

/**
 *
 * @author victori
 */
public class FileJpaController implements Serializable {

    public FileJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(File file) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (file.getUserFilesCollection() == null) {
            file.setUserFilesCollection(new ArrayList<UserFiles>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            User idUser = file.getIdUser();
            if (idUser != null) {
                idUser = em.getReference(idUser.getClass(), idUser.getIdUser());
                file.setIdUser(idUser);
            }
            Collection<UserFiles> attachedUserFilesCollection = new ArrayList<UserFiles>();
            for (UserFiles userFilesCollectionUserFilesToAttach : file.getUserFilesCollection()) {
                userFilesCollectionUserFilesToAttach = em.getReference(userFilesCollectionUserFilesToAttach.getClass(), userFilesCollectionUserFilesToAttach.getId());
                attachedUserFilesCollection.add(userFilesCollectionUserFilesToAttach);
            }
            file.setUserFilesCollection(attachedUserFilesCollection);
            em.persist(file);
            if (idUser != null) {
                idUser.getFileCollection().add(file);
                idUser = em.merge(idUser);
            }
            for (UserFiles userFilesCollectionUserFiles : file.getUserFilesCollection()) {
                File oldIdFileOfUserFilesCollectionUserFiles = userFilesCollectionUserFiles.getIdFile();
                userFilesCollectionUserFiles.setIdFile(file);
                userFilesCollectionUserFiles = em.merge(userFilesCollectionUserFiles);
                if (oldIdFileOfUserFilesCollectionUserFiles != null) {
                    oldIdFileOfUserFilesCollectionUserFiles.getUserFilesCollection().remove(userFilesCollectionUserFiles);
                    oldIdFileOfUserFilesCollectionUserFiles = em.merge(oldIdFileOfUserFilesCollectionUserFiles);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findFile(file.getIdFile()) != null) {
                throw new PreexistingEntityException("File " + file + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(File file) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            File persistentFile = em.find(File.class, file.getIdFile());
            User idUserOld = persistentFile.getIdUser();
            User idUserNew = file.getIdUser();
            Collection<UserFiles> userFilesCollectionOld = persistentFile.getUserFilesCollection();
            Collection<UserFiles> userFilesCollectionNew = file.getUserFilesCollection();
            List<String> illegalOrphanMessages = null;
            for (UserFiles userFilesCollectionOldUserFiles : userFilesCollectionOld) {
                if (!userFilesCollectionNew.contains(userFilesCollectionOldUserFiles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UserFiles " + userFilesCollectionOldUserFiles + " since its idFile field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUserNew != null) {
                idUserNew = em.getReference(idUserNew.getClass(), idUserNew.getIdUser());
                file.setIdUser(idUserNew);
            }
            Collection<UserFiles> attachedUserFilesCollectionNew = new ArrayList<UserFiles>();
            for (UserFiles userFilesCollectionNewUserFilesToAttach : userFilesCollectionNew) {
                userFilesCollectionNewUserFilesToAttach = em.getReference(userFilesCollectionNewUserFilesToAttach.getClass(), userFilesCollectionNewUserFilesToAttach.getId());
                attachedUserFilesCollectionNew.add(userFilesCollectionNewUserFilesToAttach);
            }
            userFilesCollectionNew = attachedUserFilesCollectionNew;
            file.setUserFilesCollection(userFilesCollectionNew);
            file = em.merge(file);
            if (idUserOld != null && !idUserOld.equals(idUserNew)) {
                idUserOld.getFileCollection().remove(file);
                idUserOld = em.merge(idUserOld);
            }
            if (idUserNew != null && !idUserNew.equals(idUserOld)) {
                idUserNew.getFileCollection().add(file);
                idUserNew = em.merge(idUserNew);
            }
            for (UserFiles userFilesCollectionNewUserFiles : userFilesCollectionNew) {
                if (!userFilesCollectionOld.contains(userFilesCollectionNewUserFiles)) {
                    File oldIdFileOfUserFilesCollectionNewUserFiles = userFilesCollectionNewUserFiles.getIdFile();
                    userFilesCollectionNewUserFiles.setIdFile(file);
                    userFilesCollectionNewUserFiles = em.merge(userFilesCollectionNewUserFiles);
                    if (oldIdFileOfUserFilesCollectionNewUserFiles != null && !oldIdFileOfUserFilesCollectionNewUserFiles.equals(file)) {
                        oldIdFileOfUserFilesCollectionNewUserFiles.getUserFilesCollection().remove(userFilesCollectionNewUserFiles);
                        oldIdFileOfUserFilesCollectionNewUserFiles = em.merge(oldIdFileOfUserFilesCollectionNewUserFiles);
                    }
                }
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
                BigDecimal id = file.getIdFile();
                if (findFile(id) == null) {
                    throw new NonexistentEntityException("The file with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            File file;
            try {
                file = em.getReference(File.class, id);
                file.getIdFile();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The file with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<UserFiles> userFilesCollectionOrphanCheck = file.getUserFilesCollection();
            for (UserFiles userFilesCollectionOrphanCheckUserFiles : userFilesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This File (" + file + ") cannot be destroyed since the UserFiles " + userFilesCollectionOrphanCheckUserFiles + " in its userFilesCollection field has a non-nullable idFile field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User idUser = file.getIdUser();
            if (idUser != null) {
                idUser.getFileCollection().remove(file);
                idUser = em.merge(idUser);
            }
            em.remove(file);
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

    public List<File> findFileEntities() {
        return findFileEntities(true, -1, -1);
    }

    public List<File> findFileEntities(int maxResults, int firstResult) {
        return findFileEntities(false, maxResults, firstResult);
    }

    private List<File> findFileEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(File.class));
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

    public File findFile(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(File.class, id);
        } finally {
            em.close();
        }
    }

    public int getFileCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<File> rt = cq.from(File.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
