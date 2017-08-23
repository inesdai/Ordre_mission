/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.session;

import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Devicedistributor;
import com.veganet.gps.entities.Distributer;
import com.veganet.gps.entities.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Amine
 */
@Stateless
public class DevicedistributorFacade extends AbstractFacade<Devicedistributor> {

    @PersistenceContext(unitName = "gpstrackerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DevicedistributorFacade() {
        super(Devicedistributor.class);
    }

    public int updateQuery(String namedQuery, String nameparam1, short param1, String nameparam2, User param2, String nameparam3, Device param3) {
        Query q = getEntityManager().createNamedQuery(namedQuery);
        q.setParameter(nameparam1, param1);
        q.setParameter(nameparam2, param2);
        q.setParameter(nameparam3, param3);
        return q.executeUpdate();
    }

    public List<Device> findBy(String namedQuery, String nameparam, Distributer param) {
        return getEntityManager().createNamedQuery(namedQuery)
                .setParameter(nameparam, param)
                .getResultList();
    }

    public int countRedundancyLines(String namedQuery, String nameparam1, short param1, String nameparam2,Device param2) {
        return getEntityManager().createNamedQuery(namedQuery)
                .setParameter(nameparam1, param1)
                .setParameter(nameparam2, param2)
                .getResultList().size();

    }

    public List<User> findUserDevice(String namedQuery, String nameparam2, short param2, String nameparam1, Device param1) {
        return getEntityManager().createNamedQuery(namedQuery)
                .setParameter(nameparam2, param2)
                .setParameter(nameparam1, param1)
                .getResultList();
    }

    public List<Devicedistributor> findBy(String namedQuery, String nameparam, int param) {
        return getEntityManager().createNamedQuery(namedQuery)
                .setParameter(nameparam, param)
                .getResultList();
    }

    public List<Device> findRange(int[] range, Short assigned, User user, Distributer distributer, short isDeleted) {
        List<Device> myDevices = new ArrayList<Device>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Devicedistributor> cq = cb.createQuery(Devicedistributor.class);
        Root<Devicedistributor> c = cq.from(Devicedistributor.class);
        // ParameterExpression<Integer> p = cb.parameter(Integer.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (assigned == (short) 1) {
            predicates.add(cb.equal(c.get("assigned"), assigned));
        }
        if (user != null) {

            predicates.add(cb.equal(c.get("customerid"), user));
        }
        if (distributer != null) {

            predicates.add(cb.equal(c.get("distributerid"), distributer));
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
            if (isDeleted == (short) 0) {

                if (d.getDeviceid().getIsdeleted() == (short) 0) {
                    myDevices.add(d.getDeviceid());
                }
            } else {
                myDevices.add(d.getDeviceid());

            }

        }

        return myDevices;

    }

    public List<Device> search(int[] range, Short assigned, User user, Distributer distributer, short isDeleted, String imei, String numSim, String vehicule, int status) {
        List<Device> myDevices = new ArrayList<Device>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Devicedistributor> cq = cb.createQuery(Devicedistributor.class);
        Root<Devicedistributor> c = cq.from(Devicedistributor.class);
        // ParameterExpression<Integer> p = cb.parameter(Integer.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (assigned == (short) 1) {
            predicates.add(cb.equal(c.get("assigned"), assigned));
        }
        if (user != null) {

            predicates.add(cb.equal(c.get("customerid"), user));
        }
        if (distributer != null) {

            predicates.add(cb.equal(c.get("distributerid"), distributer));
        }
        if (imei != null && !imei.equals("")) {

            predicates.add(cb.like(c.get("deviceid").<String>get("imei"), '%' + imei + '%'));
        }
        if (numSim != null && !numSim.equals("")) {

            predicates.add(cb.like(c.get("deviceid").<String>get("simcard"), '%' + numSim + '%'));
        }
        if (vehicule != null && !vehicule.equals("")) {

            predicates.add(cb.like(c.get("deviceid").get("vehicleid").<String>get("description"), '%' + vehicule + '%'));
        }

        if (status != 0) {
            //available
            if (status == 1) {
                predicates.add(cb.greaterThanOrEqualTo(c.get("deviceid").<Date>get("expirationdate"), new Date()));

            } //expired
            else {
                predicates.add(cb.lessThanOrEqualTo(c.get("deviceid").<Date>get("expirationdate"), new Date()));

            }
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
            if (isDeleted == (short) 0) {

                if (d.getDeviceid().getIsdeleted() == (short) 0) {
                    myDevices.add(d.getDeviceid());
                }
            } else {
                myDevices.add(d.getDeviceid());

            }

        }

        return myDevices;

    }

    public int count(Short assigned, User user, Distributer distributer, short isDeleted) {
        int count = 0;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<Devicedistributor> c = cq.from(Devicedistributor.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(c.get("assigned"), assigned));
        if (user != null) {
            predicates.add(cb.equal(c.get("customerid"), user));
        }
        if (distributer != null) {
            predicates.add(cb.equal(c.get("distributerid"), distributer));
        }
        //cq.select(em.getCriteriaBuilder().count(c)).where(predicates.toArray(new Predicate[]{}));

        cq.select(c).where(predicates.toArray(new Predicate[]{}));

        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.getResultList();
        Iterator<Devicedistributor> i = q.getResultList().iterator();
        Devicedistributor d;

        while (i.hasNext()) {
            d = (Devicedistributor) i.next();
            if (isDeleted == (short) 0) {
                if (d.getDeviceid().getIsdeleted() == (short) 0) {
                    count++;
                }
            } else {

            }
        }

        return (int) count;
    }
}
