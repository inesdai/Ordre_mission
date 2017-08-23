package com.veganet.gps.jsf;

import com.jsf2leaf.model.LatLong;
import com.jsf2leaf.model.Layer;
import com.jsf2leaf.model.Map;
import com.jsf2leaf.model.Marker;
import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Distributer;
import com.veganet.gps.entities.Logs;
import com.veganet.gps.entities.Position;
import com.veganet.gps.entities.User;
import com.veganet.gps.entities.Vehicle;
import com.veganet.gps.jsf.util.JsfUtil;
import com.veganet.gps.jsf.util.PaginationHelper;
import com.veganet.gps.session.PositionFacade;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.codehaus.jackson.map.ObjectMapper;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean(name = "positionController")
@SessionScoped
public class PositionController implements Serializable {

    private Position current;
    private DataModel items = null;
    @EJB
    private com.veganet.gps.session.DevicedistributorFacade ejbFacadeDeviceDistributer;
    @EJB
    private com.veganet.gps.session.PositionFacade ejbFacade;
    @EJB
    private com.veganet.gps.session.DeviceFacade deviceFacade;
    @EJB
    private com.veganet.gps.session.DistributerFacade distributerFacade;
    @EJB
    private com.veganet.gps.session.UserFacade userFacade;
    @EJB
    private com.veganet.gps.session.LogsFacade logsFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Map springfieldMap = new Map();
    private List<Vehicle> vehiclesInMonitoring;
    private Device device;
    private int i = 0;
    private Date from;
    private Date to;
    private int SpeedLimit;
    private List<Position> positionsFeedBack;
    private String array = "";
    private String arrayFeedback = "";
    private String jsonString;
    private User connectedUser;
    private boolean interpolated = false;
    private Date maxDate;
    private String infoWindowFeedback = "";
    private String jsonStopPoints = "";
    private int timezone;
    private String jsonOverSpeedPositions = "";
    private short safetyCutPetrol = 0;
    private int petrolStatus = -1;
    //Real time tracking variables
    private String arrayRealTime = "";
    private Device deviceRealTime;
    private long lastRealTimePositionid;
    private TreeNode root;
    private TreeNode selectedNodes;
    static private TreeNode oldValue;
    static private TreeNode oldRoot;
    private User userTree;

    public TreeNode getSelectedNodes() {

        return selectedNodes;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void prepareDistributerTree() {
        if (oldValue != null) {
            root = oldRoot;
            selectedNodes = oldValue;
        }
        root = new DefaultTreeNode("root", null);
        TreeNode nodeRoot = new DefaultTreeNode(connectedUser.getDistributerid(), root);
        nodeRoot.setExpanded(true);
        for (Distributer d : distributerFacade.findByParentid(connectedUser.getDistributerid())) {

            TreeNode node0 = new DefaultTreeNode(d, nodeRoot);
            List<User> users = userFacade.findByDistributer("User.findByDistributerId", "distributer", (Distributer) connectedUser.getDistributerid());

            recursiveTreeTreatement(node0, d, true);

            node0.setExpanded(true);

        }
        for (User u : userFacade.findByDistributer("User.findByDistributerId", "distributer", (Distributer) connectedUser.getDistributerid())) {
            if (u.getUserid() != connectedUser.getUserid() && u.getAccesslevel() == 2) {
                TreeNode node1 = new DefaultTreeNode(u, nodeRoot);
                node1.setExpanded(true);
            }

            //            List<Device> devices = devicedistributorFacade.findRange(null, (short) 1, u, null, (short) 0);
//            for (Device d : devices) {
//
//                TreeNode node2 = new DefaultTreeNode(d, node1);
//                node2.setExpanded(true);
//            }
        }

    }

    public void recursiveTreeTreatement(TreeNode node0, Object parent, Boolean expanded) {
        for (User u : userFacade.findByDistributer("User.findByDistributerId", "distributer", (Distributer) parent)) {
            if (u.getAccesslevel() == 2) {

                TreeNode node1 = new DefaultTreeNode(u, node0);
                node1.setExpanded(true);

            }

            //            List<Device> devices = devicedistributorFacade.findRange(null, (short) 1, u, null, (short) 0);
//            for (Device d : devices) {
//                TreeNode node2 = new DefaultTreeNode(d, node1);
//                node2.setExpanded(true);
//            }
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

    public void recursiveForClient(TreeNode childTree) {

        for (TreeNode n : childTree.getChildren()) {
            if (!n.getData().equals(userTree)) {
                n.setSelected(false);
                n.getParent().setExpanded(true);
            } else if (n.getData().getClass() == User.class && n.getData().equals(userTree)) {
                n.setSelected(true);
                TreeNode t = n.getParent();

            }
            if (n.getChildCount() > 0) {
                recursiveForClient(n);
            }

        }

    }

    public void recursiveForDistributer(TreeNode childTree) {

        for (TreeNode n : childTree.getChildren()) {

            if (!n.getData().equals(userTree.getDistributerid())) {
                n.setSelected(false);
                n.getParent().setExpanded(true);
            } else if (n.getData().getClass() == Distributer.class && n.getData().equals(userTree.getDistributerid())) {
                n.setSelected(true);
                TreeNode t = n.getParent();

            }
            if (n.getChildCount() > 0) {
                recursiveForDistributer(n);
            }

        }

    }

    public void runRecursive() {
        if (vehiclesInMonitoring.size() > 0) {

            List<User> users = ejbFacadeDeviceDistributer.findUserDevice("Devicedistributor.findByUserDevice", "assigned", (short) 1, "deviceid", vehiclesInMonitoring.get(0).getDevice());
            if (users.size() > 0) {
                userTree = users.get(0);
                if (userTree.getAccesslevel() == 2) {
                    recursiveForClient(root);
                } else {
                    recursiveForDistributer(root);
                }
            }

        }

    }

    public void testTree() {

//          testSelectedNode.setRowKey("root");
        if (selectedNodes.getData().getClass() == (User.class)) {
            vehiclesInMonitoring = new ArrayList<Vehicle>();
            oldValue = selectedNodes;

            oldRoot = selectedNodes.getParent();

            User u = (User) selectedNodes.getData();

            for (Device device : ejbFacadeDeviceDistributer.findRange(null, (short) 1, u, null, (short) 0)) {
                vehiclesInMonitoring.add(device.getVehicleid());
            }

        }
        if (selectedNodes.getData().getClass() == (Distributer.class)) {
            vehiclesInMonitoring = new ArrayList<Vehicle>();
            Distributer d = (Distributer) selectedNodes.getData();
            for (Device device : ejbFacadeDeviceDistributer.findRange(null, (short) 1, null, d, (short) 0)) {
                vehiclesInMonitoring.add(device.getVehicleid());
            }
        }
    }

    public void setSelectedNodes(TreeNode selectedNodes) {
        this.selectedNodes = selectedNodes;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedNode", selectedNodes);

    }

    public Device getDeviceRealTime() {
        return deviceRealTime;
    }

    public void setDeviceRealTime(Device deviceRealTime) {
        this.deviceRealTime = deviceRealTime;
    }

    public String getArrayRealTime() {
        return arrayRealTime;
    }

    public void setArrayRealTime(String arrayRealTime) {
        this.arrayRealTime = arrayRealTime;
    }

    public int getPetrolStatus() {
        return petrolStatus;
    }

    public void setPetrolStatus(int petrolStatus) {
        this.petrolStatus = petrolStatus;
    }

    public short getSafetyCutPetrol() {
        return safetyCutPetrol;
    }

    public void setSafetyCutPetrol(short safetyCutPetrol) {
        this.safetyCutPetrol = safetyCutPetrol;
    }

    public String getJsonOverSpeedPositions() {
        return jsonOverSpeedPositions;
    }

    public void setJsonOverSpeedPositions(String jsonOverSpeedPositions) {
        this.jsonOverSpeedPositions = jsonOverSpeedPositions;
    }

    public User getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(User connectedUser) {
        this.connectedUser = connectedUser;
    }

    public String getJsonStopPoints() {
        return jsonStopPoints;
    }

    public void setJsonStopPoints(String jsonStopPoints) {
        this.jsonStopPoints = jsonStopPoints;
    }

    @PostConstruct
    public void init() {
        connectedUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("connectedUser");
        timezone = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("timeZone"));

        initialselectlist();
    }

    public String getInfoWindowFeedback() {
        return infoWindowFeedback;
    }

    public void setInfoWindowFeedback(String infoWindowFeedback) {
        this.infoWindowFeedback = infoWindowFeedback;
    }

    public int getSpeedLimit() {
        return SpeedLimit;
    }

    public void setSpeedLimit(int SpeedLimit) {
        this.SpeedLimit = SpeedLimit;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public String getArrayFeedback() {
        return arrayFeedback;
    }

    public void setArrayFeedback(String arrayFeedback) {
        this.arrayFeedback = arrayFeedback;
    }

    public void setArray(String array) {
        this.array = array;
    }

    public List<Vehicle> getVehiclesInMonitoring() {

        return vehiclesInMonitoring;
    }

    public void setVehiclesInMonitoring(List<Vehicle> vehiclesInMonitoring) {

        this.vehiclesInMonitoring = vehiclesInMonitoring;
        if (connectedUser.getAccesslevel() == 0) {

            runRecursive();
        }
    }

    public List<Position> getPositionsFeedBack() {
        return positionsFeedBack;
    }

    public void setPositionsFeedBack(List<Position> positionsFeedBack) {
        this.positionsFeedBack = positionsFeedBack;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date To) {
        this.to = To;
    }

    public int getI() {
        return i;
    }

    public boolean isInterpolated() {
        return interpolated;
    }

    public void setInterpolated(boolean interpolated) {
        this.interpolated = interpolated;
    }

    public Layer getPlacesLayer() {
        return placesLayer;
    }

    public void setPlacesLayer(Layer placesLayer) {
        this.placesLayer = placesLayer;
    }
    private Layer placesLayer;
    private Marker marker;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Device getDevice() {
        return device;
    }

    public String getArray() {
        return array;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public PositionController() {
        connectedUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("connectedUser");
        vehiclesInMonitoring = new ArrayList<Vehicle>();

    }

    public String realTimeTracking() {
        deviceRealTime = vehiclesInMonitoring.get(0).getDevice();
        try {
            lastRealTimePositionid = deviceRealTime.getLastpositionid().getPositionid();
            refreshPositionRealTime();
        } catch (Exception e) {
            return "/device/RealTimeTracking.xhtml?faces-redirect=true";
        }

        return "/device/RealTimeTracking.xhtml?faces-redirect=true";

    }

    public void changeMydevicesPetrolStatus() {

        short action = 1;
        String typeLogs = "";
        if (petrolStatus == 0) {
            action = (short) 1;
            typeLogs = "Restore Petrol";
        }
        if (petrolStatus == 1) {
            action = (short) 2;
            typeLogs = "Cut off Petrol";
        }
        if (petrolStatus == 2) {
            action = (short) 4;
            typeLogs = "Cut off Petrol";
        }

        if ((connectedUser.getAccesslevel() == 2 || connectedUser.getAccesslevel() == 0) && (action == (short) 1 || action == (short) 2 || action == (short) 4)) {
            List<Device> devices = new ArrayList<Device>();
            for (Vehicle vehicle : vehiclesInMonitoring) {
                vehicle.getDevice().setPower(action);
                deviceFacade.edit(vehicle.getDevice());

                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                String ipAddress = request.getHeader("X-FORWARDED-FOR");
                if (ipAddress == null) {
                    ipAddress = request.getRemoteAddr();
                }
                if (ipAddress == null) {
                    ipAddress = "non dispo";
                }
                Logs log = new Logs(new Date(), typeLogs, connectedUser, ipAddress, device);
                logsFacade.create(log);
            }
        }
        petrolStatus = -1;
    }

    public void hideConfirmAction() {
        petrolStatus = -1;
        RequestContext requestContext6 = RequestContext.getCurrentInstance();
        requestContext6.execute("PF('confirmActionPetrol').hide();");

    }

    //cut Off petrol function
    public void cutOffPetrol() {
        //emergency cut off
        if (safetyCutPetrol == (short) 1) {
            vehiclesInMonitoring.get(0).getDevice().setPower((short) 4);
            deviceFacade.edit(vehiclesInMonitoring.get(0).getDevice());
        } else {
            //safety cut off
            vehiclesInMonitoring.get(0).getDevice().setPower((short) 2);
            deviceFacade.edit(vehiclesInMonitoring.get(0).getDevice());
        }
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        if (ipAddress == null) {
            ipAddress = "non dispo";
        }
        Logs log = new Logs(new Date(), "Cut off Petrol", connectedUser, ipAddress, vehiclesInMonitoring.get(0).getDevice());
        logsFacade.create(log);

    }

    //restor petrol function
    public void restorPetrol() {
        vehiclesInMonitoring.get(0).getDevice().setPower((short) 1);
        deviceFacade.edit(vehiclesInMonitoring.get(0).getDevice());
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        if (ipAddress == null) {
            ipAddress = "non dispo";
        }
        Logs log = new Logs(new Date(), "Restore Petrol", connectedUser, ipAddress, vehiclesInMonitoring.get(0).getDevice());
        logsFacade.create(log);
    }

    public void monitoring() {

        if (!vehiclesInMonitoring.isEmpty()) {

            device = (Device) vehiclesInMonitoring.get(0).getDevice();

        } else {
            springfieldMap.setWidth("1050px").setHeight("650px").setCenter(new LatLong("31.631440", "9.428711")).setZoom(5);
        }

    }

    public void initialselectlist() {

        vehiclesInMonitoring = new ArrayList<Vehicle>();
        if (connectedUser.getAccesslevel() == 0) {
            List<Device> devices = new ArrayList<Device>();
            devices = deviceFacade.findRange(null, (short) 0);
        } else if (connectedUser.getAccesslevel() == 1 || connectedUser.getAccesslevel() == 3) {

            List<Device> devices = new ArrayList<Device>();
            devices = ejbFacadeDeviceDistributer.findRange(null, (short) 1, null, connectedUser.getDistributerid(), (short) 0);

        } else {
            List<Device> devices = new ArrayList<Device>();
            devices = ejbFacadeDeviceDistributer.findRange(null, (short) 1, connectedUser, connectedUser.getDistributerid(), (short) 0);
            for (Device device : devices) {

                if (device.getIsdeleted() == (short) 0) {
                    vehiclesInMonitoring.add(device.getVehicleid());
                }

            }
        }

    }

    public void JavaArrayAsAString() {
//        System.out.println("");
        // you need more error checking here, of course...
        this.drawFeedBack();
        int k;
        String s = "";

        k = positionsFeedBack.size();
        s = positionsFeedBack.get(0) + "";
        for (int i = 1; i < k; i++) {
            s += "|" + positionsFeedBack.get(i);
        }
        arrayFeedback = s;
    }

    public void generateFeedBackJsonString(boolean interpolate) {
        this.interpolated = interpolate;
        drawFeedBack();
        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonString = mapper.writeValueAsString(positionsFeedBack);

            StopPoints();
            overSpeedPositions();

            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/gpstracker/faces/device/FeedBack.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(PositionController.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    //convert time from current Time To Gps time
    public Date ConvertDateToCst(Date date) {
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -timezone + 8); // adds one hour
        return calendar.getTime();
    }

    public void drawFeedBack() {
        try {
            positionsFeedBack = getFacade().findRange(null, ConvertDateToCst(from), ConvertDateToCst(to), vehiclesInMonitoring.get(0).getDevice(), false, 0.5, false, (short) 0);

            infoWindowFeedback
                    = vehiclesInMonitoring.get(0).getDevice().getImei()
                    + "," + vehiclesInMonitoring.get(0).getDescription();

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ErrorInGetData"));

        }

    }

    public void StopPoints() {
        List<Position> listStopPoints = new ArrayList<Position>();

        Position precPosition;
        Position nexPosition;

        positionsFeedBack = getFacade().findRange(null, ConvertDateToCst(from), ConvertDateToCst(to), vehiclesInMonitoring.get(vehiclesInMonitoring.size() - 1).getDevice(), true, null, false, (short) 0);
        if (positionsFeedBack.size() > 0) {
            precPosition = positionsFeedBack.get(0);

            for (i = 1; i < positionsFeedBack.size(); i++) {
                nexPosition = positionsFeedBack.get(i);

//     0.0001==11 Meter/0.00012==11 Meter
                if (Math.abs(precPosition.getLatitude() - nexPosition.getLatitude()) < 0.0003 && Math.abs(precPosition.getLongitude() - nexPosition.getLongitude()) < 0.00035) {

                } else {

                    if (Math.abs(precPosition.getTimecreate().getTime() - positionsFeedBack.get(i - 1).getTimecreate().getTime()) > 60000) {
                        //add this point stop
                        listStopPoints.add(precPosition);
                        listStopPoints.add(positionsFeedBack.get(i - 1));
                    }

                    precPosition = nexPosition;

                }
            }
            if (Math.abs(precPosition.getTimecreate().getTime() - positionsFeedBack.get(i - 1).getTimecreate().getTime()) > 60000) {
                //add this point stop
                listStopPoints.add(precPosition);
                listStopPoints.add(positionsFeedBack.get(i - 1));

            }

            try {
                ObjectMapper mapper = new ObjectMapper();

                jsonStopPoints = mapper.writeValueAsString(listStopPoints);

            } catch (IOException ex) {
                Logger.getLogger(PositionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void overSpeedPositions() {

        List<Position> overSpeed = new ArrayList<Position>();
        List<List<Position>> listOverSpeed = new ArrayList<List<Position>>();
        Position startP = new Position();
        Position NextP = new Position();

        positionsFeedBack = getFacade().findRange(null, ConvertDateToCst(from), ConvertDateToCst(to), vehiclesInMonitoring.get(vehiclesInMonitoring.size() - 1).getDevice(), false, (double) SpeedLimit, false, (short) 0);
        if (positionsFeedBack.size() > 1) {
            startP = positionsFeedBack.get(0);

            for (i = 1; i < positionsFeedBack.size(); i++) {
                NextP = positionsFeedBack.get(i);

//     
                if (Math.abs(startP.getTimecreate().getTime() - NextP.getTimecreate().getTime()) < 21000) {

                    overSpeed.add(startP);

                } else {
                    overSpeed.add(startP);

                    if (overSpeed.size() > 1) {
                        listOverSpeed.add(overSpeed);
                    }
                    overSpeed = new ArrayList<Position>();

                }
                startP = NextP;
            }
            if (overSpeed.size() > 0) {
                overSpeed.add(startP);
                listOverSpeed.add(overSpeed);
            }

            try {

                ObjectMapper mapper = new ObjectMapper();
                jsonOverSpeedPositions = mapper.writeValueAsString(listOverSpeed);

            } catch (IOException ex) {
                Logger.getLogger(PositionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void calculateMaxDate() {

        try {
            current = getFacade().findRange(new int[]{50, 51}, ConvertDateToCst(from), null, vehiclesInMonitoring.get(vehiclesInMonitoring.size() - 1).getDevice(), false, null, false, (short) 0).get(0);
            maxDate = current.getTimecreate();

        } catch (Exception e) {

            maxDate = null;
            System.out.println("no limit");
        }

    }

    public void refreshPosition() {
        short acc = 3;
        array = "";
        try {
            SpeedLimit = vehiclesInMonitoring.get(0).getMaxSpeed();
            for (int i = 0; i < vehiclesInMonitoring.size(); i++) {
                device = (Device) vehiclesInMonitoring.get(i).getDevice();
                device = deviceFacade.refreshDevice(device);
                if (device.getLastpositionid() != null) {
                    SimpleDateFormat mdyFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String mdy = mdyFormat.format(device.getLastpositionid().getTimecreate());
                    if (device.getLastpositionid().getOther().contains("<acc>true</acc>")) {
                        acc = 1;
                    } else {
                        acc = 3;
                    }
                    array = array + device.getLastpositionid().getLatitude() + "?" + device.getLastpositionid().getLongitude() + "?"
                            + device.getVehicleid().getDescription() + "?" + device.getImei()
                            + "?" + device.getLastpositionid().getGprsstatus() + "?" + acc
                            + "?" + mdy + "?" + device.getLastpositionid().getSpeed()
                            + "?" + device.getLastpositionid().getVibrationstatus() + "?" + device.getLastpositionid().getSosbuttonstatus() + "?" + device.getVehicleid().getIcon() + "?";
                }
            }
        } catch (Exception e) {
            array = "";
        }

    }

    public void refreshPositionRealTime() {
        short acc = 3;
        String s = "";

        List<Position> positions = ejbFacade.findByNewIdPosition("Position.findByNewIdPosition", "deviceid", deviceRealTime, "positionid", lastRealTimePositionid);

        if (positions.size() > 0) {

            for (int p = 0; p < positions.size() - 1; p++) {
                s = s + positions.get(p).getLatitude() + "?" + positions.get(p).getLongitude() + "?";

            }

            SimpleDateFormat mdyFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String mdy = mdyFormat.format(device.getLastpositionid().getTimecreate());
            if (positions.get(positions.size() - 1).getOther().contains("<acc>true</acc>")) {
                acc = 1;
            } else {
                acc = 3;
            }
            s = s + positions.get(positions.size() - 1).getLatitude() + "?" + positions.get(positions.size() - 1).getLongitude() + "?"
                    + deviceRealTime.getVehicleid().getDescription() + "?" + deviceRealTime.getImei()
                    + "?" + positions.get(positions.size() - 1).getGprsstatus() + "?" + acc
                    + "?" + mdy + "?" + positions.get(positions.size() - 1).getSpeed()
                    + "?" + positions.get(positions.size() - 1).getVibrationstatus() + "?" + positions.get(positions.size() - 1).getSosbuttonstatus() + "?" + deviceRealTime.getVehicleid().getIcon() + "?";
            lastRealTimePositionid = positions.get(positions.size() - 1).getPositionid();

        }
        s = "35.837?10.613045?35.836?10.6130444?retoursav?358899051265885?null?3?04/04/2016 17:13:08?50.0?null?1?1";
        arrayRealTime = s;

    }

    public void selectedVehiclesContext() {

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("vehiclesInMonitoring", vehiclesInMonitoring);

    }

    public Map getSpringfieldMap() {

        return springfieldMap;
    }

    public void setSpringfieldMap(Map springfieldMap) {
        this.springfieldMap = springfieldMap;
    }

    public Position getSelected() {
        if (current == null) {
            current = new Position();
            selectedItemIndex = -1;
        }
        return current;
    }

    private PositionFacade getFacade() {
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
        System.out.println("here");
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Position) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Position();
        selectedItemIndex = -1;
        return "Create";
    }

    public String prepareGeoFenceView() {

        if (vehiclesInMonitoring.size() != 0) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("vehiclesInMonitoring", vehiclesInMonitoring.get(0));

            return "/geofence/List.xhtml?faces-redirect=true";
        } else {
            return null;
        }
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PositionCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Position) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PositionUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Position) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PositionDeleted"));
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

    public String prepareMonitoring() {
        return "/device/monitoring?faces-redirect=true";

    }

    @FacesConverter(forClass = Position.class)
    public static class PositionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PositionController controller = (PositionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "positionController");
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
            if (object instanceof Position) {
                Position o = (Position) object;
                return getStringKey(o.getPositionid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Position.class.getName());
            }
        }

    }

}
