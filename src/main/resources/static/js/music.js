function initializeMusicStorage() {
    if (typeof (localStorage.songs) == "undefined") {
        localStorage.songs = "[]";
    }
    if (localStorage.songs != "[]") printMusicStorage();
    addEventHandlers();
}

function addMusicStorage() {
    var x = document.getElementById("title").value.trim();
    var y = document.getElementById("artist").value.trim();
    var z = document.getElementById("year").value.trim();
    if (x == "" && y == "") {
        alert("Name and artist can't be both left blank");
        return false;
    }
    if (x == "") x = "--";
    if (y == "") y = "--";
    if (z == "") z = "--";

    var currentdate = new Date();
    var datetime = currentdate.getDate() + "/" +
        (currentdate.getMonth() + 1) + "/" +
        currentdate.getFullYear() + " @ " +
        currentdate.getHours() + ":" +
        currentdate.getMinutes() + ":" +
        currentdate.getSeconds();


    var u = JSON.parse(localStorage.songs);
    var nextpos = u.length;
    var o = {
        title: x,
        artist: y,
        year: z,
        date: datetime
    };

    u.unshift(o);
    var len=u.length;
    while (len > 5) {
        u.pop();
        len--;
    }
    localStorage.songs = JSON.stringify(u);
    return true;
}


function printMusicStorage() {
    var u = JSON.parse(localStorage.songs);
    var l = u.length;
    var s = String("<h3>Search history:</h3>");
    var i = 0;
    while (i < l) {
        s += "<div class='search'><strong>Title: </strong>" + "<span style='display:inline' class='title'>" + u[i].title + "</span>" + "  <strong>Artist: </strong>" + "<span style='display:inline' class='artist'>" + u[i].artist + "</span>" +
            "  <strong>Released in: </strong>" + "<span style='display:inline' class='year'>" + u[i].year + "</span>" + "<strong> Time: </strong>" + u[i].date + "</div>";
        i++;

    }
    document.getElementById("musicStorage").innerHTML = s;
    return true;
}

function addEventHandlers() {
    var searchElement = document.getElementsByClassName("search");
    for (var i = 0; i < searchElement.length; i++) {
        searchElement[i].addEventListener("click", compileForm);
    }

}

function compileForm(e) {
    if (this.querySelector(".title").innerHTML != "--") {
        document.getElementById("title").value = this.querySelector(".title").innerHTML;
    } else document.getElementById("title").value = "";

    if (this.querySelector(".artist").innerHTML != "--") {
        document.getElementById("artist").value = this.querySelector(".artist").innerHTML;
    } else document.getElementById("artist").value = "";

    if (this.querySelector(".year").innerHTML != "--") {
        document.getElementById("year").value = this.querySelector(".year").innerHTML;
    } else document.getElementById("year").value = "";
}