/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.Main;

import Controllers.Main.exceptions.IllegalOrphanException;
import Controllers.Main.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.marm.parkingprogram.Model.Role;
import com.marm.parkingprogram.Model.Operation;
import com.marm.parkingprogram.Model.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author anr10
 */
public class UserJpaController implements Serializable {

    public UserJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(User user) {
        if (user.getOperationList() == null) {
            user.setOperationList(new ArrayList<Operation>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Role roleID = user.getRoleID();
            if (roleID != null) {
                roleID = em.getReference(roleID.getClass(), roleID.getId());
                user.setRoleID(roleID);
            }
            List<Operation> attachedOperationList = new ArrayList<Operation>();
            for (Operation operationListOperationToAttach : user.getOperationList()) {
                operationListOperationToAttach = em.getReference(operationListOperationToAttach.getClass(), operationListOperationToAttach.getOperationPK());
                attachedOperationList.add(operationListOperationToAttach);
            }
            user.setOperationList(attachedOperationList);
            em.persist(user);
            if (roleID != null) {
                roleID.getUserList().add(user);
                roleID = em.merge(roleID);
            }
            for (Operation operationListOperation : user.getOperationList()) {
                User oldUserOfOperationListOperation = operationListOperation.getUser();
                operationListOperation.setUser(user);
                operationListOperation = em.merge(operationListOperation);
                if (oldUserOfOperationListOperation != null) {
                    oldUserOfOperationListOperation.getOperationList().remove(operationListOperation);
                    oldUserOfOperationListOperation = em.merge(oldUserOfOperationListOperation);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(User user) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User persistentUser = em.find(User.class, user.getId());
            Role roleIDOld = persistentUser.getRoleID();
            Role roleIDNew = user.getRoleID();
            List<Operation> operationListOld = persistentUser.getOperationList();
            List<Operation> operationListNew = user.getOperationList();
            List<String> illegalOrphanMessages = null;
            for (Operation operationListOldOperation : operationListOld) {
                if (!operationListNew.contains(operationListOldOperation)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Operation " + operationListOldOperation + " since its user field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (roleIDNew != null) {
                roleIDNew = em.getReference(roleIDNew.getClass(), roleIDNew.getId());
                user.setRoleID(roleIDNew);
            }
            List<Operation> attachedOperationListNew = new ArrayList<Operation>();
            for (Operation operationListNewOperationToAttach : operationListNew) {
                operationListNewOperationToAttach = em.getReference(operationListNewOperationToAttach.getClass(), operationListNewOperationToAttach.getOperationPK());
                attachedOperationListNew.add(operationListNewOperationToAttach);
            }
            operationListNew = attachedOperationListNew;
            user.setOperationList(operationListNew);
            user = em.merge(user);
            if (roleIDOld != null && !roleIDOld.equals(roleIDNew)) {
                roleIDOld.getUserList().remove(user);
                roleIDOld = em.merge(roleIDOld);
            }
            if (roleIDNew != null && !roleIDNew.equals(roleIDOld)) {
                roleIDNew.getUserList().add(user);
                roleIDNew = em.merge(roleIDNew);
            }
            for (Operation operationListNewOperation : operationListNew) {
                if (!operationListOld.contains(operationListNewOperation)) {
                    User oldUserOfOperationListNewOperation = operationListNewOperation.getUser();
                    operationListNewOperation.setUser(user);
                    operationListNewOperation = em.merge(operationListNewOperation);
                    if (oldUserOfOperationListNewOperation != null && !oldUserOfOperationListNewOperation.equals(user)) {
                        oldUserOfOperationListNewOperation.getOperationList().remove(operationListNewOperation);
                        oldUserOfOperationListNewOperation = em.merge(oldUserOfOperationListNewOperation);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = user.getId();
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Operation> operationListOrphanCheck = user.getOperationList();
            for (Operation operationListOrphanCheckOperation : operationListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Operation " + operationListOrphanCheckOperation + " in its operationList field has a non-nullable user field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Role roleID = user.getRoleID();
            if (roleID != null) {
                roleID.getUserList().remove(user);
                roleID = em.merge(roleID);
            }
            em.remove(user);
            em.getTransaction().commit();
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

    public User findUser(Integer id) {
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
