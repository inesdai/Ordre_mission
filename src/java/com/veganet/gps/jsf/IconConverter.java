/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.jsf;

import com.veganet.gps.entities.Icon;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 *
 * @author user
 */
@FacesConverter("IconConverter")
public class IconConverter implements Converter{

    private List<Icon> icons;
 @Override
    public Icon getAsObject(FacesContext fc, UIComponent uic, final String value) {
        icons = new ArrayList<Icon>();
        icons.add(new Icon(0, "undefined", ResourceBundle.getBundle("/Bundle").getString("Undefined")));
        icons.add(new Icon(1, "smallCar", ResourceBundle.getBundle("/Bundle").getString("SmallCar")));
        icons.add(new Icon(2, "bigCar", ResourceBundle.getBundle("/Bundle").getString("BigCar")));
        icons.add(new Icon(3, "smallTruck", ResourceBundle.getBundle("/Bundle").getString("SmallTruck")));
        icons.add(new Icon(4, "bigTruck", ResourceBundle.getBundle("/Bundle").getString("BigTruck")));
        icons.add(new Icon(5, "smallBus", ResourceBundle.getBundle("/Bundle").getString("SmallBus")));
        icons.add(new Icon(6, "bigBus", ResourceBundle.getBundle("/Bundle").getString("BigBus")));
        icons.add(new Icon(7, "armored", ResourceBundle.getBundle("/Bundle").getString("Armored")));
        icons.add(new Icon(8, "moto", ResourceBundle.getBundle("/Bundle").getString("Moto")));
        icons.add(new Icon(9, "ship", ResourceBundle.getBundle("/Bundle").getString("Ship")));

        if (value != null && value.trim().length() > 0) {
            try {
                Predicate condition = new Predicate() {
                    

                    @Override
                    public boolean evaluate(Object icon) {
        return ((Icon) icon).getId() == Integer.parseInt(value);                    }

                 
                    
                };
                Icon res = (Icon) CollectionUtils.find(icons, condition);

               // Object o= vehicleController.getVehicles().get(Integer.parseInt(value));
                // System.out.println(vehicleController.getVehicles().get(Integer.parseInt(value)).getDevice().getDeviceid()+" devid");
                return res;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        } else {
            return null;
        }
    }
 @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object !=null) {
            return String.valueOf(((Icon) object).getId());
        } else {
            return null;
        }
    }

}
