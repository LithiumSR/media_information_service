function validate() {
    x = document.getElementById("title").value.trim();
    y = document.getElementById("isbn").value.trim();
    if (x == "") {
        alert("Name must be filled out");
        return false;
    }
    if (y != "") {
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