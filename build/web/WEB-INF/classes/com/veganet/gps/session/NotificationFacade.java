/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.session;

import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Notification;
import com.veganet.gps.entities.User;
import java.util.ArrayList;
import java.util.Date;
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
public class NotificationFacade extends AbstractFacade<Notification> {

    @PersistenceContext(unitName = "gpstrackerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NotificationFacade() {
        super(Notification.class);
    }

    public List<Notification> findRange(int[] range, Short status, Device device) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Notification> cq = cb.createQuery(Notification.class);
        Root<Notification> c = cq.from(Notification.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (device != null) {
            predicates.add(cb.equal(c.get("deviceid"), device));
        }
        if (status != null) {
            predicates.add(cb.equal(c.get("satus"), status));
        }
        cq.select(c).where(predicates.toArray(new Predicate[]{}));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        if (range != null) {
            q.setMaxResults(range[1] - range[0] + 1);
            q.setFirstResult(range[0]);
        }
        return q.getResultList();
    }

    public List<Notification> findRangeByUser(int[] range, Short status, List<Device> devices) {
   
        List<Notification> notifications = new ArrayList<Notification>();

        for (int i = 0; i < devices.size(); i++) {
            Device d = devices.get(i);

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Notification> cq = cb.createQuery(Notification.class);
            Root<Notification> c = cq.from(Notification.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            if (d.getDeviceid() != null) {
                predicates.add(cb.equal(c.get("deviceid"), d));
            }
            if (status != null) {
                predicates.add(cb.equal(c.get("satus"), status));
            }
            
            cq.select(c).where(predicates.toArray(new Predicate[]{}));
            javax.persistence.Query q = getEntityManager().createQuery(cq);
            if (range != null) {
                q.setMaxResults(range[1] - range[0] + 1);
                q.setFirstResult(range[0]);
            }
            
 
            notifications.addAll(q.getResultList());
        }
        return notifications;
    }
        public List<Notification> findRangeByDeviceDate(int[] range, Short status,int type, Device device,Date from,Date to) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Notification> cq = cb.createQuery(Notification.class);
        Root<Notification> c = cq.from(Notification.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (device != null) {
            predicates.add(cb.equal(c.get("deviceid"), device));
        }
        if (status != null) {
            predicates.add(cb.equal(c.get("satus"), status));
        }
         if (type != 0) {
            predicates.add(cb.equal(c.get("typenotification"), type));
        }
         if (from != null && to != null) {

            predicates.add(cb.between(c.<Date>get("timenotification"), from, to));
        }
        cq.select(c).where(predicates.toArray(new Predicate[]{}));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        if (range != null) {
            q.setMaxResults(range[1] - range[0] + 1);
            q.setFirstResult(range[0]);
        }
        return q.getResultList();
    }

    public int count(Short status, List<Device> devices) { 
            List<Notification> notifications = new ArrayList<Notification>();

        for (int i = 0; i < devices.size(); i++) {
            Device d = devices.get(i);

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Notification> cq = cb.createQuery(Notification.class);
            Root<Notification> c = cq.from(Notification.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            if (d.getDeviceid() != null) {
                predicates.add(cb.equal(c.get("deviceid"), d));
            }
            if (status != null) {
                predicates.add(cb.equal(c.get("satus"), status));
            }
            
            cq.select(c).where(predicates.toArray(new Predicate[]{}));
            javax.persistence.Query q = getEntityManager().createQuery(cq);
        
 
            notifications.addAll(q.getResultList());
        }
        return notifications.size();
    
}
}
