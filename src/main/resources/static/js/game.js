function initializeGameStorage() {
    if (typeof(localStorage.games) == "undefined") {
        localStorage.games = "[]";
    }
    if (localStorage.games!="[]") printGameStorage();
    assegnaEventHandlers()
}

    function addGameStorage() {
        x = document.getElementById("title").value.trim();
        if (x == "") {
            alert("Name must be filled out");
            return false;
        }

        var currentdate = new Date();
        var datetime = currentdate.getDate() + "/"
            + (currentdate.getMonth()+1)  + "/"
            + currentdate.getFullYear() + " @ "
            + currentdate.getHours() + ":"
            + currentdate.getMinutes() + ":"
            + currentdate.getSeconds();


        var u = JSON.parse(localStorage.games);
        var nextpos = u.length;
        var o = { title:x,date:datetime};

        u[nextpos] = o;
        localStorage.games = JSON.stringify(u);
        return true;
    }


    function printGameStorage(){
        var u = JSON.parse(localStorage.games);
        var l = u.length;
        var s = new String("<h3>Search history:</h3>");
        var i=l-1;
        while(i>=0){
            if(i!=l-1) s+="<br>";

            s += "<div class='search'><strong>Titolo: </strong>" + "<span style='display:inline' id='title'>"+u[i].title +"</span>"+"  <strong>Time: </strong>" +u[i].date;
            i--;

        }
        document.getElementById("gameStorage").innerHTML = s;
        return true;
    }


function assegnaEventHandlers() {
    var searchElement = document.getElementsByClassName("search");
    for (i = 0; i < searchElement.length; i++) {
        searchElement[i].addEventListener("click",compilaForm);
    }

}

function compilaForm(e) {
    if(e.target.id=='title') {
        document.getElementById("title").value=e.target.innerHTML;
    }
}



