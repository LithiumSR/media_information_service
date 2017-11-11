function initializeMusicStorage() {
    if (typeof(localStorage.songs) == "undefined") {
        localStorage.songs = "[]";
    }
    if (localStorage.songs!="[]") printMusicStorage();
    assegnaEventHandlers();
}

    function addMusicStorage() {
        x = document.getElementById("title").value.trim();
        y = document.getElementById("artist").value.trim();
        z=document.getElementById("year").value.trim();
        if (x == "" && y == "") {
            alert("Name and artist can't be both left blank");
            return false;
        }
        if (x=="") x="--"
        if (y=="") y="--"
        if (z=="") z="--"

        var currentdate = new Date();
        var datetime = currentdate.getDate() + "/"
            + (currentdate.getMonth()+1)  + "/"
            + currentdate.getFullYear() + " @ "
            + currentdate.getHours() + ":"
            + currentdate.getMinutes() + ":"
            + currentdate.getSeconds();


        var u = JSON.parse(localStorage.songs);
        var nextpos = u.length;
        var o = { title:x,artist:y,year:z,date:datetime};

        u[nextpos] = o;
        localStorage.songs = JSON.stringify(u);
        return true;
    }


    function printMusicStorage(){
        var u = JSON.parse(localStorage.songs);
        var l = u.length;
        var s = new String("<h3>Search history:</h3>");
        var i=l-1;
        while(i>=0){
            if(i!=l-1) s+="<br>"
            s += "<div class='search'><strong>Title: </strong>" +"<span style='display:inline' id='title'>"+u[i].title +"</span>"+ "  <strong>Artist: </strong>" +"<span style='display:inline' id='artist'>"+u[i].artist+"</span>"+
                "  <strong>Release in: </strong>"+"<span style='display:inline' id='year'>"+u[i].year+"</span>"+"<strong> Time: </strong>" +u[i].date+"</div>";
            i--;

        }
        document.getElementById("musicStorage").innerHTML = s;
        return true;
    }

function assegnaEventHandlers() {
    var searchElement = document.getElementsByClassName("search");
    for (i = 0; i < searchElement.length; i++) {
        searchElement[i].addEventListener("click",compilaForm);
    }

}

function compilaForm(e) {



}


