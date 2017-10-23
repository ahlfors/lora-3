var time = function(ms){
    return new Date(ms).toString();
};

var shapeURL = function(originURL){
    var url = new URL(window.location.href);
    var access_token = url.searchParams.get("access_token");
    return originURL + "?access_token=" + access_token;
};

var printResult = function(message){
    document.getElementById("reg-text-result").innerHTML = message;
};