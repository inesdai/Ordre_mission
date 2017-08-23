package com.veganet.gps.jsf;

import com.veganet.gps.entities.Applicationsetting;
import com.veganet.gps.entities.User;
import com.veganet.gps.jsf.util.JsfUtil;
import com.veganet.gps.jsf.util.PaginationHelper;
import com.veganet.gps.session.ApplicationsettingFacade;
import java.io.File;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.persistence.criteria.Path;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "applicationsettingController")
@SessionScoped
public class ApplicationsettingController implements Serializable {

    private Applicationsetting current;
    private DataModel items = null;
    @EJB
    private com.veganet.gps.session.ApplicationsettingFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private String ipAddress;
    private String portNumber;
    private String gpsProtocol;
    private String imageFolderPath;
    private String timeZone;
    private String lastUpdate;
    private String username;
    private String password;
    private User connectedUser;

    @PostConstruct
    public void init() {
        try {
            connectedUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("connectedUser");

            lastUpdate = getFacade().findBy("Applicationsetting.findBySettingname", "settingName", "lastUpdate").getSettingValue();
            ipAddress = getFacade().findBy("Applicationsetting.findBySettingname", "settingName", "ipAddress").getSettingValue();
            portNumber = getFacade().findBy("Applicationsetting.findBySettingname", "settingName", "portNumber").getSettingValue();
            gpsProtocol = getFacade().findBy("Applicationsetting.findBySettingname", "settingName", "gpsProtocol").getSettingValue();
            imageFolderPath = getFacade().findBy("Applicationsetting.findBySettingname", "settingName", "imageFolderPath").getSettingValue();
            timeZone = getFacade().findBy("Applicationsetting.findBySettingname", "settingName", "timeZone").getSettingValue();

        } catch (Exception e) {

        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getImageFolderPath() {
        return imageFolderPath;
    }

    public void setImageFolderPath(String imageFolderPath) {
        this.imageFolderPath = imageFolderPath;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public String getGpsProtocol() {
        return gpsProtocol;
    }

    public void setGpsProtocol(String gpsProtocol) {
        this.gpsProtocol = gpsProtocol;
    }

    public ApplicationsettingController() {
    }

    public Applicationsetting getSelected() {
        if (current == null) {
            current = new Applicationsetting();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ApplicationsettingFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Applicationsetting) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Applicationsetting();
        selectedItemIndex = -1;
        return "Create";
    }

    public void confirmPassword() {
        RequestContext requestContext6 = RequestContext.getCurrentInstance();
        requestContext6.execute("PF('dlglog').show();");
    }

    public String applicationSetting() {
        return "/applicationsetting/ApplicationSetting";
    }

    public String create() {

        if (getFacade().count() > 0) {

            return update();

        } else {
            File f = new File(imageFolderPath);
            java.nio.file.Path path = Paths.get(imageFolderPath);
            if (Files.isDirectory(path)) {
                try {

 
                    current = new Applicationsetting();
                    current.setSettingName("lastUpdate");
                    current.setSettingValue(new Date() + "");
                    getFacade().create(current);

                    current.setSettingName("ipAddress");
                    current.setSettingValue(ipAddress);
                    getFacade().create(current);

                    current.setSettingName("portNumber");
                    current.setSettingValue(portNumber);
                    getFacade().create(current);

                    current.setSettingName("imageFolderPath");
                    current.setSettingValue(imageFolderPath);
                    getFacade().create(current);

                    current.setSettingName("gpsProtocol");
                    current.setSettingValue(gpsProtocol);
                    getFacade().create(current);
                    
                    current.setSettingName("timeZone");
                    current.setSettingValue(timeZone);
                    getFacade().create(current);

                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ApplicationsettingCreated"));
                    return "/indexinitial?faces-redirect=true";
                } catch (Exception e) {
                    JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                    return null;
                }
            } else {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("pathfileinvalide"));
                return null;
            }
        }
    }

    public boolean confirmPasswordaction() {

        String passwortUser = "";
        boolean r = false;

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            if (sb.toString().equals(connectedUser.getPassword())) {
                r = true;
            } else {

                r = false;
            }

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            r = false;
        }
        return r;
    }

    public String prepareEdit() {
        current = (Applicationsetting) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        if (this.confirmPasswordaction()) {
            java.nio.file.Path path = Paths.get(imageFolderPath);
            if (Files.isDirectory(path)) {

                try {

                    current = getFacade().findBy("Applicationsetting.findBySettingname", "settingName", "lastUpdate");
                    current.setSettingValue(new Date() + "");
                    getFacade().edit(current);

                    current = getFacade().findBy("Applicationsetting.findBySettingname", "settingName", "ipAddress");
                    current.setSettingValue(ipAddress);
                    getFacade().edit(current);

                    current = getFacade().findBy("Applicationsetting.findBySettingname", "settingName", "portNumber");
                    current.setSettingValue(portNumber);
                    getFacade().edit(current);

                    current = getFacade().findBy("Applicationsetting.findBySettingname", "settingName", "gpsProtocol");
                    current.setSettingValue(gpsProtocol);
                    getFacade().edit(current);

                    current = getFacade().findBy("Applicationsetting.findBySettingname", "settingName", "imageFolderPath");
                    current.setSettingValue(imageFolderPath);
                    getFacade().edit(current);
                    
                     current = getFacade().findBy("Applicationsetting.findBySettingname", "settingName", "timeZone");
                    current.setSettingValue(timeZone);
                    getFacade().edit(current);

                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ApplicationsettingUpdated"));

                    return "/indexinitial?faces-redirect=true";
                } catch (Exception e) {
                    JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                    return null;
                }
            } else {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("pathfileinvalide"));
                return null;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("forConfirmPassword:messages5", new FacesMessage(FacesMessage.SEVERITY_FATAL, ResourceBundle.getBundle("/Bundle").getString("passwordinvalide"), ""));
            return null;
        }

    }

    public String destroy() {
        current = (Applicationsetting) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ApplicationsettingDeleted"));
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

    @FacesConverter(forClass = Applicationsetting.class)
    public static class ApplicationsettingControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ApplicationsettingController controller = (ApplicationsettingController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "applicationsettingController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Applicationsetting) {
                Applicationsetting o = (Applicationsetting) object;
                return getStringKey(o.getApplicationsettingid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Applicationsetting.class.getName());
            }
        }

    }

}
