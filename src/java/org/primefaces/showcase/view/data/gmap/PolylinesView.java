//package org.primefaces.showcase.view.data.gmap;
//
//import com.jsf2leaf.model.Marker;
//import com.veganet.gps.entities.Position;
//import java.io.File;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import javax.annotation.PostConstruct;
//import javax.faces.application.FacesMessage;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//import javax.faces.context.FacesContext;
//import org.codehaus.jackson.map.ObjectMapper;
//
//
//import org.primefaces.context.RequestContext;
//
//import org.primefaces.event.map.OverlaySelectEvent;
//import org.primefaces.model.map.DefaultMapModel;
//import org.primefaces.model.map.LatLng;
//import org.primefaces.model.map.MapModel;
//import org.primefaces.model.map.Polyline;
//
//@ManagedBean
//@SessionScoped
//public class PolylinesView implements Serializable {
//
//    private static MapModel polylineModel = null;
//    private List<Position> positions = new ArrayList<Position>();
//    private String jsonString;
//
//    public String getJsonString() {
//        return jsonString;
//    }
//
//    public void setJsonString(String jsonString) {
//        this.jsonString = jsonString;
//    }
//
//
//    public void drawFeedBackInMap() {
//        System.out.println("start drawFeedBackInMap");
//        try {
//                ObjectMapper mapper = new ObjectMapper();
//            positions = (List<Position>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("positionsFeedBack");
// 
//            
//            System.out.println(positions.size() + "size of positions");
//          //  mapper.writeValue(new File("E:\\Stage-PFE\\user.json"),positions);
//         jsonString= mapper.writeValueAsString(positions);
// 
//
//            /*
//             Polyline polyline = new Polyline();
//             //access via Iterator
//             Position firstPosition = positions.get(0);
//             Position lastPosition = positions.get(positions.size() - 1);
//             LatLng startCoord = new LatLng(firstPosition.getLatitude(), firstPosition.getLongitude());
//             LatLng endCoord = new LatLng(lastPosition.getLatitude(), lastPosition.getLongitude());
//
//             //            polylineModel.addOverlay(new Marker(startCoord, "start"));
//             //            polylineModel.addOverlay(new Marker(endCoord, "end"));
//
//             Iterator positionIterator = positions.iterator();
//             while (positionIterator.hasNext()) {
//             Position p = (Position) positionIterator.next();
//             LatLng coord = new LatLng(p.getLatitude(), p.getLongitude());
//             polyline.getPaths().add(coord);
//             }
//
//             polyline.setStrokeWeight(10);
//             polyline.setStrokeColor("#FF9900");
//             polyline.setStrokeOpacity(0.7);
//
//             polylineModel.addOverlay(polyline);
//             */
//            System.out.println("json generated");
//        } catch (Exception e) {
//           e.printStackTrace();
//        }
//
//        RequestContext requestContext = RequestContext.getCurrentInstance();
//
//        requestContext.update("gmapss");
//
////requestContext.update("GoogleMapForm:gmapss");
////requestContext.update("gmapss");
////requestContext.update("GoogleMapForm");
//        System.out.println("end drawFeedBackInMap");
//
//    }
//
//    public MapModel getPolylineModel() {
//        if (polylineModel == null) {
//            polylineModel = new DefaultMapModel();
//        } else {
//            System.out.println("polylinemodel existe deja");
//        }
//
//        //this.drawFeedBackInMap();
//        System.out.println("update");
//        return polylineModel;
//    }
//
//    public void onPolylineSelect(OverlaySelectEvent event) {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Polyline Selected", null));
//    }
//}
