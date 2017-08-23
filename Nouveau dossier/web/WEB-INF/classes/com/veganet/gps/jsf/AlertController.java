package com.veganet.gps.jsf;

import com.veganet.gps.entities.Alert;
import com.veganet.gps.entities.Driver;
import com.veganet.gps.entities.User;
import com.veganet.gps.entities.Vehicle;
import com.veganet.gps.jsf.util.JsfUtil;
import com.veganet.gps.jsf.util.PaginationHelper;
import com.veganet.gps.session.AlertFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean(name = "alertController")
@SessionScoped
public class AlertController implements Serializable {

    private Alert current;
    private DataModel items = null;
    @EJB
    private com.veganet.gps.session.AlertFacade ejbFacade;
    @EJB
    private com.veganet.gps.session.VehicleFacade vehicleFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private User connectedUser;   


    public AlertController() {
    }

    @PostConstruct
    public void init() {
        connectedUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("connectedUser");
    }

    
    

            
        public List<Driver> getItemsAvailableSelectOneForReceiver() {

            Vehicle v;
                    List<Driver> drivers = new ArrayList<>();
                    Iterator<Vehicle> i = vehicleFacade.findRange(null, (short)1, connectedUser).iterator();
                    while (i.hasNext()) {
                        v = (Vehicle) i.next();
                        drivers.addAll(v.getDriverCollection());
                    }
                    
  
                    return drivers;
 
    }
    
    public void setCurrent(Alert current) {
        this.current = current;
    }

    public Alert getSelected() {
        if (current == null) {
            current = new Alert();
            selectedItemIndex = -1;
        }
        return current;
    }

    private AlertFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
//                    return getFacade().count();
                    return getFacade().count((short)0, connectedUser);
                }

                @Override
                public DataModel createPageDataModel() {
                    //return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                     return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, (short)0, connectedUser));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
 
        recreateModel();
        return "/alert/List?faces-redirect=true";
    }

    public String prepareView() {
        current = (Alert) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View?faces-redirect=true";
    }

    public String prepareCreate() {
        current = new Alert();
        selectedItemIndex = -1;
        return "/alert/Create?faces-redirect=true";
    }

    public String create() {
        try {
            current.setActiveted((short)1);
            current.setCreatedate(new Date());
            current.setStartkm(0.0);
            current.setEndkm(0.0);
            current.setDuration(null);
            current.setRecurencetype(null);
            current.setEverydayonweek(null);
            current.setEveryweek(null);
            current.setEndoccurence(null);
            current.setIsdeleted((short)0);
            current.setRecurencetype((short)1);
            current.setIsgenerated((short)0);
            current.setUserid(connectedUser);                        
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AlertCreated"));
            
            this.prepareCreate();
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Alert) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AlertUpdated"));
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Alert) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            current.setIsdeleted((short)1);
            getFacade().edit(current);            
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AlertDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
     
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
           
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass = Alert.class)
    public static class AlertControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AlertController controller = (AlertController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "alertController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Alert) {
                Alert o = (Alert) object;
                return getStringKey(o.getAlertid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Alert.class.getName());
            }
        }

    }

}
