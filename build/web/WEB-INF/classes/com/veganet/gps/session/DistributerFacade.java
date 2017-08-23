/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.session;

import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Distributer;
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
 * @author Amine
 */
@Stateless
public class DistributerFacade extends AbstractFacade<Distributer> {

    @PersistenceContext(unitName = "gpstrackerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DistributerFacade() {
        super(Distributer.class);
    }

    public List<Distributer> findByParentid(Distributer parent) {
        return em.createNamedQuery("Distributer.findByParentid").setParameter("parentid", parent).getResultList();
    }

    public List<Distributer> findByParentidNull() {
        return em.createNamedQuery("Distributer.findByParentidNull").getResultList();

    }

    public List<Distributer> findRange(int[] range, Short isdeleted, Distributer parent) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Distributer> cq = cb.createQuery(Distributer.class);
        Root<Distributer> c = cq.from(Distributer.class);
        // ParameterExpression<Integer> p = cb.parameter(Integer.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(c.get("isdeleted"), isdeleted));
        if (parent != null) {

            predicates.add(cb.equal(c.get("parentid"), parent));
        }
        cq.select(c).where(predicates.toArray(new Predicate[]{}));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        if (range != null) {
            q.setMaxResults(range[1] - range[0] + 1);
            q.setFirstResult(range[0]);
        }
        return q.getResultList();

    }

    public int count(Short isdeleted, Distributer parent) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<Distributer> c = cq.from(Distributer.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(c.get("isdeleted"), isdeleted));
        if (parent != null) {

            predicates.add(cb.equal(c.get("parentid"), parent));
        }
        cq.select(em.getCriteriaBuilder().count(c)).where(predicates.toArray(new Predicate[]{}));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();

    }

}
