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
import com.marm.parkingprogram.Model.Operation;
import com.marm.parkingprogram.Model.ParkingSpace;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author anr10
 */
public class ParkingSpaceJpaController implements Serializable {

    public ParkingSpaceJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ParkingSpace parkingSpace) {
        if (parkingSpace.getOperationList() == null) {
            parkingSpace.setOperationList(new ArrayList<Operation>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Operation> attachedOperationList = new ArrayList<Operation>();
            for (Operation operationListOperationToAttach : parkingSpace.getOperationList()) {
                operationListOperationToAttach = em.getReference(operationListOperationToAttach.getClass(), operationListOperationToAttach.getOperationPK());
                attachedOperationList.add(operationListOperationToAttach);
            }
            parkingSpace.setOperationList(attachedOperationList);
            em.persist(parkingSpace);
            for (Operation operationListOperation : parkingSpace.getOperationList()) {
                ParkingSpace oldParkingSpaceOfOperationListOperation = operationListOperation.getParkingSpace();
                operationListOperation.setParkingSpace(parkingSpace);
                operationListOperation = em.merge(operationListOperation);
                if (oldParkingSpaceOfOperationListOperation != null) {
                    oldParkingSpaceOfOperationListOperation.getOperationList().remove(operationListOperation);
                    oldParkingSpaceOfOperationListOperation = em.merge(oldParkingSpaceOfOperationListOperation);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ParkingSpace parkingSpace) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ParkingSpace persistentParkingSpace = em.find(ParkingSpace.class, parkingSpace.getId());
            List<Operation> operationListOld = persistentParkingSpace.getOperationList();
            List<Operation> operationListNew = parkingSpace.getOperationList();
            List<String> illegalOrphanMessages = null;
            for (Operation operationListOldOperation : operationListOld) {
                if (!operationListNew.contains(operationListOldOperation)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Operation " + operationListOldOperation + " since its parkingSpace field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Operation> attachedOperationListNew = new ArrayList<Operation>();
            for (Operation operationListNewOperationToAttach : operationListNew) {
                operationListNewOperationToAttach = em.getReference(operationListNewOperationToAttach.getClass(), operationListNewOperationToAttach.getOperationPK());
                attachedOperationListNew.add(operationListNewOperationToAttach);
            }
            operationListNew = attachedOperationListNew;
            parkingSpace.setOperationList(operationListNew);
            parkingSpace = em.merge(parkingSpace);
            for (Operation operationListNewOperation : operationListNew) {
                if (!operationListOld.contains(operationListNewOperation)) {
                    ParkingSpace oldParkingSpaceOfOperationListNewOperation = operationListNewOperation.getParkingSpace();
                    operationListNewOperation.setParkingSpace(parkingSpace);
                    operationListNewOperation = em.merge(operationListNewOperation);
                    if (oldParkingSpaceOfOperationListNewOperation != null && !oldParkingSpaceOfOperationListNewOperation.equals(parkingSpace)) {
                        oldParkingSpaceOfOperationListNewOperation.getOperationList().remove(operationListNewOperation);
                        oldParkingSpaceOfOperationListNewOperation = em.merge(oldParkingSpaceOfOperationListNewOperation);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = parkingSpace.getId();
                if (findParkingSpace(id) == null) {
                    throw new NonexistentEntityException("The parkingSpace with id " + id + " no longer exists.");
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
            ParkingSpace parkingSpace;
            try {
                parkingSpace = em.getReference(ParkingSpace.class, id);
                parkingSpace.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The parkingSpace with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Operation> operationListOrphanCheck = parkingSpace.getOperationList();
            for (Operation operationListOrphanCheckOperation : operationListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ParkingSpace (" + parkingSpace + ") cannot be destroyed since the Operation " + operationListOrphanCheckOperation + " in its operationList field has a non-nullable parkingSpace field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(parkingSpace);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ParkingSpace> findParkingSpaceEntities() {
        return findParkingSpaceEntities(true, -1, -1);
    }

    public List<ParkingSpace> findParkingSpaceEntities(int maxResults, int firstResult) {
        return findParkingSpaceEntities(false, maxResults, firstResult);
    }

    private List<ParkingSpace> findParkingSpaceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ParkingSpace.class));
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

    public ParkingSpace findParkingSpace(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ParkingSpace.class, id);
        } finally {
            em.close();
        }
    }

    public int getParkingSpaceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ParkingSpace> rt = cq.from(ParkingSpace.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
