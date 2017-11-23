var stompClient = null;


function initializeChatStorage() {
    console.log("Username: " + localStorage.username);
    if (typeof (localStorage.username) == "undefined") {
        localStorage.username = "[]";
    }
    if (localStorage.username != "[]") {
        var u = JSON.parse(localStorage.username);
        document.getElementById('from').value = u.name;
    }
}

function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('response').innerHTML = '';
}

function connect() {
    if (document.getElementById('from').value.trim() == "") alert("You can't use a blank username!")
    else {
        var o = {
            name: document.getElementById('from').value.trim()
        };
        localStorage.username = JSON.stringify(o);
        console.log("THIS::" + document.getElementById('from').value);
        var socket = new SockJS('/websocket/chat');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            setConnected(true);
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/messages', function (messageOutput) {
                showMessageOutput(JSON.parse(messageOutput.body));
            });
            var author = document.getElementById('from').value;
            var message = author + " joined the chat";
            stompClient.send("/app/chat", {},
                JSON.stringify({
                    'from': "*Alert*",
                    'text': message
                }));;
        });
    }
}

function disconnect() {
    initializeChatStorage()
    var author = document.getElementById('from').value;
    var message = author + " left the chat";
    stompClient.send("/app/chat", {},
        JSON.stringify({
            'from': "*Alert*",
            'text': message
        }));

    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("We got disconnected");

}

function sendMessage() {
    var author = document.getElementById('from').value;
    var message = document.getElementById('text').value;
    document.getElementById('text').value = "";

    stompClient.send("/app/chat", {},
        JSON.stringify({
            'from': author,
            'text': message
        }));

    stompClient.send("/app/chat_check", {},
        JSON.stringify({
            'from': author,
            'text': message
        }));
    if (message.trim().startsWith("!feedback ")) {
        stompClient.send("/app/chat_feedback", {},
            JSON.stringify({
                'from': author,
                'text': message
            }));
    }
}

function showMessageOutput(messageOutput) {
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    if (messageOutput.text != "") {
        p.appendChild(document.createTextNode(messageOutput.from + ": " + messageOutput.text + " (" + messageOutput.time + ")"));
        response.appendChild(p)
    };
}