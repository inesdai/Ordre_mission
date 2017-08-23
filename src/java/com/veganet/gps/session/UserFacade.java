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
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Amine
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "gpstrackerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }

    public List<User> findBy(String namedQuery, String nameparam, String param) {
        return getEntityManager().createNamedQuery(namedQuery)
                .setParameter(nameparam, param)
                .getResultList();
    }

    public List<User> findByUserPass(String namedQuery, String nameparam1, String param1, String nameparam2, String param2) {
        return getEntityManager().createNamedQuery(namedQuery)
                .setParameter(nameparam1, param1)
                .setParameter(nameparam2, param2)
                .getResultList();
    }

    public User findAdminDistributerForDistributer(String namedQuery, String nameparam1, Distributer param1, String nameparam2, short param2) {

        List<User> u
                = getEntityManager().createNamedQuery(namedQuery)
                .setParameter(nameparam1, param1)
                .setParameter(nameparam2, param2)
                .getResultList();
        if (u.size() > 0) {
            return u.get(0);
        } else {
            return null;
        }

    }

    public List<User> findByDistributer(String namedQuery, String nameparam, Distributer param) {
        return getEntityManager().createNamedQuery(namedQuery)
                .setParameter(nameparam, param)
                .getResultList();
    }

    public List<User> findRange(int[] range, Short isdeleted, User user, String firstName, String lastName, String login, String email, Integer access) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> c = cq.from(User.class);
        // ParameterExpression<Integer> p = cb.parameter(Integer.class);
        List<Predicate> predicates = new ArrayList<Predicate>();

        if (user != null) {

            predicates.add(cb.equal(c.get("distributerid"), user.getDistributerid()));
        }
        if (isdeleted != null) {

            predicates.add(cb.equal(c.get("isdeleted"), isdeleted));
        }
      
        if (lastName!= null && firstName!=null &&!firstName.equals("") && !lastName.equals("")) {
            predicates.add(cb.or(cb.like(c.<String>get("lastname"), '%' + lastName + '%'),cb.like(c.<String>get("firstname"), '%' + firstName + '%')));
        }
        if (email != null && !email.equals("")) {
            predicates.add(cb.like(c.<String>get("email"), '%' + email + '%'));
        }
        if (login != null && !login.equals("")) {
            predicates.add(cb.like(c.<String>get("username"), '%' + login + '%'));
        }
         if (access != 4) {

            predicates.add(cb.equal(c.get("accesslevel"), access));
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

    public int count(Short isdeleted, User user) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        // CriteriaQuery<User> cq = cb.createQuery(User.class);
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<User> c = cq.from(User.class);
        List<Predicate> predicates = new ArrayList<Predicate>();

        if (user != null) {

            predicates.add(cb.equal(c.get("distributerid"), user.getDistributerid()));
        }
        if (isdeleted != null) {

            predicates.add(cb.equal(c.get("isdeleted"), isdeleted));
        }

        cq.select(em.getCriteriaBuilder().count(c)).where(predicates.toArray(new Predicate[]{}));
        //.where(predicates.toArray(new Predicate[]{}));
        //javax.persistence.Query q = getEntityManager().createQuery(cq);
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
