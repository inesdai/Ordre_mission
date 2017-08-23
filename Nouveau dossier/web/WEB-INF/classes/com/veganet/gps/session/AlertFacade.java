/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.session;

import com.veganet.gps.entities.Alert;
import com.veganet.gps.entities.Devicedistributor;
import com.veganet.gps.entities.User;
import com.veganet.gps.entities.Vehicle;
import java.util.ArrayList;
import java.util.Date;
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
public class AlertFacade extends AbstractFacade<Alert> {

    @PersistenceContext(unitName = "gpstrackerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AlertFacade() {
        super(Alert.class);
    }

    public List<Alert> findBy(String namedQuery, String nameparam, String param) {

        return getEntityManager().createNamedQuery(namedQuery).getResultList();
    }
    
        public List<Alert> findByBeforeThisDate(String namedQuery, String nameparam1,short param1) {
        return getEntityManager().createNamedQuery(namedQuery)
                .setParameter(nameparam1, param1)                
                .getResultList();
    }

    public List<Alert> findRange(int[] range, Short isdeleted, User user) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Alert> cq = cb.createQuery(Alert.class);
        Root<Alert> c = cq.from(Alert.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (user != null) {
            predicates.add(cb.equal(c.get("userid"), user));
        }
        if (isdeleted != null) {
            predicates.add(cb.equal(c.get("isdeleted"), isdeleted));
        }
        cq.select(c).where(predicates.toArray(new Predicate[]{}));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        if (range != null) {
            q.setMaxResults(range[1] - range[0] + 1);
            q.setFirstResult(range[0]);
        }

        return q.getResultList();
    }

    public int count(Short isdeleted, User user) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<Alert> c = cq.from(Alert.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (user != null) {
            predicates.add(cb.equal(c.get("userid"), user));
        }
        if (isdeleted != null) {
            predicates.add(cb.equal(c.get("isdeleted"), isdeleted));
        }
        cq.select(em.getCriteriaBuilder().count(c)).where(predicates.toArray(new Predicate[]{}));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
