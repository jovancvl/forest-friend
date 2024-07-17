if (document.readyState === "loading") {
  // Loading hasn't finished yet
  document.addEventListener("DOMContentLoaded", initialize);
} else {
  // `DOMContentLoaded` has already fired
  initialize();
}

const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:12345/websocket_connect'
});

stompClient.activate();

stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
    var url = '/response/' + document.getElementById("serverid").innerHTML;
    stompClient.subscribe(url, (response) => {
        //console.log(response);
        showResponse(JSON.parse(response.body).query);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function initialize(){
    document.getElementById("querybutton").addEventListener("click", sendQuery);
}

function sendQuery(){
    var querybox = document.getElementById("query");
    var query = querybox.value;
    //console.log(query);
    var url = "/app/controls/" + document.getElementById("serverid").innerHTML + "/addsong";
    stompClient.publish({
        destination: url,
        body: JSON.stringify({'query': query})
    });
    querybox.value = "";
}

function showResponse(response){
    //console.log("response received: " + response);
    const para = document.createElement("p");
    const node = document.createTextNode(response);
    para.appendChild(node);
    document.getElementById("maindiv").appendChild(para);
}
