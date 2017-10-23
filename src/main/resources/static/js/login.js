document.addEventListener("DOMContentLoaded", function(){
    document.getElementById("login-button").addEventListener("click", login);
});

var login = function(){
    var username = document.getElementById("username").value.toString();
    var password = document.getElementById("password").value.toString();

    var data = new FormData();
    data.append("grant_type","password");
    data.append("username", username);
    data.append("password", password);
    data.append("client_id", "client");
    data.append("client_secret", "secret");

    var request = new XMLHttpRequest;
    request.onload = function() {
        if (request.readyState === 4 && request.status === 200){
            var res = JSON.parse(request.responseText);
            printResult(res["token_type"] + ": " + res["access_token"] + "<br>" + "expires in: " + res["expires_in"]);
            setTimeout(function () {
                window.location.href = "/devices?access_token=" + res["access_token"];
            }, 1000);
        }
        else {
            printResult(JSON.parse(request.responseText)["message"]);
        }
    };
    request.open("POST", "/oauth/token");
    request.send(data);
};