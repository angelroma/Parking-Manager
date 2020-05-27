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
import com.marm.parkingprogram.Model.Vehicle;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author anr10
 */
public class VehicleJpaController implements Serializable {

    public VehicleJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Vehicle vehicle) {
        if (vehicle.getOperationList() == null) {
            vehicle.setOperationList(new ArrayList<Operation>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Operation> attachedOperationList = new ArrayList<Operation>();
            for (Operation operationListOperationToAttach : vehicle.getOperationList()) {
                operationListOperationToAttach = em.getReference(operationListOperationToAttach.getClass(), operationListOperationToAttach.getOperationPK());
                attachedOperationList.add(operationListOperationToAttach);
            }
            vehicle.setOperationList(attachedOperationList);
            em.persist(vehicle);
            for (Operation operationListOperation : vehicle.getOperationList()) {
                Vehicle oldVehicleOfOperationListOperation = operationListOperation.getVehicle();
                operationListOperation.setVehicle(vehicle);
                operationListOperation = em.merge(operationListOperation);
                if (oldVehicleOfOperationListOperation != null) {
                    oldVehicleOfOperationListOperation.getOperationList().remove(operationListOperation);
                    oldVehicleOfOperationListOperation = em.merge(oldVehicleOfOperationListOperation);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Vehicle vehicle) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vehicle persistentVehicle = em.find(Vehicle.class, vehicle.getId());
            List<Operation> operationListOld = persistentVehicle.getOperationList();
            List<Operation> operationListNew = vehicle.getOperationList();
            List<String> illegalOrphanMessages = null;
            for (Operation operationListOldOperation : operationListOld) {
                if (!operationListNew.contains(operationListOldOperation)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Operation " + operationListOldOperation + " since its vehicle field is not nullable.");
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
            vehicle.setOperationList(operationListNew);
            vehicle = em.merge(vehicle);
            for (Operation operationListNewOperation : operationListNew) {
                if (!operationListOld.contains(operationListNewOperation)) {
                    Vehicle oldVehicleOfOperationListNewOperation = operationListNewOperation.getVehicle();
                    operationListNewOperation.setVehicle(vehicle);
                    operationListNewOperation = em.merge(operationListNewOperation);
                    if (oldVehicleOfOperationListNewOperation != null && !oldVehicleOfOperationListNewOperation.equals(vehicle)) {
                        oldVehicleOfOperationListNewOperation.getOperationList().remove(operationListNewOperation);
                        oldVehicleOfOperationListNewOperation = em.merge(oldVehicleOfOperationListNewOperation);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = vehicle.getId();
                if (findVehicle(id) == null) {
                    throw new NonexistentEntityException("The vehicle with id " + id + " no longer exists.");
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
            Vehicle vehicle;
            try {
                vehicle = em.getReference(Vehicle.class, id);
                vehicle.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vehicle with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Operation> operationListOrphanCheck = vehicle.getOperationList();
            for (Operation operationListOrphanCheckOperation : operationListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vehicle (" + vehicle + ") cannot be destroyed since the Operation " + operationListOrphanCheckOperation + " in its operationList field has a non-nullable vehicle field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(vehicle);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Vehicle> findVehicleEntities() {
        return findVehicleEntities(true, -1, -1);
    }

    public List<Vehicle> findVehicleEntities(int maxResults, int firstResult) {
        return findVehicleEntities(false, maxResults, firstResult);
    }

    private List<Vehicle> findVehicleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vehicle.class));
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

    public Vehicle findVehicle(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Vehicle.class, id);
        } finally {
            em.close();
        }
    }

    public int getVehicleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Vehicle> rt = cq.from(Vehicle.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
