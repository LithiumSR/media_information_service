function initializeFilmStorage() {
    if (typeof(localStorage.films) == "undefined") {
        localStorage.films = "[]";
    }
    if (localStorage.films!="[]"){
        printFilmStorage();
    }
    addEventHandlers()
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

        if (x=="") x= "--";
        if (z=="") z="--";
        if (y=="") y="--";
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
        var l = u.length;
        var s = new String("<h3>Search history:</h3>");
        var i=l-1;
        while(i>=0){
            if(i!=l-1) s+="<br>"
            s += "<div class='search'><strong>Title: </strong>"+ "<span style='display:inline' class='title'>" + u[i].title +"</span>"+ "<strong> Released in: </strong>" +"<span style='display:inline' class='year'>"+u[i].year+"</span>"+ "  <strong>Language: </strong>"+ "<span style='display:inline' class='lang'>"+u[i].lang +"</span>"+" <strong>Time: </strong>" +u[i].date+"</div>";
            i--;
        }
        document.getElementById("filmStorage").innerHTML = s;
        return true;
    }

function addEventHandlers() {
    var searchElement = document.getElementsByClassName("search");
    for (i = 0; i < searchElement.length; i++) {
        searchElement[i].addEventListener("click",compileForm);
    }

}

function compileForm() {
    if(this.querySelector(".title").innerHTML!="--"){
        document.getElementById("title").value=this.querySelector(".title").innerHTML;
    }
    else document.getElementById("title").value="";

    if(this.querySelector(".year").innerHTML!="--"){
        document.getElementById("year").value=this.querySelector(".year").innerHTML;
    }
    else document.getElementById("year").value="";

    if (this.querySelector(".lang").innerHTML!="--"){
        document.getElementById("lang").value=this.querySelector(".lang").innerHTML;
    }
    else document.getElementById("lang").value="";


}



