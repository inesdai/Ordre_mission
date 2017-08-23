/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.session;

import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Position;
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
public class PositionFacade extends AbstractFacade<Position> {

    @PersistenceContext(unitName = "gpstrackerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PositionFacade() {
        super(Position.class);
    }

    public Position findBy(String namedQuery, String nameparam1, Device param1, String nameparam2, Date param2) {
        return (Position) getEntityManager().createNamedQuery(namedQuery)
                .setParameter(nameparam1, param1)
                .setParameter(nameparam2, param2)
                .setMaxResults(1)
                .getResultList().get(0);
    }

    public List<Position> findByNewIdPosition(String namedQuery, String nameparam1, Device param1, String nameparam2, long param2) {
        return getEntityManager().createNamedQuery(namedQuery)
                .setParameter(nameparam1, param1)
                .setParameter(nameparam2, param2)
                .getResultList();
    }

    public List<Position> findRange(int[] range, Date from, Date to, Device device, boolean stopPosition, Double overSpeed, boolean orderByDate, short power) {
        System.out.println("from" + from + "to" + to);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Position> cq = cb.createQuery(Position.class);
        Root<Position> c = cq.from(Position.class);
        // ParameterExpression<Integer> p = cb.parameter(Integer.class);
        List<Predicate> predicates = new ArrayList<Predicate>();

        if (from != null && to != null) {

            predicates.add(cb.between(c.<Date>get("timecreate"), from, to));
        }
        if (from != null && to == null) {
            predicates.add(cb.greaterThan(c.<Date>get("timecreate"), from));
        }

        if (device != null) {

            predicates.add(cb.equal(c.get("deviceid"), device));
        }
        if (overSpeed != null) {

            predicates.add(cb.greaterThan(c.<Double>get("speed"), overSpeed));
        }
        if (stopPosition == true) {

            predicates.add(cb.lessThan(c.<Double>get("speed"), (Double) 2.0));
        }

        predicates.add(cb.equal(c.<Short>get("power"), (short) power));

        cq.select(c).where(predicates.toArray(new Predicate[]{}));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        if (range != null) {
            q.setMaxResults(range[1] - range[0] + 1);
            q.setFirstResult(range[0]);
        }
        if (orderByDate == true) {
            q = getEntityManager().createQuery(cq.orderBy(cb.asc(c.get("timecreate"))));
        }

        return q.getResultList();

    }

}
