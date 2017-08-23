package com.veganet.gps.jsf;

import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Devicedistributor;
import com.veganet.gps.entities.Distributer;
import com.veganet.gps.entities.Logs;
import com.veganet.gps.entities.User;
import com.veganet.gps.entities.Vehicle;
import com.veganet.gps.jsf.util.JsfUtil;
import com.veganet.gps.jsf.util.PaginationHelper;
import com.veganet.gps.session.DeviceFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean(name = "deviceController")
@SessionScoped
public class DeviceController implements Serializable {

    private Device current;
    private DataModel items = null;
    @EJB
    private com.veganet.gps.session.DeviceFacade ejbFacade;
    @EJB
    private com.veganet.gps.session.DevicedistributorFacade ejbFacadeDeviceDistributer;
    @EJB
    com.veganet.gps.session.VehicleFacade VehicleFacade;
    @EJB
    private com.veganet.gps.session.DistributerFacade distributerFacade;
    @EJB
    private com.veganet.gps.session.UserFacade userFacade;
    @EJB
    private com.veganet.gps.session.LogsFacade logsFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private User connectedUser;
    private short zero = 0;
    private short one = 1;
    private List<Device> devices;
    private List<Devicedistributor> affectedTo;
    private Vehicle vehicle;
    private int periodeSubscription;
    private boolean filter = false;
    private String VehiculeSearch;
    private String affectedToSearch;
    private int SubscriptionStatus = 0;
    private TreeNode root;
    private TreeNode selectedNodes;
    private User selectedUserTree;

    public TreeNode getSelectedNodes() {

        return selectedNodes;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public void setSelectedNodes(TreeNode selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void prepareDistributerTree() {

        if (filter == false) {
            root = new DefaultTreeNode("root", null);
            TreeNode nodeRoot = new DefaultTreeNode(connectedUser.getDistributerid(), root);
            nodeRoot.setExpanded(true);
            nodeRoot.setSelectable(true);
            nodeRoot.setSelected(true);

            for (Distributer d : distributerFacade.findByParentid(connectedUser.getDistributerid())) {
                TreeNode node0 = new DefaultTreeNode(d, nodeRoot);
                recursiveTreeTreatement(node0, d, true);
                node0.setExpanded(true);
            }
        }

    }

    public void recursiveTreeTreatement(TreeNode node0, Object parent, Boolean expanded) {
        for (Distributer d : distributerFacade.findByParentid((Distributer) parent)) {
            TreeNode node = new DefaultTreeNode(d, node0);
            node.setExpanded(expanded);
            recursiveTreeTreatement(node, d, expanded);
        }

    }

    public void recursiveTreeTreatementView(TreeNode node0, Distributer parent, Boolean expanded, Boolean selectable) {
        for (Distributer d : distributerFacade.findByParentid(parent)) {
            TreeNode node = new DefaultTreeNode(d, node0);
            node.setExpanded(expanded);
            node.setSelectable(selectable);
            if (d.getDistributerid() == connectedUser.getDistributerid().getDistributerid()) {
                selectedNodes = node;
            }
            recursiveTreeTreatementView(node, d, expanded, selectable);
        }
    }

    public String getVehiculeSearch() {
        return VehiculeSearch;
    }

    public void setVehiculeSearch(String VehiculeSearch) {
        this.VehiculeSearch = VehiculeSearch;
    }

    public int getSubscriptionStatus() {
        return SubscriptionStatus;
    }

    public void setSubscriptionStatus(int SubscriptionStatus) {
        this.SubscriptionStatus = SubscriptionStatus;
    }

    public String getAffectedToSearch() {
        return affectedToSearch;
    }

    public void setAffectedToSearch(String affectedToSearch) {
        this.affectedToSearch = affectedToSearch;
    }

    public boolean isFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    public int getPeriodeSubscription() {
        return periodeSubscription;
    }

    public void setPeriodeSubscription(int periodeSubscription) {
        this.periodeSubscription = periodeSubscription;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public List<Devicedistributor> getAffectedTo() {
        return affectedTo;
    }

    public void setAffectedTo(List<Devicedistributor> affectedTo) {
        this.affectedTo = affectedTo;
    }

    public List<Device> getDevices() {
        devices = getFacade().findRange(null, zero);
        return devices;
    }

    public void prepareaffect() {
        affectedTo = ejbFacadeDeviceDistributer.findBy("Devicedistributor.findByAssigned", "assigned", 1);
        devices = getFacade().findRange(null, zero);
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public User getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(User connectedUser) {
        this.connectedUser = connectedUser;
    }

    public DeviceController() {

    }

    public Device getSelected() {
        if (current == null) {

            current = new Device();
            selectedItemIndex = -1;
        }
        return current;
    }

    private DeviceFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {

                    if (selectedUserTree == null) {

                        if (connectedUser.getAccesslevel() == 1 || connectedUser.getAccesslevel() == 3) {

                            return ejbFacadeDeviceDistributer.count(one, null, connectedUser.getDistributerid(), zero);

                        } else if (connectedUser.getAccesslevel() == 0) {

                            return getFacade().count(zero);
                        } else {
                            return ejbFacadeDeviceDistributer.count(one, connectedUser, connectedUser.getDistributerid(), zero);

                        }
                    } else {
                        if (selectedUserTree.getDistributerid().getDistributerid() == connectedUser.getDistributerid().getDistributerid() && connectedUser.getAccesslevel() == 0) {
                            return getFacade().count(zero);

                        } else {
                            return ejbFacadeDeviceDistributer.count(one, null, selectedUserTree.getDistributerid(), zero);

                        }

                    }

                }

                @Override
                public DataModel createPageDataModel() {
                    if (filter == false) {

                        if (selectedUserTree == null) {
                            //agent
                            if (connectedUser.getAccesslevel() == 1 || connectedUser.getAccesslevel() == 3) {
                                return new ListDataModel(ejbFacadeDeviceDistributer.findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, one, null, connectedUser.getDistributerid(), zero));
                            } //admin
                            else if (connectedUser.getAccesslevel() == 0) {

                                return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, zero));

                            } //customer
                            else {
                                return new ListDataModel(ejbFacadeDeviceDistributer.findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, one, connectedUser, connectedUser.getDistributerid(), zero));

                            }
                        } else {

                            //admin
                            if (selectedUserTree.getDistributerid().getDistributerid() == connectedUser.getDistributerid().getDistributerid() && connectedUser.getAccesslevel() == 0) {

                                return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, zero));
                                //agent
                            } else {
                                return new ListDataModel(ejbFacadeDeviceDistributer.findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, one, null, selectedUserTree.getDistributerid(), zero));
                            }

                        }
                    } else {
                        return null;
                    }

                }
            };
        }
        return pagination;
    }

    public void refreshTableDevices() {
        filter = false;
        items = null;
        selectedUserTree = userFacade.findAdminDistributerForDistributer("User.findAdminDistributerForDistributer", "distributerid", (Distributer) selectedNodes.getData(), "accesslevel", (short) 3);
        recreatePagination();
        items = getPagination().createPageDataModel();

    }
    public List<Distributer> ds = new ArrayList<Distributer>();

    public void findAllChildDistr(Distributer d) {

        for (Distributer fd : d.getDistributerCollection()) {
            ds.add(fd);
            if (fd.getDistributerCollection().size() > 0) {
                findAllChildDistr(fd);
            }
        }

        System.out.println("number of distributer" + ds.size());
    }

    public void filterDevices() {

        filter = true;
        recreatePagination();
        selectedUserTree = userFacade.findAdminDistributerForDistributer("User.findAdminDistributerForDistributer", "distributerid", (Distributer) selectedNodes.getData(), "accesslevel", (short) 3);
        items = getPagination().createPageDataModel();

        List<Device> devices = new ArrayList<Device>();
        List<Device> devicesAffected = new ArrayList<Device>();

        //adminagent or agent
        if (connectedUser.getAccesslevel() == 1 || connectedUser.getAccesslevel() == 3) {
            if (selectedUserTree == null) {
                devices = ejbFacadeDeviceDistributer.search(null, one, null, connectedUser.getDistributerid(), zero, current.getImei(), current.getSimcard(), VehiculeSearch, SubscriptionStatus);

            }
            if (selectedUserTree != null) {
                devices = ejbFacadeDeviceDistributer.search(null, one, null, selectedUserTree.getDistributerid(), zero, current.getImei(), current.getSimcard(), VehiculeSearch, SubscriptionStatus);

            }

        } //admin                      
        else if (connectedUser.getAccesslevel() == 0) {
            if (selectedUserTree == null && connectedUser.getAccesslevel() == 0) {

                devices = getFacade().search(null, zero, current.getImei(), current.getSimcard(), VehiculeSearch, SubscriptionStatus);

            } else {
                devices = ejbFacadeDeviceDistributer.search(null, one, null, selectedUserTree.getDistributerid(), zero, current.getImei(), current.getSimcard(), VehiculeSearch, SubscriptionStatus);

            }

        } //customer
        else {
            devices = ejbFacadeDeviceDistributer.search(null, one, connectedUser, connectedUser.getDistributerid(), zero, current.getImei(), current.getSimcard(), current.getVehicleid().getDescription(), SubscriptionStatus);

        }

        if (!affectedToSearch.equals("")) {

            for (Device d : devices) {
                for (Devicedistributor dd : d.getDevicedistributorCollection()) {
                    if (dd.getAssigned() == (short) 1 && dd.getCustomerid().getUsername().equals(affectedToSearch)) {
                        devicesAffected.add(dd.getDeviceid());
                    }
                }

            }
        } else {
            devicesAffected.addAll(devices);
        }
        items = new ListDataModel(devicesAffected);

    }

    public boolean refreshAffection() {
        affectedTo = ejbFacadeDeviceDistributer.findBy("Devicedistributor.findByAssigned", "assigned", 1);
        return true;
    }

    public String prepareList() {
        filter = false;
        current = new Device();
        affectedToSearch = "";
        VehiculeSearch = "";
        SubscriptionStatus = 0;
        recreateModel();
        affectedTo = ejbFacadeDeviceDistributer.findBy("Devicedistributor.findByAssigned", "assigned", 1);
        // selectedUserTree = connectedUser;
        return "/device/List?faces-redirect=true";
    }

    public String affectedToDevice(int idDevice) {

        String r = "";
        for (Devicedistributor d : affectedTo) {
            if (d.getDeviceid().getDeviceid() == idDevice) {
                r = d.getCustomerid().getUsername();
            }

        }
        return r;
    }

    public String prepareView() {
        current = (Device) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {

        current = new Device();

        vehicle = new Vehicle();
        periodeSubscription = 12;
        selectedItemIndex = -1;
        return "/device/Create?faces-redirect=true";
    }

    public String prepareFeedBack() {
        return "/device/FeedBack?faces-redirect=true";

    }

    public Date addMonthsTodate(Date date, int months) {
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months); // adds one hour
        return calendar.getTime();
    }

    public String create() {
        if (!this.duplicatedDeviceImei()) {
            try {
                vehicle.setFuelconsumption(5.5f);
                vehicle.setCreatedate(new Date());
                vehicle.setIsactive((short) 1);
                vehicle.setIcon((short) 0);
                vehicle.setMaxSpeed(100);
                vehicle.setRememberbeforeoil(300);
                vehicle.setRememberbeforetechnical(30);
                if (vehicle.getDescription().equals("")) {
                    vehicle.setDescription("GT06-" + current.getImei().substring(11, 15));
                }
                VehicleFacade.create(vehicle);
                current.setCreatedate(new Date());
                current.setIsactive(one);
                current.setIsdeleted(zero);
                current.setAddedby(connectedUser);
                current.setVehicleid(vehicle);
                current.setGeostatus((short) 0);
                current.setCommandtype((short)0);
                //default power on
                current.setPower((short) 0);
                current.setActivatedate(new Date());
                current.setUseractivationdate(new Date());
                Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
                calendar.setTime(new Date());
                calendar.add(Calendar.YEAR, 1); // adds one hour
                current.setExpirationdate(calendar.getTime());
                switch (periodeSubscription) {
                    case 1:
                        current.setUserexpirationdate(addMonthsTodate(new Date(), 1));
                        break;
                    case 3:
                        current.setUserexpirationdate(addMonthsTodate(new Date(), 3));
                        break;
                    case 6:
                        current.setUserexpirationdate(addMonthsTodate(new Date(), 6));
                        break;
                    case 9:
                        current.setUserexpirationdate(addMonthsTodate(new Date(), 9));
                        break;
                    default:

                        Calendar calendar2 = GregorianCalendar.getInstance();
                        calendar2.setTime(new Date());
                        calendar2.add(Calendar.YEAR, 1);
                        current.setUserexpirationdate(calendar2.getTime());
                        break;
                }
                current = getFacade().createReturnID(current);
                //add Device saved in  logs table
                HttpServletRequest request2 = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                String ipAddress = request2.getHeader("X-FORWARDED-FOR");
                if (ipAddress == null) {
                    ipAddress = request2.getRemoteAddr();
                }
                if (ipAddress == null) {
                    ipAddress = "non dispo";
                }
                Logs log = new Logs(new Date(), "Add Device", connectedUser, ipAddress, current);
                logsFacade.create(log);

                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DeviceCreated"));
                prepareCreate();

                RequestContext requestContext6 = RequestContext.getCurrentInstance();
                requestContext6.execute("PF('dlgmessagetogps').show();");

                // return prepareList();
                return "";
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                return null;
            }
        } else {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("Imeialreadyexsist"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Device) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(current.getUseractivationdate());
        int sMonth = calendar.get(Calendar.MONTH);
        calendar.setTime(current.getUserexpirationdate());
        int eMonth = calendar.get(Calendar.MONTH);
        periodeSubscription = eMonth - sMonth;
        if (eMonth - sMonth == 0) {
            periodeSubscription = 12;
        }
        return "Edit";
    }

    public String update() {
        if (connectedUser.getAccesslevel() == 0) {
            if (!this.duplicatedDeviceImei()) {
                try {

                    switch (periodeSubscription) {
                        case 1:
                            current.setUserexpirationdate(addMonthsTodate(current.getActivatedate(), 1));
                            break;
                        case 3:
                            current.setUserexpirationdate(addMonthsTodate(current.getActivatedate(), 3));
                            break;
                        case 6:
                            current.setUserexpirationdate(addMonthsTodate(current.getActivatedate(), 6));
                            break;
                        case 9:
                            current.setUserexpirationdate(addMonthsTodate(current.getActivatedate(), 9));
                            break;
                        default:

                            Calendar calendar2 = GregorianCalendar.getInstance();
                            calendar2.setTime(current.getActivatedate());
                            calendar2.add(Calendar.YEAR, 1);
                            current.setUserexpirationdate(calendar2.getTime());
                            break;
                    }

                    getFacade().edit(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DeviceUpdated"));
                    return prepareList();
                } catch (Exception e) {
                    JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                    return null;
                }
            } else {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("Imeialreadyexsist"));
                return null;
            }
        } else {
            try {

                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DeviceUpdated"));
                return prepareList();
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                return null;
            }
        }
    }

    public String destroyWithConfirm() {
        String value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("devicetoremove");
        current = ejbFacade.find(Integer.valueOf(value));
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return prepareList();
    }

    public String destroy() {
        current = (Device) getItems().getRowData();
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

            current.setIsdeleted(one);
            current.setIsactive(zero);
            getFacade().edit(current);
            //remove device saved in  logs table
            HttpServletRequest request2 = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String ipAddress = request2.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request2.getRemoteAddr();
            }
            if (ipAddress == null) {
                ipAddress = "non dispo";
            }
            Logs log = new Logs(new Date(), "Remove Device", connectedUser, ipAddress, current);
            logsFacade.create(log);

            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DeviceDeleted"));
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

    public boolean duplicatedDeviceImei() {

        List<Device> listDevice = new ArrayList<Device>();
        listDevice = getFacade().findBy("Device.findByImei", "imei", current.getImei());
        System.out.println(listDevice.size());

        if (listDevice.isEmpty() || (listDevice.size() == 1 && listDevice.get(0).getDeviceid() == current.getDeviceid())) {
            return false;

        } else {
            return true;
        }

    }

//    public void prepareGetAllDevice() {
//        devices = ejbFacade.findAll();
//
//    }
    @PostConstruct
    public void init() {
        connectedUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("connectedUser");
        prepareDistributerTree();
    }

//    public void myDevices(){
//        System.out.println("startmydevices");
//        connectedUser=(User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("connectedUser");
//       
//        System.out.println("userid:"+connectedUser.getUserid());        
//        //listofdevicesaddedbyme
//        System.out.println("list of my devices:"+connectedUser.getDeviceCollection().size());
//        //devicedistributoraddbyme
//        System.out.println("list of my devices:"+connectedUser.getDevicedistributoraddbymeCollection().size());
//        //mydevices
//        System.out.println(connectedUser.getMydevicedistributorCollection().size());
//    }
    @FacesConverter(forClass = Device.class)
    public static class DeviceControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DeviceController controller = (DeviceController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "deviceController");
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
            if (object instanceof Device) {
                Device o = (Device) object;
                return getStringKey(o.getDeviceid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Device.class.getName());
            }
        }

    }

}
