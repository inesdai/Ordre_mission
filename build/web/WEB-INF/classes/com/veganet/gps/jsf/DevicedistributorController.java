package com.veganet.gps.jsf;

import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Devicedistributor;
import com.veganet.gps.entities.Distributer;
import com.veganet.gps.entities.User;
import com.veganet.gps.entities.Vehicle;
import com.veganet.gps.jsf.util.JsfUtil;
import com.veganet.gps.jsf.util.PaginationHelper;
import com.veganet.gps.session.DevicedistributorFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean(name = "devicedistributorController")
@SessionScoped
public class DevicedistributorController implements Serializable {

    private Devicedistributor current;
    private DataModel items = null;
    @EJB
    private com.veganet.gps.session.DevicedistributorFacade ejbFacade;
    @EJB
    private com.veganet.gps.session.DistributerFacade distributerFacade;
    @EJB
    private com.veganet.gps.session.DeviceFacade deviceFacade;
    @EJB
    private com.veganet.gps.session.UserFacade userFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private User connectedUser;
    private int typeUser;
    private List<Device> devices;
    private TreeNode root;

    private TreeNode selectedNodes;

    public TreeNode getSelectedNodes() {

        return selectedNodes;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void prepareDistributerTree() {
        root = new DefaultTreeNode("root", null);
        TreeNode nodeRoot = new DefaultTreeNode(connectedUser, root);
        nodeRoot.setExpanded(true);

//        if (connectedUser.getAccesslevel() == 0) {
        for (Distributer d : distributerFacade.findByParentid(connectedUser.getDistributerid())) {

            TreeNode node0 = new DefaultTreeNode(d, nodeRoot);
            List<User> users = userFacade.findByDistributer("User.findByDistributerId", "distributer", (Distributer) connectedUser.getDistributerid());

            recursiveTreeTreatement(node0, d, false);

            node0.setExpanded(false);

        }
        for (User u : userFacade.findByDistributer("User.findByDistributerId", "distributer", (Distributer) connectedUser.getDistributerid())) {

            if (connectedUser.getUserid() != u.getUserid() && u.getAccesslevel() == 2) {
                TreeNode node1 = new DefaultTreeNode(u, nodeRoot);
                node1.setExpanded(false);


            }

        }
//        }
//        if (connectedUser.getAccesslevel() == 1 || connectedUser.getAccesslevel() == 3) {
//            for (Distributer d : distributerFacade.findByParentid(connectedUser.getDistributerid())) {
//                TreeNode node0 = new DefaultTreeNode(d, root);
//                node0.setExpanded(true);
//                recursiveTreeTreatement(node0, d, true);
//            }
//        }

    }

    public void recursiveTreeTreatement(TreeNode node0, Object parent, Boolean expanded) {
        for (User u : userFacade.findByDistributer("User.findByDistributerId", "distributer", (Distributer) parent)) {
            if (u.getAccesslevel() == 2) {
                TreeNode node1 = new DefaultTreeNode(u, node0);
                node1.setExpanded(false);

            }
        }
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

    public void setSelectedNodes(TreeNode selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    @PostConstruct
    public void init() {
        connectedUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("connectedUser");
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public int getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(int typeUser) {
        this.typeUser = typeUser;
    }
    private final Short one = 1;
    private final Short zero = 0;

    public User getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(User connectedUser) {
        this.connectedUser = connectedUser;
    }

    public DevicedistributorController() {
    }

    public Devicedistributor getSelected() {
        if (current == null) {
            current = new Devicedistributor();
            selectedItemIndex = -1;
        }
        return current;
    }

    private DevicedistributorFacade getFacade() {
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
                    if (connectedUser.getAccesslevel() != 3) {

                        return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, zero, connectedUser, null, zero));

                    } else {

                        return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                    }

                    //return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
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
        current = (Devicedistributor) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Devicedistributor();
        selectedItemIndex = -1;
        return "List?faces-redirect=true";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DevicedistributorCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Devicedistributor) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DevicedistributorUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Devicedistributor) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DevicedistributorDeleted"));
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

    public String prepareAffectDevice() {

        return "/device/UserDevice";
    }

    public void selectedTreeDevice() {

        int id = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("selectedDevice"));
        Device device = deviceFacade.find(id);

        for (Devicedistributor d : device.getDevicedistributorCollection()) {
            if (d.getDeviceid().getDeviceid() == id) {
                selectedNodes = new DefaultTreeNode(deviceFacade.find(5));

            }

        }
    }

    public String affectdevice() {

        System.out.println("here");
        if (selectedNodes.getData() != null) {

            String value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("devicetoaffect");
            Device device = deviceFacade.find(Integer.valueOf(value));

            current = new Devicedistributor();
            current.setAddedby(connectedUser);
            current.setAssigned(one);
            current.setCreatedate(new Date());

            if (selectedNodes.getData().getClass() == (User.class)) {
                User u1 = (User) selectedNodes.getData();
                current.setDistributerid(u1.getDistributerid());
                current.setCustomerid(u1);

            }
            if (selectedNodes.getData().getClass() == (Distributer.class)) {
                Distributer d = (Distributer) selectedNodes.getData();
                User u2 = userFacade.findAdminDistributerForDistributer("User.findAdminDistributerForDistributer", "distributerid", d, "accesslevel", (short) 3);
                current.setDistributerid(u2.getDistributerid());
                current.setCustomerid(u2);
            }
            connectedUser.getDeviceCollection().remove(current.getDeviceid());
            try {
                current.setDeviceid(device);
                getFacade().updateQuery("Devicedistributor.insertIsAssigned", "assigned", zero, "customerid", current.getCustomerid(), "deviceid", current.getDeviceid());

                if (getFacade().countRedundancyLines("Devicedistributor.addedYet", "assigned", (short) 1, "device", device) == 0) {
                    getFacade().create(current);
                }

                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DevicedistributorCreated"));
                current = new Devicedistributor();
                selectedItemIndex = -1;
                selectedNodes.setSelected(false);
                return prepareCreate();

            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                return null;
            }

        } else {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("selectuserrequiremessage"));
            return null;
        }

    }

    public void typeUserCodeChange(ValueChangeEvent e) {

        this.setTypeUser((int) e.getNewValue());

    }

    public void prepareGetMyDevice() {

        //        devices = getFacade().findBy("Devicedistributor.findDeviceByDestributor", "distributerid", connectedUser.getDistributerid());
        if (connectedUser.getAccesslevel() == 1 || connectedUser.getAccesslevel() == 3) {

            devices = ejbFacade.findRange(null, (short) 1, null, connectedUser.getDistributerid(), (short) 0);

        } else if (connectedUser.getAccesslevel() == 0) {

            devices = ejbFacade.findRange(null, (short) 1, connectedUser, connectedUser.getDistributerid(), (short) 0);

        }

    }

    @FacesConverter(forClass = Devicedistributor.class)
    public static class DevicedistributorControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DevicedistributorController controller = (DevicedistributorController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "devicedistributorController");
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
            if (object instanceof Devicedistributor) {
                Devicedistributor o = (Devicedistributor) object;
                return getStringKey(o.getDevicedistributerid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Devicedistributor.class.getName());
            }
        }

    }

}
