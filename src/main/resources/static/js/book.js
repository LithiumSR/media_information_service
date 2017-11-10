function initializeBookStorage() {
    if (typeof(localStorage.books) == "undefined") {
        localStorage.books = "[]";
    }
    if (localStorage.books!="[]")printBookStorage();
}

    function addBookStorage() {
        x = document.getElementById("title").value.trim();
        y = document.getElementById("isbn").value.trim();
        if (x == "" && y == "") {
            alert("Title and ISBN can't be both left blank");
            return false;
        }
        if (x == "" && y != "") {
            if (isNaN(y)) {
                alert("ISBN must be a number");
                return false;
            }
            if (y.length != 13) {
                alert("ISBN is too short");
                return false;
            }
        }

        if (y=="") y= "--"
        if (x=="") x= "--"
        var currentdate = new Date();
        var datetime = currentdate.getDate() + "/"
            + (currentdate.getMonth()+1)  + "/"
            + currentdate.getFullYear() + " @ "
            + currentdate.getHours() + ":"
            + currentdate.getMinutes() + ":"
            + currentdate.getSeconds();


        var u = JSON.parse(localStorage.books);
        var nextpos = u.length;
        var o = { title:x,
            isbn:y,date:datetime};

        u[nextpos] = o;
        localStorage.books = JSON.stringify(u);
        return true;
    }


    function printBookStorage(){
        var u = JSON.parse(localStorage.books);
        var l = u.length;
        var s = new String("<h3>Recent searches:</h3>");
        var i=l-1;
        while(i>=0){
            if(i!=l-1) s+="<br>"
            s += "<strong>Titolo: </strong>" + u[i].title + "  <strong>ISBN:  </strong>" + u[i].isbn+ "  <strong>Time:  </strong>" +u[i].date;
            i--;
        }
        alert(s)
        document.getElementById("bookStorage").innerHTML = s;
        return true;
    }



