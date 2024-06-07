var address;
var brokerURL = `ws://${address}:8080/gs-guide-websocket`;
const stompClient = new StompJs.Client({
     brokerURL: brokerURL
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/results', (greeting) => {
            ListenServer(greeting);
    });
};


function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function callOnConnect() {
    stompClient.onConnect();
    Reboot(0,0,1);
}


function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());
});

function handleFormSubmit(event) {
    event.preventDefault();
    var query = document.getElementById('searchInput').value;
    sendQueryToServer(query);
}

function ListenServer(greeting) {
    if(greeting != null) console.log(greeting.body)
    else console.log("none")
}

function sendQueryToServer(query) {
    console.log(address)
    stompClient.publish({
        destination: "/app/shop",
        body: JSON.stringify({'name': query})
    });
}