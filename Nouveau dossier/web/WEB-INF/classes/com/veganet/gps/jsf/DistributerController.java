package com.veganet.gps.jsf;

import com.veganet.gps.entities.Distributer;
import com.veganet.gps.entities.User;
import com.veganet.gps.jsf.util.JsfUtil;
import com.veganet.gps.jsf.util.PaginationHelper;
import com.veganet.gps.session.DistributerFacade;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.Part;

@ManagedBean(name = "distributerController")
@SessionScoped
public class DistributerController implements Serializable {

    private Distributer current;
    private DataModel items = null;
    @EJB
    private com.veganet.gps.session.DistributerFacade ejbFacade;
    @EJB
    private com.veganet.gps.session.UserFacade userEjbFacade;
    @EJB
    private com.veganet.gps.session.ApplicationsettingFacade applicationsettingFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private short one = 1;
    private short zero = 0;
    private User connectedUser;
    //@Resource(name = "mailSession")
    private Session mailSession;
    private User adminDistributer;
    private Part file;

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
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
                current.setLogopath(fileName);
            }

        } catch (Exception e) {

            current.setLogopath("defaultDistributerPicture.png");

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

    public User getAdminDistributer() {
        return adminDistributer;
    }

    public void setAdminDistributer(User adminDistributer) {
        this.adminDistributer = adminDistributer;
    }

    public DistributerController() {
    }

    public Distributer getSelected() {
        if (current == null) {
            current = new Distributer();
            selectedItemIndex = -1;
        }
        return current;
    }

    private DistributerFacade getFacade() {
        return ejbFacade;
    }

    @PostConstruct
    public void init() {
        connectedUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("connectedUser");
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    if (connectedUser.getAccesslevel() == 0) {
                        return getFacade().count(zero, null);

                    } else {
                        return getFacade().count(zero, connectedUser.getDistributerid());
                    }
                }

                @Override
                public DataModel createPageDataModel() {
                    if (connectedUser.getAccesslevel() == 0) {
                        return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, zero, null));

                    }
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, zero, connectedUser.getDistributerid()));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "/distributer/List?faces-redirect=true";
    }

    public String prepareView() {
        current = (Distributer) getItems().getRowData();
        adminDistributer = new User();
        adminDistributer = userEjbFacade.findAdminDistributerForDistributer("User.findAdminDistributerForDistributer", "distributerid", current, "accesslevel", (short) 3);
        if (adminDistributer == null) {
            adminDistributer = userEjbFacade.findAdminDistributerForDistributer("User.findAdminDistributerForDistributer", "distributerid", current, "accesslevel", (short) 0);
        }
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Distributer();
        adminDistributer = new User();
        selectedItemIndex = -1;
        return "/distributer/Create?faces-redirect=true";
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
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "password not generated";
    }

    public String create() {
        try {
            String randomString = Integer.toString((int) (Math.random() * Integer.MAX_VALUE), 36);

            adminDistributer.setAccesslevel(3);
            adminDistributer.setAdmincreateaccess((short) 2);
            adminDistributer.setCreatedate(new Date());
            //defaultDistributer.set(connectedUser.getDistributerid());
            adminDistributer.setIsactive(one);
            adminDistributer.setIsdeleted(zero);
            adminDistributer.setResetpassword(zero);
            adminDistributer.setCreatedby(connectedUser);

//            adminDistributer.setPassword(this.shaGeneration(randomString));
            adminDistributer.setPassword(randomString);
            adminDistributer.setEmail(adminDistributer.getFirstname() + "." + adminDistributer.getLastname() + "@gmail.com");
            adminDistributer.setDistributerid(current);

            adminDistributer.setDateofbirth(new Date());

            adminDistributer.setPhonenumber("(216) 11-111-111");
            adminDistributer.setUsedmap("GoogleMap");
            //    if(connectedUser.getAccesslevel()==3){
            current.setParentid(connectedUser.getDistributerid());
            connectedUser.getDistributerid().getDistributerCollection().add(current);
            //  }
            current.setCreatedate(new Date());
            current.setIsactive(one);
            current.setIsdeleted(zero);
            current.setAddeddby(connectedUser);
            this.upload(file);
            getFacade().create(current);
            userEjbFacade.create(adminDistributer);
            //System.out.println("how many distributor have the distributor" + connectedUser.getDistributerid().getContact() + "value" + connectedUser.getDistributerid().getDistributerCollection().size());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DistributerCreated"));

            Message msg = new MimeMessage(mailSession);
            msg.setSubject(ResourceBundle.getBundle("/Bundle").getString("applicationname") + " " + ResourceBundle.getBundle("/Bundle").getString("PasswordReset"));
            msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(
                    current.getEmail(), ResourceBundle.getBundle("/Bundle").getString("applicationname")));
            msg.setFrom(new InternetAddress("vega-tracking@vegatech.com.tn"));
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(ResourceBundle.getBundle("/Bundle").getString("DearSir") + " " + ResourceBundle.getBundle("/Bundle").getString("Messagefrom") + "" + ResourceBundle.getBundle("/Bundle").getString("Messageregister") + " " + ResourceBundle.getBundle("/Bundle").getString("Username") + adminDistributer.getUsername() + "||" + ResourceBundle.getBundle("/Bundle").getString("Password") + ":" + randomString + ResourceBundle.getBundle("/Bundle").getString("Note"));
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            msg.setContent(multipart);

            Transport.send(msg);
            prepareCreate();
            adminDistributer = new User();
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Distributer) getItems().getRowData();
        adminDistributer = new User();
        adminDistributer = userEjbFacade.findAdminDistributerForDistributer("User.findAdminDistributerForDistributer", "distributerid", current, "accesslevel", (short) 3);
        if (adminDistributer == null) {
            adminDistributer = userEjbFacade.findAdminDistributerForDistributer("User.findAdminDistributerForDistributer", "distributerid", current, "accesslevel", (short) 0);
        }
//       this.adminDistributer=userEjbFacade.findAdminDistributerForDistributer("User.findAdminDistributerForDistributer", "distributerid",current, "admincreateaccess", (short)3).size();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            this.upload(file);
            getFacade().edit(current);
            userEjbFacade.edit(adminDistributer);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DistributerUpdated"));
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Distributer) getItems().getRowData();
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
            current.setIsactive(zero);
            current.setIsdeleted(one);
            List<User> users = new ArrayList<User>();
            users = (List<User>) current.getUserCollection();
            for (int u = 0; u < users.size(); u++) {
                User user = users.get(u);
                user.setIsactive((short) 0);
                user.setIsdeleted((short) 1);
                userEjbFacade.edit(user);
            }
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DistributerDeleted"));
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

    @FacesConverter(forClass = Distributer.class)
    public static class DistributerControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DistributerController controller = (DistributerController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "distributerController");
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
            if (object instanceof Distributer) {
                Distributer o = (Distributer) object;
                return getStringKey(o.getDistributerid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Distributer.class.getName());
            }
        }

    }

}
