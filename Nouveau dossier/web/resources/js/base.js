//global variable for openlayer & googlemap
var convertArray = [];
var tabFeatures = [];
//googlemap global variables
var googlemap;
//marker array to hold your markers
var googlemapMarkers = [];
var googlemapMarkerRealTime;

var imagemoving;
var imagemovingRealTime;
var myLatLng;
var myLatLngRealTime;
var shape;
var marker;
var markerRealTime;
var opPos;
var b;
var a;
var duration;
var start;
var pan;
var bounce;
var tabFeatures2;
var point;
var savemarker;
var saveinfowindow;
var arrayinfowindo;
var infowindow;
//****OpenstreetMap Variables****
//openlayermap global variables
var statusinfowindows = [];
var openstreetmap;
var view;
//vector layer for openstreetmap
var vector_layer;
//iconfeature for openstreetmap
var iconFeature;
//iconstyle for openstreet
var iconStyle;
//vectorsource for openstreetmap
var vectorSource;
//feedback googlemap variables
// Icons for markers
var RED_MARKER = 'https://maps.google.com/mapfiles/ms/icons/red-dot.png';
var BLUE_MARKER = 'https://maps.google.com/mapfiles/ms/icons/blue-dot.png';
var YELLOW_MARKER = 'https://maps.google.com/mapfiles/ms/icons/yellow-dot.png';
// Replace with your own API key
var API_KEY = 'AIzaSyCs2n9svhqi7H1mddr3OEj6acbw7kQlx_o';
// URL for places requests
var PLACES_URL = 'https://maps.googleapis.com/maps/api/place/details/json?' +
        'key=' + API_KEY + '&placeid=';
// URL for Speed limits
var SPEED_LIMIT_URL = 'https://roads.googleapis.com/v1/speedLimits';
var coords;
var interpolate = true;
var map;
var placesService;
var originalCoordsLength;
// Settingup Arrays
var closeInfoW = 0;
var markers = [];
var placeIds = [];
var polylines = [];
var snappedCoordinates = [];
var snappedCoordinates2 = [];
var validPoints = [];
var validMarkers = [];
var mapOptions;
var firstrefresh = 0;
var geofencArrayGoogle;
var drawingManager;
var speedArray = [];
var speedArray2 = [];
var lineCoordinates;
var originalid = 0;
var speed = 100;
var infoWindowArrayConvert;


//roadToMyCar
var googlemap3;
//google map for geofence
var googlemap4;
var markerRoadToMyCar;
var countRefresh = 0;
var directionsDisplay;
var indianapolis;
var circle;
var undefined = "undefined";
var language = "";
var openInfowindowImei = undefined;
var oldInfowindow;
var googlemapRealTime;
var realTimePath = [];
var polylineRealTime;
var ttime = 15;
var stopAnimation = 0;


function reactToChangedSelect(par) {
    if (par === "F")
        speed = 1;
    else if (par === "M")
        speed = 10;
    else
        speed = 100;
}

function stopAnim() {


    if (stopAnimation === 0) {
        stopAnimation = 1;
        $("#stopButton").text(" Marche");
        $("#stopButton").removeClass('icon-pause');
        $("#stopButton").addClass('icon-play');

    }
    else {
        $("#stopButton").text(" Pause");
        $("#stopButton").removeClass('icon-play');
        $("#stopButton").addClass('icon-pause');

        stopAnimation = 0;
    }

}

function setLanguage(lang) {

    language = lang;

}

$(function () {


    $('.subnavbar').find('li').each(function (i) {

        var mod = i % 3;
        if (mod === 2) {
            $(this).addClass('subnavbar-open-right');
        }
    });
});
function toggle(id) {
    var el = document.getElementById(id);
    var img = document.getElementById("arrowm");
    var boxm = el.getAttribute("class");
    if (boxm == "hidem") {
        el.setAttribute("class", "showm");
        delay(img, "../resources/images/Right_arrow_angle_32.png", 400);
    } else {
        el.setAttribute("class", "hidem");
        delay(img, "../resources/images/Left_arrow_angle_32.png", 400);
    }
}

function delay(elem, src, delayTime) {
    window.setTimeout(function () {
        elem.setAttribute("src", src);
    }, delayTime);
}







//add openstreetmap with  openlayer API
function openlayermap() {
    firstrefresh = 0;
    var osm = new ol.layer.Tile({
        source: new ol.source.OSM()
    });
    var bing = new ol.layer.Tile({
        source: new ol.source.BingMaps({
            key: 'Ak-dzM4wZjSqTlzveKz5u0d4IQ4bRzVI309GxmkgSVr1ewS6iPSrOvOKhA-CJlm3',
            imagerySet: 'Aerial',
            projection: 'EPSG:4326'
        })
    });
    view = new ol.View({
        center: [1130387.81, 3801177.4366930],
        rotation: Math.PI / 100,
        zoom: 6
    });
    openstreetmap = new ol.Map({
        layers: [osm, bing],
        loadTilesWhileAnimating: true,
        target: 'map',
        controls: ol.control.defaults().extend([
            new ol.control.FullScreen({
                className: 'screen2'
            }),
            new ol.control.ScaleLine({
                units: 'metric'
            })



        ]),
        view: view
    });
    /*exporting png*/

    var exportPNGElement = document.getElementById('export-png');
    if ('download' in exportPNGElement) {
        exportPNGElement.addEventListener('click', function (e) {
            openstreetmap.once('postcompose', function (event) {
                var canvas = event.context.canvas;
                exportPNGElement.href = canvas.toDataURL('image/png');
            });
            openstreetmap.renderSync();
        }, false);
    } else {
        var info = document.getElementById('no-download');
        /**
         * display error message
         */
        info.style.display = '';
    }

    var swipe = document.getElementById('swipe');
    bing.on('precompose', function (event) {

        var ctx = event.context;
        var width = ctx.canvas.width * (swipe.value / 100);
        ctx.save();
        ctx.beginPath();
        ctx.rect(width, 0, ctx.canvas.width - width, ctx.canvas.height);
        ctx.clip();
    });
    bing.on('postcompose', function (event) {
        var ctx = event.context;
        ctx.restore();
    });
    swipe.addEventListener('input', function () {
        openstreetmap.render();
    }, false);
    var featureOverlay = new ol.FeatureOverlay({
        style: new ol.style.Style({
            fill: new ol.style.Fill({
                color: 'rgba(255, 255, 255, 0.2)'
            }),
            stroke: new ol.style.Stroke({
                color: '#ffcc33',
                width: 2
            }),
            image: new ol.style.Circle({
                radius: 7,
                fill: new ol.style.Fill({
                    color: '#ffcc33'
                })
            })
        })
    });
    featureOverlay.setMap(openstreetmap);
    var i = 0;
    openstreetmap.on('dblclick', function (evt) {

        var feature = openstreetmap.forEachFeatureAtPixel(evt.pixel,
                function (feature, layer) {
                    document.getElementById('geofenceform:zone').value = "fdgbfr";
                });
    });
    var modify = new ol.interaction.Modify({
        features: featureOverlay.getFeatures(),
        // the SHIFT key must be pressed to delete vertices, so
        // that new vertices can be drawn at the same position
        // of existing vertices
        //<![CDATA[        
        deleteCondition: function (event) {
            return ol.events.condition.shiftKeyOnly(event) &&
                    ol.events.condition.singleClick(event);
        }
        //]]>
    });
//    openstreetmap.addInteraction(modify);
    //var typeSelect = document.getElementById('geom_type');
//    var draw; // global so we can remove it later
//    function addInteraction() {
//        draw = new ol.interaction.Draw({
//            features: featureOverlay.getFeatures(),
//            type: /** @type {ol.geom.GeometryType} */ (typeSelect.value)
//        });
//        openstreetmap.addInteraction(draw);
//    }



    iconFeature = new ol.Feature({
        geometry: new ol.geom.Point([1070397.81, 4055477.4366930]),
        name: 'Null Island',
        population: 4000,
        rainfall: 500
    });
    iconStyle = new ol.style.Style({
        image: new ol.style.Icon(/** @type {olx.style.IconOptions} */ ({
            anchor: [0.5, 32],
            anchorXUnits: 'fraction',
            anchorYUnits: 'pixels',
            src: '../resources/images/moving.png'
        }))
    });
    iconFeature.setStyle(iconStyle);
    vectorSource = new ol.source.Vector({
        features: [iconFeature]
    });
    vector_layer = new ol.layer.Vector({
        source: vectorSource
    });
    var i;
    var element = document.getElementById('popup');
    var popup = new ol.Overlay({
        element: element,
        positioning: 'bottom-center',
        stopEvent: false
    });
    openstreetmap.addOverlay(popup);
// display popup on click
    openstreetmap.on('click', function (evt) {

        var feature = openstreetmap.forEachFeatureAtPixel(evt.pixel,
                function (feature, layer) {
                    return feature;
                });
        if (feature) {
            var geometry = feature.getGeometry();
            var coord = geometry.getCoordinates();
            popup.setPosition(coord);
            $(element).popover({
                'placement': 'top',
                'html': true,
                'content': feature.get('name')
            });
            $(element).popover('show');
        } else {
            $(element).popover('destroy');
        }

    });
    /**
     * Let user change the geometry type.
     * @param {Event} e Change event.
     */
//    typeSelect.onchange = function (e) {
//
//        openstreetmap.removeInteraction(draw);
//        addInteraction();
//    };
//    addInteraction();
// make interactions global so they can later be removed
    var select_interaction,
            draw_interaction,
            modify_interaction;
//    var x = document.createElement("INPUT");
//    x.setAttribute("type", "radio");
//    x.setAttribute("name", "interaction_type");
//    x.setAttribute("value", "draw");

//    var x = document.createElement("SELECT");
// 
//    x.setAttribute("name", "data_type");
//    x.setAttribute("value", "GeoJSON");



// get the interaction type
    var $interaction_type = $('[name="interaction_type"]');
// rebuild interaction when changed
    $interaction_type.on('click', function (e) {

        // add new interaction
        if (this.value === 'draw') {

            addDrawInteraction();
        } else {
            addModifyInteraction();
        }
    });
// get geometry type
    var $geom_type = $('#geom_type');
// rebuild interaction when the geometry type is changed
    $geom_type.on('change', function (e) {
        openstreetmap.removeInteraction(draw_interaction);
        addDrawInteraction();
    });
// get data type to save in
    $data_type = $('#data_type');
// clear map and rebuild interaction when changed
    $data_type.onchange = function () {

        clearMap();
        openstreetmap.removeInteraction(draw_interaction);
        addDrawInteraction();
    };
// build up modify interaction
// needs a select and a modify interaction working together
    function addModifyInteraction() {

        // remove draw interaction
        openstreetmap.removeInteraction(draw_interaction);
        // create select interaction
        select_interaction = new ol.interaction.Select({
            // make sure only the desired layer can be selected
            layers: function (vector_layer) {
                return vector_layer.get('name') === 'my_vectorlayer';
            }
        });
        openstreetmap.addInteraction(select_interaction);
        // grab the features from the select interaction to use in the modify interaction
        var selected_features = select_interaction.getFeatures();
        // when a feature is selected...
        selected_features.on('add', function (event) {
            // grab the feature
            var feature = event.element;
            // ...listen for changes and save them
            feature.on('change', saveData);
            // listen to pressing of delete key, then delete selected features
            $(document).on('keyup', function (event) {
                if (event.keyCode == 46) {
                    // remove all selected features from select_interaction and my_vectorlayer
                    selected_features.forEach(function (selected_feature) {
                        var selected_feature_id = selected_feature.getId();
                        // remove from select_interaction
                        selected_features.remove(selected_feature);
                        // features aus vectorlayer entfernen
                        var vectorlayer_features = vector_layer.getSource().getFeatures();
                        vectorlayer_features.forEach(function (source_feature) {
                            var source_feature_id = source_feature.getId();
                            if (source_feature_id === selected_feature_id) {
                                // remove from my_vectorlayer
                                vector_layer.getSource().removeFeature(source_feature);
                                // save the changed data
                                saveData();
                            }
                        });
                    });
                    // remove listener
                    $(document).off('keyup');
                }
            });
        });
        // create the modify interaction
        modify_interaction = new ol.interaction.Modify({
            features: selected_features,
            // delete vertices by pressing the SHIFT key
            deleteCondition: function (event) {
                return ol.events.condition.shiftKeyOnly(event) &&
                        ol.events.condition.singleClick(event);
            }
        });
        // add it to the map
        openstreetmap.addInteraction(modify_interaction);
    }

// creates a draw interaction
    function addDrawInteraction() {

// remove other interactions
        openstreetmap.removeInteraction(select_interaction);
        openstreetmap.removeInteraction(modify_interaction);
        // create the interaction
        draw_interaction = new ol.interaction.Draw({
            source: vector_layer.getSource(),
            type: /** @type {ol.geom.GeometryType} */ ($geom_type.val())
        });
        // add it to the map
        openstreetmap.addInteraction(draw_interaction);
        // when a new feature has been drawn...
        draw_interaction.on('drawend', function (event) {
            // create a unique id
            // it is later needed to delete features
            var id = uid();
            // give the feature this id
            event.feature.setId(id);
            // save the changed data
            saveData();
        });
    }

// add the draw interaction when the page is first shown
//    addDrawInteraction();
// shows data in textarea
// replace this function by what you need
//dt is the format of data that will be saved
    var dt = 'GeoJSON';
    function saveData() {

        // get the format the user has chosen
        var data_type = dt,
                // define a format the data shall be converted to
                format = new ol.format[data_type](),
                // this will be the data in the chosen format
                data;
        try {

            // convert the data of the vector_layer into the chosen format
            data = format.writeFeatures(vector_layer.getSource().getFeatures());
            var v = format.writeFeatures(vector_layer.getSource().getFeatures());
        } catch (e) {
            // at time of creation there is an error in the GPX format (18.7.2014)
            $('#data').val(e.name + ": " + e.message);
            return;
        }
        if (data_type === 'GeoJSON') {
            // format is JSON

            $('#data').val(JSON.stringify(data, null, 4));
        }

    }

// clear map when user clicks on 'Delete all features'
    $("#delete").click(function () {
        clearMap();
    });
// clears the map and the output of the data
    function clearMap() {

        vector_layer.getSource().clear();
        if (select_interaction) {
            select_interaction.getFeatures().clear();
        }
        $('#data').val('');
    }

// creates unique id's
    function uid() {
        var id = 0;
        return function () {
            if (arguments[0] === 0) {
                id = 0;
            }
            return id++;
        }
    }


}


function getJavaArray() {



    var arrayFromJava = stringToConvert = document.getElementById('feedbackwidget:arrayFeedback').value;
//    alert("Java Array length = " + arrayFromJava.length + "\r\n" +
//            "element 2 is " + arrayFromJava[1]);
}


function stringToArryConverter() {

    convertArray = "";
    stringToConvert = "";
    stringToConvert = document.getElementById('allMaps:array').value;
    convertArray = stringToConvert.split("?");
    var Online;
    var Offline;
    var On;
    var Activation;
    var Deactivation;
    var Off;

    if (language === 'fr') {
        Online = "En ligne";
        Offline = "Hors ligne";
        On = "Activer";
        Activation = "En activation";
        Deactivation = "En Désactivation";
        Off = "Désactiver";


    }
    else if (language === 'en') {
        Online = "Online";
        Offline = "Offline";
        On = "On";
        Activation = "Activation";
        Deactivation = "Deactivation";
        Off = "Off";
    }
    for (var j = 0; j < convertArray.length - 1; j = j + 11) {



//status
 
        if (convertArray[j + 4] === 1) {


            convertArray[j + 4] = Online;
        }
        if (convertArray[j + 4] !== 1) {


            convertArray[j + 4] = Offline;
        }

//ACC STATUS
        if (convertArray[j + 5] === '1') {
            convertArray[j + 5] = On;
        }
        if (convertArray[j + 5] === '3') {
            convertArray[j + 5] = Off;
        }



//VIBRATION STATUS
        if (convertArray[j + 8] === 1) {
            convertArray[j + 8] = On;
        }
        if (convertArray[j + 8] !== 1) {
            convertArray[j + 8] = Off;
        }
//SOS BUTTON                 
        if (convertArray[j + 9] === 1) {
            convertArray[j + 9] = On;
        }
        if (convertArray[j + 9] !== 1) {
            convertArray[j + 9] = Off;
        }


    }
console.log('end converter');
}


function refreshMarkerOpenstreet() {

    stringToArryConverter();
    if (firstrefresh === 0 && (convertArray[0] !== null || convertArray[1] !== null)) {
        a = parseFloat(convertArray[0]);
        b = parseFloat(convertArray[1]);
        opPos = ol.proj.fromLonLat([b, a]);
        duration = 5000;
        start = +new Date();
        pan = ol.animation.pan({
            duration: duration,
            source: /** @type {ol.Coordinate} */ (view.getCenter()),
            start: start
        });
        bounce = ol.animation.bounce({
            duration: duration,
            resolution: 4 * view.getResolution(),
            start: start
        });
        view.setZoom(15);
        openstreetmap.beforeRender(pan, bounce);
        view.setCenter(opPos);
        firstrefresh++;
    }
    openstreetmap.removeLayer(vector_layer, true);
    tabFeatures2 = [];
    var Status;
    var Lastupdate;
    var Speed;
    if (language === 'fr') {
        Status = "Statut";
        Lastupdate = "dernière verification";
        Speed = "vitesse";

    }
    else if (language === 'en') {
        Status = "Status";
        Lastupdate = "Last update";
        Speed = "Speed";
    }
    for (i = 0; i < convertArray.length - 1; i = i + 10) {

        a = parseFloat(convertArray[i]);
        b = parseFloat(convertArray[i + 1]);
        point = new ol.geom.Point(ol.proj.transform([b, a], 'EPSG:4326', 'EPSG:3857'));
        iconFeature = new ol.Feature({
            geometry: point,
            name: '<div id="content">' +
                    '<div id="siteNotice">' +
                    '</div>' +
                    '<h1 id="firstHeading" class="firstHeading">' + convertArray[i + 2]
                    + '</h1>' +
                    '<div id="bodyContent">' +
                    '<p><b>IMEI : </b>' + convertArray[i + 3] + '</p>' +
                    '<p><b>' + Status + ': </b>' + convertArray[i + 4 ] + '</p>' +
                    '<p><b>ACC ' + Status + ': </b>' + convertArray[i + 5] + '</p>' +
                    '<p><b>' + Lastupdate + ': </b>' + convertArray[i + 6] + '</p>' +
                    '<p><b>' + Speed + ': </b>' + convertArray[i + 7] + 'Km/h</p>' +
                    '<p><b>Vibration ' + Status + ': </b>' + convertArray[i + 8] + '</p>' +
                    '<p><b>SOS: </b>' + convertArray[i + 9] + '</p>' +
                    '</div>' +
                    '</div>',
            population: 4000,
            rainfall: 500
        });
        iconFeature.setStyle(iconStyle);
        tabFeatures2.push(iconFeature);
//            popup = new OpenLayers.Popup("chicken",
//                    new OpenLayers.LonLat(convertArray[i], convertArray[i + 1]),
//                    new OpenLayers.Size(200, 200),
//                    "example popup",
//                    true);
//
//            map.addPopup(popup);
//            alert(convertArray[i].concat("long"));
//            alert(i);
    }
    vectorSource = new ol.source.Vector({
        features: tabFeatures2
    });
    vector_layer = new ol.layer.Vector({
        source: vectorSource
    });
    openstreetmap.addLayer(vector_layer);
}

function googlemapcontactinitialize() {

    var mapOptions2 = {
        center: {lat: 35.884445, lng: 10.597917},
        scaleControl: true,
        zoom: 12,
        mapTypeControl: true,
        mapTypeControlOptions: {
            style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
            position: google.maps.ControlPosition.LEFT_TOP
        },
        zoomControl: true,
        zoomControlOptions: {
            position: google.maps.ControlPosition.LEFT_CENTER
        },
        streetViewControl: true,
        streetViewControlOptions: {
            position: google.maps.ControlPosition.LEFT_TOP
        }
    };
    var googlemap2 = new google.maps.Map(document.getElementById('devv2'),
            mapOptions2);

    var myLatlng = new google.maps.LatLng(35.884445, 10.597917);
    var marker = new google.maps.Marker({
        position: myLatlng,
        title: "VEGATECH"
    });

// To add the marker to the map, call setMap();
    marker.setMap(googlemap2);

}

function googlemapinitialize() {



    mapOptions = {
        center: {lat: 33.756544, lng: 9.417724},
        scaleControl: true,
        zoom: 12,
        mapTypeControl: true,
        mapTypeControlOptions: {
            style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
            position: google.maps.ControlPosition.LEFT_TOP
        },
        zoomControl: true,
        zoomControlOptions: {
            position: google.maps.ControlPosition.LEFT_CENTER
        },
        streetViewControl: true,
        streetViewControlOptions: {
            position: google.maps.ControlPosition.LEFT_TOP
        }



    };

    googlemap = new google.maps.Map(
            document.getElementById('devv'),
            mapOptions);

    firstrefresh = 0;


    saveinfowindow = new google.maps.InfoWindow();

    savemarker = new google.maps.Marker();


}

////////////////////

/// <reference path="../typings/google.maps.d.ts" />
function googleMapButton(text, className) {
    "use strict";
    var controlDiv = document.createElement("divv");
    controlDiv.className = className;
    controlDiv.index = 1;
    controlDiv.style.padding = "10px";
    // set CSS for the control border.
    var controlUi = document.createElement("div");
    controlUi.style.backgroundColor = "rgb(255, 255, 255)";
    controlUi.style.color = "#565656";
    controlUi.style.cursor = "pointer";
    controlUi.style.textAlign = "center";
    controlUi.style.boxShadow = "rgba(0, 0, 0, 0.298039) 0px 1px 4px -1px";
    controlDiv.appendChild(controlUi);
    // set CSS for the control interior.
    var controlText = document.createElement("div");
    controlText.style.fontFamily = "Roboto,Arial,sans-serif";
    controlText.style.fontSize = "11px";
    controlText.style.paddingTop = "8px";
    controlText.style.paddingBottom = "8px";
    controlText.style.paddingLeft = "8px";
    controlText.style.paddingRight = "8px";
    controlText.innerHTML = text;
    controlUi.appendChild(controlText);
    $(controlUi).on("mouseenter", function () {
        controlUi.style.backgroundColor = "rgb(235, 235, 235)";
        controlUi.style.color = "#000";
    });
    $(controlUi).on("mouseleave", function () {
        controlUi.style.backgroundColor = "rgb(255, 255, 255)";
        controlUi.style.color = "#565656";
    });
    return controlDiv;
}
function FullScreenControl(map, enterFull, exitFull) {



    var Fullscreen;
    var Exitfullscreen;

    if (language === 'fr') {
        Fullscreen = "Plein écran";
        Exitfullscreen = "Quitter Plein écran";

    }
    else if (language === 'en') {
        Fullscreen = "Full screen";
        Exitfullscreen = "Exit full screen";

    }


    if (enterFull === void 0) {
        enterFull = null;
    }
    if (exitFull === void 0) {
        exitFull = null;
    }
    if (enterFull == null) {
        enterFull = "Full screen";
    }
    if (exitFull == null) {
        exitFull = "Exit full screen";
    }
    var controlDiv = googleMapButton(enterFull, "fullScreen");
    var fullScreen = false;
    var interval;
    var mapDiv = map.getDiv();
    var divStyle = mapDiv.style;
    if (mapDiv.runtimeStyle) {
        divStyle = mapDiv.runtimeStyle;
    }
    var originalPos = divStyle.position;
    var originalWidth = divStyle.width;
    var originalHeight = divStyle.height;
    // ie8 hack
    if (originalWidth === "") {
        originalWidth = mapDiv.style.width;
    }
    if (originalHeight === "") {
        originalHeight = mapDiv.style.height;
    }
    var originalTop = divStyle.top;
    var originalLeft = divStyle.left;
    var originalZIndex = divStyle.zIndex;
    var bodyStyle = document.body.style;
    if (document.body.runtimeStyle) {
        bodyStyle = document.body.runtimeStyle;
    }
    var originalOverflow = bodyStyle.overflow;
    controlDiv.goFullScreen = function () {
        var center = map.getCenter();
        mapDiv.style.position = "fixed";
        mapDiv.style.width = "100%";
        mapDiv.style.height = "100%";
        mapDiv.style.top = "0";
        mapDiv.style.left = "0";
        mapDiv.style.zIndex = "100";
        document.body.style.overflow = "hidden";
        $(controlDiv).find("div div").html(exitFull);
        fullScreen = true;
        google.maps.event.trigger(map, "resize");
        map.setCenter(center);
        // this works around street view causing the map to disappear, which is caused by Google Maps setting the 
        // css position back to relative. There is no event triggered when Street View is shown hence the use of setInterval
        interval = setInterval(function () {
            if (mapDiv.style.position !== "fixed") {
                mapDiv.style.position = "fixed";
                google.maps.event.trigger(map, "resize");
            }
        }, 100);
    };
    controlDiv.exitFullScreen = function () {
        var center = map.getCenter();
        if (originalPos === "") {
            mapDiv.style.position = "relative";
        } else {
            mapDiv.style.position = originalPos;
        }
        mapDiv.style.width = originalWidth;
        mapDiv.style.height = originalHeight;
        mapDiv.style.top = originalTop;
        mapDiv.style.left = originalLeft;
        mapDiv.style.zIndex = originalZIndex;
        document.body.style.overflow = originalOverflow;
        $(controlDiv).find("div div").html(enterFull);
        fullScreen = false;
        google.maps.event.trigger(map, "resize");
        map.setCenter(center);
        clearInterval(interval);
    };
    // setup the click event listener
    google.maps.event.addDomListener(controlDiv, "click", function () {
        if (!fullScreen) {
            controlDiv.goFullScreen();
        } else {
            controlDiv.exitFullScreen();
        }
    });
    return controlDiv;
}
/////////////////////

function setfirstrefresh() {

    firstrefresh = 2;

}
function getTimeRemaining(s) {



    return {
        'seconds': s
    };
}
function initializeClock(id, endtime) {

    clock = document.getElementById(id);
    secondsSpan = clock.querySelector('.seconds');
    myTimer = setInterval(updateClock, 1000);
}
var clock;
var secondsSpan;
var message;
function updateClock() {

    message = getTimeRemaining(ttime);
    secondsSpan.innerHTML = ('0' + message.seconds).slice(-2);
    ttime = ttime - 1;
    if (ttime <= 0) {
        ttime = 14;

    }

}
var myTimer;



function setMarkers() {
    clearInterval(myTimer);

    ttime = 14;

    myTimer = setInterval(updateClock, 1000);

    stringToArryConverter();

    arrayinfowindo = [];


    if (convertArray.length > 1 && firstrefresh === 0) {

        mapOptions = {
            center: {lat: 35.350415, lng: 13.687884},
            zoom: 6,
            mapTypeControl: true,
            mapTypeControlOptions: {
                style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
                position: google.maps.ControlPosition.LEFT_TOP
            },
            zoomControl: true,
            zoomControlOptions: {
                position: google.maps.ControlPosition.LEFT_CENTER
            },
            streetViewControl: true,
            streetViewControlOptions: {
                position: google.maps.ControlPosition.LEFT_TOP
            }
        };
        var Fullscreen;
        var Exit;

        if (language === 'fr') {
            Fullscreen = "Plein écran";
            Exit = "Quitter";

        }
        else if (language === 'en') {
            Fullscreen = "Full screen";
            Exit = "Exit";

        }
        googlemap = new google.maps.Map(document.getElementById('devv'),
                mapOptions);
        googlemap.controls[google.maps.ControlPosition.LEFT_TOP].push(
                new FullScreenControl(googlemap, Fullscreen, Exit));
        firstrefresh++;
    }
    if (firstrefresh === 2 && !(typeof convertArray[0] === undefined || typeof convertArray[1] === undefined)) {
        if (convertArray.length === 12) {
            googlemap.setZoom(15);
            googlemap.panTo(new google.maps.LatLng(parseFloat(convertArray[0]), parseFloat(convertArray[ 1])));

        }
        else {
            googlemap.panTo(new google.maps.LatLng(33.756544, 9.417724));
            googlemap.setZoom(6);
//            console.log('point');
        }

        firstrefresh++;


    }

    for (i = 0; i < convertArray.length - 1; i = i + 11) {

        myLatLng = new google.maps.LatLng(parseFloat(convertArray[i]), parseFloat(convertArray[i + 1]));


        var s = '';
        switch (convertArray[i + 10]) {
            case '1':
                s = '../resources/images/smallCar?.png';
                break;
            case '2':
                s = '../resources/images/bigCar?.png';
                break;
            case '3':
                s = '../resources/images/smallTruck?.png';
                break;
            case '4':

                s = '../resources/images/bigTruck?.png';
                break;
            case '5':
                s = '../resources/images/smallBus?.png';
                break;
            case '6':
                s = '../resources/images/bigBus?.png';
                break;
            case '7':
                s = '../resources/images/armored?.png';
                break;
            case '8':
                s = '../resources/images/moto?.png';
                break;
            case '8':
                s = '../resources/images/ship?.png';
                break;

            default:
                s = '../resources/images/undefined?.png';
        }
        if (convertArray[i + 7] > 1 && convertArray[i + 7] !== null) {
            s = s.replace("?", "Move");
            imagemoving = {
                url: s,
                // This marker is 20 pixels wide by 32 pixels tall.
                size: new google.maps.Size(35, 34),
                // The origin for this image is 0,0.
                origin: new google.maps.Point(0, 0),
                // The anchor for this image is the base of the flagpole at 0,32.
                anchor: new google.maps.Point(17, 34)
            };
        } else {
            s = s.replace("?", "Stop");
            imagemoving = {
                url: s,
                // This marker is 20 pixels wide by 32 pixels tall.
                size: new google.maps.Size(35, 34),
                // The origin for this image is 0,0.
                origin: new google.maps.Point(0, 0),
                // The anchor for this image is the base of the flagpole at 0,32.
                anchor: new google.maps.Point(17, 34)
            };
        }
        shape = {
            coords: [1, 1, 1, 20, 18, 20, 18, 1],
            type: 'poly'
        };
        marker = new MarkerWithLabel({
            position: myLatLng,
            draggable: false,
            labelContent: convertArray[i + 2],
            labelAnchor: new google.maps.Point(22, 0),
            labelClass: "labels", // the CSS class for the label
            labelStyle: {opacity: 0.75},
            map: googlemap,
            icon: imagemoving,
            shape: shape,
            title: convertArray[i + 3]
        });
        var stopTimeString = "";
        var Status;
        var Lastupdate;
        var Speed;

        var hoursString;
        var minString;
        var stopTime;
        var Offline;
        var Online;

        if (language === 'fr') {
            Status = "Statut";
            Lastupdate = "dernière verification";
            Speed = "vitesse";
            Offline = "Hors ligne";
            Online = "En ligne";
            minString = "minutes";
            hoursString = "heures";
            stopTime = "<b>temps d'arrêt:</b>";
        }
        else if (language === 'en') {
            Offline = "Offline";
            Online = "Online";
            Status = "Status";
            Lastupdate = "Last update";
            Speed = "Speed";

            minString = "minutes";
            hoursString = "hours";
            stopTime = "<b>Stop time:</b>";
        }
        googlemapMarkers.push(marker);

        if (convertArray[i + 5] === 'Désactiver' || convertArray[i + 5] === 'Off') {
            var t = convertArray[i + 6].split(new RegExp('[/: ]'));
            var day = moment(t[2] + "-" + parseInt(t[1]) + "-" + (t[0]) + " " + parseInt(t[3]) + ":" + t[4] + ":" + t[5], "YYYY-MM-DD HH:mm:ss");
            console.log(parseInt(t[3]) + ":" + t[4] + ":" + t[5]);
            var zone = "Africa/Tunis";

            var ms = moment(moment().tz(zone).format(), "YYYY-MM-DD HH:mm:ss").diff(moment(day, "YYYY-MM-DD HH:mm:ss"));
            var d = moment.duration(ms);
            stopTimeString = Math.floor(d.asHours()) + hoursString + "," + moment.utc(ms).format("mm") + minString;
            stopTimeString = stopTime + " " + stopTimeString;

            console.log(Math.floor(moment.utc(ms).format("m")) + Math.floor(d.asHours()) * 60);

            if (Math.floor(moment.utc(ms).format("m")) + Math.floor(d.asHours()) * 60 > 15) {
                convertArray[i + 4 ] = Offline;

            }
            else if (Math.floor(moment.utc(ms).format("m")) + Math.floor(d.asHours()) * 60 < 15) {
                convertArray[i + 4 ] = Online;
            }

        }
        else {

            var t = convertArray[i + 6].split(new RegExp('[/: ]'));
            var day = moment(t[2] + "-" + parseInt(t[1]) + "-" + (t[0] + 1) + " " + parseInt(t[3]) + ":" + t[4] + ":" + t[5], "YYYY-MM-DD HH:mm:ss");
            var zone = "Africa/Tunis";
            var ms = moment(moment().tz(zone).format(), "YYYY-MM-DD HH:mm:ss").diff(moment(day, "YYYY-MM-DD HH:mm:ss"));
            var d = moment.duration(ms);
            if (Math.floor(moment.utc(ms).format("m")) + Math.floor(d.asHours()) * 60 > 15) {
//                console.log(Math.floor(moment.utc(ms).format("m")) + Math.floor(d.asHours()) * 60);
                convertArray[i + 4 ] = Offline;

            }
            else if (Math.floor(moment.utc(ms).format("m")) + Math.floor(d.asHours()) * 60 < 15) {
                convertArray[i + 4 ] = Online;
            }

        }
        var contentString = '<div id="content">' +
                '<div id="siteNotice">' +
                '</div>' +
                '<h3 id="firstHeading" class="firstHeading">' + convertArray[i + 2]
                + '</h3>' +
                '<div id="bodyContent">' +
                '<b>IMEI: </b>' + convertArray[i + 3] + '</br>' +
                '<b>' + Status + ': </b>' + convertArray[i + 4 ] + '</br>' +
                '<b>ACC: </b>' + convertArray[i + 5] + '</br>' +
                '<b>' + Lastupdate + ': </b>' + convertArray[i + 6] + '</br>' +
                '<b>' + Speed + ': </b>' + convertArray[i + 7] + 'Km/h</br>' +
                '<b>Vibration ' + Status + ': </b>' + convertArray[i + 8] + '</br>' +
                '<b>SOS: </b>' + convertArray[i + 9] + '</br>'
                + stopTimeString + '</div>' +
                '</div>';
        infowindow = new google.maps.InfoWindow();
        infowindow.setContent(contentString);
        marker.set("id", convertArray[i + 3]);
        marker.set("infowindo", infowindow);
        google.maps.event.addListener(infowindow, 'closeclick', function () {
            openInfowindowImei = null;
        });
        google.maps.event.addListener(marker, 'click', (function (marker, i) {
            return function () {
                if (oldInfowindow !== undefined) {
                    oldInfowindow.close();
                }
//                alert("add listener click " + marker.get("id"));
                openInfowindowImei = marker.get("id");
                oldInfowindow = marker.get("infowindo");
                marker.get("infowindo").open(googlemap, marker);
            };
        })(marker, i));
//        alert(openInfowindowImei+"/"+convertArray[i + 3]);
        if (openInfowindowImei !== null && openInfowindowImei === convertArray[i + 3]) {
//            alert("open" + convertArray[i + 3]);
            if (oldInfowindow !== undefined) {
                oldInfowindow.close();
            }
            infowindow.open(googlemap, marker);
            oldInfowindow = infowindow;
        }


    }
//    end for all markers 



}






//delete old markers
function reloadMarkers() {

// Loop through markers and set map to null for each

    for (var i = 0; i < googlemapMarkers.length; i++) {

        googlemapMarkers[i].setMap(null);
    }

// Reset the markers array
    googlemapMarkers = [];
    // Call set markers to re-add markers
    setMarkers();
}
function realTimeTrackingInitialize() {
    realTimePath = [];

    var iConvertArrayRealTime = document.getElementById('allMaps:arrayRealTime').value.split("?");
//        console.log(iConvertArrayRealTime = document.getElementById('allMaps:arrayRealTime').value.split("?"));

    if (iConvertArrayRealTime.length > 11) {


        for (var c = 0; c < iConvertArrayRealTime.length - 12; c = c + 2) {

            realTimePath.push(new google.maps.LatLng(parseFloat(iConvertArrayRealTime[0]), parseFloat(iConvertArrayRealTime[1])));
            iConvertArrayRealTime.splice(0, 1);
            iConvertArrayRealTime.splice(0, 1);
        }

        realTimePath.push(new google.maps.LatLng(iConvertArrayRealTime[0], iConvertArrayRealTime[1]));
//        iConvertArrayRealTime.splice(0, 1);
//        iConvertArrayRealTime.splice(0, 1);




        polylineRealTime = new google.maps.Polyline({
            path: realTimePath,
            strokeColor: '#00FF00',
            strokeOpacity: 1.0,
            strokeWeight: 5
        });

        polylineRealTime.setMap(null);


        var Online;
        var Offline;
        var On;
        var Activation;
        var Deactivation;
        var Off;

        if (language === 'fr') {

            Online = "En ligne";
            Offline = "Hors ligne";
            On = "Activer";
            Activation = "En activation";
            Deactivation = "En Désactivation";
            Off = "Désactiver";
        }
        else if (language === 'en') {
            Online = "Online";
            Offline = "Offline";
            On = "On";
            Activation = "Activation";
            Deactivation = "Deactivation";
            Off = "Off";
        }

        var j = 0;





//status
        if (iConvertArrayRealTime[j + 4] === 1) {
           

            iConvertArrayRealTime[j + 4] = Online;
        }
        if (iConvertArrayRealTime[j + 4] !== 1) {


            iConvertArrayRealTime[j + 4] = Offline;
        }

//ACC STATUS
        if (iConvertArrayRealTime[j + 5] === '0') {
            iConvertArrayRealTime[j + 5] = On;
        }
        if (iConvertArrayRealTime[j + 5] === '1') {
            iConvertArrayRealTime[j + 5] = Activation;
        }
        if (iConvertArrayRealTime[j + 5] === '2') {
            iConvertArrayRealTime[j + 5] = Deactivation;
        }
        if (iConvertArrayRealTime[j + 5] === '3') {
            iConvertArrayRealTime[j + 5] = Off;
        }
        if (iConvertArrayRealTime[j + 5] === '4') {
            iConvertArrayRealTime[j + 5] = Deactivation;
        }

//VIBRATION STATUS
        if (iConvertArrayRealTime[j + 8] === 1) {
            iConvertArrayRealTime[j + 8] = On;
        }
        if (iConvertArrayRealTime[j + 8] !== 1) {
            iConvertArrayRealTime[j + 8] = Off;
        }
//SOS BUTTON                 
        if (iConvertArrayRealTime[j + 9] === 1) {
            iConvertArrayRealTime[j + 9] = On;
        }
        if (iConvertArrayRealTime[j + 9] !== 1) {
            iConvertArrayRealTime[j + 9] = Off;
        }






        if (iConvertArrayRealTime.length > 1) {

            mapOptionsRealTime = {
                center: {lat: 35.350415, lng: 13.687884},
                zoom: 6,
                mapTypeControl: true,
                mapTypeControlOptions: {
                    style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
                    position: google.maps.ControlPosition.LEFT_TOP
                },
                zoomControl: true,
                zoomControlOptions: {
                    position: google.maps.ControlPosition.LEFT_CENTER
                },
                streetViewControl: true,
                streetViewControlOptions: {
                    position: google.maps.ControlPosition.LEFT_TOP
                }
            };



            googlemapRealTime = new google.maps.Map(document.getElementById('realTimeMaps'),
                    mapOptionsRealTime);
            googlemapRealTime.setZoom(18);
            googlemapRealTime.panTo(new google.maps.LatLng(parseFloat(iConvertArrayRealTime[0]), parseFloat(iConvertArrayRealTime[ 1])));
            polylineRealTime.setMap(googlemapRealTime);

        }


        if (iConvertArrayRealTime.length > 9) {

            myLatLngRealTime = new google.maps.LatLng(parseFloat(iConvertArrayRealTime[0]), parseFloat(iConvertArrayRealTime[1]));


            var s = '';

            switch (iConvertArrayRealTime[ 10]) {
                case '1':
                    s = '../resources/images/smallCar?.png';
                    break;
                case '2':
                    s = '../resources/images/bigCar?.png';
                    break;
                case '3':
                    s = '../resources/images/smallTruck?.png';
                    break;
                case '4':

                    s = '../resources/images/bigTruck?.png';
                    break;
                case '5':
                    s = '../resources/images/smallBus?.png';
                    break;
                case '6':
                    s = '../resources/images/bigBus?.png';
                    break;
                case '7':
                    s = '../resources/images/armored?.png';
                    break;
                case '8':
                    s = '../resources/images/moto?.png';
                    break;
                case '8':
                    s = '../resources/images/ship?.png';
                    break;

                default:
                    s = '../resources/images/undefined?.png';
            }


            if (iConvertArrayRealTime[ 7] > 1 && iConvertArrayRealTime[7] !== null) {
                s = s.replace("?", "Move");
                imagemovingRealTime = {
                    url: s,
                    // This marker is 20 pixels wide by 32 pixels tall.
                    size: new google.maps.Size(35, 34),
                    // The origin for this image is 0,0.
                    origin: new google.maps.Point(0, 0),
                    // The anchor for this image is the base of the flagpole at 0,32.
                    anchor: new google.maps.Point(17, 34)
                };
            } else {
                s = s.replace("?", "Stop");
                imagemovingRealTime = {
                    url: s,
                    // This marker is 20 pixels wide by 32 pixels tall.
                    size: new google.maps.Size(35, 34),
                    // The origin for this image is 0,0.
                    origin: new google.maps.Point(0, 0),
                    // The anchor for this image is the base of the flagpole at 0,32.
                    anchor: new google.maps.Point(17, 34)
                };
            }

            shape = {
                coords: [1, 1, 1, 20, 18, 20, 18, 1],
                type: 'poly'
            };
            googlemapMarkerRealTime = new google.maps.Marker({
                position: new google.maps.LatLng(parseFloat(iConvertArrayRealTime[0]), parseFloat(iConvertArrayRealTime[1])),
                draggable: false,
                optimized: false,
                map: googlemapRealTime,
                icon: imagemovingRealTime,
                shape: shape,
                title: iConvertArrayRealTime[0]
            });
            var Status;
            var Lastupdate;
            var Speed;
            var goTo;
            if (language === 'fr') {
                Status = "Statut";
                Lastupdate = "dernière verification";
                Speed = "vitesse";
                goTo = "naviger vers cette position";
            }
            else if (language === 'en') {
                Status = "Status";
                Lastupdate = "Last update";
                Speed = "Speed";
                goTo = "Go to this position";
            }
            googlemapMarkerRealTime;
            var contentString = '<div id="content">' +
                    '<div id="siteNotice">' +
                    '</div>' +
                    '<h2 id="firstHeading" class="firstHeading">' + iConvertArrayRealTime[2]
                    + '</h2>' +
                    '<div id="bodyContent">' +
                    '<b>IMEI: </b>' + iConvertArrayRealTime[3] + '</br>' +
                    '<b>' + Status + ': </b>' + iConvertArrayRealTime[4] + '</br>' +
                    '<b>ACC: </b>' + iConvertArrayRealTime[5] + '</br>' +
                    '<b>' + Lastupdate + ': </b>' + iConvertArrayRealTime[6] + '</br>' +
                    '<b>' + Speed + ': </b>' + iConvertArrayRealTime[7] + 'Km/h</br>' +
                    '<b>Vibration ' + Status + ': </b>' + iConvertArrayRealTime[8] + '</br>' +
                    '<b>SOS: </b>' + iConvertArrayRealTime[9] + '</br>' +
                    '<b>Position:<a href="http://maps.google.com/maps?q=' + parseFloat(iConvertArrayRealTime[0]) + ',' + parseFloat(iConvertArrayRealTime[1]) + '" target="_blank" </a>' + goTo + '</b>' +
                    '</div>' +
                    '</div>';

            var infowindowRealTime = new google.maps.InfoWindow({
                content: contentString});


            infowindowRealTime.open(googlemapRealTime, googlemapMarkerRealTime);

        }
    } else {


        mapOptionsRealTime = {
            center: {lat: 35.350415, lng: 13.687884},
            zoom: 6,
            mapTypeControl: true,
            mapTypeControlOptions: {
                style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
                position: google.maps.ControlPosition.LEFT_TOP
            },
            zoomControl: true,
            zoomControlOptions: {
                position: google.maps.ControlPosition.LEFT_CENTER
            },
            streetViewControl: true,
            streetViewControlOptions: {
                position: google.maps.ControlPosition.LEFT_TOP
            }
        };



        googlemapRealTime = new google.maps.Map(document.getElementById('realTimeMaps'),
                mapOptionsRealTime);
        googlemapRealTime.setZoom(6);
        googlemapRealTime.panTo(new google.maps.LatLng(33.756544, 9.417724));

    }

}

function realTimeTracking() {

    var iConvertArrayRealTime = document.getElementById('allMaps:arrayRealTime').value.split("?");

    if (iConvertArrayRealTime.length > 11) {

        if (googlemapMarkerRealTime !== undefined && polylineRealTime !== undefined)
        {
//            console.log("start here");
            googlemapMarkerRealTime.setMap(null);
            polylineRealTime.setMap(null);

//            console.log("entred here");
        }


        for (var c = 0; c < iConvertArrayRealTime.length - 12; c = c + 2) {
            realTimePath.push(new google.maps.LatLng(iConvertArrayRealTime[0], iConvertArrayRealTime[1]));
            iConvertArrayRealTime.splice(0, 1);
            iConvertArrayRealTime.splice(0, 1);

        }

//        realTimePath.push(new google.maps.LatLng(iConvertArrayRealTime[0], iConvertArrayRealTime[1]));
//        iConvertArrayRealTime.splice(0, 1);
//        iConvertArrayRealTime.splice(0, 1);

        polylineRealTime.setPath(realTimePath);
// alert(polylineRealTime.getPath().getArray().toString());

//        stringToArryConverter();
        convertArrayRealTime = "";
        stringToConvertrealtime = "";
        stringToConvertRealTime = document.getElementById('allMaps:arrayRealTime').value;
        convertArrayRealTime = stringToConvertRealTime.split("?");
        if (convertArrayRealTime.length > 11) {
            var Online;
            var Offline;
            var On;
            var Activation;
            var Deactivation;
            var Off;

            if (language === 'fr') {
                alert("here1");
                Online = "En ligne";
                Offline = "Hors ligne";
                On = "Activer";
                Activation = "En activation";
                Deactivation = "En Désactivation";
                Off = "Désactiver";


            }
            else if (language === 'en') {
                Online = "Online";
                Offline = "Offline";
                On = "On";
                Activation = "Activation";
                Deactivation = "Deactivation";
                Off = "Off";
            }
            var j = 0;



//status
            if (iConvertArrayRealTime[j + 4] === 1) {


                iConvertArrayRealTime[j + 4] = Online;
            }
            if (iConvertArrayRealTime[j + 4] !== 1) {


                iConvertArrayRealTime[j + 4] = Offline;
            }

//ACC STATUS
            if (iConvertArrayRealTime[j + 5] === '0') {
                iConvertArrayRealTime[j + 5] = On;
            }
            if (iConvertArrayRealTime[j + 5] === '1') {
                iConvertArrayRealTime[j + 5] = Activation;
            }
            if (iConvertArrayRealTime[j + 5] === '2') {
                iConvertArrayRealTime[j + 5] = Deactivation;
            }
            if (iConvertArrayRealTime[j + 5] === '3') {
                iConvertArrayRealTime[j + 5] = Off;
            }
            if (iConvertArrayRealTime[j + 5] === '4') {
                iConvertArrayRealTime[j + 5] = Deactivation;
            }

//VIBRATION STATUS
            if (iConvertArrayRealTime[j + 8] === 1) {
                iConvertArrayRealTime[j + 8] = On;
            }
            if (iConvertArrayRealTime[j + 8] !== 1) {
                iConvertArrayRealTime[j + 8] = Off;
            }
//SOS BUTTON                 
            if (iConvertArrayRealTime[j + 9] === 1) {
                iConvertArrayRealTime[j + 9] = On;
            }
            if (iConvertArrayRealTime[j + 9] !== 1) {
                iConvertArrayRealTime[j + 9] = Off;
            }





            if (iConvertArrayRealTime.length > 1) {

                mapOptionsRealTime = {
                    center: {lat: 35.350415, lng: 13.687884},
                    zoom: 6,
                    mapTypeControl: true,
                    mapTypeControlOptions: {
                        style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
                        position: google.maps.ControlPosition.LEFT_TOP
                    },
                    zoomControl: true,
                    zoomControlOptions: {
                        position: google.maps.ControlPosition.LEFT_CENTER
                    },
                    streetViewControl: true,
                    streetViewControlOptions: {
                        position: google.maps.ControlPosition.LEFT_TOP
                    }
                };



                googlemapRealTime.setZoom(18);
                googlemapRealTime.panTo(new google.maps.LatLng(parseFloat(iConvertArrayRealTime[0]), parseFloat(iConvertArrayRealTime[ 1])));


            }


            if (convertArrayRealTime.length > 9) {

                myLatLngRealTime = new google.maps.LatLng(parseFloat(iConvertArrayRealTime[0]), parseFloat(iConvertArrayRealTime[1]));


                var s = '';
                switch (iConvertArrayRealTime[ 10]) {
                    case '1':
                        s = '../resources/images/smallCar?.png';
                        break;
                    case '2':
                        s = '../resources/images/bigCar?.png';
                        break;
                    case '3':
                        s = '../resources/images/smallTruck?.png';
                        break;
                    case '4':

                        s = '../resources/images/bigTruck?.png';
                        break;
                    case '5':
                        s = '../resources/images/smallBus?.png';
                        break;
                    case '6':
                        s = '../resources/images/bigBus?.png';
                        break;
                    case '7':
                        s = '../resources/images/armored?.png';
                        break;
                    case '8':
                        s = '../resources/images/moto?.png';
                        break;
                    case '8':
                        s = '../resources/images/ship?.png';
                        break;

                    default:
                        s = '../resources/images/undefined?.png';
                }
                if (iConvertArrayRealTime[ 7] > 1 && iConvertArrayRealTime[7] !== null) {
                    s = s.replace("?", "Move");
                    imagemovingRealTime = {
                        url: s,
                        // This marker is 20 pixels wide by 32 pixels tall.
                        size: new google.maps.Size(35, 34),
                        // The origin for this image is 0,0.
                        origin: new google.maps.Point(0, 0),
                        // The anchor for this image is the base of the flagpole at 0,32.
                        anchor: new google.maps.Point(17, 34)
                    };
                } else {
                    s = s.replace("?", "Stop");
                    imagemovingRealTime = {
                        // This marker is 20 pixels wide by 32 pixels tall.
                        size: new google.maps.Size(35, 34),
                        // The origin for this image is 0,0.
                        origin: new google.maps.Point(0, 0),
                        // The anchor for this image is the base of the flagpole at 0,32.
                        anchor: new google.maps.Point(17, 34)
                    };
                }
                shape = {
                    coords: [1, 1, 1, 20, 18, 20, 18, 1],
                    type: 'poly'
                };
                googlemapMarkerRealTime = new google.maps.Marker({
                    position: new google.maps.LatLng(parseFloat(iConvertArrayRealTime[0]), parseFloat(iConvertArrayRealTime[1])),
                    draggable: false,
                    optimized: false,
                    map: googlemapRealTime,
                    icon: imagemovingRealTime,
                    shape: shape,
                    title: convertArrayRealTime[3]
                });
                var Status;
                var Lastupdate;
                var Speed;
                var goTo;
                if (language === 'fr') {
                    Status = "Statut";
                    Lastupdate = "dernière verification";
                    Speed = "vitesse";
                    goTo = "naviger vers cette position";
                }
                else if (language === 'en') {
                    Status = "Status";
                    Lastupdate = "Last update";
                    Speed = "Speed";
                    goTo = "Go to this position";
                }
//        googlemapMarkerRealTime = markerRealTime;
                var contentString = '<div id="content">' +
                        '<div id="siteNotice">' +
                        '</div>' +
                        '<h2 id="firstHeading" class="firstHeading">' + iConvertArrayRealTime[2]
                        + '</h2>' +
                        '<div id="bodyContent">' +
                        '<b>IMEI: </b>' + iConvertArrayRealTime[3] + '</br>' +
                        '<b>' + Status + ': </b>' + iConvertArrayRealTime[4] + '</br>' +
                        '<b>ACC: </b>' + iConvertArrayRealTime[5] + '</br>' +
                        '<b>' + Lastupdate + ': </b>' + iConvertArrayRealTime[6] + '</br>' +
                        '<b>' + Speed + ': </b>' + iConvertArrayRealTime[7] + 'Km/h</br>' +
                        '<b>Vibration ' + Status + ': </b>' + iConvertArrayRealTime[8] + '</br>' +
                        '<b>SOS: </b>' + iConvertArrayRealTime[9] + '</br>' +
                        '<b>Position:<a href="http://maps.google.com/maps?q=' + parseFloat(iConvertArrayRealTime[0]) + ',' + parseFloat(iConvertArrayRealTime[1]) + '" target="_blank" </a>' + goTo + '</b>' +
                        '</div>' +
                        '</div>';

                var infowindowRealTime = new google.maps.InfoWindow({
                    content: contentString});


                infowindowRealTime.open(googlemapRealTime, googlemapMarkerRealTime);






            }
        } else {

            mapOptionsRealTime = {
                center: {lat: 35.350415, lng: 13.687884},
                zoom: 6,
                mapTypeControl: true,
                mapTypeControlOptions: {
                    style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
                    position: google.maps.ControlPosition.LEFT_TOP
                },
                zoomControl: true,
                zoomControlOptions: {
                    position: google.maps.ControlPosition.LEFT_CENTER
                },
                streetViewControl: true,
                streetViewControlOptions: {
                    position: google.maps.ControlPosition.LEFT_TOP
                }
            };



            googlemapRealTime = new google.maps.Map(document.getElementById('realTimeMaps'),
                    mapOptionsRealTime);
            googlemapRealTime.setZoom(6);
            googlemapRealTime.panTo(new google.maps.LatLng(33.756544, 9.417724));

        }
    }
}

function refreshForAllMapsProcess() {

    if (document.getElementById('allMaps:usedMap').value === 'GoogleMap' && document.getElementById('allMaps:array').value.length > 0) {

        reloadMarkers();


    } else if (document.getElementById('allMaps:array').value.length > 0)
//        (document.getElementById('allMaps:usedMap').value === 'OpenStreetMap' && document.getElementById('allMaps:array').value.length > 0) 
    {


        refreshMarkerOpenstreet();

    }
    else if (document.getElementById('allMaps:usedMap').value === 'GoogleMap' && document.getElementById('allMaps:array').value.length === 0) {

        reloadMarkers();
    }
}

function  drawOverSpeed() {

    var drawingstringposition2 = '';
    var coords2;

    var ind = 0;

    var listOfListOverspeed = JSON.parse(document.getElementById('jsonOverSpeedPositions').value);

    for (var l = 0; l < listOfListOverspeed.length; l++) {
        var arrayOverSpeedList;
        coords2 = "";
        drawingstringposition2 = '';
        snappedCoordinates = [];
        arrayOverSpeedList = listOfListOverspeed[l];
        bendAndSnap();
    }







//    e.preventDefault();
    // Make AJAX request to the snapToRoadsAPI
    // with coordinates parsed from text input element.

    function bendAndSnap() {


        interpolatboolean = document.getElementById('interpolated').value;

        if (interpolatboolean == 'false') {
            prepareCoordinatePolyline(arrayOverSpeedList);

            drawSnappedPolyline(snappedCoordinates);
        }
        else {

            var st = 0;
            for (var l = 0; l < Math.floor(arrayOverSpeedList.length / 100) * 100; l = l + 100) {

                drawingstringposition2 = '';
                for (var k = 0; k < 100; k++) {


                    drawingstringposition2 = drawingstringposition2 + arrayOverSpeedList[l + k].latitude + ',' + arrayOverSpeedList[l + k].longitude + '|';


                    st++;
                }




                drawingstringposition2 = drawingstringposition2.substring(0, drawingstringposition2.length - 1);
                coords2 = parseCoordsFromQuery(drawingstringposition2);


                $.ajax({
                    type: 'GET',
                    url: 'https://roads.googleapis.com/v1/snapToRoads',
                    data: {
                        interpolate: true,
                        key: API_KEY,
                        path: coords2

                    },
                    success: function (data) {
                        $('#requestURL').html('<a target="blank" href="' +
                                this.url + '">Request URL</a>');

                        processSnapToRoadResponse(data);
                    },
                    error: function () {
                        alert("error");
                        $('#requestURL').html('<strong>That query didn\'t work :(</strong>' +
                                '<p>Try looking at the <a href="' + this.url +
                                '">Request URL</a></p>');
                        clearMap();
                    }
                });
            }



            drawingstringposition2 = '';
            for (var l = Math.floor(arrayOverSpeedList.length / 100) * 100; l < arrayOverSpeedList.length; l++) {

                drawingstringposition2 = drawingstringposition2 + arrayOverSpeedList[l].latitude + ',' + arrayOverSpeedList[l].longitude + '|';



                st++;
            }



            drawingstringposition2 = drawingstringposition2.substring(0, drawingstringposition2.length - 1);

            coords2 = parseCoordsFromQuery(drawingstringposition2);




            $.ajax({
                type: 'GET',
                url: 'https://roads.googleapis.com/v1/snapToRoads',
                data: {
                    interpolate: true,
                    key: API_KEY,
                    path: coords2
                },
                success: function (coords2) {

                    $('#requestURL').html('<a target="blank" href="' +
                            this.url + '">Request URL</a>');

                    processSnapToRoadResponse(coords2);

                    drawSnappedPolyline(snappedCoordinates);


                },
                error: function () {
                    alert("error");
                    $('#requestURL').html('<strong>That query didn\'t work :(</strong>' +
                            '<p>Try looking at the <a href="' + this.url +
                            '">Request URL</a></p>');
                    clearMap();
                }
            });



            //start marker

            //         googlemapMarkers.push(marker);
        }
    }
    // Parse the value in the input element
    // to get all coordinates
    function parseCoordsFromQuery(input) {

        var coords;
        input = decodeURIComponent(input);
        if (input.split('path=').length > 1) {
            input = decodeURIComponent(input);
            // Split on the ampersand to get all params
            var parts = input.split('&');
            // Check each part to see if it starts with 'path='
            // grabbing out the coordinates if it does
            for (var i = 0; i < parts.length; i++) {
                if (parts[i].split('path=').length > 1) {
                    coords = parts[i].split('path=')[1];
                    break;
                }
            }
        } else {
            coords = decodeURIComponent(input);
        }
        return coords;

    }

//convert data to polyline
    function prepareCoordinatePolyline(data) {
        for (var i = 0; i < data.length; i++) {
            var latlng = {
                'lat': data[i].latitude,
                'lng': data[i].longitude
            };

            snappedCoordinates.push(latlng);

        }
    }
    function processSnapToRoadResponse(data) {
        snappedCoordinates = [];

        var originalIndexes = [];
        for (var i = 0; i < data.snappedPoints.length; i++) {

            var latlng = {
                'lat': data.snappedPoints[i].location.latitude,
                'lng': data.snappedPoints[i].location.longitude
            };
            var interpolated = true;
            if (data.snappedPoints[i].originalIndex != undefined) {

                interpolated = false;
                // originalIndexes.push(ind);
                latlng.originalIndex = ind;
                ind++;
            }
            if (data.snappedPoints[i].originalIndex == undefined) {

            }

            latlng.interpolated = interpolated;
            snappedCoordinates.push(latlng);
            placeIds.push(data.snappedPoints[i].placeId);
        }




    }

// Draw the polyline for the snapToRoads API response
// Call functions to add markers and infowindows for each snapped
// point along the polyline.
    function drawSnappedPolyline(snappedCoords) {



        var snappedPolyline = new google.maps.Polyline({
            path: snappedCoords,
            strokeColor: '#FF0000',
            strokeWeight: 6,
            icons: [{
                    offset: '100%'
                }]
        });
        snappedCoords = [];
        snappedPolyline.setMap(googlemap);



        polylines.push(snappedPolyline);

    }



}


function drawfeedBackGooglemap() {

    snappedCoordinates = [];
    var infoWindowConv = document.getElementById('infoWindowFeedback').value;
    infoWindowArrayConvert = infoWindowConv.split(",");
    var ind = 0;
    googlemapinitialize();

//    alert('2');
    var feedBack = JSON.parse('{"positions":' + document.getElementById('jsonString').value + '}');
    var stoppoints = JSON.parse('{"positions":' + document.getElementById('jsonStopPoints').value + '}');


    var drowingstringposition = '';
//    var drowingstringposition2 = '';
//
//    for (var k = 150; k < 240; k++) {
//        drowingstringposition2 = feedBack.positions[k].latitude + ',' + feedBack.positions[k].longitude + '|' + drowingstringposition2;
//    }
//    drowingstringposition2 = drowingstringposition2 + feedBack.positions[k - 6].latitude + ',' + feedBack.positions[k - 6].longitude;
// 
// Symbol that gets animated along the polyline
    var lineSymbol = {
        path: google.maps.SymbolPath.CIRCLE,
        scale: 8,
        strokeColor: '#005db5',
        strokeWidth: '#005db5'
    };
//point to print 


    // Places object
    placesService = new google.maps.places.PlacesService(googlemap);
    // Reset the map to a clean state and reset all variables
    // used for displaying each request


    function clearMap() {
        // Clear the polyline
        for (var i = 0; i < polylines.length; i++) {
            polylines[i].setMap(null);
        }
        // Clear all markers
        for (var i = 0; i < markers.length; i++) {
            markers[i].setMap(null);
        }

        // Empty everything
        polylines = [];
        markers = [];
        snappedCoordinates = [];
        placeIds = [];
        infoWindows = [];
    }

    // Parse the value in the input element
    // to get all coordinates
    function parseCoordsFromQuery(input) {

        var coords;
        input = decodeURIComponent(input);
        if (input.split('path=').length > 1) {
            input = decodeURIComponent(input);
            // Split on the ampersand to get all params
            var parts = input.split('&');
            // Check each part to see if it starts with 'path='
            // grabbing out the coordinates if it does
            for (var i = 0; i < parts.length; i++) {
                if (parts[i].split('path=').length > 1) {
                    coords = parts[i].split('path=')[1];
                    break;
                }
            }
        } else {
            coords = decodeURIComponent(input);
        }

        return coords;

    }

    // Clear the map of any old data and plot the request


    clearMap();
    bendAndSnap();


//    e.preventDefault();
    // Make AJAX request to the snapToRoadsAPI
    // with coordinates parsed from text input element.

    function bendAndSnap() {


        interpolatboolean = document.getElementById('interpolated').value;
        speedArray = [];
        var infoposition;
        var st = 0;


        for (var l = 0; l < Math.floor(feedBack.positions.length / 100) * 100; l = l + 100) {

            drowingstringposition = '';
            for (var k = 0; k < 100; k++) {


                drowingstringposition = drowingstringposition + feedBack.positions[l + k].latitude + ',' + feedBack.positions[l + k].longitude + '|';
                infoposition = {time: feedBack.positions[l + k].timecreate, speed: feedBack.positions[l + k].speed};
                speedArray.push(infoposition);
                //speedArray.push(feedBack.positions[l + k].speed);
                st++;
            }


            //drowingstringposition = drowingstringposition + feedBack.positions[l + 96].latitude + ',' + feedBack.positions[l + 98].longitude;
            //drowingstringposition = drowingstringposition + '33.333' + ',' + '33.3333';



            drowingstringposition = drowingstringposition.substring(0, drowingstringposition.length - 1);
            coords = parseCoordsFromQuery(drowingstringposition);


            if (interpolatboolean == 'true') {
                $.ajax({
                    type: 'GET',
                    url: 'https://roads.googleapis.com/v1/snapToRoads',
                    data: {
                        interpolate: true,
                        key: API_KEY,
                        path: coords

                    },
                    success: function (data) {
                        $('#requestURL').html('<a target="blank" href="' +
                                this.url + '">Request URL</a>');

                        processSnapToRoadResponse(data);


                    },
                    error: function () {
                        alert("error");
                        $('#requestURL').html('<strong>That query didn\'t work :(</strong>' +
                                '<p>Try looking at the <a href="' + this.url +
                                '">Request URL</a></p>');
                        clearMap();
                    }
                });


            }







        }
        nextDrow();

        function nextDrow() {

            drowingstringposition = '';
            for (var l = Math.floor(feedBack.positions.length / 100) * 100; l < feedBack.positions.length; l++) {

                drowingstringposition = drowingstringposition + feedBack.positions[l].latitude + ',' + feedBack.positions[l].longitude + '|';
                //  speedArray.push(feedBack.positions[l].speed);
                infoposition = {time: feedBack.positions[l].timecreate, speed: feedBack.positions[l].speed};
                speedArray.push(infoposition);
                st++;
            }

            //drowingstringposition = drowingstringposition + feedBack.positions[l + 96].latitude + ',' + feedBack.positions[l + 98].longitude;
            //drowingstringposition = drowingstringposition + '33.333' + ',' + '33.3333';

            drowingstringposition = drowingstringposition.substring(0, drowingstringposition.length - 1);
//        alert("speed" + speedArray);
//        alert(st);
            coords = parseCoordsFromQuery(drowingstringposition);



            if (interpolatboolean == 'true') {
                $.ajax({
                    type: 'GET',
                    url: 'https://roads.googleapis.com/v1/snapToRoads',
                    data: {
                        interpolate: true,
                        key: API_KEY,
                        path: coords
                    },
                    success: function (data) {

                        $('#requestURL').html('<a target="blank" href="' +
                                this.url + '">Request URL</a>');

                        processSnapToRoadResponse(data);


                        drawSnappedPolyline(snappedCoordinates);
                        fitBounds(markers);

                    },
                    error: function () {
                        alert("error");
                        $('#requestURL').html('<strong>That query didn\'t work :(</strong>' +
                                '<p>Try looking at the <a href="' + this.url +
                                '">Request URL</a></p>');
                        clearMap();
                    }
                });
            }
            else {
                prepareCoordinatePolyline(feedBack.positions);
                drawSnappedPolyline(snappedCoordinates);

            }





            //start marker

            //         googlemapMarkers.push(marker);

        }

    }




//convert data to polyline
    function prepareCoordinatePolyline(data) {

        for (var i = 0; i < data.length; i++) {
            var latlng = {
                'lat': data[i].latitude,
                'lng': data[i].longitude
            };
            latlng.interpolated = false;
            snappedCoordinates.push(latlng);

        }

    }

// Parse response from snapToRoads API request
// Store all coordinates in response
// Calls functions to add markers to map for unsnapped coordinates
    function processSnapToRoadResponse(data) {


        var originalIndexes = [];
        for (var i = 0; i < data.snappedPoints.length; i++) {

            var latlng = {
                'lat': data.snappedPoints[i].location.latitude,
                'lng': data.snappedPoints[i].location.longitude
            };
            var interpolated = true;
            if (data.snappedPoints[i].originalIndex != undefined) {

                interpolated = false;
                // originalIndexes.push(ind);
                latlng.originalIndex = ind;
                ind++;
            }
            if (data.snappedPoints[i].originalIndex == undefined) {

            }

            latlng.interpolated = interpolated;
            snappedCoordinates.push(latlng);
            placeIds.push(data.snappedPoints[i].placeId);
        }




    }

// Draw the polyline for the snapToRoads API response
// Call functions to add markers and infowindows for each snapped
// point along the polyline.
    function drawSnappedPolyline(snappedCoords) {


        var snappedPolyline = new google.maps.Polyline({
            path: snappedCoords,
            strokeColor: '#3ADF00',
            strokeWeight: 6,
            icons: [{
                    offset: '100%'
                }]
        });
        snappedPolyline.setMap(googlemap);

        for (var i = 0; i < snappedPolyline.getPath().getArray().length; i++) {

            var marker = addMarker(snappedCoords[i]);
            var infoWindow = addDetailedInfoWindow(marker,
                    snappedCoords[i], i);
            infoWindows.push(infoWindow);
        }

        animateCircle(snappedPolyline);
        polylines.push(snappedPolyline);
        drawOverSpeed();
//deleted
    }
// Infowindow used for snapped points
// Makes request to Places Details API to get data about each
// Place ID.
// Requests speed limit of each location using Roads SpeedLimit API
    function addDetailedInfoWindow(marker, coords, i) {
        var content;
        if (interpolatboolean == 'true') {
            if (!(coords.interpolated)) {

                originalid = coords.originalIndex;
                speedArray2.push(speedArray[coords.originalIndex]);
            }
            else {

                speedArray2.push(speedArray[originalid]);
            }
        }
        else {
            speedArray2.push(speedArray[i]);
        }



        return marker.info;
    }

// Avoid infoWindows staying open if the pano changes

    listenForPanoChange();
// End init function

    /**
     *  latlng literal with extra properties to use with the RoadsAPI
     *  @typedef {Object} ExtendedLatLng
     *   lat:string|float
     *   lng:string|float
     *   interpolated:boolean
     *   unsnapped:boolean
     *  @return {Object}
     */

    /**
     * Add a marker to the map and check for special 'interpolated'
     * and 'unsnapped' properties to control which colour marker is used
     * @param {ExtendedLatLng} coords - Coords of where to add the marker
     * @return {!Object} marker - the marker object created
     */
    function addMarker(coords) {
        var marker = new google.maps.Marker({
            position: coords

        });
        markers.push(marker);
        return marker;
    }
    /**
     * Animate an icon along a polyline
     * @param {Object} polyline The line to animate the icon along
     */
    function animateCircle(polyline) {

        var imageStart = {
            url: '../resources/images/start.png',
            // This marker is 20 pixels wide by 32 pixels tall.
            size: new google.maps.Size(35, 34),
            // The origin for this image is 0,0.
            origin: new google.maps.Point(0, 0),
            // The anchor for this image is the base of the flagpole at 0,32.
            anchor: new google.maps.Point(17, 34)
        };
        var shape = {
            coords: [1, 1, 1, 20, 18, 20, 18, 1],
            type: 'poly'
        };

        var startMarker;
        var contentString;
        var startpoint;
        var starttime;

        if (language === 'fr') {
            startpoint = "POINT DEBUT";
            starttime = "TEMPS DEBUT";


        }
        else if (language === 'en') {
            startpoint = "START POINT";
            starttime = "START TIME";

        }

        if (feedBack.positions[0].positionid < stoppoints.positions[0].positionid)


        {


            startMarker = new google.maps.Marker({
                position: new google.maps.LatLng(feedBack.positions[0].latitude, feedBack.positions[0].longitude),
                map: googlemap,
                animation: google.maps.Animation.DROP,
                icon: imageStart,
                shape: shape,
                title: startpoint

            });



            contentString = '<div id="content">' +
                    '<div id="siteNotice">' +
                    '</div>' +
                    '<h5 id="firstHeading" class="firstHeading">' + startpoint
                    + '</h5>' +
                    '<div id="bodyContent">' +
                    '<p><b>' + starttime + ': </b>' + moment(new Date(feedBack.positions[0].timecreate)).format('YYYY/MM/DD HH:mm:ss') + '</p>' +
                    '</div>' +
                    '</div>';

        }
        else {
            startMarker = new google.maps.Marker({
                position: new google.maps.LatLng(feedBack.positions[0].latitude, feedBack.positions[0].longitude),
                map: googlemap,
                animation: google.maps.Animation.DROP,
                icon: imageStart,
                shape: shape,
                title: startpoint
            });



            contentString = '<div id="content">' +
                    '<div id="siteNotice">' +
                    '</div>' +
                    '<h5 id="firstHeading" class="firstHeading">' + startpoint
                    + '</h5>' +
                    '<div id="bodyContent">' +
                    '<p><b>' + starttime + ': </b>' + moment(new Date(stoppoints.positions[0].timecreate)).format('YYYY/MM/DD HH:mm:ss') + '</p>' +
                    '</div>' +
                    '</div>';

        }
        var infowindowstart = new google.maps.InfoWindow({
            content: contentString
        });
        google.maps.event.addListener(startMarker, 'click', function () {

            infowindowstart.open(googlemap, startMarker);
        });

        //PARKING position

        var contentStringParking = "";

        var parkingLang = 0.0;
        var parkingLat = 0.0;
        var s;
        var con = 0;
        for (s = 0; s < Math.floor(stoppoints.positions.length); s = s + 2) {
            var infowindowparking = new google.maps.InfoWindow();
            drowingstringposition = stoppoints.positions[s].latitude + ',' + stoppoints.positions[s].longitude;

            coords = '';
            coords = parseCoordsFromQuery(drowingstringposition);
//            console.log(coords);
//            function setPoints(data) {

//            parkingLang = data.snappedPoints[0].location.latitude;
//            parkingLat = data.snappedPoints[0].location.longitude;
            var imageStop = {url: '../resources/images/parking.png',
                // This marker is 20 pixels wide by 32 pixels tall.
                size: new google.maps.Size(35, 34),
                // The origin for this image is 0,0.
                origin: new google.maps.Point(0, 0),
                // The anchor for this image is the base of the flagpole at 0,32.
                anchor: new google.maps.Point(17, 34)
            };
            var shape = {
                coords: [1, 1, 1, 20, 18, 20, 18, 1],
                type: 'poly'
            };


            var parkingMarker = new google.maps.Marker({
                position: new google.maps.LatLng(stoppoints.positions[s].latitude, stoppoints.positions[s].longitude),
                map: googlemap,
                animation: google.maps.Animation.DROP,
                icon: imageStop,
                shape: shape,
                title: 'PARKING POINT'

            });
            var time = new Date(stoppoints.positions[con + 1].timecreate).getTime() - new Date(stoppoints.positions[con].timecreate).getTime();

            var start;
            var end;
            var stoptime;
            if (language === 'fr') {
                start = "Start";
                end = "TEMPS DEBUT";
                stoptime = "Temps d'arret";


            }
            else if (language === 'en') {
                start = "POINT DEBUT";
                end = "End";
                stoptime = "Stop Time";

            }
            contentStringParking = '<div id="content">' +
                    '<div id="siteNotice">' +
                    '</div>' +
                    '<h5 id="firstHeading" class="firstHeading">' + 'Parking POSITION'
                    + '</h5>' +
                    '<div id="bodyContent">' +
                    '<p><b>' + start + ': </b>' + moment(new Date(stoppoints.positions[con].timecreate)).format('YYYY/MM/DD HH:mm:ss') + '</p>' +
                    '<p><b>' + end + ': </b>' + moment(new Date(stoppoints.positions[con + 1].timecreate)).format('YYYY/MM/DD HH:mm:ss') + '</p>' +
                    '<p><b>Lat: </b>' + stoppoints.positions[con ].latitude.toFixed(3) + ",<b>Long</b>:" + stoppoints.positions[con ].longitude.toFixed(3) + '</p>' +
                    '<p><b>' + stoptime + ': </b>' + Math.floor(time / (1000 * 60 * 60)) + 'h' + Math.floor(new Date(time).getMinutes()) + 'm' + '</p>' +
                    '</div>' +
                    '</div>';

            google.maps.event.addListener(parkingMarker, 'click', (function (parkingMarker, contentStringParking, infowindowparking) {
                return function () {
                    infowindowparking.setContent(contentStringParking);
                    infowindowparking.open(googlemap, parkingMarker);
                };
            })(parkingMarker, contentStringParking, infowindowparking));
            con = con + 2;

//            }
//            data = '';
//            $.ajax({
//                type: 'GET',
//                url: 'https://roads.googleapis.com/v1/snapToRoads',
//                data: {
//                    interpolate: false,
//                    key: API_KEY, path: coords
//                },
//                success: function (data) {
//
//                    $('#requestURL').html('<a target="blank" href="' +
//                            this.url + '">Request URL</a>');
//                    console.log(data);
//                    setPoints(data);
//
//
//                },
//                error: function () {
//
//                    $('#requestURL').html('<strong>That query didn\'t work :(</strong>' +
//                            '<p>Try looking at the <a href="' + this.url +
//                            '">Request URL</a></p>');
//
//                }
//            });



        }
        //END marker    


        var endposition;
        var endtime;

        if (language === 'fr') {
            endposition = "FIN POSITION";
            endtime = "TEMPS FIN";
        }
        else if (language === 'en') {
            endposition = "END POSITION";
            endtime = "END TIME";


        }

        if (feedBack.positions[feedBack.positions.length - 1].positionid > stoppoints.positions[stoppoints.positions.length - 1].positionid) {
            var shape = {
                coords: [1, 1, 1, 20, 18, 20, 18, 1], type: 'poly'
            };
            var imageEnd = {
                url: '../resources/images/stop.png',
                // This marker is 20 pixels wide by 32 pixels tall.
                size: new google.maps.Size(35, 34),
                // The origin for this image is 0,0.
                origin: new google.maps.Point(0, 0),
                // The anchor for this image is the base of the flagpole at 0,32.
                anchor: new google.maps.Point(17, 34)
            };
            var EndMarker = new google.maps.Marker({
                position: snappedCoordinates[snappedCoordinates.length - 1],
                map: googlemap,
                animation: google.maps.Animation.DROP,
                icon: imageEnd,
                shape: shape,
                title: 'STOP POINT'

            });

            var contentString = '<div id="content">' +
                    '<div id="siteNotice">' +
                    '</div>' +
                    '<h5 id="firstHeading" class="firstHeading">' + endposition
                    + '</h5>' +
                    '<div id="bodyContent">' +
                    '<p><b>' + endtime + ': </b>' + moment(new Date(feedBack.positions[feedBack.positions.length - 1].timecreate)).format('YYYY/MM/DD HH:mm:ss') + '</p>' +
                    '</div>' +
                    '</div>';

            var infowindowstop = new google.maps.InfoWindow({
                content: contentString
            });
            google.maps.event.addListener(EndMarker, 'click', function () {
                infowindowstop.open(googlemap, EndMarker);
            });
        }
        else {
            var shape = {
                coords: [1, 1, 1, 20, 18, 20, 18, 1], type: 'poly'
            };
            var imageEnd = {
                url: '../resources/images/stop.png',
                // This marker is 20 pixels wide by 32 pixels tall.
                size: new google.maps.Size(35, 34),
                // The origin for this image is 0,0.
                origin: new google.maps.Point(0, 0),
                // The anchor for this image is the base of the flagpole at 0,32.
                anchor: new google.maps.Point(17, 34)
            }
            ;

            var EndMarker = new google.maps.Marker({
                position: snappedCoordinates[snappedCoordinates.length - 1],
                map: googlemap,
                animation: google.maps.Animation.DROP,
                icon: imageEnd,
                shape: shape,
                title: 'STOP POINT'

            });

            var contentString = '<div id="content">' +
                    '<div id="siteNotice">' +
                    '</div>' +
                    '<h5 id="firstHeading" class="firstHeading">' + endposition
                    + '</h5>' +
                    '<div id="bodyContent">' +
                    '<p><b>' + endtime + ': </b>' + moment(new Date(stoppoints.positions[stoppoints.positions.length - 1].timecreate)).format('YYYY/MM/DD HH:mm:ss') + '</p>' +
                    '</div>' +
                    '</div>';



            var infowindowstop = new google.maps.InfoWindow({
                content: contentString
            });
            google.maps.event.addListener(EndMarker, 'click', function () {
                infowindowstop.open(googlemap, EndMarker);
            });
        }
        var marker2;
        var infowindow = new google.maps.InfoWindow();
        var count = -1;
        var pa = 0;
        var pb = 1;
        var inc = 0;
        var v = 1 * speed;
        // fallback icon if the poly has no icon to animate
        lineCoordinates = polyline.getPath().getArray();

        window.setInterval(function () {

            if (stopAnimation === 0)
            {

                v = 1 * speed;
                //            alert(document.getElementById('speed').value );


                //            alert("restart");


                //     alert(lineCoordinates +"pol");

                //            alert(lineCoordinates.length );

                count = (count + 1) % (v + 1);
                var icons = polyline.get('icons') || defaultIcon;
                icons[0].offset = (count / 2) + '%';
                //polyline.set('icons', icons);

                if (count / 2 == 0) {

                    pa = pa + inc;
                    inc = 0;
//                alert("if");
                    pa++;
                    pb++;
                    while (google.maps.geometry.spherical.computeDistanceBetween(lineCoordinates[pa], lineCoordinates[pb]) < 7)
                    {

                        pb++;
                        //count = 98 * 2;
                        inc++;
                        //                    alert("inc");
                        count = 0;
                    }
                    if (google.maps.geometry.spherical.computeDistanceBetween(lineCoordinates[pa], lineCoordinates[pb]) > 20) {
                        //slow
                        v = speed * 100;
                    }
                    if (google.maps.geometry.spherical.computeDistanceBetween(lineCoordinates[pa], lineCoordinates[pb]) < 20) {
                        //fast
                        v = speed / 10;
                    }
//                alert(inc+"inc");
//                alert(pa+"pa");
//                  alert(pb+"pb");


                }
                if (pb >= lineCoordinates.length) {
                    alert("fin");
                    pa = 0;
                    pb = 1;
                    inc = 0;
                    count = 0;
                }
                //            alert("pa" + pa + "pb" + pb + "count" + count);

                var position = google.maps.geometry.spherical.interpolate(
                        lineCoordinates[pa], lineCoordinates[pb], (count / v
                        ));
                //            alert("alert1"+google.maps.geometry.spherical.interpolate(
                //                    lineCoordinates[v], lineCoordinates[v + 1], (count / 200)));
                // alert("alert2"+google.maps.geometry.spherical.interpolate(
                //                    lineCoordinates[v], lineCoordinates[v + 1], (count / 200000)));

                if (!marker2) {



                    var imageMovingCar = {
                        url: '../resources/images/movingcar.png',
                        // This marker is 20 pixels wide by 32 pixels tall.
                        size: new google.maps.Size(35, 35),
                        // The origin for this image is 0,0.
                        origin: new google.maps.Point(0, 0),
                        // The anchor for this image is the base of the flagpole at 0,32.
                        anchor: new google.maps.Point(17, 15)
                    };


                    var shape = {
                        coords: [1, 1, 1, 20, 18, 20, 18, 1],
                        type: 'poly'
                    };
                    marker2 = new google.maps.Marker({
                        position: position, map: googlemap,
                        animation: google.maps.Animation.DROP,
                        icon: imageMovingCar,
                        shape: shape,
                        title: 'CAR'

                    });


                    infowindow.setContent(
                            '<div id="content">' +
                            '<div id="siteNotice">' +
                            '</div>' +
                            '<h3 id="firstHeading" class="firstHeading">' + convertArray[2]
                            + '</h3>' +
                            '<div id="bodyContent">' +
                            '<b>IMEI: </b>' + convertArray[  3] + '</br>' +
                            '<b>' + Status + ': </b>' + convertArray[  4 ] + '</br>' +
                            '<b>ACC ' + Status + ': </b>' + convertArray[5] + '</br>' +
                            '<b>' + time + ': </b>' + moment(new Date(speedArray2[pa].time)).format('YYYY/MM/DD HH:mm:ss') + '</br>' +
                            '<b>' + Speed + ': </b>' + speedArray2[pa].speed + ' Km/h </br>' +
                            '<b>Vibration ' + Status + ': </b>' + convertArray[  8] + '</br>' +
                            '</div>' +
                            '</div>');


                    infowindow.open(googlemap, marker2);
                    marker2.addListener('click', function () {
                        infowindow.open(googlemap, marker2);
                    });
                } else {


                    marker2.setPosition(position);
                }
                var Status;
                var Lastupdate;
                var Speed;
                var time;
                if (language === 'en') {
                    Status = "Status";
                    Lastupdate = "Last update";
                    Speed = "Speed";
                    time = "Time";
                }
                else {
                    Status = "Statut";
                    Lastupdate = "dernière verification";
                    Speed = "vitesse";
                    time = "Temps";

                }

                //            alert(pa);

                infowindow.setContent(
                        '<div id="content">' +
                        '<div id="siteNotice">' +
                        '</div>' +
                        '<h3 id="firstHeading" class="firstHeading">' + infoWindowArrayConvert[1]
                        + '</h3>' +
                        '<div id="bodyContent">' +
                        '<b>IMEI: </b>' + infoWindowArrayConvert[0] + '</br>' + '<b>Status: </b>' + convertArray[4] + '</br>' + '<b>ACC status: </b>' + convertArray[5] + '</br>' +
                        '<b>' + time + ': </b>' + moment(new Date(speedArray2[pa].time)).format('YYYY/MM/DD HH:mm:ss') + '</br>' +
                        '<b>' + Speed + ': </b>' + speedArray2[pa + inc].speed + ' Km/h </br>' +
                        '<b>Vibration ' + Status + ': </b>' + convertArray[8] + '</br>' +
                        '</div>' +
                        '</div>');

            }
        }, 20);




    }

    /**
     * Fit the map bounds to the current set of markers
     * @param {Array<Object>} markers Array of all map markers
     */
    function fitBounds(markers) {
        var bounds = new google.maps.LatLngBounds;
        for (var i = 0; i < markers.length; i++) {
            bounds.extend(markers[i].getPosition());
        }
        googlemap.fitBounds(bounds);
    }
    /**
     * Uses Places library to get Place Details for a Place ID
     * @param {string} placeId The Place ID to look up
     * @param {Function} successCallback Called if request succeeds
     * @param {Function} errorCallback   Called if request fails
     */
    function placeDetailsRequest(placeId, successCallback, errorCallback) {
        var request = {
            placeId: placeId
        };
        placesService.getDetails(request, function (place, status) {
            if (status == google.maps.places.PlacesServiceStatus.OK) {
                successCallback(place);
            }
            else {
                errorCallback();
            }
        });
    }
    /**
     * Open an infowindow on either the map or the active streetview pano
     * @param {Object} infowindow Infowindow to be opened
     * @param {Object} marker Marker the infowindow is anchored to
     */
    function openInfoWindow(infowindow, marker) {

        // If streetView is visible display the infoWindow over the pano
        // and anchor to the marker
        if (googlemap.getStreetView().getVisible()) {
            infowindow.open(googlemap.getStreetView(), marker);
        }
        // Otherwise open it on the map and anchor to the marker
        else {
            infowindow.open(googlemap, marker);
        }
    }

    /**
     * Add event listener to for when the active pano changes
     */
    function listenForPanoChange() {
        var pano = googlemap.getStreetView();
        // Close all open markers when the pano changes
        google.maps.event.addListener(pano, 'position_changed', function () {
            closeAllInfoWindows(infoWindows);
        });
    }

    /**
     * Close all open infoWindows
     * @param {Array<Object>} infoWindows - all infowindow objects
     */
    function closeAllInfoWindows(infoWindows) {
        for (var i = 0; i < infoWindows.length; i++) {
            infoWindows[i].close();
        }
    }

}

function googlemapgeofence() {


    drawingManager = new google.maps.drawing.DrawingManager({
        drawingMode: google.maps.drawing.OverlayType.POLYGON,
        drawingControl: true,
        drawingControlOptions: {
            position: google.maps.ControlPosition.TOP_CENTER,
            drawingModes: [google.maps.drawing.OverlayType.POLYGON]
        },
        polygonOptions: {
            editable: true
        }
    });

    drawingManager.setMap(googlemap);
    google.maps.event.addListener(drawingManager, "overlaycomplete", function (event) {
        var newShape = event.overlay;
        newShape.type = event.type;
    });
    google.maps.event.addListener(drawingManager, "overlaycomplete", function (event) {
        overlayClickListener(event.overlay);
        geofencArrayGoogle = event.overlay.getPath().getArray();
        document.getElementById('formGeoFence:geofencezone').value = geofencArrayGoogle;

        PF('dlg1').show();


        googlemapinitialize();

    });
    function overlayClickListener(overlay) {
        google.maps.event.addListener(overlay, "mouseup", function (event) {


        });
    }
}
function  initGoogleMapPrintGeofence() {
    //    alert("init map");
    var mapOptions4 = {
        center: {lat: 33.756544, lng: 9.417724},
        scaleControl: true,
        zoom: 6,
        mapTypeControl: true,
        mapTypeControlOptions: {
            style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
            position: google.maps.ControlPosition.LEFT_TOP
        },
        zoomControl: true,
        zoomControlOptions: {
            position: google.maps.ControlPosition.LEFT_CENTER
        },
        streetViewControl: true,
        streetViewControlOptions: {
            position: google.maps.ControlPosition.LEFT_TOP
        }
    };
    var Fullscreen;
    var Exitfullscreen;

    if (language === 'fr') {
        Fullscreen = "Plein écran";
        Exitfullscreen = "Quitter";

    }
    else if (language === 'en') {
        Fullscreen = "Full screen";
        Exitfullscreen = "Exit";

    }
    googlemap4 = new google.maps.Map(document.getElementById('geofenceMap'),
            mapOptions4);
    googlemap4.controls[google.maps.ControlPosition.LEFT_TOP].push(
            new FullScreenControl(googlemap4, Fullscreen, Exitfullscreen));
}

////
function googleMapDrawNewGeofence(LatVehicle, langVehicle, nameVehicle) {

    googlemap4.setCenter({lat: parseFloat(LatVehicle), lng: parseFloat(langVehicle)});
    googlemap4.setZoom(10);

    var imagecar = {
        url: '../resources/images/moving.png',
        // This marker is 20 pixels wide by 32 pixels tall.
        size: new google.maps.Size(35, 34),
        // The origin for this image is 0,0.
        origin: new google.maps.Point(0, 0),
        // The anchor for this image is the base of the flagpole at 0,32.
        anchor: new google.maps.Point(17, 34)
    };
    var shape = {
        coords: [1, 1, 1, 20, 18, 20, 18, 1],
        type: 'poly'
    };
    var startMarker = new google.maps.Marker({
        position: new google.maps.LatLng(LatVehicle, langVehicle),
        map: googlemap4,
        animation: google.maps.Animation.DROP,
        icon: imagecar,
        shape: shape,
        title: 'Mon véhicule'

    });
    var vehicle;


    if (language === 'fr') {
        vehicle = "Voiture";

    }
    else if (language === 'en') {
        vehicle = "Vehicle";


    }
    var contentString = '<div id="content">' +
            '<div id="siteNotice">' +
            '</div>' +
            '<h5 id="firstHeading" class="firstHeading">' + 'POSITION'
            + '</h5>' +
            '<div id="bodyContent">' +
            '<p><b>' + vehicle + ': </b>' + nameVehicle + '</p>' +
            '</div>' +
            '</div>';

    var infowindowstart = new google.maps.InfoWindow({
        content: contentString
    });
    google.maps.event.addListener(startMarker, 'click', function () {

        infowindowstart.open(googlemap4, startMarker);
    });





    // Define a circle and set its editable property to true.
    circle = new google.maps.Circle({
        center: {lat: parseFloat(LatVehicle), lng: parseFloat(langVehicle)},
        radius: 6035,
        editable: true
    });
    circle.setMap(googlemap4);
    document.getElementById('geofenceZoneForm:validGeofenceZone').value = "valCir";

    drawingManager = new google.maps.drawing.DrawingManager({
        drawingMode: google.maps.drawing.OverlayType.POLYGON,
        drawingControl: true,
        drawingControlOptions: {
            position: google.maps.ControlPosition.TOP_CENTER,
            drawingModes: [google.maps.drawing.OverlayType.POLYGON]
        },
        polygonOptions: {
            editable: true
        }
    });
    var newShape;
    google.maps.event.addListener(drawingManager, "overlaycomplete", function (event) {
        newShape = event.overlay;
        newShape.type = event.type;
    });
    google.maps.event.addListener(drawingManager, "overlaycomplete", function (event) {

//        overlayClickListener(event.overlay);
        geofencArrayGoogle = event.overlay.getPath().getArray();
        document.getElementById("geofenceZoneForm:validGeofenceZone").value = 'valPoly';
        document.getElementById('geofenceZoneForm:GeofenceZone').value = geofencArrayGoogle;

//        PF('dialogInformationGeofence').show();
// PF('dialogInformationGeofence').hide();
    });
//    function overlayClickListener(overlay) {
//        google.maps.event.addListener(overlay, "mouseup", function (event) {
//
//
//        });
//    }

    // Create the DIV to hold the control and call the CenterControl()
    // constructor passing in this DIV.
    var centerControlDiv = document.createElement('div');
    var centerControl = new CenterControl(centerControlDiv, googlemap4);

    centerControlDiv.index = 1;
    googlemap4.controls[google.maps.ControlPosition.TOP_CENTER].push(centerControlDiv);
    /**
     * The CenterControl adds a control to the map that recenters the map on
     * Chicago.
     * This constructor takes the control DIV as an argument.
     * @constructor
     */
    function CenterControl(controlDiv, map) {

        // Set CSS for the control border.
        var controlUI = document.createElement('div');
        controlUI.style.backgroundColor = '#fff';
        controlUI.style.border = '2px solid #fff';
        controlUI.style.borderRadius = '3px';
        controlUI.style.boxShadow = '0 2px 6px rgba(0,0,0,.3)';
        controlUI.style.cursor = 'pointer';
        controlUI.style.marginBottom = '22px';
        controlUI.style.marginTop = '2px';

        controlUI.style.textAlign = 'center';
        controlUI.title = 'Click to recenter the map';
        controlDiv.appendChild(controlUI);

        // Set CSS for the control interior.
        var controlText = document.createElement('div');
        controlText.style.color = 'rgb(25,25,25)';
        controlText.style.fontFamily = 'Roboto,Arial,sans-serif';
        controlText.style.fontSize = '16px';
        controlText.style.lineHeight = '38px';
        controlText.style.paddingLeft = '5px';
        controlText.style.paddingRight = '5px';
        controlText.innerHTML = 'Polyghone';
        controlUI.appendChild(controlText);

        // Setup the click event listeners: simply set the map to Chicago.
        controlUI.addEventListener('click', function () {


            if (controlText.innerHTML === "Polyghone") {
                circle.setMap(null);
                drawingManager.setMap(googlemap4);
                controlText.innerHTML = "Circle";
                document.getElementById('geofenceZoneForm:validGeofenceZone').value = "error";
            }
            else {
                circle.setMap(googlemap4);

                document.getElementById('geofenceZoneForm:validGeofenceZone').value = "valCir";

                newShape.setMap(null);
                drawingManager.setMap(null);
                controlText.innerHTML = "Polyghone";
            }
        });

    }
}

function saveCircle() {

    if (document.getElementById('geofenceZoneForm:validGeofenceZone').value === "valCir") {

        document.getElementById('geofenceZoneForm:GeofenceZone').value = circle.getRadius() + "," + circle.getCenter();
        PF('geofenceDialogDateId').show();
    }
    if (document.getElementById('geofenceZoneForm:validGeofenceZone').value === "valPoly") {
        PF('geofenceDialogDateId').show();
    }

    if (document.getElementById('geofenceZoneForm:validGeofenceZone').value === "error") {
        alert('invalid Geo-fence polygone zone');

    }

}







function drawGeofenceZoneCircle(zones, name, LatVehicle, langVehicle, nameVehicle) {


    var z = zones;
    z = z.replace("(", "");
    z = z.replace(")", "");
    z = z.replace(" ", "");


    var convertPointArray = z.split(",");
//    console.log(convertPointArray[0] + "/" + convertPointArray[1] + "/" + convertPointArray[2]);
    var circleDraw = new google.maps.Circle({
        center: new google.maps.LatLng(parseFloat(convertPointArray[1]), parseFloat(convertPointArray[2])),
        radius: parseFloat(convertPointArray[0]),
        map: googlemap4,
        editable: false
    });



    var infowindow = new google.maps.InfoWindow({
        content: name});

    infowindow.setPosition({lat: parseFloat(convertPointArray[1]), lng: parseFloat(convertPointArray[2])});
    infowindow.open(googlemap4);

    googlemap4.setCenter({lat: parseFloat(convertPointArray[1]), lng: parseFloat(convertPointArray[2])});
    googlemap4.setZoom(10);


    var imagecar = {
        url: '../resources/images/moving.png',
        // This marker is 20 pixels wide by 32 pixels tall.
        size: new google.maps.Size(35, 34),
        // The origin for this image is 0,0.
        origin: new google.maps.Point(0, 0),
        // The anchor for this image is the base of the flagpole at 0,32.
        anchor: new google.maps.Point(17, 34)
    };
    var shape = {
        coords: [1, 1, 1, 20, 18, 20, 18, 1],
        type: 'poly'
    };
    var startMarker = new google.maps.Marker({
        position: new google.maps.LatLng(LatVehicle, langVehicle),
        map: googlemap4,
        animation: google.maps.Animation.DROP,
        icon: imagecar,
        shape: shape,
        title: 'Mon véhicule'

    });
    var vehicle;


    if (language === 'fr') {
        vehicle = "Voiture";

    }
    else if (language === 'en') {
        vehicle = "Vehicle";


    }
    var contentString = '<div id="content">' +
            '<div id="siteNotice">' +
            '</div>' +
            '<div id="bodyContent">' +
            '<p><b>' + vehicle + ': </b>' + nameVehicle + '</p>' +
            '</div>' +
            '</div>';

    var infowindowstart = new google.maps.InfoWindow({
        content: contentString
    });
    google.maps.event.addListener(startMarker, 'click', function () {

        infowindowstart.open(googlemap4, startMarker);
    });
}


function drawGeofenceZonePolygone(zones, name, LatVehicle, langVehicle, nameVehicle) {
    var convertZonesArray = zones.split("&&");
    //    alert('number of zone:' + (convertZonesArray.length - 1));

    var poly = new google.maps.Polyline({
        strokeColor: '#000000',
        strokeOpacity: 1.0,
        strokeWeight: 3
    });

    poly.setMap(googlemap4);

    var z = convertZonesArray[0];
    z = z.replace(/[{()}]/g, "");

    var convertPointArray = z.split(",");



    var convertPointArray = z.split(",");

    var path = poly.getPath();
    for (var j = 0; j < convertPointArray.length; j = j + 2) {


        var pLatlng = new google.maps.LatLng(convertPointArray[j], convertPointArray[j + 1]);
        path.push(pLatlng);
    }

    pLatlng = new google.maps.LatLng(convertPointArray[0], convertPointArray[1]);
    path.push(pLatlng);

    var infowindow = new google.maps.InfoWindow({
        content: name});
    infowindow.setPosition(pLatlng);
    infowindow.open(googlemap4);
    googlemap4.setCenter(pLatlng);
    googlemap4.setZoom(10);

    var imagecar = {
        url: '../resources/images/moving.png',
        // This marker is 20 pixels wide by 32 pixels tall.
        size: new google.maps.Size(35, 34),
        // The origin for this image is 0,0.
        origin: new google.maps.Point(0, 0),
        // The anchor for this image is the base of the flagpole at 0,32.
        anchor: new google.maps.Point(17, 34)
    };
    var shape = {
        coords: [1, 1, 1, 20, 18, 20, 18, 1],
        type: 'poly'
    };
    var startMarker = new google.maps.Marker({
        position: new google.maps.LatLng(LatVehicle, langVehicle),
        map: googlemap4,
        animation: google.maps.Animation.DROP,
        icon: imagecar,
        shape: shape,
        title: 'Mon véhicule'

    });
    var vehicle;


    if (language === 'fr') {
        vehicle = "Voiture";

    }
    else if (language === 'en') {
        vehicle = "Vehicle";


    }
    var contentString = '<div id="content">' +
            '<div id="siteNotice">' +
            '</div>' +
            '<h5 id="firstHeading" class="firstHeading">' + 'POSITION'
            + '</h5>' +
            '<div id="bodyContent">' +
            '<p><b>' + vehicle + ': </b>' + nameVehicle + '</p>' +
            '</div>' +
            '</div>';

    var infowindowstart = new google.maps.InfoWindow({
        content: contentString
    });
    google.maps.event.addListener(startMarker, 'click', function () {

        infowindowstart.open(googlemap4, startMarker);
    });
}



function  initgooglemaproadtomycar() { //    alert("init map");
    var mapOptions3 = {
        center: {lat: 33.756544, lng: 9.417724},
        scaleControl: true,
        zoom: 6,
        mapTypeControl: true,
        mapTypeControlOptions: {
            style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
            position: google.maps.ControlPosition.LEFT_TOP
        },
        zoomControl: true,
        zoomControlOptions: {
            position: google.maps.ControlPosition.LEFT_CENTER
        },
        streetViewControl: true,
        streetViewControlOptions: {
            position: google.maps.ControlPosition.LEFT_TOP
        }
    };
    googlemap3 = new google.maps.Map(document.getElementById('devgoto'),
            mapOptions3);

    var Fullscreen;
    var Exitfullscreen;

    if (language === 'fr') {
        Fullscreen = "Plein écran";
        Exitfullscreen = "Quitter";

    }
    else if (language === 'en') {
        Fullscreen = "Full screen";
        Exitfullscreen = "Exit";

    }
    googlemap3.controls[google.maps.ControlPosition.LEFT_TOP].push(
            new FullScreenControl(googlemap3, Fullscreen, Exitfullscreen));
    
    console.log('end init');
}
function  googlemaproadtomycar() {
    //// To add the marker to the map, call setMap();
//    marker.setMap(googlemap3);
    // Try HTML5 geolocation.
    stringToArryConverter();
    if (countRefresh == 0) {
//        alert("first refresh");
        directionsDisplay = new google.maps.DirectionsRenderer({map: googlemap3});
    }
    var destination = {lat: parseFloat(convertArray[0]), lng: parseFloat(convertArray[1])};

    if (convertArray[0] !== null || convertArray[1] !== null) {


        if (navigator.geolocation) {

            navigator.geolocation.getCurrentPosition(function (position) {
                
                var origin = {lat: position.coords.latitude, lng: position.coords.longitude};
 
                googlemap3.setCenter(new google.maps.LatLng(position.coords.latitude, position.coords.longitude));
                googlemap3.setZoom(16);

                // Set destination, origin and travel mode.
                
                console.log(destination);
                console.log(origin);
                var request = {
                    destination: destination,
                    origin: origin,
                    travelMode: google.maps.TravelMode.DRIVING
                };
 
// Pass the directions request to the directions service.
                var directionsService = new google.maps.DirectionsService();
                directionsService.route(request, function (response, status) {
//                    if (status === google.maps.DirectionsStatus.OK) {
                          
                        // Display the route on the map.
                        directionsDisplay.setOptions({preserveViewport: true});
                        directionsDisplay.setDirections(response);
                        console.log("2");
//                    }
                });
                countRefresh++;
            });
        } else {
            alert('Browser doesn t support Geolocation');
        }
        
    }
}



//delete old markers
function reloadMarkerRoadToMyCar() {

    if (countRefresh != 0) {
        markerRoadToMyCar.setMap(null);
    }
    stringToArryConverter();
    navigator.geolocation.getCurrentPosition(function (position) {
        //        alert(position.coords.latitude + "0000" + position.coords.longitude);         var myLatlng2 = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
    });
    //    alert("here out");
    var imagemoving = {
        url: '../resources/images/movingcar.png',
        // This marker is 20 pixels wide by 32 pixels tall.
        size: new google.maps.Size(35, 34),
        // The origin for this image is 0,0.
        origin: new google.maps.Point(0, 0),
        // The anchor for this image is the base of the flagpole at 0,32.
        anchor: new google.maps.Point(17, 34)
    };

    var shape = {
        coords: [1, 1, 1, 20, 18, 20, 18, 1], type: 'poly'
    };
    markerRoadToMyCar = new MarkerWithLabel({
        position: myLatlng2,
        draggable: false,
        labelAnchor: new google.maps.Point(22, 0),
        labelStyle: {opacity: 0.75},
        map: googlemap3,
        icon: imagemoving,
        shape: shape,
        title: 'my position'


    });

    //     markerRoadToMyCar = new google.maps.Marker({
//        position: myLatlng,
//        title: "My Positision"
//    });

    // To add the marker to the map, call setMap();
    markerRoadToMyCar.setMap(googlemap3);
    countRefresh++;
}





function testcontainsLocation() {
    var triangleCoords = [
        new google.maps.LatLng(40.84285, 15.59893),
        new google.maps.LatLng(10.84285, 15.59893),
        new google.maps.LatLng(18.466465, -66.118292),
        new google.maps.LatLng(32.321384, -64.75737)
    ];
    //    alert(triangleCoords);
    var bermudaTriangle = new google.maps.Polygon({
        paths: geofencArrayGoogle
    });
    //bermudaTriangle.setMap(googlemap);
    var myLatLng = new google.maps.LatLng(parseFloat(convertArray[0]), parseFloat(convertArray[1]));
    if (google.maps.geometry.poly.containsLocation(myLatLng, bermudaTriangle)) {
        alert('you are inside zone');
    } else {
        alert('worning you are out of zone');
    }
}

function drawGeofencesGoogleMap(zones, name) {


    var convertZonesArray = zones.split("&&");
    //    alert('number of zone:' + (convertZonesArray.length - 1));
    for (var j = 0; j < convertZonesArray.length - 1; j++) {
        var poly = new google.maps.Polyline({
            strokeColor: '#FF0000',
            strokeOpacity: 1.0,
            strokeWeight: 3
        });

        poly.setMap(googlemap);

        var z = convertZonesArray[j];
        z = z.replace(/[{()}]/g, "");

        var convertPointArray = z.split(",");
        for (var j = 0; j < convertPointArray.length; j = j + 2) {

            var path = poly.getPath();
            var pLatlng = new google.maps.LatLng(convertPointArray[j], convertPointArray[j + 1]);
            path.push(pLatlng);
        }
        var pLatlng = new google.maps.LatLng(convertPointArray[0], convertPointArray[1]);
        path.push(pLatlng);
        var infowindow = new google.maps.InfoWindow({
            content: name});
        infowindow.setPosition(pLatlng);
        infowindow.open(googlemap);


    }
}








          