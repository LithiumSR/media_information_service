function initializeMusicStorage() {
    if (typeof(localStorage.songs) == "undefined") {
        localStorage.songs = "[]";
    }
    if (localStorage.songs!="[]") printMusicStorage();
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
        var s = new String("<h3>Recent searches:</h3>");
        var i=l-1;
        console.log(i);
        while(i>=0){
            if(i!=l-1) s+="<br>"
            s += "<strong>Titolo: </strong>" + u[i].title + "  <strong>Artist: </strong>" +u[i].artist+ "  <strong>Release in: </strong>" +u[i].year+ "<strong> Time: </strong>" +u[i].date;
            i--;

        }
        document.getElementById("musicStorage").innerHTML = s;
        return true;
    }



