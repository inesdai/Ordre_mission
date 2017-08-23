package com.veganet.gps.jsf;

import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Distributer;
import com.veganet.gps.entities.Icon;
import com.veganet.gps.entities.Notification;
import com.veganet.gps.entities.Position;
import com.veganet.gps.entities.User;
import com.veganet.gps.entities.Vehicle;
import com.veganet.gps.jsf.util.JsfUtil;
import com.veganet.gps.jsf.util.PaginationHelper;
import com.veganet.gps.session.VehicleFacade;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;
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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.primefaces.context.RequestContext;
import org.primefaces.model.TreeNode;

@ManagedBean(name = "vehicleController")
@SessionScoped
public class VehicleController implements Serializable {

    private Vehicle current;
    private DataModel items = null;
    @EJB
    private com.veganet.gps.session.VehicleFacade ejbFacade;

    @EJB
    private com.veganet.gps.session.PositionFacade positionFacade;
    @EJB
    private com.veganet.gps.session.DevicedistributorFacade devicedistributorFacade;
    @EJB
    private com.veganet.gps.session.NotificationFacade notificationFacade;
    @EJB
    private com.veganet.gps.session.DeviceFacade deviceFacade;

    private PaginationHelper pagination;
    private int selectedItemIndex;
    private short one = 1;
    private short zero = 0;
    private User connectedUser;
    private List<Vehicle> vehicles = new ArrayList<Vehicle>();
    private Date fromStatistics;
    private Date toStatistics;
    private int timezone;
    private List<Device> myDevices = new ArrayList<Device>();
    private List<Vehicle> MovingOverView = new ArrayList<Vehicle>();
    private Vehicle vehicleFilter = null;
    private short staticsType = 0;
    private List<Icon> icons;
    private Icon iconVehicule;

    public Icon getIconVehicule() {
        return iconVehicule;
    }

    public void setIconVehicule(Icon iconVehicule) {
        this.iconVehicule = iconVehicule;
    }

    public List<Icon> getIcons() {
        return icons;
    }

    public void setIcons(List<Icon> icons) {
        this.icons = icons;
    }

    public short getStaticsType() {
        return staticsType;
    }

    public void setStaticsType(short staticsType) {
        this.staticsType = staticsType;
    }

    public Vehicle getVehicleFilter() {
        return vehicleFilter;
    }

    public void setVehicleFilter(Vehicle vehicleFilter) {
        this.vehicleFilter = vehicleFilter;
    }

    public Date getFromStatistics() {
        return fromStatistics;
    }

    public void setFromStatistics(Date fromStatistics) {
        this.fromStatistics = fromStatistics;
    }

    public Date getToStatistics() {
        return toStatistics;
    }

    public void setToStatistics(Date toStatistics) {
        this.toStatistics = toStatistics;
    }

    public List<Vehicle> getVehicles() {
           vehicles = new ArrayList<Vehicle>();
        User selectedNodeUser = null;
        Distributer selectedNodeDistri = null;
        TreeNode selectedNode = (TreeNode) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedNode");
        try {
            if (selectedNode.getData().getClass() == User.class) {
                selectedNodeUser = (User) selectedNode.getData();
            }
            if (selectedNode.getData().getClass() == Distributer.class) {
                selectedNodeDistri = (Distributer) selectedNode.getData();
            }
         
            //if selected node is client
            if (selectedNodeUser != null) {
                List<Device> devices = new ArrayList<Device>();
                devices = devicedistributorFacade.findRange(null, (short) 1, selectedNodeUser, selectedNodeUser.getDistributerid(), (short) 0);
                for (Device device : devices) {
                    if (device.getIsdeleted() == (short) 0) {
                        vehicles.add(device.getVehicleid());
                    }
                }
            }
            //if selected node is distributer
            if (selectedNodeDistri != null) {
                //if selected distributer==connected distributer==admin vegatech
                if (connectedUser.getAccesslevel() == 0 && connectedUser.getDistributerid().getDistributerid() == selectedNodeDistri.getDistributerid()) {
                    List<Device> devices = new ArrayList<Device>();
                    devices = deviceFacade.findRange(null, (short) 0);
                    for (Device device : devices) {
                        if (device.getIsdeleted() == (short) 0) {
                            vehicles.add(device.getVehicleid());
                        }
                    }
                } //if selected distributer!=admin veganet
                else {
                    List<Device> devices = new ArrayList<Device>();
                    devices = devicedistributorFacade.findRange(null, one, null, selectedNodeDistri, (short) 0);
                    for (Device device : devices) {
                        if (device.getIsdeleted() == (short) 0) {
                            vehicles.add(device.getVehicleid());
                        }
                    }
                }
            }

            // no node selected
        } catch (Exception e) {
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
                devices = devicedistributorFacade.findRange(null, one, null, connectedUser.getDistributerid(), (short) 0);

                for (Device device : devices) {
                    if (device.getIsdeleted() == (short) 0) {
                        vehicles.add(device.getVehicleid());
                    }
                }
            } else {
                List<Device> devices = new ArrayList<Device>();
                devices = devicedistributorFacade.findRange(null, (short) 1, connectedUser, connectedUser.getDistributerid(), (short) 0);
                for (Device device : devices) {

                    if (device.getIsdeleted() == (short) 0) {
                        vehicles.add(device.getVehicleid());
                    }

                }
            }

        }

        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    @PostConstruct
    public void init() {
        connectedUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("connectedUser");
        timezone = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("timeZone"));
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

    }

    public void generateDownloadPDFMoving() {
        PdfMovingOverviewConverter pdfConverter = new PdfMovingOverviewConverter(connectedUser);
        pdfConverter.exportPDF(items);
    }

    public void generateDownloadPDFAlarmO() {
        PdfAlarmOverviewConverter pdfConverter = new PdfAlarmOverviewConverter(connectedUser);
        pdfConverter.exportPDF(items);
    }

    public void generateDownloadPDFAlarmD() {
        PdfAlarmDetailsConverter pdfConverter = new PdfAlarmDetailsConverter(connectedUser);
        pdfConverter.exportPDF(items);
    }

    public void generateDownloadPDFDeviceStatistics() {
        PdfDeviceStatisticsConverter pdfConverter = new PdfDeviceStatisticsConverter(connectedUser);
        pdfConverter.exportPDF(items, staticsType);
    }

    public int StopPoints(Date from, Date to, Device d) {
        int counter = 0;
        Date dStart = null;
        Date dStop = null;
        Position precPosition;
        Position nexPosition;
        int i;
        List<Position> positionsFeedBack = positionFacade.findRange(null, ConvertDateToCst(from), ConvertDateToCst(to), d, true, null, false, (short) 0);
        if (positionsFeedBack.size() > 0) {
            precPosition = positionsFeedBack.get(0);
            for (i = 1; i < positionsFeedBack.size(); i++) {
                nexPosition = positionsFeedBack.get(i);
//     0.0001==11 Meter/0.00012==11 Meter
                if (Math.abs(precPosition.getLatitude() - nexPosition.getLatitude()) < 0.0003 && Math.abs(precPosition.getLongitude() - nexPosition.getLongitude()) < 0.00035) {
                } else {
                    if (Math.abs(precPosition.getTimecreate().getTime() - positionsFeedBack.get(i - 1).getTimecreate().getTime()) > 60000) {
                        //add this point stop
                        counter++;
                    }
                    precPosition = nexPosition;
                }
            }
            if (Math.abs(precPosition.getTimecreate().getTime() - positionsFeedBack.get(i - 1).getTimecreate().getTime()) > 60000) {
                //add this point stop
                counter++;
            }
        }
        return counter;
    }

    public List<Vehicle> overSpeedPositions(Vehicle vehicle) {

        List<Position> overSpeed = new ArrayList<Position>();
        List<Vehicle> VehiculeOverSpeed = new ArrayList<Vehicle>();
        Position startP = new Position();
        Position NextP = new Position();
        Vehicle item = new Vehicle();
        double maxSpeed = 0.0;

        List<Position> positionsFeedBack = positionFacade.findRange(null, ConvertDateToCst(fromStatistics), ConvertDateToCst(toStatistics), vehicle.getDevice(), false, (double) vehicle.getMaxSpeed(), false, (short) 0);
        if (positionsFeedBack.size() > 1) {
            startP = positionsFeedBack.get(0);

            for (int i = 1; i < positionsFeedBack.size(); i++) {
                NextP = positionsFeedBack.get(i);

//     
                if (Math.abs(startP.getTimecreate().getTime() - NextP.getTimecreate().getTime()) < 21000) {

                    overSpeed.add(startP);
                    if (maxSpeed < startP.getSpeed()) {
                        maxSpeed = startP.getSpeed();
                    }
                } else {
                    overSpeed.add(startP);
                    if (maxSpeed < startP.getSpeed()) {
                        maxSpeed = startP.getSpeed();
                    }
                    if (overSpeed.size() > 1) {
                        item = new Vehicle();
                        item.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                        item.setStartTime(overSpeed.get(0).getTimecreate());
                        item.setEndTime(overSpeed.get(overSpeed.size() - 1).getTimecreate());
                        item.setLangLat((double) Math.round(overSpeed.get(0).getLatitude() * 100000) / 100000 + "," + (double) Math.round(overSpeed.get(0).getLongitude() * 100000) / 100000);
                        item.setSpeedLimit(maxSpeed);
                        int s = (int) (item.getEndTime().getTime() - item.getStartTime().getTime());

                        item.setContinueTime(ConvertSecondToHHMMString(s / 1000));
                        VehiculeOverSpeed.add(item);
                        maxSpeed = 0.0;

                    }
                    overSpeed = new ArrayList<Position>();

                }
                startP = NextP;
            }
            if (overSpeed.size() > 0) {
                overSpeed.add(startP);
                if (maxSpeed < startP.getSpeed()) {
                    maxSpeed = startP.getSpeed();
                }
                item = new Vehicle();
                item.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                item.setStartTime(overSpeed.get(0).getTimecreate());
                item.setEndTime(overSpeed.get(overSpeed.size() - 1).getTimecreate());
                item.setLangLat((double) Math.round(overSpeed.get(0).getLatitude() * 100000) / 100000 + "," + (double) Math.round(overSpeed.get(0).getLongitude() * 100000) / 100000);
                item.setSpeedLimit(maxSpeed);
                item.setContinueTime(ConvertSecondToHHMMString((int) (item.getEndTime().getTime() - item.getStartTime().getTime()) / 1000));
                VehiculeOverSpeed.add(item);
                maxSpeed = 0.0;

            }

            return VehiculeOverSpeed;

        } else {
            return VehiculeOverSpeed;
        }

    }

    public int overSpeedPositions(Date from, Date to, Device d) {

        List<Position> overSpeed = new ArrayList<Position>();
        List<List<Position>> listOverSpeed = new ArrayList<List<Position>>();
        Position startP = new Position();
        Position NextP = new Position();
        int counter = 0;

        List<Position> positionsFeedBack = positionFacade.findRange(null, ConvertDateToCst(from), ConvertDateToCst(to), d, false, (double) d.getVehicleid().getMaxSpeed(), false, (short) 0);
        if (positionsFeedBack.size() > 1) {
            startP = positionsFeedBack.get(0);

            for (int i = 1; i < positionsFeedBack.size(); i++) {
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
                counter++;
            }

        }
        return counter;
    }

    //statistics functions
    public void movingOverview() {
        MovingOverView = new ArrayList<Vehicle>();
        int accCounter = 0;
        Vehicle vehicle = new Vehicle();
        vehicle.setMileage(0);

        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance

        calendar.setTime(toStatistics);
        int toDay = calendar.get(Calendar.DAY_OF_MONTH);
        int toMin = calendar.get(Calendar.MINUTE);
        int tosec = calendar.get(Calendar.SECOND);
        calendar.setTime(fromStatistics);
        int totalDaysNumber = toDay - calendar.get(Calendar.DAY_OF_MONTH);
        if ((toMin - calendar.get(Calendar.MINUTE)) + (toMin - calendar.get(Calendar.MINUTE)) + (tosec - calendar.get(Calendar.SECOND)) > 0) {
            totalDaysNumber++;
        }

        if (totalDaysNumber > 0) {
            Date currentDate;
            Date nextDate;
            CharSequence sfalse = "false";
            CharSequence strue = "true";
            for (int dev = 0; dev < myDevices.size(); dev++) {

                calendar.setTime(fromStatistics);
                currentDate = calendar.getTime();
                for (int dayNumber = 0; dayNumber < totalDaysNumber; dayNumber++) {
                    vehicle = new Vehicle();
                    vehicle.setMark(myDevices.get(dev).getVehicleid().getDescription() + " " + myDevices.get(dev).getVehicleid().getMark() + "[" + myDevices.get(dev).getVehicleid().getModel() + "] ");
                    if (myDevices.get(dev).getVehicleid().getFuelconsumption() == null) {
                        vehicle.setFuelconsumption(0.0f);
                    } else {
                        vehicle.setFuelconsumption(myDevices.get(dev).getVehicleid().getFuelconsumption());
                    }
                    vehicle.setMileage(0);
                    if (totalDaysNumber - dayNumber > 1) {
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.add(Calendar.DATE, 1);
                    } else {
                        calendar.setTime(toStatistics);
                    }

                    nextDate = calendar.getTime();
                    //all positions for one device
                    List<Position> positionsFeedBack = positionFacade.findRange(null, ConvertDateToCst(currentDate), ConvertDateToCst(nextDate), myDevices.get(dev), false, null, true, (short) 0);

                    //all positions with precise date
                    for (int posid = 0; posid < positionsFeedBack.size() - 1; posid++) {
                        vehicle.setMileage(vehicle.getMileage() + distance(positionsFeedBack.get(posid), positionsFeedBack.get(posid + 1)));

                        if (positionsFeedBack.get(posid + 1).getOther().contains(strue)) {
                            if (positionsFeedBack.get(posid).getOther().contains(sfalse)) {
                                accCounter++;
                            }
                        }
                    }
                    if (positionsFeedBack.size() > 0) {
                        if (positionsFeedBack.get(0).getOther().contains(strue)) {
                            if (positionFacade.findBy("Position.findByBeforePosition", "deviceid", positionsFeedBack.get(0).getDeviceid(), "timecreate", positionsFeedBack.get(0).getTimecreate()).getOther().contains(sfalse)) {
                                accCounter++;
                            }
                        }
                    }
                    vehicle.setMileage(vehicle.getMileage());
                    vehicle.setOverSpeed(overSpeedPositions(currentDate, nextDate, myDevices.get(dev)));
                    vehicle.setStopPoints(StopPoints(currentDate, nextDate, myDevices.get(dev)));
                    vehicle.setWorkingTime(accCounter);
                    vehicle.setTimeStatistics(currentDate);
                    vehicle.setMileage(vehicle.getMileage());
                    vehicle.setFuelConsumptionStatistics((vehicle.getFuelconsumption() / 100) * (float) vehicle.getMileage());

                    MovingOverView.add(vehicle);
                    vehicle = new Vehicle();
                    vehicle.setMark(myDevices.get(dev).getVehicleid().getMark());
                    accCounter = 0;
                    vehicle.setMileage(0);
                    vehicle.setOverSpeed(0);
                    vehicle.setStopPoints(0);
                    vehicle.setWorkingTime(0);
                    currentDate = nextDate;

                }
            }
            items = new ListDataModel(MovingOverView);

        }
    }

    public String prepareMovingOverview() {
        recreateModel();
        myDevices = new ArrayList<>();

        myDevices.addAll(this.getallMydevices());

        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        fromStatistics = new Date();
        calendar.setTime(fromStatistics);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        fromStatistics = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        toStatistics = calendar.getTime();
        vehicleFilter = null;
        movingOverview();
        return "/statistics/MovingOverview?faces-redirect=true";
    }

    public void prepareMovingOverviewFilter() {
        recreateModel();

        myDevices = new ArrayList<Device>();
        if (vehicleFilter != null) {
            myDevices.add(vehicleFilter.getDevice());
        } else {
            myDevices.addAll(this.getallMydevices());

        }

        movingOverview();

    }

    public String prepareAlarmOverview() {
        recreateModel();

        myDevices = new ArrayList<>();
        myDevices.addAll(this.getallMydevices());

        System.out.println("myDevices" + myDevices.size());

        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        fromStatistics = new Date();
        calendar.setTime(fromStatistics);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        fromStatistics = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        toStatistics = calendar.getTime();
        vehicleFilter = null;
        alarmOverview();
        return "/statistics/AlarmOverview?faces-redirect=true";
    }

    public void alarmOverview() {
        items = null;

        items = new ListDataModel();
        MovingOverView = new ArrayList<Vehicle>();
        int lowBatteryCounter = 0, shockCounter = 0, sosCounter = 0;

        Vehicle vehicle = new Vehicle();

        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance

        calendar.setTime(toStatistics);
        int toDay = calendar.get(Calendar.DAY_OF_MONTH);
        int toMin = calendar.get(Calendar.MINUTE);
        int tosec = calendar.get(Calendar.SECOND);
        calendar.setTime(fromStatistics);
        int totalDaysNumber = toDay - calendar.get(Calendar.DAY_OF_MONTH);
        if ((toMin - calendar.get(Calendar.MINUTE)) + (toMin - calendar.get(Calendar.MINUTE)) + (tosec - calendar.get(Calendar.SECOND)) > 0) {
            totalDaysNumber++;
        }

        if (totalDaysNumber > 0) {
            Date currentDate;
            Date nextDate;
            CharSequence sLowBattery = "<vol>2</vol>";
            CharSequence sMediumBattery = "<vol>3</vol>";

            CharSequence sShock = "<sh>1</sh>";
            CharSequence sSos = "<sos>1</sos>";

            for (int dev = 0; dev < myDevices.size(); dev++) {

                calendar.setTime(fromStatistics);
                currentDate = calendar.getTime();
                for (int dayNumber = 0; dayNumber < totalDaysNumber; dayNumber++) {
                    vehicle = new Vehicle();
                    vehicle.setMark(myDevices.get(dev).getVehicleid().getDescription() + " " + myDevices.get(dev).getVehicleid().getMark() + "[" + myDevices.get(dev).getVehicleid().getModel() + "] ");
                    if (myDevices.get(dev).getVehicleid().getFuelconsumption() == null) {
                        vehicle.setFuelconsumption(0.0f);
                    } else {
                        vehicle.setFuelconsumption(myDevices.get(dev).getVehicleid().getFuelconsumption());
                    }
                    vehicle.setMileage(0);
                    if (totalDaysNumber - dayNumber > 1) {
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.add(Calendar.DATE, 1);
                    } else {
                        calendar.setTime(toStatistics);
                    }

                    nextDate = calendar.getTime();
                    //all positions for one device
                    List<Position> positionsFeedBack = positionFacade.findRange(null, ConvertDateToCst(currentDate), ConvertDateToCst(nextDate), myDevices.get(dev), false, null, true, (short) 0);

                    //all positions with precise date
                    for (int posid = 0; posid < positionsFeedBack.size() - 1; posid++) {
                        vehicle.setMileage(vehicle.getMileage() + distance(positionsFeedBack.get(posid), positionsFeedBack.get(posid + 1)));

                        if (positionsFeedBack.get(posid + 1).getOther().contains(sLowBattery)) {
                            if (positionsFeedBack.get(posid).getOther().contains(sMediumBattery)) {
                                lowBatteryCounter++;
                            }
                        }

                        if (positionsFeedBack.get(posid).getOther().contains(sShock)) {
                            shockCounter++;
                        }
                        if (positionsFeedBack.get(posid).getOther().contains(sSos)) {
                            sosCounter++;
                        }
                    }
                    if (positionsFeedBack.size() > 0) {
                        if (positionsFeedBack.get(0).getOther().contains(sLowBattery)) {
                            if (positionFacade.findBy("Position.findByBeforePosition", "deviceid", positionsFeedBack.get(0).getDeviceid(), "timecreate", positionsFeedBack.get(0).getTimecreate()).getOther().contains(sMediumBattery)) {
                                lowBatteryCounter++;
                            }
                        }
                    }
                    vehicle.setCut_Off(positionFacade.findRange(null, currentDate, nextDate, myDevices.get(dev), false, null, true, (short) 2).size());
                    vehicle.setLowBattery(lowBatteryCounter);

                    vehicle.setshocks(shockCounter);
                    vehicle.setSos(sosCounter);
                    vehicle.setTimeStatistics(currentDate);
                    MovingOverView.add(vehicle);
                    vehicle = new Vehicle();
                    vehicle.setMark(myDevices.get(dev).getVehicleid().getMark());
                    lowBatteryCounter = 0;
                    shockCounter = 0;
                    sosCounter = 0;

                    currentDate = nextDate;

                }
            }
            items = new ListDataModel(MovingOverView);

        }
    }

    public List<Device> getallMydevices() {
        List<Device> devices = new ArrayList<Device>();
        if (connectedUser.getAccesslevel() == 0) {

            devices = deviceFacade.findRange(null, (short) 0);

        } else if (connectedUser.getAccesslevel() == 1 || connectedUser.getAccesslevel() == 3) {

            devices = devicedistributorFacade.findRange(null, one, null, connectedUser.getDistributerid(), (short) 0);

        } else {

            devices = devicedistributorFacade.findRange(null, (short) 1, connectedUser, connectedUser.getDistributerid(), (short) 0);

        }
        return devices;
    }

    public void prepareAlarmOverviewFilter() {
        recreateModel();
        myDevices = new ArrayList<Device>();
        if (vehicleFilter != null) {
            myDevices.add(vehicleFilter.getDevice());
        } else {

            myDevices.addAll(this.getallMydevices());

        }

        alarmOverview();

    }

    public String prepareDeviceStatistics() {
        recreateModel();
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        fromStatistics = new Date();
        calendar.setTime(fromStatistics);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        fromStatistics = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        toStatistics = calendar.getTime();
        myDevices = new ArrayList<Device>();
        if (vehicleFilter != null) {
            myDevices.add(vehicleFilter.getDevice());
        } else {
            myDevices.addAll(this.getallMydevices());

        }
        vehicleFilter = null;
        deviceStatistics();
        return "/statistics/DeviceStatistics?faces-redirect=true";
    }

    public void deviceStatistics() {
        MovingOverView = new ArrayList<Vehicle>();
        List<Vehicle> vehicleItems = new ArrayList<Vehicle>();
        if (staticsType == (short) 0) {
            //overspeed function
            for (int dev = 0; dev < myDevices.size(); dev++) {

                vehicleItems.addAll(overSpeedPositions(myDevices.get(dev).getVehicleid()));

            }
        } else if (staticsType == (short) 1) {
            //stop details

            for (int dev = 0; dev < myDevices.size(); dev++) {

                vehicleItems.addAll(stopPoints(myDevices.get(dev).getVehicleid()));

            }
        } else {
            //ignition statistics
            for (int dev = 0; dev < myDevices.size(); dev++) {
                vehicleItems.addAll(accDetails(myDevices.get(dev).getVehicleid()));
            }
        }

        items = new ListDataModel(vehicleItems);

    }

    public List<Vehicle> stopPoints(Vehicle vehicle) {
        List<Vehicle> listStopPoints = new ArrayList<Vehicle>();

        Date dStart = null;
        Date dStop = null;
        Position precPosition;
        Position nexPosition;
        int i;

        List<Position> positionsFeedBack = positionFacade.findRange(null, ConvertDateToCst(fromStatistics), ConvertDateToCst(toStatistics), vehicle.getDevice(), true, null, false, (short) 0);
        if (positionsFeedBack.size() > 0) {
            precPosition = positionsFeedBack.get(0);

            for (i = 1; i < positionsFeedBack.size(); i++) {
                nexPosition = positionsFeedBack.get(i);

//     0.0001==11 Meter/0.00012==11 Meter
                if (Math.abs(precPosition.getLatitude() - nexPosition.getLatitude()) < 0.0003 && Math.abs(precPosition.getLongitude() - nexPosition.getLongitude()) < 0.00035) {

                } else {

                    if (Math.abs(precPosition.getTimecreate().getTime() - positionsFeedBack.get(i - 1).getTimecreate().getTime()) > 60000) {
                        //add this point stop
                        Vehicle item = new Vehicle();

                        item.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                        item.setStartTime(precPosition.getTimecreate());
                        item.setEndTime(positionsFeedBack.get(i - 1).getTimecreate());
                        item.setLangLat((double) Math.round(precPosition.getLatitude() * 1000000) / 1000000 + "," + (double) Math.round(precPosition.getLongitude() * 1000000) / 1000000);
                        item.setContinueTime(ConvertSecondToHHMMString((int) (item.getEndTime().getTime() - item.getStartTime().getTime()) / 1000));
                        listStopPoints.add(item);
                    }

                    precPosition = nexPosition;

                }
            }
            if (Math.abs(precPosition.getTimecreate().getTime() - positionsFeedBack.get(i - 1).getTimecreate().getTime()) > 60000) {
                //add this point stop
                Vehicle item = new Vehicle();

                item.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                item.setStartTime(precPosition.getTimecreate());
                item.setEndTime(positionsFeedBack.get(i - 1).getTimecreate());
                item.setLangLat((double) Math.round(precPosition.getLatitude() * 1000000) / 1000000 + "," + (double) Math.round(precPosition.getLongitude() * 1000000) / 1000000);
                item.setContinueTime(ConvertSecondToHHMMString((int) (item.getEndTime().getTime() - item.getStartTime().getTime()) / 1000));

                listStopPoints.add(item);
            }

        }
        return listStopPoints;
    }

    public void prepareDeviceStatisticsFilterVehicule() {
        recreateModel();

        myDevices = new ArrayList<Device>();
        if (vehicleFilter != null) {
            myDevices.add(vehicleFilter.getDevice());
        } else {
            myDevices.addAll(this.getallMydevices());

        }

        deviceStatistics();
    }

    public List<Vehicle> accDetails(Vehicle vehicle) {

        List<Vehicle> listAccDetails = new ArrayList<Vehicle>();
        CharSequence accTrue = "<acc>true</acc>";
        CharSequence accFalse = "<acc>false</acc>";

        int i;

        List<Position> positionsFeedBack = positionFacade.findRange(null, ConvertDateToCst(fromStatistics), ConvertDateToCst(toStatistics), vehicle.getDevice(), true, null, false, (short) 0);
        if (positionsFeedBack.size() > 0) {
            Vehicle item = new Vehicle();

            for (i = 0; i < positionsFeedBack.size(); i++) {

                if (positionsFeedBack.get(i).getOther().contains(accTrue)) {

                    if (item.getStartTime() == null) {
                        item.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                        item.setStartTime(positionsFeedBack.get(i).getTimecreate());
                        item.setLangLat((double) Math.round(positionsFeedBack.get(i).getLatitude() * 1000000) / 1000000 + "," + (double) Math.round(positionsFeedBack.get(i).getLongitude() * 1000000) / 1000000);
                    }
                } else if (positionsFeedBack.get(i).getOther().contains(accFalse) && item.getStartTime() != null) {

                    item.setEndTime(positionsFeedBack.get(i).getTimecreate());
                    item.setContinueTime(ConvertSecondToHHMMString((int) (item.getEndTime().getTime() - item.getStartTime().getTime()) / 1000));
                    listAccDetails.add(item);
                    item = new Vehicle();
                }

            }

        }
        return listAccDetails;
    }

    public String prepareAlarmDetails() {
        recreateModel();
        myDevices = new ArrayList<>();
        myDevices.addAll(this.getallMydevices());

        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        fromStatistics = new Date();
        calendar.setTime(fromStatistics);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        fromStatistics = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        toStatistics = calendar.getTime();
        vehicleFilter = null;
        alarmDetails();
        return "/statistics/AlarmDetails?faces-redirect=true";
    }

    public void prepareAlarmDetailsFilter() {
        recreateModel();

        myDevices = new ArrayList<Device>();
        if (vehicleFilter != null) {
            myDevices.add(vehicleFilter.getDevice());
        } else {
            myDevices.addAll(this.getallMydevices());

        }

        alarmDetails();
    }

    public void alarmDetails() {

        List<Vehicle> vehicleItems = new ArrayList<Vehicle>();
        if (staticsType == (short) 0) {
            //all Alarms

            for (int dev = 0; dev < myDevices.size(); dev++) {

                vehicleItems.addAll(geofenceInAlarms(myDevices.get(dev).getVehicleid()));
                vehicleItems.addAll(geofenceOutAlarms(myDevices.get(dev).getVehicleid()));
                vehicleItems.addAll(batteryShockSosAlarms(myDevices.get(dev).getVehicleid()));

            }
        } else if (staticsType == (short) 1) {
            //geo-fence in

            for (int dev = 0; dev < myDevices.size(); dev++) {

                vehicleItems.addAll(geofenceInAlarms(myDevices.get(dev).getVehicleid()));

            }
        } else if (staticsType == (short) 2) {
            //geo-fence out

            for (int dev = 0; dev < myDevices.size(); dev++) {

                vehicleItems.addAll(geofenceOutAlarms(myDevices.get(dev).getVehicleid()));

            }
        } else if (staticsType == (short) 3) {
            //low battery

            for (int dev = 0; dev < myDevices.size(); dev++) {

                vehicleItems.addAll(batteryAlarms(myDevices.get(dev).getVehicleid()));

            }
        } else if (staticsType == (short) 4) {
            //sos alarm

            for (int dev = 0; dev < myDevices.size(); dev++) {

                vehicleItems.addAll(sosAlarms(myDevices.get(dev).getVehicleid()));

            }
        } else if (staticsType == (short) 5) {
            //cut-off petrol

            for (int dev = 0; dev < myDevices.size(); dev++) {

                vehicleItems.addAll(cutOffAlarms(myDevices.get(dev).getVehicleid()));

            }
        } else if (staticsType == (short) 6) {
            //restor petrol

            for (int dev = 0; dev < myDevices.size(); dev++) {

                vehicleItems.addAll(restorPetrolAlarms(myDevices.get(dev).getVehicleid()));

            }
        } else if (staticsType == (short) 7) {
            //vibration

            for (int dev = 0; dev < myDevices.size(); dev++) {

                vehicleItems.addAll(shockAlarms(myDevices.get(dev).getVehicleid()));

            }
        } else {
            //displacement

            for (int dev = 0; dev < myDevices.size(); dev++) {

                vehicleItems.addAll(stopPoints(myDevices.get(dev).getVehicleid()));

            }
        }

        items = new ListDataModel(vehicleItems);
    }

    public List<Vehicle> batteryShockSosAlarms(Vehicle vehicle) {
        MovingOverView = new ArrayList<Vehicle>();
        int lowBatteryCounter = 0, shockCounter = 0, sosCounter = 0;

        CharSequence sLowBattery = "<vol>2</vol>";
        CharSequence sMediumBattery = "<vol>3</vol>";

        CharSequence sShock = "<sh>1</sh>";
        CharSequence sSos = "<sos>1</sos>";

        //all positions for one device
        List<Position> positionsFeedBack = positionFacade.findRange(null, ConvertDateToCst(fromStatistics), ConvertDateToCst(toStatistics), vehicle.getDevice(), false, null, true, (short) 0);

        //all positions with precise date
        if (positionsFeedBack.size() > 0) {
            for (int posid = 0; posid < positionsFeedBack.size() - 1; posid++) {

                if (positionsFeedBack.get(posid + 1).getOther().contains(sLowBattery)) {
                    if (positionsFeedBack.get(posid).getOther().contains(sMediumBattery)) {
                        Vehicle vehicleItem = new Vehicle();
                        vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                        vehicleItem.setAlarmType((short) 3);
                        vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("LowBatteryAlarm"));
                        vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(posid + 1).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(posid + 1).getLongitude() * 100000) / 100000);
                        vehicleItem.setTimeStatistics(positionsFeedBack.get(posid + 1).getTimecreate());
                        MovingOverView.add(vehicleItem);
                    }
                }

                if (positionsFeedBack.get(posid).getOther().contains(sShock)) {
                    Vehicle vehicleItem = new Vehicle();
                    vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                    vehicleItem.setAlarmType((short) 7);
                    vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("VibrationAlarm"));
                    vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(posid).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(posid).getLongitude() * 100000) / 100000);
                    vehicleItem.setTimeStatistics(positionsFeedBack.get(posid).getTimecreate());
                    MovingOverView.add(vehicleItem);
                }
                if (positionsFeedBack.get(posid).getOther().contains(sSos)) {
                    Vehicle vehicleItem = new Vehicle();
                    vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                    vehicleItem.setAlarmType((short) 4);
                    vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("SOSAlarm"));
                    vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(posid).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(posid).getLongitude() * 100000) / 100000);
                    vehicleItem.setTimeStatistics(positionsFeedBack.get(posid).getTimecreate());
                    MovingOverView.add(vehicleItem);
                }
            }
            if (positionsFeedBack.get(positionsFeedBack.size() - 1).getOther().contains(sShock)) {
                Vehicle vehicleItem = new Vehicle();
                vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                vehicleItem.setAlarmType((short) 7);
                vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("VibrationAlarm"));
                vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(positionsFeedBack.size() - 1).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(positionsFeedBack.size() - 1).getLongitude() * 100000) / 100000);
                vehicleItem.setTimeStatistics(positionsFeedBack.get(positionsFeedBack.size() - 1).getTimecreate());
                MovingOverView.add(vehicleItem);
            }
            if (positionsFeedBack.get(positionsFeedBack.size() - 1).getOther().contains(sSos)) {
                Vehicle vehicleItem = new Vehicle();
                vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                vehicleItem.setAlarmType((short) 4);
                vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("SOSAlarm"));
                vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(positionsFeedBack.size() - 1).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(positionsFeedBack.size() - 1).getLongitude() * 100000) / 100000);
                vehicleItem.setTimeStatistics(positionsFeedBack.get(positionsFeedBack.size() - 1).getTimecreate());
                MovingOverView.add(vehicleItem);
            }
            if (positionsFeedBack.size() > 0) {
                if (positionsFeedBack.get(0).getOther().contains(sLowBattery)) {
                    if (positionFacade.findBy("Position.findByBeforePosition", "deviceid", positionsFeedBack.get(0).getDeviceid(), "timecreate", positionsFeedBack.get(0).getTimecreate()).getOther().contains(sMediumBattery)) {
                        Vehicle vehicleItem = new Vehicle();
                        vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                        vehicleItem.setAlarmType((short) 3);
                        vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("LowBatteryAlarm"));
                        vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(0).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(0).getLongitude() * 100000) / 100000);
                        vehicleItem.setTimeStatistics(positionsFeedBack.get(0).getTimecreate());
                        MovingOverView.add(0, vehicleItem);
                    }
                }
            }
        }

        return MovingOverView;
    }

    public List<Vehicle> geofenceInAlarms(Vehicle vehicle) {

        List<Vehicle> listAccDetails = new ArrayList<Vehicle>();

        List<Notification> notifications = notificationFacade.findRangeByDeviceDate(null, null, 2, vehicle.getDevice(), fromStatistics, toStatistics);
        if (notifications.size() > 0) {

            for (int i = 0; i < notifications.size(); i++) {
                Vehicle item = new Vehicle();
                item.setAlarmType((short) 1);
                item.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("GeoFenceIn"));
                item.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                item.setTimeStatistics(notifications.get(i).getTimenotification());
                item.setLangLat((double) Math.round(notifications.get(i).getPositionid().getLatitude() * 1000000) / 1000000 + "," + (double) Math.round(notifications.get(i).getPositionid().getLongitude() * 1000000) / 1000000);
                listAccDetails.add(item);
            }

        }
        return listAccDetails;
    }

    public List<Vehicle> geofenceOutAlarms(Vehicle vehicle) {

        List<Vehicle> listAccDetails = new ArrayList<Vehicle>();

        List<Notification> notifications = notificationFacade.findRangeByDeviceDate(null, null, 1, vehicle.getDevice(), fromStatistics, toStatistics);
        if (notifications.size() > 0) {

            for (int i = 0; i < notifications.size(); i++) {
                Vehicle item = new Vehicle();
                item.setAlarmType((short) 2);
                item.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("GeoFenceOut"));
                item.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                item.setTimeStatistics(notifications.get(i).getTimenotification());
                item.setLangLat((double) Math.round(notifications.get(i).getPositionid().getLatitude() * 1000000) / 1000000 + "," + (double) Math.round(notifications.get(i).getPositionid().getLongitude() * 1000000) / 1000000);
                listAccDetails.add(item);
            }

        }
        return listAccDetails;
    }

    public List<Vehicle> batteryAlarms(Vehicle vehicle) {
        MovingOverView = new ArrayList<Vehicle>();

        CharSequence sLowBattery = "<vol>2</vol>";
        CharSequence sMediumBattery = "<vol>3</vol>";

        //all positions for one device
        List<Position> positionsFeedBack = positionFacade.findRange(null, ConvertDateToCst(fromStatistics), ConvertDateToCst(toStatistics), vehicle.getDevice(), false, null, true, (short) 0);

        //all positions with precise date
        if (positionsFeedBack.size() > 0) {
            for (int posid = 0; posid < positionsFeedBack.size() - 1; posid++) {

                if (positionsFeedBack.get(posid + 1).getOther().contains(sLowBattery)) {
                    if (positionsFeedBack.get(posid).getOther().contains(sMediumBattery)) {
                        Vehicle vehicleItem = new Vehicle();
                        vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                        vehicleItem.setAlarmType((short) 3);
                        vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("LowBatteryAlarm"));
                        vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(posid + 1).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(posid + 1).getLongitude() * 100000) / 100000);
                        vehicleItem.setTimeStatistics(positionsFeedBack.get(posid + 1).getTimecreate());
                        MovingOverView.add(vehicleItem);
                    }
                }

            }
            if (positionsFeedBack.size() > 0) {
                if (positionsFeedBack.get(0).getOther().contains(sLowBattery)) {
                    if (positionFacade.findBy("Position.findByBeforePosition", "deviceid", positionsFeedBack.get(0).getDeviceid(), "timecreate", positionsFeedBack.get(0).getTimecreate()).getOther().contains(sMediumBattery)) {
                        Vehicle vehicleItem = new Vehicle();
                        vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                        vehicleItem.setAlarmType((short) 3);
                        vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("LowBatteryAlarm"));
                        vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(0).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(0).getLongitude() * 100000) / 100000);
                        vehicleItem.setTimeStatistics(positionsFeedBack.get(0).getTimecreate());
                        MovingOverView.add(0, vehicleItem);
                    }
                }
            }

        }

        return MovingOverView;
    }

    public List<Vehicle> shockAlarms(Vehicle vehicle) {
        MovingOverView = new ArrayList<Vehicle>();
        int lowBatteryCounter = 0, shockCounter = 0, sosCounter = 0;

        CharSequence sShock = "<sh>1</sh>";

        //all positions for one device
        List<Position> positionsFeedBack = positionFacade.findRange(null, ConvertDateToCst(fromStatistics), ConvertDateToCst(toStatistics), vehicle.getDevice(), false, null, true, (short) 0);

        //all positions with precise date
        if (positionsFeedBack.size() > 0) {
            for (int posid = 0; posid < positionsFeedBack.size() - 1; posid++) {

                if (positionsFeedBack.get(posid).getOther().contains(sShock)) {
                    Vehicle vehicleItem = new Vehicle();
                    vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                    vehicleItem.setAlarmType((short) 7);
                    vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("VibrationAlarm"));
                    vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(posid).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(posid).getLongitude() * 100000) / 100000);
                    vehicleItem.setTimeStatistics(positionsFeedBack.get(posid).getTimecreate());
                    MovingOverView.add(vehicleItem);
                }
            }
            if (positionsFeedBack.get(positionsFeedBack.size() - 1).getOther().contains(sShock)) {
                Vehicle vehicleItem = new Vehicle();
                vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                vehicleItem.setAlarmType((short) 7);
                vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("VibrationAlarm"));
                vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(positionsFeedBack.size() - 1).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(positionsFeedBack.size() - 1).getLongitude() * 100000) / 100000);
                vehicleItem.setTimeStatistics(positionsFeedBack.get(positionsFeedBack.size() - 1).getTimecreate());
                MovingOverView.add(vehicleItem);
            }
        }

        return MovingOverView;
    }

    public List<Vehicle> sosAlarms(Vehicle vehicle) {
        MovingOverView = new ArrayList<Vehicle>();
        CharSequence sSos = "<sos>1</sos>";

        //all positions for one device
        List<Position> positionsFeedBack = positionFacade.findRange(null, ConvertDateToCst(fromStatistics), ConvertDateToCst(toStatistics), vehicle.getDevice(), false, null, true, (short) 0);

        //all positions with precise date
        if (positionsFeedBack.size() > 0) {
            for (int posid = 0; posid < positionsFeedBack.size() - 1; posid++) {

                if (positionsFeedBack.get(posid).getOther().contains(sSos)) {
                    Vehicle vehicleItem = new Vehicle();
                    vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                    vehicleItem.setAlarmType((short) 4);
                    vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("SOSAlarm"));
                    vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(posid).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(posid).getLongitude() * 100000) / 100000);
                    vehicleItem.setTimeStatistics(positionsFeedBack.get(posid).getTimecreate());
                    MovingOverView.add(vehicleItem);
                }
            }

            if (positionsFeedBack.get(positionsFeedBack.size() - 1).getOther().contains(sSos)) {
                Vehicle vehicleItem = new Vehicle();
                vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                vehicleItem.setAlarmType((short) 4);
                vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("SOSAlarm"));
                vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(positionsFeedBack.size() - 1).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(positionsFeedBack.size() - 1).getLongitude() * 100000) / 100000);
                vehicleItem.setTimeStatistics(positionsFeedBack.get(positionsFeedBack.size() - 1).getTimecreate());
                MovingOverView.add(vehicleItem);
            }

        }

        return MovingOverView;
    }

    public List<Vehicle> cutOffAlarms(Vehicle vehicle) {
        MovingOverView = new ArrayList<Vehicle>();

        //all positions for one device
        List<Position> positionsFeedBack = positionFacade.findRange(null, ConvertDateToCst(fromStatistics), ConvertDateToCst(toStatistics), vehicle.getDevice(), false, null, true, (short) 2);

        //all positions with precise date
        if (positionsFeedBack.size() > 0) {
            for (int posid = 0; posid < positionsFeedBack.size(); posid++) {
                Position lastp = positionFacade.findBy("Position.findByBeforePetrolPosition", "deviceid", vehicle.getDevice(), "timecreate", ConvertDateToCst(positionsFeedBack.get(posid).getTimeCreateWitoutconvertion()));
                Vehicle vehicleItem = new Vehicle();
                vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                vehicleItem.setAlarmType((short) 5);
                vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("CutoffAlarm"));
                vehicleItem.setLangLat((double) Math.round(lastp.getLatitude() * 100000) / 100000 + "," + (double) Math.round(lastp.getLongitude() * 100000) / 100000);
                vehicleItem.setTimeStatistics(positionsFeedBack.get(posid).getTimecreate());
                MovingOverView.add(vehicleItem);

            }

        }

        return MovingOverView;
    }

    public List<Vehicle> restorPetrolAlarms(Vehicle vehicle) {
        MovingOverView = new ArrayList<Vehicle>();

        //all positions for one device
        List<Position> positionsFeedBack = positionFacade.findRange(null, ConvertDateToCst(fromStatistics), ConvertDateToCst(toStatistics), vehicle.getDevice(), false, null, true, (short) 1);

        //all positions with precise date
        if (positionsFeedBack.size() > 0) {
            for (int posid = 0; posid < positionsFeedBack.size(); posid++) {
                Position lastp = positionFacade.findBy("Position.findByBeforePetrolPosition", "deviceid", vehicle.getDevice(), "timecreate", ConvertDateToCst(positionsFeedBack.get(posid).getTimeCreateWitoutconvertion()));
                Vehicle vehicleItem = new Vehicle();
                vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                vehicleItem.setAlarmType((short) 6);
                vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("RestorPetrolAlarm"));
                vehicleItem.setLangLat((double) Math.round(lastp.getLatitude() * 100000) / 100000 + "," + (double) Math.round(lastp.getLongitude() * 100000) / 100000);
                vehicleItem.setTimeStatistics(positionsFeedBack.get(posid).getTimecreate());
                MovingOverView.add(vehicleItem);

            }

        }

        return MovingOverView;
    }

    //conevrt seconds to time
    private String ConvertSecondToHHMMString(int secondtTime) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(tz);
        String time = df.format(new Date(secondtTime * 1000L));

        return time;

    }

    //calcul distance between two positions
    //example System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, 'K') + " Kilometers\n");
    private double distance(Position p1, Position p2) {

        double lat1 = p1.getLatitude();
        double lon1 = p1.getLongitude();
        double lat2 = p2.getLatitude();
        double lon2 = p2.getLongitude();
        double theta = lon1 - lon2;
        if (theta != 0 && lat1 - lat2 != 0) {
            double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
//        if (unit == 'K') {
            dist = dist * 1.609344;
//        } else if (unit == 'N') {
//            dist = dist * 0.8684;
//        }
            return (dist);
        } else {
            return 0;
        }

    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    //convert time from current Time To Gps time
    public Date ConvertDateToCst(Date date) {
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -timezone + 9); // adds one hour
        return calendar.getTime();
    }

    //convert time from current Time To Gps time
    public Date ConvertDateCurrentTo(Date date) {
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -timezone + 9); // adds one hour
        return calendar.getTime();
    }

    public VehicleController() {

    }

    public Vehicle getSelected() {
        if (current == null) {
            current = new Vehicle();
            selectedItemIndex = -1;
        }
        return current;
    }

    private VehicleFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count(one, connectedUser);
                }

                @Override
                public DataModel createPageDataModel() {
                    //  return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, one, connectedUser));

                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "/vehicle/List?faces-redirect=true";

    }

    public String prepareView() {
        current = (Vehicle) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Vehicle();

        selectedItemIndex = -1;
        return "/vehicle/Create?faces-redirect=true";
    }

    public void prepareCreateDefaultVehicle() {
        current = new Vehicle();
        selectedItemIndex = -1;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ejbFacadeVehicle", ejbFacade);

    }

    public void googlemaproadtomycar() {
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("googlemaproadtomycar();");

    }

    public String create() {

        try {
            current.setCreatedate(new Date());
            current.setIsactive(one);
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("VehicleCreated"));
            prepareCreate();
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }

    }

    public String prepareEdit() {

        current = (Vehicle) getItems().getRowData();
        if (current.getIcon() != null) {

            Predicate condition = new Predicate() {
                public boolean evaluate(Object icon) {
                    return ((Icon) icon).getId() == (int) (current.getIcon());
                }

            };
            iconVehicule = (Icon) CollectionUtils.find(icons, condition);

        } else {
            iconVehicule = icons.get(0);
        }

        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            //              current.setCreatedate(new Date());
            System.out.println("zjdkked");
            current.setIcon((short) iconVehicule.getId());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("VehicleUpdated"));
            return prepareList();
        } catch (Exception e) {

            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Vehicle) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("VehicleDeleted"));
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
        return "/vehicule/List?faces-redirect=true";
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

    public SelectItem[] getItemsAvailableSelectOneForDriver() {

        return JsfUtil.getSelectItems(getFacade().findRange(null, one, connectedUser), true);
    }

    @FacesConverter(forClass = Vehicle.class)
    public static class VehicleControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VehicleController controller = (VehicleController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "vehicleController");
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
            if (object instanceof Vehicle) {
                Vehicle o = (Vehicle) object;
                return getStringKey(o.getVehicleid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Vehicle.class.getName());
            }
        }

    }

}
