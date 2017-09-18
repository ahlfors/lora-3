var info;

document.addEventListener("DOMContentLoaded", function(){
    info = document.getElementById("info");
});


function initMap() {
    var promise = new Promise(function (resolve, reject) {
        var request = new XMLHttpRequest;
        request.onreadystatechange = function () {
            if (request.readyState === 4) {
                if(request.status === 200) {
                    var data = JSON.parse(request.responseText);
                    info.innerHTML = JSON.stringify(data);

                    var position = {
                        "lat": data["latitude"],
                        "lng": data["longitude"]
                    };

                    resolve(position);
                }
                else reject("error");
            }
        };
        var url = window.location.protocol +"//"+ window.location.host +"/rest"+ window.location.pathname;
        request.open("GET", url);
        request.send();
    });

    promise.then(
        result => {
            var map = new google.maps.Map(document.getElementById('map'), {
                zoom: 15,
                center: result
            });
            new google.maps.Marker({
                position: result,
                map: map
            });
        },
        error => { console.log("Rejected: " + error); }
    )
}