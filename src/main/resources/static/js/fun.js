function validate() {
    x = document.getElementById("title").value.trim();
    y = document.getElementById("isbn").value.trim();
    if (x == "" && y=="") {
        alert("Title and ISBN can't be both filled out");
        return false;
    }
    if (x=="" && y != "") {
        if (isNaN(y)) {
            alert("ISBN must be a number");
            return false;
        }
        if (y.length != 13) {
            alert("ISBN is too short");
            return false;
        }
    }

}