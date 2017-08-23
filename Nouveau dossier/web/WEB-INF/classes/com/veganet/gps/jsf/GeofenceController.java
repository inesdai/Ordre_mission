package com.veganet.gps.jsf;

import com.gpstracker.graphic.Polygon2D;
//import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;
//import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;
//import com.veganet.gps.entities.Alert;

import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Geofence;
import com.veganet.gps.entities.Notification;
import com.veganet.gps.entities.User;
import com.veganet.gps.entities.Vehicle;
import com.veganet.gps.jsf.util.JsfUtil;
import com.veganet.gps.jsf.util.PaginationHelper;
import com.veganet.gps.session.DeviceFacade;
import com.veganet.gps.session.GeofenceFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "geofenceController", eager = true)
@SessionScoped
public class GeofenceController implements Serializable {

    private Geofence current;
    private List<Geofence> listGeofences = new ArrayList<Geofence>();
    private DataModel items = null;
    @EJB
    private com.veganet.gps.session.GeofenceFacade ejbFacade;

    @EJB
    private com.veganet.gps.session.DeviceFacade deviceFacade;

    @EJB
    private com.veganet.gps.session.DevicedistributorFacade ejbFacadeDeviceDistributer;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<Vehicle> vehicles = new ArrayList<Vehicle>();
    private User connectedUser;
    private Vehicle vehicleInMonitoring;
    private short actionGeofence = (short) 0;
    private String validGeofence = "no";
    private String GeofenceZone = "";
    private boolean printOrEdit = false;

    public boolean getPrintOrEdit() {
        return printOrEdit;
    }

    public void setPrintOrEdit(boolean printOrEdit) {
        this.printOrEdit = printOrEdit;
    }

    public short getActionGeofence() {
        return actionGeofence;
    }

    public void setActionGeofence(short actionGeofence) {
        this.actionGeofence = actionGeofence;
    }

    public String getValidGeofence() {
        return validGeofence;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public Vehicle getVehicleInMonitoring() {
        return vehicleInMonitoring;
    }

    public void setVehicleInMonitoring(Vehicle vehicleInMonitoring) {
        this.vehicleInMonitoring = vehicleInMonitoring;
    }

    public void setValidGeofence(String validGeofence) {
        this.validGeofence = validGeofence;
    }

    public String getGeofenceZone() {
        return GeofenceZone;
    }

    public void setGeofenceZone(String GeofenceZone) {
        this.GeofenceZone = GeofenceZone;
    }

    @PostConstruct
    public void init() {
        connectedUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("connectedUser");
    }

    public GeofenceFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(GeofenceFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public DeviceFacade getDeviceFacade() {
        return deviceFacade;
    }

    public void setDeviceFacade(DeviceFacade deviceFacade) {
        this.deviceFacade = deviceFacade;
    }

    public GeofenceController() {
    }

    public List<Geofence> getListGeofences() {
        return listGeofences;
    }

    public void setListGeofences(List<Geofence> listGeofences) {
        this.listGeofences = listGeofences;
    }

    public Geofence getSelected() {
        if (current == null) {
            current = new Geofence();
            selectedItemIndex = -1;
        }
        return current;
    }

    private GeofenceFacade getFacade() {
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

    public void prepareListByVehicle() {
        vehicleInMonitoring = (Vehicle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("vehiclesInMonitoring");
        items = new ListDataModel(getFacade().findRange(null, vehicleInMonitoring.getDevice()));

    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Geofence) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Geofence();
        selectedItemIndex = -1;
        return "Create";
    }

    public void selectMyGeofences() {
        List<Geofence> geofences = new ArrayList<Geofence>();
        Geofence geofence;
        Iterator<Device> iDevices;
        List<Device> devices = new ArrayList<Device>();
        Iterator<Geofence> iGeofence;
        Device d;
        String geoFenceZone = "";
        String nameGeofence = "";
        boolean emptyGeoFence = false;
        devices = ejbFacadeDeviceDistributer.findRange(null, (short) 1, connectedUser, connectedUser.getDistributerid(), (short) 0);
        iDevices = devices.iterator();
        for (int dv = 0; dv < devices.size(); dv++) {

            d = (Device) iDevices.next();
            if (!d.getGeofenceCollection().isEmpty()) {
                emptyGeoFence = true;
                geofences.addAll(d.getGeofenceCollection());

                iGeofence = geofences.iterator();

                for (int i = 0; i < geofences.size(); i++) {

                    geofence = (Geofence) iGeofence.next();
                    geoFenceZone = geoFenceZone + geofence.getGeofencezone() + "&&";
                    nameGeofence = geofence.getGeofencename();
                }
                RequestContext requestContext2 = RequestContext.getCurrentInstance();
                requestContext2.execute("drawGeofencesGoogleMap('" + geoFenceZone + "','" + nameGeofence + "');");
                geofences = new ArrayList<Geofence>();
                geoFenceZone = "";
                nameGeofence = "";

            }
        }
        if (!emptyGeoFence) {
            RequestContext requestContext7 = RequestContext.getCurrentInstance();
            requestContext7.execute("PF('emptyGeofence').show();");

        }

    }

//    public void geoFenceFunction() {
////        System.out.println("start geo feces");
//        iDevices = null;
//        d = null;
//        short zero = 0;
//
//        iDevices = deviceFacade.findRange(null, zero).iterator();
//
//        while (iDevices.hasNext()) {
//            d = (Device) iDevices.next();
//            if (!d.getGeofenceCollection().isEmpty()) {
//                if (containFunction(d.getLastpositionid().getLatitude(), d.getLastpositionid().getLongitude(), (List<Geofence>) d.getGeofenceCollection()) == false);
//                {
//
//                    context = FacesContext.getCurrentInstance();
//                    elContext = context.getELContext();
//                    notification = new Notification(0, "out of geoFence", new Date(), (short) 1, d, d.getLastpositionid());
//
//                    NotificationController n = (NotificationController) elContext.getELResolver().getValue(elContext, null, "notificationController");
//
//                    n.setCurrent(notification);
//
//                    n.create();
//                }
//            }
//        }
//
//    }
    public boolean containFunction(double xPoint, double yPoint, List<Geofence> geoFences) {
        float[] xPoly = null;
        float[] yPoly = null;
        String s = null;
        boolean r = false;
        Polygon2D p = null;
        Geofence geofence = null;

        if (geoFences.size() > 0) {
            Iterator<Geofence> iGeofence = geoFences.iterator();
            while (iGeofence.hasNext()) {
                geofence = iGeofence.next();
                //type of geofence 0==polygon
                if (geofence.getTypegeofence() != (short) 0) {
                    s = geofence.getGeofencezone();
                    s = s.replace("(", "");
                    s = s.replace(")", "");
                    String[] point = s.split(",");
                    xPoly = new float[point.length];
                    yPoly = new float[point.length];
                    int i = 0;
                    for (int k = 0; k < point.length; k = k + 2) {
//                        System.out.println(point[k] + "////////");
//     System.out.println(point[k+1] + "////////");
                        xPoly[i] = Float.parseFloat(point[k]);
                        yPoly[i] = Float.parseFloat(point[k + 1]);
                        i++;
                    }
                    p = new Polygon2D(xPoly, yPoly, xPoly.length);
                }
//                      System.out.println("poly"+p+"x"+xPoint+"y"+yPoint);
// les traitements a faire
                if (p.contains(xPoint, yPoint)) {

                    System.out.println("contain");
                    r = true;
                } else if (!p.contains(xPoint, yPoint)) {
                    System.out.println("not container");
                    r = false;
                }

            }

        }
        return r;
    }

    public void prepareShowGeofence() {
        printOrEdit = false;
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("PF('geoFenceZone').show();");
        current = (Geofence) getItems().getRowData();
        requestContext.execute("initGoogleMapPrintGeofence();");

        if (current != null) {
            if (current.getTypegeofence() % 10 == 2) {
                RequestContext requestContext2 = RequestContext.getCurrentInstance();
                requestContext2.execute("drawGeofenceZonePolygone('" + current.getGeofencezone() + "','" + current.getGeofencename() + "','" + vehicleInMonitoring.getDevice().getLastpositionid().getLatitude() + "','" + vehicleInMonitoring.getDevice().getLastpositionid().getLongitude() + "','" + vehicleInMonitoring.getDescription() + "');");
            }
            if (current.getTypegeofence() % 10 == 1) {
                RequestContext requestContext2 = RequestContext.getCurrentInstance();
                requestContext2.execute("drawGeofenceZoneCircle('" + current.getGeofencezone() + "','" + current.getGeofencename() + "','" + vehicleInMonitoring.getDevice().getLastpositionid().getLatitude() + "','" + vehicleInMonitoring.getDevice().getLastpositionid().getLongitude() + "','" + vehicleInMonitoring.getDescription() + "');");
            }

        }

    }

    public void prepareCreateGeofence() {
        printOrEdit = true;
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("PF('geoFenceZone').show();");

        requestContext.execute("initGoogleMapPrintGeofence();");

        if (vehicleInMonitoring.getDevice() != null && vehicleInMonitoring.getDevice().getLastpositionid() != null) {

            RequestContext requestContext2 = RequestContext.getCurrentInstance();
            requestContext2.execute("googleMapDrawNewGeofence(" + vehicleInMonitoring.getDevice().getLastpositionid().getLatitude() + "," + vehicleInMonitoring.getDevice().getLastpositionid().getLongitude() + ",'" + vehicleInMonitoring.getDescription() + "');");
            vehicles.add(vehicleInMonitoring);
        }

    }

    public void create() {

        current.setCreatedate(new Date());
        current.setStatus(new Short("0"));
        current.setGeofencezone(GeofenceZone);
        if (current.getDesactivatetime() == null && current.getActivatetime() == null) {
            current.setCreatedate(new Date());
        }
        if (validGeofence.equals("valCir")) {
            current.setTypegeofence((short) 1);
        } else if (validGeofence.equals("valPoly")) {
            current.setTypegeofence((short) 2);
        }

        actionGeofence = (short) (actionGeofence * 10);
        current.setTypegeofence((short) (current.getTypegeofence() + actionGeofence));
        if (vehicles.size() > 1) {
            for (int i = 0; i < vehicles.size(); i++) {
                current.setDeviceid(vehicles.get(i).getDevice());
                try {

                    getFacade().create(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("GeofenceCreated"));
                    prepareCreate();
                } catch (Exception e) {
                    JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));

                }
            }

        } else if (vehicles.size() == 1) {

            current.setDeviceid(vehicles.get(0).getDevice());
            try {
                getFacade().create(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("GeofenceCreated"));
                prepareCreate();
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));

            }

        }

    }

    public String prepareEdit() {
        current = (Geofence) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("GeofenceUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroyOneGeoFence() {
        current = (Geofence) getItems().getRowData();

        performDestroy();
        recreatePagination();
        recreateModel();
        prepareListByVehicle();
        return prepareList();
    }

    public String destroyAllGeoFence() {
        System.out.println("start destroy all geo fences");
//        List<Geofence> geofences = new ArrayList<Geofence>();
//        Geofence geofence;
//        Iterator<Device> iDevices;
//        Device d;
//        iDevices = ejbFacadeDeviceDistributer.findRange(null, (short) 1, connectedUser, connectedUser.getDistributerid(), (short) 0).iterator();
//
//        while (iDevices.hasNext()) {
//            d = (Device) iDevices.next();
//            geofences.addAll(d.getGeofenceCollection());
//            while (geofences.iterator().hasNext()) {
//                geofence = (Geofence) geofences.iterator().next();
//                getFacade().remove(geofence);
//
//            }
//        }

//        current = (Geofence) getItems().getRowData();
//        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
//        performDestroy();
//        recreatePagination();
//        recreateModel();
//        return "List";
        List<Geofence> geofences = new ArrayList<Geofence>();
        Geofence geofence;
        Iterator<Device> iDevices;
        List<Device> devices = new ArrayList<Device>();
        Iterator<Geofence> iGeofence;
        Device d;
        String geoFenceZone = "";
        String nameGeofence = "";

        devices = ejbFacadeDeviceDistributer.findRange(null, (short) 1, connectedUser, connectedUser.getDistributerid(), (short) 0);
        iDevices = devices.iterator();
        for (int dv = 0; dv < devices.size(); dv++) {

            d = (Device) iDevices.next();
            if (!d.getGeofenceCollection().isEmpty()) {
                geofences.addAll(d.getGeofenceCollection());
                iGeofence = geofences.iterator();

                for (int i = 0; i < geofences.size(); i++) {
                    System.out.println("i:" + i);
                    geofence = (Geofence) iGeofence.next();
                    getFacade().remove(geofence);
                }

            }
        }
        if (geofences.isEmpty()) {
            RequestContext requestContext6 = RequestContext.getCurrentInstance();
            requestContext6.execute("PF('erroresetgeofence').show();");
        }

        return null;
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
//            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("GeofenceDeleted"));
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

    @FacesConverter(forClass = Geofence.class)
    public static class GeofenceControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GeofenceController controller = (GeofenceController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "geofenceController");
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
            if (object instanceof Geofence) {
                Geofence o = (Geofence) object;
                return getStringKey(o.getGeofenceid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Geofence.class.getName());
            }
        }

    }

}
