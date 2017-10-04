document.addEventListener("DOMContentLoaded", function(){
    getMessages();
});

var getMessages = function(){
    document.getElementById("table").innerHTML = '';
    var request = new XMLHttpRequest;
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            var data = JSON.parse(request.responseText);
            var table = document.getElementById("table");
            var header = table.createTHead();
            var headerRow = header.insertRow(0);
            headerRow.insertCell(0).innerHTML = "<b>UUID</b>";
            headerRow.insertCell(1).innerHTML = "<b>Lora ID</b>";
            headerRow.insertCell(2).innerHTML = "<b>Coordinates</b>";
            headerRow.insertCell(3).innerHTML = "<b>Last Used Gateway</b>";
            headerRow.insertCell(4).innerHTML = "<b>LUG Time</b>";

            var foot = table.createTFoot();
            for (var i = 0; i < data.length; i++) {
                var row = foot.insertRow(i);
                row.insertCell(0).innerHTML = `<a href="/device/${data[i]["uuid"]}">${data[i]["uuid"].substring(0, 8) + "..."}</a>`;
                row.insertCell(1).innerHTML = data[i]["loraid"];
                row.insertCell(2).innerHTML = data[i]["longitude"] + ", " + data[i]["latitude"];
                row.insertCell(3).innerHTML = data[i]["lastUsedGateway"];
                row.insertCell(4).innerHTML = time(data[i]["lastUsedGatewayDate"]);
            }
        }
    };
    console.log();
    request.open("GET", "/lora/devices");
    request.send();
};

var time = function(ms){
    return new Date(ms).toString();
};