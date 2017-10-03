document.addEventListener("DOMContentLoaded", function(){
    document.getElementById("regbutton").addEventListener("click", saveUser);
});

var saveUser = function(){
    var loginInput = document.getElementById("username");
    var passwordInput = document.getElementById("password");
    var nameInput = document.getElementById("name");
    var surnameInput = document.getElementById("surname");
    var emailInput= document.getElementById("email");

    if (loginInput.value.toString()==='' ||
        passwordInput.value.toString()==='' ||
        nameInput.value.toString()==='' ||
        surnameInput.value.toString()==='' ||
        emailInput.value.toString()===''){
        printResult("Fill out all fields!!!");
        return;
    }

    if (passwordInput.value.toString().length < 4){
        printResult("Password is to short!!!");
        return;
    }

    var data = new FormData();
    data.append("login", loginInput.value.toString());
    data.append("password", passwordInput.value.toString());
    data.append("name", nameInput.value.toString());
    data.append("surname", surnameInput.value.toString());
    data.append("email", emailInput.value.toString());

    var request = new XMLHttpRequest;
    request.onreadystatechange = function() {
        if (request.readyState === 4 && request.status === 200){
            printResult(request.responseText);
        }
    };
    request.open("POST", "/user/add");
    request.send(data);
};

var printResult = function(message){
    document.getElementById("reg-text-result").innerHTML = message;
};