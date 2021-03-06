var stompClient = null;


function initializeChatStorage() {
    document.getElementById("text").disabled = true;
    document.getElementById("conversationDiv").hidden = true;
    if (typeof (localStorage.username) == "undefined") {
        localStorage.username = "[]";
    }
    if (localStorage.username != "[]") {
        var u = JSON.parse(localStorage.username);
        document.getElementById("from").value = u.name;
    }
}

function setConnected(connected) {
    document.getElementById("connect").disabled = connected;
    document.getElementById("disconnect").disabled = !connected;
    document.getElementById("conversationDiv").style.visibility = connected ? "visible" : "hidden";
    document.getElementById("response").innerHTML = "";
    document.getElementById("from").disabled = connected;
    document.getElementById("text").disabled = !connected;
    document.getElementById("sendMessage").disabled = !connected;
}

function connect() {
    if (document.getElementById("from").value.trim() == "") alert("You can't use a blank username!");
    else if (document.getElementById("from").value.trim().toLocaleLowerCase() == "mis bot") alert("You can't use this username");
    else if (document.getElementById("from").value.trim().toLocaleLowerCase() == "server") alert("You can't use this username");
    else {
        var o = {
            name: document.getElementById("from").value.trim()
        };
        localStorage.username = JSON.stringify(o);
        var socket = new SockJS("/websocket/chat");
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            setConnected(true);
            stompClient.subscribe("/topic/messages", function (messageOutput) {
                showMessageOutput(JSON.parse(messageOutput.body));
            });
            welcomeMessage();
            var author = document.getElementById("from").value;
            var message = author + " joined the chat";
            stompClient.send("/app/chat", {},
                JSON.stringify({
                    "from": "Alert",
                    "text": message
                }));
        });
    }
}

function cleanInstance() {
    initializeChatStorage();
    if (stompClient != null) {
        var author = document.getElementById("from").value;
        var message = author + " left the chat";
        stompClient.send("/app/chat", {},
            JSON.stringify({
                "from": "Alert",
                "text": message
            }));

        stompClient.disconnect();
    }
    setConnected(false);

}

function sendMessage() {
    var author = document.getElementById("from").value;
    var message = document.getElementById("text").value;
    document.getElementById("text").value = "";

    stompClient.send("/app/chat", {},
        JSON.stringify({
            "from": author,
            "text": message
        }));

    stompClient.send("/app/chat_check", {},
        JSON.stringify({
            "from": author,
            "text": message
        }));
    if (message.startsWith("!feedback ")) {
        stompClient.send("/app/chat_feedback", {},
            JSON.stringify({
                "from": author,
                "text": message
            }));
    }
}

function showMessageOutput(messageOutput) {
    var response = document.getElementById("response");
    if (document.getElementById("conversationDiv").hidden == true) {
        document.getElementById("conversationDiv").hidden = false;
    }
    var p = document.createElement("p");
    if (messageOutput.from == "Alert") {
        p.setAttribute("class", "alert");
    } else if (messageOutput.from == "Server" || messageOutput.from == "MIS Bot") {
        p.setAttribute("class", "server");
    } else p.setAttribute("class", "usermsg");

    p.style.wordWrap = "break-word";
    if (messageOutput.text != "") {
        var from = "<span class='from'>" + messageOutput.from + "</span>";
        var txt = "<span class='text'>" + messageOutput.text + "</span>";
        p.innerHTML = from + ": " + txt + " (" + messageOutput.time + ")";
        response.appendChild(p);
    }
}

function sendMessageOnEnter(e) {
    if (e.keyCode == 13) {
        sendMessage();
    }
}

function loginOnEnter(e) {
    if (e.keyCode == 13) {
        connect();
    }
}

function welcomeMessage(){
    var response = document.getElementById("response");
    var p = document.createElement("p");
    p.setAttribute("class", "server");
    p.style.wordWrap = "break-word";
    var from = "<span class='from'> MIS Bot</span>";
    var txt = "<span class='text'> Thank you for joining the chat. If you don't what this chat can offer write !help.</span>";
    p.innerHTML = from + ": " + txt;
    response.appendChild(p);
    }
