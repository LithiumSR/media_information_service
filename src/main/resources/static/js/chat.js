var stompClient = null;

function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('conversationDiv').style.visibility
        = connected ? 'visible' : 'hidden';
    document.getElementById('response').innerHTML = '';
}

function connect() {
    if(document.getElementById('from').value.trim()=="") alert("You can't use a blank username!")
    else{
        console.log("THIS::" +document.getElementById('from').value);
        var socket = new SockJS('/websocket/chat');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function(frame) {
            setConnected(true);
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/messages', function(messageOutput) {
                showMessageOutput(JSON.parse(messageOutput.body));
            });
            var author = document.getElementById('from').value;
            var message=author+" joined the chat";
            stompClient.send("/app/chat", {},
                JSON.stringify({'from':"*Alert*", 'text':message}));
            ;
        });
    }
}

function disconnect() {
    var author = document.getElementById('from').value;
    var message=author+" left the chat";
    stompClient.send("/app/chat", {},
        JSON.stringify({'from':"*Alert*", 'text':message}));

    if(stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("We got disconnected");
}

function sendMessage() {
    var author = document.getElementById('from').value;
    var message = document.getElementById('text').value;

    stompClient.send("/app/chat", {},
        JSON.stringify({'from':author, 'text':message}));

    stompClient.send("/app/chat_check", {},
        JSON.stringify({'from':author, 'text':message}));
}

function showMessageOutput(messageOutput) {
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    if (messageOutput.text!="") {
        p.appendChild(document.createTextNode(messageOutput.from + ": " + messageOutput.text + " (" + messageOutput.time + ")"));
        response.appendChild(p)
    };
}