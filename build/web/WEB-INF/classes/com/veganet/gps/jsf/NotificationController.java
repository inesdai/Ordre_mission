package com.veganet.gps.jsf;

import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Notification;
import com.veganet.gps.entities.User;
import com.veganet.gps.jsf.util.JsfUtil;
import com.veganet.gps.jsf.util.PaginationHelper;
import com.veganet.gps.session.NotificationFacade;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
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

@ManagedBean(name = "notificationController")
@SessionScoped
public class NotificationController implements Serializable {

    private Notification current;
    private DataModel items = null;
    @EJB
    private com.veganet.gps.session.NotificationFacade ejbFacade;
    @EJB
    private com.veganet.gps.session.DevicedistributorFacade ejbFacadeDeviceDistributer;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private User connectedUser;
    private String message;
 private List<Notification> notifications =new  ArrayList<Notification>();

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

 

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @PostConstruct
    public void init() {
        connectedUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("connectedUser");
  List<Device> devices = ejbFacadeDeviceDistributer.findRange(null, (short) 1, connectedUser, connectedUser.getDistributerid(), (short) 0);
 
    }

    public NotificationController() {
    }

    public void setCurrent(Notification current) {
        this.current = current;
    }

    public Notification getSelected() {
        if (current == null) {
            current = new Notification();
            selectedItemIndex = -1;
        }
        return current;
    }

    private NotificationFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    List<Device> devices = ejbFacadeDeviceDistributer.findRange(null, (short) 1, connectedUser, connectedUser.getDistributerid(), (short) 0);
                    return getFacade().findRangeByUser(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, null, devices).size();
                }

                @Override
                public DataModel createPageDataModel() {
                    List<Device> devices = ejbFacadeDeviceDistributer.findRange(null, (short) 1, connectedUser, connectedUser.getDistributerid(), (short) 0);
                    return new ListDataModel(getFacade().findRangeByUser(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, null, devices));
                }
            };
        }
        return pagination;
    }

    public void generateDownloadPDF(){
        PdfNotificationConverter pdfConverter=new PdfNotificationConverter(connectedUser);
        pdfConverter.exportPDF(items);
    }
    
    
    public void showMessage() {
       
        List<Device> devices = ejbFacadeDeviceDistributer.findRange(null, (short) 1, connectedUser, connectedUser.getDistributerid(), (short) 0);
       notifications = ejbFacade.findRangeByUser(null, (short) 0, devices);
        FacesContext context = FacesContext.getCurrentInstance();
        Notification notification;
        for (int i = 0; i < notifications.size(); i++) {
            notification = notifications.get(i);
            context.addMessage("messageForm:messagename", new FacesMessage("Notification3", notification.getDescription()));          

        }
    }

    public String prepareList() {
        recreateModel();
        return "/notification/List?faces-redirect=true";
    }

    public String prepareView() {
        current = (Notification) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Notification();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("NotificationCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Notification) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("NotificationUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Notification) getItems().getRowData();
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
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("NotificationDeleted"));
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

    @FacesConverter(forClass = Notification.class)
    public static class NotificationControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            NotificationController controller = (NotificationController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "notificationController");
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
            if (object instanceof Notification) {
                Notification o = (Notification) object;
                return getStringKey(o.getNotificationid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Notification.class.getName());
            }
        }

    }

}
