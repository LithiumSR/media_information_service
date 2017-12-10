function initializeGameStorage() {
    if (typeof (localStorage.games) == "undefined") {
        localStorage.games = "[]";
    }
    if (localStorage.games != "[]") printGameStorage();
    addEventHandlers();
}

function addGameStorage() {
    var x = document.getElementById("title").value.trim();
    if (x == "") {
        alert("Name must be filled out");
        return false;
    }

    var currentdate = new Date();
    var datetime = currentdate.getDate() + "/" +
        (currentdate.getMonth() + 1) + "/" +
        currentdate.getFullYear() + " @ " +
        currentdate.getHours() + ":" +
        currentdate.getMinutes() + ":" +
        currentdate.getSeconds();


    var u = JSON.parse(localStorage.games);
    var nextpos = u.length;
    var o = {
        title: x,
        date: datetime
    };

    u.unshift(o);
    if (u.length>5){
        u.pop();
    }
    localStorage.games = JSON.stringify(u);
    return true;
}


function printGameStorage() {
    var u = JSON.parse(localStorage.games);
    var l = u.length;
    var s = new String("<h3>Search history:</h3>");
    var i = 0;
    while (i<l) {
        s += "<div class='search'><strong>Titolo: </strong>" + "<span style='display:inline' class='title'>" + u[i].title + "</span>" + "  <strong>Time: </strong>" + u[i].date + "</div>";
        i++;

    }
    document.getElementById("gameStorage").innerHTML = s;
    return true;
}


function addEventHandlers() {
    var searchElement = document.getElementsByClassName("search");
    for (var i = 0; i < searchElement.length; i++) {
        searchElement[i].addEventListener("click", compileForm);
    }

}

function compileForm() {
    if (this.querySelector(".title").innerHTML != "--") {
        document.getElementById("title").value = this.querySelector(".title").innerHTML;
    }
}
