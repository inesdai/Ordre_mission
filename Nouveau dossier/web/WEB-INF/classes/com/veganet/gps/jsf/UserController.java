package com.veganet.gps.jsf;

import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Devicedistributor;
import com.veganet.gps.entities.Distributer;
import com.veganet.gps.entities.Logs;
import com.veganet.gps.entities.User;
import com.veganet.gps.jsf.util.JsfUtil;
import com.veganet.gps.jsf.util.PaginationHelper;
import com.veganet.gps.session.UserFacade;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "userController")
@SessionScoped
public class UserController implements Serializable {

    private User current;
    private DataModel items = null;
    @EJB
    private com.veganet.gps.session.UserFacade ejbFacade;
    @EJB
    private com.veganet.gps.session.LogsFacade logsFacade;
    @EJB
    private com.veganet.gps.session.ApplicationsettingFacade applicationsettingFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private String username;
    private String password;
    private static int nbrlogin = 0;
    private User connectedUser;
    private boolean filter = false;

    public boolean isFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    public String getKeepSigned() {
        return keepSigned;
    }

    public void setKeepSigned(String keepSigned) {
        this.keepSigned = keepSigned;
    }
    private List<User> users;
    private int myUsers;
    private String keepSigned;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    //@Resource(name = "mailSession")
    private Session mailSession;

    public User getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(User connectedUser) {
        this.connectedUser = connectedUser;
    }

    public UserController() {

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

    public void accesslevelCodeChanged(ValueChangeEvent e) {
        current.setAccesslevel((int) e.getNewValue());
    }

    public User getSelected() {

        if (current == null) {
            current = new User();
            selectedItemIndex = -1;
        }
        return current;
    }

    private UserFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {

                    if (connectedUser.getAccesslevel() != 0) {
                        return getFacade().count((short) 0, connectedUser);
                    } else {
                        return getFacade().count((short) 0, null);
                    }
                }

                @Override
                public DataModel createPageDataModel() {
                    if (connectedUser.getAccesslevel() != 0) {
                        return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize() - 1}, (short) 0, connectedUser, null, null, null, null, 4));
                    } else {

                        return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize() - 1}, (short) 0, null, null, null, null, null, 4));

                    }
                }
            };
        }
        return pagination;
    }

    public void filterUsers() {

        filter = true;
        if (connectedUser.getAccesslevel() != 0) {

            items = new ListDataModel(getFacade().findRange(null, (short) 0, connectedUser, current.getFirstname(), current.getFirstname(), current.getUsername(), current.getEmail(), current.getAccesslevel()));

        } else {

            items = new ListDataModel(getFacade().findRange(null, (short) 0, null, current.getFirstname(), current.getFirstname(), current.getUsername(), current.getEmail(), current.getAccesslevel()));

        }

    }

    public String prepareList() {
        filter = false;
        current = new User();
        recreateModel();
        return "/user/List?faces-redirect=true";
    }

    public String prepareView() {
        current = (User) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/user/View?faces-redirect=true";
    }

    public String prepareCreate() {
        username = "";
        password = "";
        current = new User();
        selectedItemIndex = -1;
        return "/user/createadmin?faces-redirect=true";
    }

    public String shaGeneration(String password) {

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
            current.setPassword(sb.toString());
            return current.getPassword();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }

    }

    public String create() {
        try {
            //this.shaGeneration(current.getPassword());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public boolean duplicatedUser() {

        //User u = new User();
        List<User> listUser = new ArrayList<User>();
        listUser = getFacade().findBy("User.findByUsername", "username", current.getUsername());
        System.out.println(listUser.size());

        if (listUser.isEmpty() || (listUser.size() == 1 && listUser.get(0).getUserid() == current.getUserid())) {
            return false;

        } else {
            return true;
        }

    }

    public boolean duplicatedEmail() {
        if (current.getEmail() != null && !current.getEmail().equals("")) {
            //User u = new User();
            List<User> listUser = new ArrayList<User>();
            listUser = getFacade().findBy("User.findByEmail", "email", current.getEmail());

            if (listUser.isEmpty() || (listUser.size() == 1 && listUser.get(0).getUserid() == current.getUserid())) {
                return false;

            } else {
                return true;
            }
        } else {
            return false;
        }

    }

    public String createAdmin() {
        //System.out.print(current.getConfirmPassword()+"aaaaaaaaaaaaaaaaaaa"+current.getUserid()+current.getAccesslevel()+current.getPassword()+current.getAdmincreateaccess());                   

        if (current.getPassword().equals(current.getConfirmPassword())) {
            try {
                if (this.duplicatedUser()) {

                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("usernamealreadyexists"));
                    return null;
                } else if (this.duplicatedEmail()) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("Emailalreadyexists"));
                    return null;
                } else {
                    if (current.getAdmincreateaccess() == null) {
                        current.setAdmincreateaccess((short) 2);
                    }
                    if (current.getAccesslevel() == null) {
                        current.setAccesslevel(2);
                    }
                    current.setUsedmap("GoogleMap");
//                    this.shaGeneration(current.getPassword());
                    current.setDistributerid(connectedUser.getDistributerid());
                    current.setIsactive((short) 1);
                    current.setIsdeleted((short) 0);
                    current.setResetpassword((short) 0);
                    current.setCreatedby(connectedUser);
                    current.setCreatedate(new Date());

                    getFacade().create(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserSuccessfullyCreated"));
                    this.prepareCreate();
                    return prepareList();

                }
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                return null;
            }
        } else {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("incorrestconfirmpassword"));
            return null;
        }
    }

    public String ForgetPassword() {
        //  User u = new User();
        List<User> listUser;
        listUser = getFacade().findBy("User.findByEmail", "email", current.getEmail());

        if (listUser.isEmpty() || listUser.get(0).getEmail() == null || listUser.get(0).getEmail().equals("")) {
            System.out.println("no mail found");
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("unfoundmail"));
            return null;

        } else {
            current = listUser.get(0);
            try {

                String randomPassword = Integer.toString((int) (Math.random() * Integer.MAX_VALUE), 36);
                current.setResetpassword((short) 1);
//                this.shaGeneration(randomPassword);
                current.setPassword(randomPassword);
                //System.out.println(RandomStringUtils.randomAlphanumeric(8));
                //u.setPassword();
                getFacade().edit(current);
                Message msg = new MimeMessage(mailSession);
                msg.setSubject(ResourceBundle.getBundle("/Bundle").getString("applicationname") + " " + ResourceBundle.getBundle("/Bundle").getString("PasswordReset"));
                msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(
                        current.getEmail(), ResourceBundle.getBundle("/Bundle").getString("applicationname")));
                msg.setFrom(new InternetAddress("GpsTrackerVegatech@gmail.com"));

                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(ResourceBundle.getBundle("/Bundle").getString("ForgetPasswordText1") + " " + current.getFirstname() + " " + current.getLastname() + " " + ResourceBundle.getBundle("/Bundle").getString("ForgetPasswordText2") + "User Name:" + current.getUsername() + " /" + "Password :" + randomPassword);

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);

                msg.setContent(multipart);

                Transport.send(msg);
                return "login";
            } catch (MessagingException ex) {
                //Logger.getLogger(StartupListener.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            } catch (UnsupportedEncodingException ex) {
                // Logger.getLogger(StartupListener.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String login() {

        String outcome = "login";
        if (this.nbrlogin < 3) {
            // LoginBean.nbrlogin ++;

            try {

//                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//                request.login(this.username, this.password);
                connectedUser = getFacade().findByUserPass("User.findByUserPass", "password", this.password, "username", this.username).get(0);
                String timeZone = applicationsettingFacade.findBy("Applicationsetting.findBySettingname", "settingName", "timeZone").getSettingValue();
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("timeZone", timeZone);

                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("connectedUser", connectedUser);
                //login saved in  logs table
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                String ipAddress = request.getHeader("X-FORWARDED-FOR");
                if (ipAddress == null) {
                    ipAddress = request.getRemoteAddr();
                }
                if (ipAddress == null) {
                    ipAddress = "non dispo";
                }
                Logs log = new Logs(new Date(), "Login", connectedUser, ipAddress);
                logsFacade.create(log);
                if (connectedUser.getResetpassword() == (short) 1) {
                    outcome = this.editAcoount();
                } else {

                    outcome = "/user/List?faces-redirect=true";
                    if (connectedUser.getAccesslevel() == 2) {
                        outcome = "/device/Monitoring?faces-redirect=true";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e.toString().equalsIgnoreCase("javax.servlet.ServletException: This is request has already been authenticated")) {
                    //login saved in  logs table
                    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                    String ipAddress = request.getHeader("X-FORWARDED-FOR");
                    if (ipAddress == null) {
                        ipAddress = request.getRemoteAddr();
                    }
                    if (ipAddress == null) {
                        ipAddress = "non dispo";
                    }
                    Logs log = new Logs(new Date(), "Login", connectedUser, ipAddress);
                    logsFacade.create(log);

                    outcome = "/user/List?faces-redirect=true";
                    if (connectedUser.getAccesslevel() == 2) {
                        outcome = "/device/Monitoring?faces-redirect=true";
                    }

                } else {

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("usernamePasswordInvalide")));
                }

            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("tray again in 5 minutes"));

        }

        return outcome;
    }

    public String logout() {
        //logout saved in  logs table
        HttpServletRequest request2 = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAddress = request2.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request2.getRemoteAddr();
        }
        if (ipAddress == null) {
            ipAddress = "non dispo";
        }
        Logs log = new Logs(new Date(), "Logout", connectedUser, ipAddress);        
        logsFacade.create(log);
        // Notice the redirect syntax. The forward slash means start at
        // the root of the web application.
        String destination = "/login?faces-redirect=true";

        // FacesContext provides access to other container managed objects,
        // such as the HttpServletRequest object, which is needed to perform
        // the logout operation.
        FacesContext context = FacesContext.getCurrentInstance();

        HttpServletRequest request
                = (HttpServletRequest) context.getExternalContext().getRequest();

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("connectedUser", null);

        try {
            // added May 12, 2014
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

            // this does not invalidate the session but does null out the user Principle
            request.logout();
        } catch (ServletException e) {

            destination = "/login?faces-redirect=true";
        }

        return destination; // go to destination
    }

    public void updateUsedMap() {

        getFacade().edit(connectedUser);

    }

    public String editAcoount() {
        current = connectedUser;

        return "/user/EditAdmin";
    }

    public String prepareEdit() {
        current = (User) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/user/EditAdmin";
    }

    public String update() {
//        try {
//            getFacade().edit(current);
//            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserUpdated"));
//            return "View";
//        } catch (Exception e) {
//            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
//            return null;
//        }

        if (current.getPassword().equals(current.getConfirmPassword()) || current.getUserid() != connectedUser.getUserid()) {

            try {
                if (!this.duplicatedUser()) {
//                    this.shaGeneration(current.getPassword());
                    current.setIsactive((short) 1);
                    current.setIsdeleted((short) 0);
                    current.setResetpassword((short) 0);
                    current.setCreatedby(current.getCreatedby());
                    current.setCreatedate(current.getCreatedate());

                    getFacade().edit(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserUpdated"));
                    this.prepareCreate();

                    if (connectedUser.getAccesslevel() != 2) {
                        return prepareList();
                    } else {
                        return "/device/Monitoring?faces-redirect=true";
                    }
                } else {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("usernamealreadyexists"));
                    return null;
                }
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                return null;
            }
        } else {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("incorrestconfirmpassword"));
            return null;
        }

    }

    public String destroyWithConfirm() {
        String value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("usertoremove");
        current = ejbFacade.find(Integer.valueOf(value));
        performDestroy();
        return prepareList();
    }

    public String destroy() {
        current = (User) getItems().getRowData();
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
            current.setIsactive((short) 0);
            current.setIsdeleted((short) 1);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserDeleted"));
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

        if (connectedUser.getAccesslevel() == 0) {
            return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
        } else {
            return JsfUtil.getSelectItems((List<User>) connectedUser.getDistributerid().getUserCollection(), false);
        }
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        if (connectedUser.getAccesslevel() == 0) {
            return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
        } else {
            return JsfUtil.getSelectItems((List<User>) connectedUser.getDistributerid().getUserCollection(), true);
        }

    }

    //this methode will be invoked only by user type
    //because admin can affect device to all users
    public void prepareGetMyUser() {

        if (connectedUser.getAccesslevel() == 0) {
            users = ejbFacade.findAll();
        } else {
            users = ejbFacade.findByDistributer("User.findByDistributerId", "distributer", connectedUser.getDistributerid());

            List<Distributer> distributers = new ArrayList<Distributer>();
            distributers = (List<Distributer>) connectedUser.getDistributerid().getDistributerCollection();
            for (int ds = 0; ds < distributers.size(); ds++) {
                Distributer s = distributers.get(ds);
                List<User> userss = new ArrayList<User>();
                userss = (List<User>) s.getUserCollection();
                for (int us = 0; us < userss.size(); us++) {
                    User user = userss.get(us);
                    if (user.getAccesslevel() == 3 || user.getAccesslevel() == 1) {
                        users.add(user);
                    }
                }

            }
        }
    }

    //my devices
    public List<Device> myDevices() {
        List<Device> myDevices = new ArrayList<Device>();
        Devicedistributor d;
        Iterator<Devicedistributor> i = connectedUser.getMydevicedistributorCollection().iterator();
        while (i.hasNext()) {
            d = (Devicedistributor) i.next();
            if (d.getAssigned() == (short) 1) {
                myDevices.add(d.getDeviceid());
            }

        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("myDevices", myDevices);

        return myDevices;

    }

    @FacesConverter(forClass = User.class)
    public static class UserControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserController controller = (UserController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userController");
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
            if (object instanceof User) {
                User o = (User) object;
                return getStringKey(o.getUserid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + User.class.getName());
            }
        }

    }

    public static int getNbrlogin() {
        return nbrlogin;
    }

    public static void setNbrlogin(int nbrlogin) {
        UserController.nbrlogin = nbrlogin;
    }
}
