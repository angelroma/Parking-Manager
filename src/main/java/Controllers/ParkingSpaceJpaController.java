/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.NonexistentEntityException;
import com.marm.parkingprogram.Model.ParkingSpace;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(parkingSpace);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ParkingSpace parkingSpace) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            parkingSpace = em.merge(parkingSpace);
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

    public void destroy(Integer id) throws NonexistentEntityException {
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
