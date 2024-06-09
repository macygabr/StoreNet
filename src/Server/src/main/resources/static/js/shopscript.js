var address;

var brokerURL = `ws://${address}:333/gs-guide-websocket`;
const stompClient = new StompJs.Client({
     brokerURL: brokerURL
});

stompClient.onConnect = (frame) => {
    console.log(address)
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/search_results', (greeting) => {
        SearchResults(greeting);
    });
    stompClient.subscribe('/topic/buy_results', (greeting) => {
        BuyResults(greeting);
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

function SearchResults(greeting) {
    var body = JSON.parse(greeting.body)
    if(body.id == 0) {
        document.getElementById('product').style.visibility = 'hidden';
        alert("product not found!");
        return;
    }
    document.getElementById("name").innerText = body.name;
    document.getElementById("etypeid").innerText = body.etypeid;
    document.getElementById("price").innerText = body.price;
    document.getElementById("count").innerText = body.count;
    document.getElementById("archive").innerText = body.archive;
    document.getElementById("description").innerText = body.description;
    document.getElementById('product').style.visibility = 'visible';
}

function BuyResults(greeting) {
    var body = JSON.parse(greeting.body)
    if(body.status == 404) return;
    if(body.status == 200) document.getElementById('product').style.visibility = 'hidden';
}

function sendQueryToServer(query) {
    stompClient.publish({
        destination: "/app/search",
        body: JSON.stringify({'name': query})
    });
}

function Buy() {
    stompClient.publish({
        destination: "/app/buy",
        body: JSON.stringify({'status': 200, 'query': document.getElementById("name").innerText})
    });
}