document.addEventListener("DOMContentLoaded", function(){
    document.getElementById("login-button").addEventListener("click", login);
});

var login = function(){
    var username = document.getElementById("username").value.toString();
    var password = document.getElementById("password").value.toString();

    var data = new FormData();
    data.append("username", username);
    data.append("password", password);

    var request = new XMLHttpRequest;
    request.onload = function() {
        if (request.readyState === 4 && request.status === 200){
            window.location.href = this.responseURL;
        }
        else {
            console.log(JSON.parse(request.responseText));
            printResult(JSON.parse(request.responseText)["message"]);
        }
    };
    request.open("POST", "/login");
    request.send(data);
};

var printResult = function(message){
    document.getElementById("reg-text-result").innerHTML = message;
};