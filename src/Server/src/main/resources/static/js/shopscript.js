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
    stompClient.subscribe('/topic/findAllPurchase_results', (greeting) => {
        findAllResults(greeting);
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
        alert("product not found!");
        return;
    }

    var name = document.createElement('h2').innerText;
    var etypeid = document.createElement('h3').innerText = body.etypeid;
    var price = document.createElement('h3').innerText = body.price;
    var count = document.createElement('h3').innerText = body.count;
    var archive = document.createElement('h3').innerText = body.archive;
    var description = document.createElement('h3').innerText = body.description;

    // var product = document.createElement('div');
    // product.appendChild(name);
    // product.appendChild(etypeid);
    // product.appendChild(price);
    // product.appendChild(count);
    // product.appendChild(archive);
    // product.appendChild(description);

    // document.getElementById('container').appendChild(product);
}

function BuyResults(greeting) {
    var body = JSON.parse(greeting.body)
    if(body.status == 404) return;
    if(body.status == 200) document.getElementById('product').remove();
}

function findAllResults(greeting) {
    var body = JSON.parse(greeting.body)
    if(body.status == 404) return;
    
    for(var i = 0; i < body.length; i++) {
         var purchase = document.createElement('div');
         purchase.textContent = 'This is a purchase!';
         purchase.style = 'width: 33%';
         document.getElementById('container').appendChild(purchase);
    }
}

function sendQueryToServer(query) {
    stompClient.publish({
        destination: "/app/search",
        body: JSON.stringify({'name': query})
    });
}

function sendQueryToFindAll() {
    stompClient.publish({
        destination: "/app/findAllPurchase",
        body: JSON.stringify({'status': 200})
    });
}

function Buy() {
    stompClient.publish({
        destination: "/app/buy",
        body: JSON.stringify({'status': 200, 'query': document.getElementById("name").innerText})
    });
}