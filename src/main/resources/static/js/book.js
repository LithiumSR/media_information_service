function initializeBookStorage() {
	if (typeof (localStorage.books) == "undefined") {
		localStorage.books = "[]";
	}
	if (localStorage.books != "[]") printBookStorage();
	addEventHandlers();
}

function addBookStorage() {
	var x = document.getElementById("title").value.trim();
	var y = document.getElementById("isbn").value.trim();
	if (x == "" && y == "") {
		alert("Title and ISBN can't be both left blank");
		return false;
	}
	if (x == "" && y != "") {
		if (isNaN(y)) {
			alert("ISBN must be a number");
			return false;
		}
	} else if ((x == "" && y != "") || (x != "" && y != "")) {
		if (isNaN(y)) {
			alert("ISBN must be a number");
			return false;
		}
		if (y.length != 13) {
			alert("ISBN must be 13 digits long");
			return false;
		}
	}

	if (y == "") y = "--";
	if (x == "") x = "--";
	var currentdate = new Date();
	var datetime = currentdate.getDate() + "/" +
		(currentdate.getMonth() + 1) + "/" +
		currentdate.getFullYear() + " @ " +
		currentdate.getHours() + ":" +
		currentdate.getMinutes() + ":" +
		currentdate.getSeconds();


	var u = JSON.parse(localStorage.books);
	var o = {
		title: x,
		isbn: y,
		date: datetime
	};
	u.unshift(o);
	var len=u.length;
	while (len > 5) {
		u.pop();
		len--;
	}
	localStorage.books = JSON.stringify(u);
	return true;
}


function printBookStorage() {
	var u = JSON.parse(localStorage.books);
	var l = u.length;
	var s = String("<h3>Search history:</h3>");
	var i = 0;
	while (i < l) {
		s += "<div class='search'><strong>Title: </strong>" + "<span style='display:inline' class='title'>" + u[i].title + "</span>" + " <strong>ISBN:  </strong>" + "<span class='isbn' style='display:inline'>" + u[i].isbn + "</span>" +
			" <strong>Time:  </strong>" + "<span id='time' style='display:inline'>" + u[i].date + "</span>" + "</div>";
		i++;
	}
	document.getElementById("bookStorage").innerHTML = s;
	return true;
}

function addEventHandlers() {
	var searchElement = document.getElementsByClassName("search");
	for (var i = 0; i < searchElement.length; i++) {
		searchElement[i].addEventListener("click", compileForm);
	}

}


function compileForm() {

	if (this.querySelector(".title").innerHTML != "--") {
		document.getElementById("title").value = this.querySelector(".title").innerHTML;
	} else document.getElementById("title").value = "";

	if (this.querySelector(".isbn").innerHTML != "--") {
		document.getElementById("isbn").value = this.querySelector(".isbn").innerHTML;
	} else document.getElementById("isbn").value = "";

}