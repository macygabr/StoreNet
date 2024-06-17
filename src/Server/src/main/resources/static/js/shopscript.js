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
    container.innerHTML = '';
    if(body.id == 0) {
        alert("product not found!");
        return;
    }

    var name = document.createElement('h2');
    name.id = 'name';
    name.innerText = body.name;
    var etypeid = document.createElement('div');
    etypeid.innerText = body.etypeid;
    var price = document.createElement('div');
    price.innerText = body.price;
    var count = document.createElement('div');
    count.innerText = body.count;
    var archive = document.createElement('div');
    archive.innerText = body.archive;
    var description = document.createElement('div');
    description.innerText = body.description;

    var buyButton = document.createElement('class');
    buyButton.className = 'btn btn-lg btn-primary';
    buyButton.innerText = 'Buy';
    buyButton.onclick = function() {
        Buy();
    }

    var product = document.createElement('div');
    product.appendChild(name);
    product.appendChild(etypeid);
    product.appendChild(price);
    product.appendChild(count);
    product.appendChild(archive);
    product.appendChild(description);
    product.appendChild(buyButton);
    product.style = 'padding: 10px;';

    document.getElementById('container').appendChild(product);
}

function BuyResults(greeting) {
    var body = JSON.parse(greeting.body)
    if(body.status == 404) return;
    if(body.status == 200) container.innerHTML = '';
}

function findAllResults(greeting) {
    var body = JSON.parse(greeting.body)
    if(body.status == 404) return;
    
    container.innerHTML = '';
    console.log(body)

    for(var i = 0; i < body.length; i++) {
         var id = document.createElement('h2');
         id.innerText = body[i].id;
         var electroid = document.createElement('div');
         electroid.innerText = body[i].electroid;
         var employeeid = document.createElement('div');
         employeeid.innerText = body[i].employeeid;
         var purchasedate = document.createElement('div');
         purchasedate.innerText = body[i].purchasedate;
         var timestamp = document.createElement('div');
         timestamp.innerText = body[i].timestamp;
         var typeid = document.createElement('div');
         typeid.innerText = body[i].typeid;
         var shopid = document.createElement('div');
         shopid.innerText = body[i].shopid;

         var purchase = document.createElement('div');
         purchase.appendChild(id);
         purchase.appendChild(electroid);
         purchase.appendChild(employeeid);
         purchase.appendChild(purchasedate);
         purchase.appendChild(timestamp);
         purchase.appendChild(typeid);
         purchase.appendChild(shopid);
         purchase.style = 'width: 33%; padding: 2%;';
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