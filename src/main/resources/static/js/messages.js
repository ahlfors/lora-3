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
            headerRow.insertCell(1).innerHTML = "<b>TMST</b>";
            headerRow.insertCell(2).innerHTML = "<b>Data</b>";
            headerRow.insertCell(3).innerHTML = "<b>FREQ</b>";
            headerRow.insertCell(4).innerHTML = "<b>UserID</b>";
            headerRow.insertCell(5).innerHTML = "<b>Size</b>";

            var foot = table.createTFoot();
            for (var i = 0; i < data.length; i++) {
                var row = foot.insertRow(i);
                row.insertCell(0).innerHTML = data[i]["uuid"].substring(0, 20);
                row.insertCell(1).innerHTML = data[i]["tmst"];
                row.insertCell(2).innerHTML = data[i]["data"];
                row.insertCell(3).innerHTML = data[i]["freq"];
                row.insertCell(4).innerHTML = data[i]["userID"];
                row.insertCell(5).innerHTML = data[i]["size"];
            }
        }
    };
    console.log();
    request.open("GET", "/rest/messages", false);
    request.send();
};