package com.veganet.gps.jsf;

import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Driver;
import com.veganet.gps.entities.User;
import com.veganet.gps.entities.Vehicle;
import com.veganet.gps.jsf.util.JsfUtil;
import com.veganet.gps.jsf.util.PaginationHelper;
import com.veganet.gps.session.DriverFacade;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
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
import javax.servlet.http.Part;

@ManagedBean(name = "driverController")
@SessionScoped
public class DriverController implements Serializable {

    private Driver current;
    private DataModel items = null;
    @EJB
    private com.veganet.gps.session.DriverFacade ejbFacade;
    @EJB
    private com.veganet.gps.session.VehicleFacade vehicleFacade;
    @EJB
    private com.veganet.gps.session.ApplicationsettingFacade applicationsettingFacade;
    @EJB
    private com.veganet.gps.session.DevicedistributorFacade ejbFacadeDeviceDistributer;
    @EJB
    private com.veganet.gps.session.DeviceFacade deviceFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private User connectedUser;
    private Vehicle vehicle;
    private short one = 1;
    private Vehicle myVehicle;
    private Part file;

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public Vehicle getMyVehicle() {
        return this.getSelected().getVehicleCollection().iterator().next();
    }

    public void setMyVehicle(Vehicle myVehicle) {
        this.myVehicle = myVehicle;
    }

    public DriverController() {
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Driver getSelected() {
        if (current == null) {
            current = new Driver();
            selectedItemIndex = -1;
        }
        return current;
    }

    private DriverFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return vehicleFacade.count(one, connectedUser);
                }

                @Override
                public DataModel createPageDataModel() {

//                        return new ListDataModel((List<Driver>) connectedUser.getDeviceCollection().iterator());
                    Vehicle v;
                    List<Driver> drivers = new ArrayList<>();

                    Iterator<Vehicle> i = myVehicles().iterator();
                    while (i.hasNext()) {
                        v = (Vehicle) i.next();
                        drivers.addAll(v.getDriverCollection());
                    }

                    return new ListDataModel(drivers);
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "/driver/List?faces-redirect=true";
    }

    public List<Vehicle> myVehicles() {
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        //super admin vehicules
        if (connectedUser.getAccesslevel() == 0) {
            List<Device> devices = new ArrayList<Device>();
            devices = deviceFacade.findRange(null, (short) 0);

            for (Device device : devices) {
                if (device.getIsdeleted() == (short) 0) {
                    vehicles.add(device.getVehicleid());
                }
            }
        } else if (connectedUser.getAccesslevel() == 1 || connectedUser.getAccesslevel() == 3) {

            List<Device> devices = new ArrayList<Device>();
            devices = ejbFacadeDeviceDistributer.findRange(null, one, null, connectedUser.getDistributerid(), (short) 0);

            for (Device device : devices) {
                if (device.getIsdeleted() == (short) 0) {
                    vehicles.add(device.getVehicleid());
                }
            }
        } else {
            List<Device> devices = new ArrayList<Device>();
            devices = ejbFacadeDeviceDistributer.findRange(null, (short) 1, connectedUser, connectedUser.getDistributerid(), (short) 0);
            for (Device device : devices) {

                if (device.getIsdeleted() == (short) 0) {
                    vehicles.add(device.getVehicleid());
                }

            }
        }

        return vehicles;
    }

    public String prepareView() {
        current = (Driver) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Driver();
        selectedItemIndex = -1;
        return "/driver/Create?faces-redirect=true";
    }

    public String getFolderPath() {
        //return "C:" + File.separator + "pictures" + File.separator;
        return applicationsettingFacade.findBy("Applicationsetting.findBySettingname", "settingName", "imageFolderPath").getSettingValue();
    }

    public void upload(Part file) {

        try {
            if (!getFilename(file).equals("")) {

//                System.out.println(getFilename(file));
//                file.write("C:\\data\\" + getFilename(file));
                // Extract file name from content-disposition header of file part
                String fileName = this.getFilename(file);
                Calendar calendar = Calendar.getInstance();
                String timestamp = Long.toString(calendar.getTimeInMillis());
                fileName = fileName.replace(".", timestamp + ".");
                String basePath = getFolderPath();
                basePath += File.separator;
                File outputFilePath = new File(basePath + fileName);

                // Copy uploaded file to destination path
                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    inputStream = file.getInputStream();
                    outputStream = new FileOutputStream(outputFilePath);
                    int read = 0;
                    final byte[] bytes = new byte[1024];
                    while ((read = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, read);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    fileName = "";
                } finally {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
                current.setPicturepath(fileName);
            }

        } catch (Exception e) {

            if (current.getGender().equals("Female")) {
                current.setPicturepath("defaultwomenPicture.png");
            } else {
                current.setPicturepath("defaultManPicture.png");
            }

        }
    }

    private static String getFilename(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.  
            }
        }
        return null;
    }

    public String create() {

        try {
            this.upload(file);
            List<Vehicle> vehicles = new ArrayList<Vehicle>();
            vehicles.add(vehicle);
            current.setVehicleCollection(vehicles);
            current.setCreatedate(new Date());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DriverCreated"));
            prepareCreate();
            return prepareList();
        } catch (Exception e) {

            return null;
        }
    }

    public String prepareEdit() {
        current = (Driver) getItems().getRowData();
        vehicle = current.getVehicleCollection().iterator().next();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            this.upload(file);
            List<Vehicle> v = new ArrayList<>();
            v.add(vehicle);
            current.setVehicleCollection(v);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DriverUpdated"));
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Driver) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return prepareList();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DriverDeleted"));
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

    @PostConstruct
    public void init() {
        connectedUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("connectedUser");
    }

    @FacesConverter(forClass = Driver.class)
    public static class DriverControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DriverController controller = (DriverController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "driverController");
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
            if (object instanceof Driver) {
                Driver o = (Driver) object;
                return getStringKey(o.getDriverid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Driver.class.getName());
            }
        }

    }

}
