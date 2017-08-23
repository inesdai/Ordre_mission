/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.session;

import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Devicedistributor;
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
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Amine
 */
@Stateless
public class DeviceFacade extends AbstractFacade<Device> {

    @PersistenceContext(unitName = "gpstrackerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Device createReturnID(Device d) {
        getEntityManager().persist(d);
        getEntityManager().flush();
        return d;

    }

    public Device refreshDevice(Device device) {

        return (Device) getEntityManager().createNamedQuery("Device.findByDeviceid")
                .setParameter("deviceid", device.getDeviceid())
                .getSingleResult();
    }

    public DeviceFacade() {
        super(Device.class);
    }

    public List<Device> findBy(String namedQuery, String nameparam, String param) {
        return getEntityManager().createNamedQuery(namedQuery)
                .setParameter(nameparam, param)
                .getResultList();
    }

    public List<Device> findRange(int[] range, Short isdeleted) {
//        System.out.println("start find range");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Device> cq = cb.createQuery(Device.class);
        Root<Device> c = cq.from(Device.class);
        // ParameterExpression<Integer> p = cb.parameter(Integer.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(c.get("isdeleted"), isdeleted));

        cq.select(c).where(predicates.toArray(new Predicate[]{}));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        if (range != null) {
            q.setMaxResults(range[1] - range[0] + 1);
            q.setFirstResult(range[0]);
        }
//        System.out.println("longeur liuste admin" + q.getResultList().size());
        System.out.println(q.getResultList().size() + "size");
        return q.getResultList();

    }

    public List<Device> search(int[] range, Short isdeleted, String imei, String numSim, String vehicule, int status) {
//        System.out.println("start find range");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Device> cq = cb.createQuery(Device.class);
        Root<Device> c = cq.from(Device.class);
        // ParameterExpression<Integer> p = cb.parameter(Integer.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(c.get("isdeleted"), isdeleted));

        if (imei != null && !imei.equals("")) {

            predicates.add(cb.like(c.<String>get("imei"), '%' + imei + '%'));
        }
        if (numSim != null && !numSim.equals("")) {

            predicates.add(cb.like(c.<String>get("simcard"), '%' + numSim + '%'));
        }
        if (vehicule != null && !vehicule.equals("")) {

            predicates.add(cb.like(c.get("vehicleid").<String>get("description"), '%' + vehicule + '%'));
        }

        if (status != 0) {
            //available
            if (status == 1) {
                predicates.add(cb.greaterThanOrEqualTo(c.<Date>get("expirationdate"), new Date()));

            } //expired
            else {
                predicates.add(cb.lessThanOrEqualTo(c.<Date>get("expirationdate"), new Date()));

            }
        }
        cq.select(c).where(predicates.toArray(new Predicate[]{}));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        if (range != null) {
            q.setMaxResults(range[1] - range[0] + 1);
            q.setFirstResult(range[0]);
        }

//        System.out.println("longeur liuste admin" + q.getResultList().size());
        System.out.println(q.getResultList().size() + "size");
        return q.getResultList();

    }

    public int count(Short isdeleted) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<Device> c = cq.from(Device.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(c.get("isdeleted"), isdeleted));

        cq.select(em.getCriteriaBuilder().count(c)).where(predicates.toArray(new Predicate[]{}));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();

    }

}
