/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.session;

import com.veganet.gps.entities.Logs;
import java.util.ArrayList;
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
 * @author user
 */
@Stateless
public class LogsFacade extends AbstractFacade<Logs> {

    @PersistenceContext(unitName = "gpstrackerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LogsFacade() {
        super(Logs.class);
    }

    public List<Logs> findRange(int[] range) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Logs> cq = cb.createQuery(Logs.class);
        Root<Logs> c = cq.from(Logs.class);
        cq.select(c);
        javax.persistence.Query q = getEntityManager().createQuery(cq.orderBy(cb.desc(c.get("datetime"))));
        if (range != null) {
            q.setMaxResults(range[1] - range[0] + 1);
            q.setFirstResult(range[0]);
        }
        return q.getResultList();
    }

    public List<Logs> findRange(int[] range, String login, String imei, String description, String ipaddress) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Logs> cq = cb.createQuery(Logs.class);
        Root<Logs> c = cq.from(Logs.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (!login.equals("")) {
            predicates.add(cb.like(c.get("userid").<String>get("username"), '%' + login + '%'));
        }
        if (!imei.equals("")) {
            predicates.add(cb.like(c.get("deviceid").<String>get("imei"), '%' + imei + '%'));
        }
        if (description != null) {
            predicates.add(cb.like(c.<String>get("description"), '%' + description + '%'));
        }
        if (!ipaddress.equals("")) {
            predicates.add(cb.like(c.<String>get("ipaddress"), '%' + ipaddress + '%'));
        }

        cq.select(c).where(predicates.toArray(new Predicate[]{}));
        javax.persistence.Query q = getEntityManager().createQuery(cq.orderBy(cb.desc(c.get("datetime"))));
        if (range != null) {
            q.setMaxResults(range[1] - range[0] + 1);
            q.setFirstResult(range[0]);
        }
        return q.getResultList();

    }

}
