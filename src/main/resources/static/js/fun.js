function bookValidate() {
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
}

function filmValidate() {
    x = document.getElementById("title").value.trim();
    y = document.getElementById("lang").value.trim();
    if (x == "") {
        alert("Name must be filled out");
        return false;
    }
    if (y.length == 1) {
        alert("Language code is too short");
        return false;
    }
    return true
}

function musicValidate() {
    x = document.getElementById("title").value.trim();
    y = document.getElementById("artist").value.trim();
    if (x == "" && y == "") {
        alert("Name and artist can't be both left blank");
        return false;
    }
}

function gameValidate() {
    x = document.getElementById("title").value.trim();
    if (x == "") {
        alert("Name must be filled out");
        return false;
    }
}