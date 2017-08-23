 function openlayermap(){
   
var osm = new ol.layer.Tile({
                source: new ol.source.OSM()
            });
              
            var bing = new ol.layer.Tile({
                source: new ol.source.BingMaps({
                    key: 'Ak-dzM4wZjSqTlzveKz5u0d4IQ4bRzVI309GxmkgSVr1ewS6iPSrOvOKhA-CJlm3',
                    imagerySet: 'Aerial'
                })
            });

            var map = new ol.Map({
                layers: [osm, bing],
                target: 'map',
                controls: ol.control.defaults().extend([
                    new ol.control.FullScreen(),
                    new ol.control.ScaleLine({
                        units: 'metric'
                    })


                ]),
                view: new ol.View({
                    center: [1070397.81, 4055477.4366930],
                    rotation: Math.PI / 100,
                    zoom: 6.5
                })
            });

            var exportPNGElement = document.getElementById('export-png');

            if ('download' in exportPNGElement) {
                exportPNGElement.addEventListener('click', function (e) {
                    map.once('postcompose', function (event) {
                        var canvas = event.context.canvas;
                        exportPNGElement.href = canvas.toDataURL('image/png');
                    });
                    map.renderSync();
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
                map.render();
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
            featureOverlay.setMap(map);
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
            map.addInteraction(modify);

            var draw; // global so we can remove it later
            function addInteraction() {
                draw = new ol.interaction.Draw({
                    features: featureOverlay.getFeatures(),
                    type: /** @type {ol.geom.GeometryType} */ (typeSelect.value)
                });
                map.addInteraction(draw);
            }

            var typeSelect = document.getElementById('type');


            /**
             * Let user change the geometry type.
             * @param {Event} e Change event.
             */
            typeSelect.onchange = function (e) {
                map.removeInteraction(draw);
                addInteraction();
            };
            addInteraction();

 } 