/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.service;

import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Logs;
import com.veganet.gps.entities.User;
import com.veganet.gps.entities.Vehicle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author user
 */
@Stateless
@Path("com.veganet.gps.entities.device")
public class DeviceFacadeREST extends AbstractFacade<Device> {

    @PersistenceContext(unitName = "gpstrackerPU")
    private EntityManager em;
    @EJB
    private com.veganet.gps.session.LogsFacade logsFacade;

    public DeviceFacadeREST() {
        super(Device.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Device entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Integer id, Device entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Device find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Device> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Device> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @POST
    @Path("petrolAction")
    @Consumes(MediaType.APPLICATION_JSON)
    public void petrolAction(Vehicle vehicle) {
        getEntityManager().createNamedQuery("Device.updatePetrolAction")
                .setParameter("action", vehicle.getActionPetrol())
                .setParameter("vehicleid", vehicle.getVehicleid())
                .executeUpdate();

        //login saved in  logs table
       
         
        if (vehicle.getActionPetrol() == 4 || vehicle.getActionPetrol() == 2) {
            Logs log = new Logs(new Date(), "Cut off Petrol", null, "AndroidApp",vehicle.getDevice());
            logsFacade.create(log);

        }
        if (vehicle.getActionPetrol() == 1 ) {
            Logs log = new Logs(new Date(), "Restore Petrol", null, "AndroidApp",vehicle.getDevice());
            logsFacade.create(log);

        }

    }

}
