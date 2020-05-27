/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.Main;

import Controllers.Main.exceptions.NonexistentEntityException;
import Controllers.Main.exceptions.PreexistingEntityException;
import com.marm.parkingprogram.Model.Operation;
import com.marm.parkingprogram.Model.OperationPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.marm.parkingprogram.Model.ParkingSpace;
import com.marm.parkingprogram.Model.User;
import com.marm.parkingprogram.Model.Vehicle;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author anr10
 */
public class OperationJpaController implements Serializable {

    public OperationJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Operation operation) throws PreexistingEntityException, Exception {
        if (operation.getOperationPK() == null) {
            operation.setOperationPK(new OperationPK());
        }
        operation.getOperationPK().setParkingSpaceID(operation.getParkingSpace().getId());
        operation.getOperationPK().setVehicleID(operation.getVehicle().getId());
        operation.getOperationPK().setUserID(operation.getUser().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ParkingSpace parkingSpace = operation.getParkingSpace();
            if (parkingSpace != null) {
                parkingSpace = em.getReference(parkingSpace.getClass(), parkingSpace.getId());
                operation.setParkingSpace(parkingSpace);
            }
            User user = operation.getUser();
            if (user != null) {
                user = em.getReference(user.getClass(), user.getId());
                operation.setUser(user);
            }
            Vehicle vehicle = operation.getVehicle();
            if (vehicle != null) {
                vehicle = em.getReference(vehicle.getClass(), vehicle.getId());
                operation.setVehicle(vehicle);
            }
            em.persist(operation);
            if (parkingSpace != null) {
                parkingSpace.getOperationList().add(operation);
                parkingSpace = em.merge(parkingSpace);
            }
            if (user != null) {
                user.getOperationList().add(operation);
                user = em.merge(user);
            }
            if (vehicle != null) {
                vehicle.getOperationList().add(operation);
                vehicle = em.merge(vehicle);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOperation(operation.getOperationPK()) != null) {
                throw new PreexistingEntityException("Operation " + operation + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Operation operation) throws NonexistentEntityException, Exception {
        operation.getOperationPK().setParkingSpaceID(operation.getParkingSpace().getId());
        operation.getOperationPK().setVehicleID(operation.getVehicle().getId());
        operation.getOperationPK().setUserID(operation.getUser().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Operation persistentOperation = em.find(Operation.class, operation.getOperationPK());
            ParkingSpace parkingSpaceOld = persistentOperation.getParkingSpace();
            ParkingSpace parkingSpaceNew = operation.getParkingSpace();
            User userOld = persistentOperation.getUser();
            User userNew = operation.getUser();
            Vehicle vehicleOld = persistentOperation.getVehicle();
            Vehicle vehicleNew = operation.getVehicle();
            if (parkingSpaceNew != null) {
                parkingSpaceNew = em.getReference(parkingSpaceNew.getClass(), parkingSpaceNew.getId());
                operation.setParkingSpace(parkingSpaceNew);
            }
            if (userNew != null) {
                userNew = em.getReference(userNew.getClass(), userNew.getId());
                operation.setUser(userNew);
            }
            if (vehicleNew != null) {
                vehicleNew = em.getReference(vehicleNew.getClass(), vehicleNew.getId());
                operation.setVehicle(vehicleNew);
            }
            operation = em.merge(operation);
            if (parkingSpaceOld != null && !parkingSpaceOld.equals(parkingSpaceNew)) {
                parkingSpaceOld.getOperationList().remove(operation);
                parkingSpaceOld = em.merge(parkingSpaceOld);
            }
            if (parkingSpaceNew != null && !parkingSpaceNew.equals(parkingSpaceOld)) {
                parkingSpaceNew.getOperationList().add(operation);
                parkingSpaceNew = em.merge(parkingSpaceNew);
            }
            if (userOld != null && !userOld.equals(userNew)) {
                userOld.getOperationList().remove(operation);
                userOld = em.merge(userOld);
            }
            if (userNew != null && !userNew.equals(userOld)) {
                userNew.getOperationList().add(operation);
                userNew = em.merge(userNew);
            }
            if (vehicleOld != null && !vehicleOld.equals(vehicleNew)) {
                vehicleOld.getOperationList().remove(operation);
                vehicleOld = em.merge(vehicleOld);
            }
            if (vehicleNew != null && !vehicleNew.equals(vehicleOld)) {
                vehicleNew.getOperationList().add(operation);
                vehicleNew = em.merge(vehicleNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                OperationPK id = operation.getOperationPK();
                if (findOperation(id) == null) {
                    throw new NonexistentEntityException("The operation with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(OperationPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Operation operation;
            try {
                operation = em.getReference(Operation.class, id);
                operation.getOperationPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The operation with id " + id + " no longer exists.", enfe);
            }
            ParkingSpace parkingSpace = operation.getParkingSpace();
            if (parkingSpace != null) {
                parkingSpace.getOperationList().remove(operation);
                parkingSpace = em.merge(parkingSpace);
            }
            User user = operation.getUser();
            if (user != null) {
                user.getOperationList().remove(operation);
                user = em.merge(user);
            }
            Vehicle vehicle = operation.getVehicle();
            if (vehicle != null) {
                vehicle.getOperationList().remove(operation);
                vehicle = em.merge(vehicle);
            }
            em.remove(operation);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Operation> findOperationEntities() {
        return findOperationEntities(true, -1, -1);
    }

    public List<Operation> findOperationEntities(int maxResults, int firstResult) {
        return findOperationEntities(false, maxResults, firstResult);
    }

    private List<Operation> findOperationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Operation.class));
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

    public Operation findOperation(OperationPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Operation.class, id);
        } finally {
            em.close();
        }
    }

    public int getOperationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Operation> rt = cq.from(Operation.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
