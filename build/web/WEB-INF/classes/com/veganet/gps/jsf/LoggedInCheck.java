/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.jsf;

import com.veganet.gps.entities.User;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.management.relation.Role;

/**
 *
 * @author user
 */
public class LoggedInCheck implements PhaseListener {

    @Override

    public void afterPhase(PhaseEvent event) {

        FacesContext fc = event.getFacesContext();
        System.out.println(fc.getViewRoot().getViewId());
        boolean loginPage = (fc.getViewRoot().getViewId().lastIndexOf("login") > -1) ? true : false;
        boolean forgetpasswordPage = (fc.getViewRoot().getViewId().lastIndexOf("ForgotPassword") > -1) ? true : false;
        String pageUrl = "";
        User connectedUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("connectedUser");
        NavigationHandler nh = fc.getApplication().getNavigationHandler();
        // not logged user open others interfaces
        if (connectedUser == null && !loginPage && !forgetpasswordPage) {

            nh.handleNavigation(fc, null, "login");

        } //logged user open interface 
        else if (connectedUser != null && !loginPage) {
            pageUrl = fc.getViewRoot().getViewId();
            // if user is admin
            if (connectedUser.getAccesslevel() == 0) {

            } // if user is admindistributer
            else if (connectedUser.getAccesslevel() == 3) {
                if (pageUrl.contains("/applicationsetting/")) {
                    nh.handleNavigation(fc, null, "login");
                } else if (pageUrl.contains("/device/Create")) {
                    nh.handleNavigation(fc, null, "login");
                } else if (pageUrl.contains("/device/UserDevice")) {
                    nh.handleNavigation(fc, null, "login");
                } else if (pageUrl.contains("/devicedistributor/")) {
                    nh.handleNavigation(fc, null, "login");
                } else if (pageUrl.contains("/position/")) {
                    nh.handleNavigation(fc, null, "login");
                } else if (pageUrl.contains("/logs/")) {
                    nh.handleNavigation(fc, null, "login");
                }
            } // if user is agent
            else if (connectedUser.getAccesslevel() == 1) {
                if (pageUrl.contains("/applicationsetting/")) {
                    nh.handleNavigation(fc, null, "login");
                } else if (pageUrl.contains("/device/Create")) {
                    nh.handleNavigation(fc, null, "login");
                } else if (pageUrl.contains("/device/UserDevice")) {
                    nh.handleNavigation(fc, null, "login");
                } else if (pageUrl.contains("/devicedistributor/")) {
                    nh.handleNavigation(fc, null, "login");
                } else if (pageUrl.contains("/position/")) {
                    nh.handleNavigation(fc, null, "login");
                } else if (pageUrl.contains("/logs/")) {
                    nh.handleNavigation(fc, null, "login");
                }
            } // if user is customer
            else if (connectedUser.getAccesslevel() == 2){
                if (pageUrl.contains("/applicationsetting/")) {
                    nh.handleNavigation(fc, null, "login");
                } else if (pageUrl.contains("/device/Create")) {
                    nh.handleNavigation(fc, null, "login");
                } else if (pageUrl.contains("/device/UserDevice")) {
                    nh.handleNavigation(fc, null, "login");
                } else if (pageUrl.contains("/devicedistributor/")) {
                    nh.handleNavigation(fc, null, "login");
                } else if (pageUrl.contains("/distributer/")) {
                    nh.handleNavigation(fc, null, "login");
                } else if (pageUrl.contains("/position/")) {
                    nh.handleNavigation(fc, null, "login");
                } else if (pageUrl.contains("/user/Create")) {
                    nh.handleNavigation(fc, null, "login");
                } else if (pageUrl.contains("/user/create")) {
                    nh.handleNavigation(fc, null, "login");
                } else if (pageUrl.contains("/logs/")) {
                    nh.handleNavigation(fc, null, "login");
                }

            }
            // crash accesslevel
            else{
                 nh.handleNavigation(fc, null, "login");
            }

        }
     
//        else if (fc.getViewRoot().getViewId().startsWith("/device/") && connectedUser.getAccesslevel()==1) {
//            System.out.println("access allow");
//        } else {
//    System.out.println("access block");
//        }

    }

    @Override
    public void beforePhase(PhaseEvent event) {

    }

    @Override
    public PhaseId getPhaseId() {

        return PhaseId.RESTORE_VIEW;

    }
}
