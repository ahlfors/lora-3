document.addEventListener("DOMContentLoaded", function () {
    buildTable();
    document.getElementById("addemail").addEventListener("click", addEMail);
});

var buildTable = function () {
    document.getElementById("table").innerHTML = '';
    var request = new XMLHttpRequest;
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            var data = JSON.parse(request.responseText);
            var table = document.getElementById("table");
            var header = table.createTHead();
            var headerRow = header.insertRow(0);
            headerRow.insertCell(0).innerHTML = "<b>E-Mail</b>";

            var foot = table.createTFoot();
            for (var i = 0; i < data.length; i++) {
                var row = foot.insertRow(i);
                row.insertCell(0).innerHTML = data[i];
            }
        }
    };
    request.open("GET", "/user/emails");
    request.send();
};

var addEMail = function(){
    var email = document.getElementById("email").value.toString();
    if(/^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(email)){
        var data = new FormData();
        var request = new XMLHttpRequest;
        data.append("email", email);
        request.open("POST", "/user/addemail", false);
        request.send(data);
        buildTable();
        document.getElementById("reg-text-result").innerHTML = "";
    }
    else document.getElementById("reg-text-result").innerHTML = "enter valid e-mail!!!";
};