/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.jsf;
//
//import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import com.veganet.gps.entities.Vehicle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
 
 

/**
 *
 * @author Amine
 */
@FacesConverter("vehicleConverter")
public class VehicleConverter implements Converter{
    @EJB
    private com.veganet.gps.session.VehicleFacade ejbFacade;
    
    
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
 
       if(value != null && value.trim().length() > 0) {
            try {                               
               Object o=   ejbFacade.find(Integer.parseInt(value));
               // Object o= vehicleController.getVehicles().get(Integer.parseInt(value));
                
               // System.out.println(vehicleController.getVehicles().get(Integer.parseInt(value)).getDevice().getDeviceid()+" devid");
          return o;
            
            
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        }
        else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(((Vehicle) object).getVehicleid());
        }
        else {
            return null;
        }
    }
    
}
