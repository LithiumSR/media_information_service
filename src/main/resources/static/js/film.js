function initializeFilmStorage() {
    if (typeof(localStorage.films) == "undefined") {
        localStorage.films = "[]";
    }
    if (localStorage.films!="[]") printFilmStorage();
}

    function addFilmStorage() {
        x = document.getElementById("title").value.trim();
        y = document.getElementById("lang").value.trim();
        z=document.getElementById("year").value.trim();
        if (x == "") {
            alert("Name must be filled out");
            return false;
        }
        if (y.length == 1) {
            alert("Language code is too short");
            return false;
        }

        if (x=="") x= "--"
        if (z=="") z="--"
        var currentdate = new Date();
        var datetime =currentdate.getDate() + "/"
            + (currentdate.getMonth()+1)  + "/"
            + currentdate.getFullYear() + " @ "
            + currentdate.getHours() + ":"
            + currentdate.getMinutes() + ":"
            + currentdate.getSeconds();


        var u = JSON.parse(localStorage.films);
        var nextpos = u.length;
        var o = {title:x,
            year:z,lang:y,date:datetime};

        u[nextpos] = o;
        localStorage.films = JSON.stringify(u);
        return true;
    }


    function printFilmStorage(){
        var u = JSON.parse(localStorage.films);
        console.log(u);
        var l = u.length;
        var s = new String("<h3>Recent searches:</h3>");
        var i=l-1;
        while(i>=0){
            if(i!=l-1) s+="<br>"
            s += "<strong>Titolo: </strong>" + u[i].title + "  <strong>Released in: </strong>" + u[i].year+ "  <strong>Language: </strong>"+u[i].lang +"  <strong>Time: </strong>" +u[i].date;
            i--;
        }
        document.getElementById("filmStorage").innerHTML = s;
        return true;
    }



