var infowindow = new google.maps.InfoWindow();
var BoozePal = {
    initMap : function(data, contentText) {
        var parsedUsers = data;
        var map = new google.maps.Map(document.getElementById('map'), {
            center : {
                lat : -34.397,
                lng : 150.644
            },
            zoom : 12
        });

        function createMarker(pos, text) {
            var marker = new google.maps.Marker({
                position : pos,
                map : map
            });
            google.maps.event.addListener(marker, 'click', function() {
                infowindow.close();
                infowindow.setContent(text);
                infowindow.open(map, marker);
            });
        }

        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                var pos = {
                    lat : position.coords.latitude,
                    lng : position.coords.longitude
                };
                // Local position.
                createMarker(pos, contentText);
                map.setCenter(pos);
                for (var i = 0; i < parsedUsers.length; i++) {
                    var userPos = {
                        lat : parsedUsers[i].latitude,
                        lng : parsedUsers[i].longitude
                    };
                    createMarker(userPos, parsedUsers[i].username);
                }
            }, function() {
                handleLocationError(true, infoWindow, map.getCenter());
            });
        } else {
            geocoder.geocode({
                'address' : 'Debrecen,Hungary'
            }, function(results, status) {
                if (status == google.maps.GeocoderStatus.OK) {
                    newAddress = results[0].geometry.location;
                    var latlng = new google.maps.LatLng(parseFloat(newAddress
                            .lat()), parseFloat(newAddress.lng()));
                    gmarkers.push(new google.maps.Marker({
                        position : latlng,
                        map : map
                    }));
                }
            });
        }

        function handleLocationError(browserHasGeolocation, infoWindow, pos) {
            infoWindow.setPosition(pos);
            infoWindow
                    .setContent(browserHasGeolocation ? 'Error: The Geolocation service failed.'
                            : 'Error: Your browser doesn\'t support geolocation.');
        }
    }
}