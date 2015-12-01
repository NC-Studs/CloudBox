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
import entity.UserFiles;
import java.util.ArrayList;
import java.util.Collection;
import entity.File;
import entity.User;
import entity.UserRole;
import java.math.BigDecimal;
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
public class UserJpaController implements Serializable {

    public UserJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(User user) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (user.getUserFilesCollection() == null) {
            user.setUserFilesCollection(new ArrayList<UserFiles>());
        }
        if (user.getFileCollection() == null) {
            user.setFileCollection(new ArrayList<File>());
        }
        if (user.getUserRoleCollection() == null) {
            user.setUserRoleCollection(new ArrayList<UserRole>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<UserFiles> attachedUserFilesCollection = new ArrayList<UserFiles>();
            for (UserFiles userFilesCollectionUserFilesToAttach : user.getUserFilesCollection()) {
                userFilesCollectionUserFilesToAttach = em.getReference(userFilesCollectionUserFilesToAttach.getClass(), userFilesCollectionUserFilesToAttach.getId());
                attachedUserFilesCollection.add(userFilesCollectionUserFilesToAttach);
            }
            user.setUserFilesCollection(attachedUserFilesCollection);
            Collection<File> attachedFileCollection = new ArrayList<File>();
            for (File fileCollectionFileToAttach : user.getFileCollection()) {
                fileCollectionFileToAttach = em.getReference(fileCollectionFileToAttach.getClass(), fileCollectionFileToAttach.getIdFile());
                attachedFileCollection.add(fileCollectionFileToAttach);
            }
            user.setFileCollection(attachedFileCollection);
            Collection<UserRole> attachedUserRoleCollection = new ArrayList<UserRole>();
            for (UserRole userRoleCollectionUserRoleToAttach : user.getUserRoleCollection()) {
                userRoleCollectionUserRoleToAttach = em.getReference(userRoleCollectionUserRoleToAttach.getClass(), userRoleCollectionUserRoleToAttach.getId());
                attachedUserRoleCollection.add(userRoleCollectionUserRoleToAttach);
            }
            user.setUserRoleCollection(attachedUserRoleCollection);
            em.persist(user);
            for (UserFiles userFilesCollectionUserFiles : user.getUserFilesCollection()) {
                User oldIdUserOfUserFilesCollectionUserFiles = userFilesCollectionUserFiles.getIdUser();
                userFilesCollectionUserFiles.setIdUser(user);
                userFilesCollectionUserFiles = em.merge(userFilesCollectionUserFiles);
                if (oldIdUserOfUserFilesCollectionUserFiles != null) {
                    oldIdUserOfUserFilesCollectionUserFiles.getUserFilesCollection().remove(userFilesCollectionUserFiles);
                    oldIdUserOfUserFilesCollectionUserFiles = em.merge(oldIdUserOfUserFilesCollectionUserFiles);
                }
            }
            for (File fileCollectionFile : user.getFileCollection()) {
                User oldIdUserOfFileCollectionFile = fileCollectionFile.getIdUser();
                fileCollectionFile.setIdUser(user);
                fileCollectionFile = em.merge(fileCollectionFile);
                if (oldIdUserOfFileCollectionFile != null) {
                    oldIdUserOfFileCollectionFile.getFileCollection().remove(fileCollectionFile);
                    oldIdUserOfFileCollectionFile = em.merge(oldIdUserOfFileCollectionFile);
                }
            }
            for (UserRole userRoleCollectionUserRole : user.getUserRoleCollection()) {
                User oldIdUserOfUserRoleCollectionUserRole = userRoleCollectionUserRole.getIdUser();
                userRoleCollectionUserRole.setIdUser(user);
                userRoleCollectionUserRole = em.merge(userRoleCollectionUserRole);
                if (oldIdUserOfUserRoleCollectionUserRole != null) {
                    oldIdUserOfUserRoleCollectionUserRole.getUserRoleCollection().remove(userRoleCollectionUserRole);
                    oldIdUserOfUserRoleCollectionUserRole = em.merge(oldIdUserOfUserRoleCollectionUserRole);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUser(user.getIdUser()) != null) {
                throw new PreexistingEntityException("User " + user + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(User user) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            User persistentUser = em.find(User.class, user.getIdUser());
            Collection<UserFiles> userFilesCollectionOld = persistentUser.getUserFilesCollection();
            Collection<UserFiles> userFilesCollectionNew = user.getUserFilesCollection();
            Collection<File> fileCollectionOld = persistentUser.getFileCollection();
            Collection<File> fileCollectionNew = user.getFileCollection();
            Collection<UserRole> userRoleCollectionOld = persistentUser.getUserRoleCollection();
            Collection<UserRole> userRoleCollectionNew = user.getUserRoleCollection();
            List<String> illegalOrphanMessages = null;
            for (UserFiles userFilesCollectionOldUserFiles : userFilesCollectionOld) {
                if (!userFilesCollectionNew.contains(userFilesCollectionOldUserFiles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UserFiles " + userFilesCollectionOldUserFiles + " since its idUser field is not nullable.");
                }
            }
            for (File fileCollectionOldFile : fileCollectionOld) {
                if (!fileCollectionNew.contains(fileCollectionOldFile)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain File " + fileCollectionOldFile + " since its idUser field is not nullable.");
                }
            }
            for (UserRole userRoleCollectionOldUserRole : userRoleCollectionOld) {
                if (!userRoleCollectionNew.contains(userRoleCollectionOldUserRole)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UserRole " + userRoleCollectionOldUserRole + " since its idUser field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<UserFiles> attachedUserFilesCollectionNew = new ArrayList<UserFiles>();
            for (UserFiles userFilesCollectionNewUserFilesToAttach : userFilesCollectionNew) {
                userFilesCollectionNewUserFilesToAttach = em.getReference(userFilesCollectionNewUserFilesToAttach.getClass(), userFilesCollectionNewUserFilesToAttach.getId());
                attachedUserFilesCollectionNew.add(userFilesCollectionNewUserFilesToAttach);
            }
            userFilesCollectionNew = attachedUserFilesCollectionNew;
            user.setUserFilesCollection(userFilesCollectionNew);
            Collection<File> attachedFileCollectionNew = new ArrayList<File>();
            for (File fileCollectionNewFileToAttach : fileCollectionNew) {
                fileCollectionNewFileToAttach = em.getReference(fileCollectionNewFileToAttach.getClass(), fileCollectionNewFileToAttach.getIdFile());
                attachedFileCollectionNew.add(fileCollectionNewFileToAttach);
            }
            fileCollectionNew = attachedFileCollectionNew;
            user.setFileCollection(fileCollectionNew);
            Collection<UserRole> attachedUserRoleCollectionNew = new ArrayList<UserRole>();
            for (UserRole userRoleCollectionNewUserRoleToAttach : userRoleCollectionNew) {
                userRoleCollectionNewUserRoleToAttach = em.getReference(userRoleCollectionNewUserRoleToAttach.getClass(), userRoleCollectionNewUserRoleToAttach.getId());
                attachedUserRoleCollectionNew.add(userRoleCollectionNewUserRoleToAttach);
            }
            userRoleCollectionNew = attachedUserRoleCollectionNew;
            user.setUserRoleCollection(userRoleCollectionNew);
            user = em.merge(user);
            for (UserFiles userFilesCollectionNewUserFiles : userFilesCollectionNew) {
                if (!userFilesCollectionOld.contains(userFilesCollectionNewUserFiles)) {
                    User oldIdUserOfUserFilesCollectionNewUserFiles = userFilesCollectionNewUserFiles.getIdUser();
                    userFilesCollectionNewUserFiles.setIdUser(user);
                    userFilesCollectionNewUserFiles = em.merge(userFilesCollectionNewUserFiles);
                    if (oldIdUserOfUserFilesCollectionNewUserFiles != null && !oldIdUserOfUserFilesCollectionNewUserFiles.equals(user)) {
                        oldIdUserOfUserFilesCollectionNewUserFiles.getUserFilesCollection().remove(userFilesCollectionNewUserFiles);
                        oldIdUserOfUserFilesCollectionNewUserFiles = em.merge(oldIdUserOfUserFilesCollectionNewUserFiles);
                    }
                }
            }
            for (File fileCollectionNewFile : fileCollectionNew) {
                if (!fileCollectionOld.contains(fileCollectionNewFile)) {
                    User oldIdUserOfFileCollectionNewFile = fileCollectionNewFile.getIdUser();
                    fileCollectionNewFile.setIdUser(user);
                    fileCollectionNewFile = em.merge(fileCollectionNewFile);
                    if (oldIdUserOfFileCollectionNewFile != null && !oldIdUserOfFileCollectionNewFile.equals(user)) {
                        oldIdUserOfFileCollectionNewFile.getFileCollection().remove(fileCollectionNewFile);
                        oldIdUserOfFileCollectionNewFile = em.merge(oldIdUserOfFileCollectionNewFile);
                    }
                }
            }
            for (UserRole userRoleCollectionNewUserRole : userRoleCollectionNew) {
                if (!userRoleCollectionOld.contains(userRoleCollectionNewUserRole)) {
                    User oldIdUserOfUserRoleCollectionNewUserRole = userRoleCollectionNewUserRole.getIdUser();
                    userRoleCollectionNewUserRole.setIdUser(user);
                    userRoleCollectionNewUserRole = em.merge(userRoleCollectionNewUserRole);
                    if (oldIdUserOfUserRoleCollectionNewUserRole != null && !oldIdUserOfUserRoleCollectionNewUserRole.equals(user)) {
                        oldIdUserOfUserRoleCollectionNewUserRole.getUserRoleCollection().remove(userRoleCollectionNewUserRole);
                        oldIdUserOfUserRoleCollectionNewUserRole = em.merge(oldIdUserOfUserRoleCollectionNewUserRole);
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
                BigDecimal id = user.getIdUser();
                if (findUser(id) == null) {
                    throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
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
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getIdUser();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<UserFiles> userFilesCollectionOrphanCheck = user.getUserFilesCollection();
            for (UserFiles userFilesCollectionOrphanCheckUserFiles : userFilesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the UserFiles " + userFilesCollectionOrphanCheckUserFiles + " in its userFilesCollection field has a non-nullable idUser field.");
            }
            Collection<File> fileCollectionOrphanCheck = user.getFileCollection();
            for (File fileCollectionOrphanCheckFile : fileCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the File " + fileCollectionOrphanCheckFile + " in its fileCollection field has a non-nullable idUser field.");
            }
            Collection<UserRole> userRoleCollectionOrphanCheck = user.getUserRoleCollection();
            for (UserRole userRoleCollectionOrphanCheckUserRole : userRoleCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the UserRole " + userRoleCollectionOrphanCheckUserRole + " in its userRoleCollection field has a non-nullable idUser field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(user);
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

    public List<User> findUserEntities() {
        return findUserEntities(true, -1, -1);
    }

    public List<User> findUserEntities(int maxResults, int firstResult) {
        return findUserEntities(false, maxResults, firstResult);
    }

    private List<User> findUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
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

    public User findUser(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
