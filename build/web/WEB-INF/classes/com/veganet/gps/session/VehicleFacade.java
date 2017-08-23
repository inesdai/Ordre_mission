/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.session;


import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Devicedistributor;
import com.veganet.gps.entities.User;
import com.veganet.gps.entities.Vehicle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Amine
 */
@Stateless
public class VehicleFacade extends AbstractFacade<Vehicle> {

    @PersistenceContext(unitName = "gpstrackerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VehicleFacade() {
        super(Vehicle.class);
    }
    
        public List<Vehicle> findBy(String namedQuery,String nameparam,String param) {
       return getEntityManager().createNamedQuery(namedQuery)
                .setParameter(nameparam,param)
                .getResultList();
    }

public List<Vehicle> findRange(int[] range, Short assigned, User user) {
        List<Vehicle> myVehicules = new ArrayList<Vehicle>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Devicedistributor> cq = cb.createQuery(Devicedistributor.class);
        Root<Devicedistributor> c = cq.from(Devicedistributor.class);
        // ParameterExpression<Integer> p = cb.parameter(Integer.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(c.get("assigned"), assigned));
        if (user != null) {

            predicates.add(cb.equal(c.get("customerid"), user));
        }
        cq.select(c).where(predicates.toArray(new Predicate[]{}));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        if (range != null) {
            
            q.setMaxResults(range[1] - range[0] + 1);
            q.setFirstResult(range[0]);
        }
        Devicedistributor d;
        Iterator<Devicedistributor> i = q.getResultList().iterator();
        while (i.hasNext()) {
            d = (Devicedistributor) i.next();

            myVehicules.add(d.getDeviceid().getVehicleid());

        }
          
        return myVehicules;

    }


    public int count(Short assigned, User user) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<Devicedistributor> c = cq.from(Devicedistributor.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(c.get("assigned"), assigned));
        if (user != null) {

            predicates.add(cb.equal(c.get("customerid"), user));
        }
        cq.select(em.getCriteriaBuilder().count(c)).where(predicates.toArray(new Predicate[]{}));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();

    }
        
        
        
        
        
        
        
        
        
        
        
}
