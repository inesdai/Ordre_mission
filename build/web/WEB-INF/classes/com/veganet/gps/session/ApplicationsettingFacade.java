/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.session;

import com.veganet.gps.entities.Applicationsetting;
import com.veganet.gps.entities.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Amine
 */
@Stateless
public class ApplicationsettingFacade extends AbstractFacade<Applicationsetting> {

    @PersistenceContext(unitName = "gpstrackerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ApplicationsettingFacade() {
        super(Applicationsetting.class);
    }

    public Applicationsetting findBy(String namedQuery, String nameparam, String param) {               
        try {
            return (Applicationsetting) getEntityManager().createNamedQuery(namedQuery)
                .setParameter(nameparam, param)
                .getSingleResult();
            
        } catch (Exception e) {
            return null;
        }
                
    }

}
