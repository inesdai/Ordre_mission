/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.session;

import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Geofence;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

/**
 *
 * @author Amine
 */
@Stateless
public class GeofenceFacade extends AbstractFacade<Geofence> {

    @PersistenceContext(unitName = "gpstrackerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GeofenceFacade() {
        super(Geofence.class);
    }

    public List<Geofence> findByTwoDate(String namedQuery, String nameparam1, Date fromDate, String nameparam2, Date toDate) {
        return getEntityManager().createNamedQuery(namedQuery)
                .setParameter(nameparam1, fromDate)
                .setParameter(nameparam2, toDate)
                .getResultList();
    }
       public List<Geofence> findByAllDate(String namedQuery) {
        return getEntityManager().createNamedQuery(namedQuery).getResultList();
    }

    public List<Geofence> findRange(int[] range, Device device) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Geofence> cq = cb.createQuery(Geofence.class);
        Root<Geofence> c = cq.from(Geofence.class);
        // ParameterExpression<Integer> p = cb.parameter(Integer.class);
        List<Predicate> predicates = new ArrayList<Predicate>();

        if (device != null) {

            predicates.add(cb.equal(c.get("deviceid"), device));
        }

        cq.select(c).where(predicates.toArray(new Predicate[]{}));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        if (range != null) {
            q.setMaxResults(range[1] - range[0] + 1);
            q.setFirstResult(range[0]);
        }
        //System.out.println("liste des device for user" + user.getUserid() + "size" + q.getResultList().size());
        //q.getParameterValue()         

        return q.getResultList();

    }

}
