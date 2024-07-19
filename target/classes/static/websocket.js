if (document.readyState === "loading") {
  // Loading hasn't finished yet
  document.addEventListener("DOMContentLoaded", initialize);
} else {
  // `DOMContentLoaded` has already fired
  initialize();
}
var server_id = null;

const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:12345/websocket_connect'
});

stompClient.activate();

stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
    // track start, track end, track paused, track add
    stompClient.subscribe("/controls/" + server_id + "/addSong", (response) => {
        //console.log(response);
        addSong(JSON.parse(response.body));
    });
    stompClient.subscribe("/controls/" + server_id + "/addPlaylist", (response) => {
        //console.log(response);
        addPlaylist(JSON.parse(response.body));
    });
    stompClient.subscribe("/controls/" + server_id + "/trackStart", (response) => {
        //console.log(response);
        trackStart(JSON.parse(response.body));
    });
    stompClient.subscribe("/controls/" + server_id + "/trackEnd", (response) => {
        //console.log(response);
        trackEnd(JSON.parse(response.body));
    });
    stompClient.subscribe("/controls/" + server_id + "/pauseOrResume", (response) => {
        //console.log(response);
        pauseOrResume(JSON.parse(response.body));
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
    console.log("initialize started");
    server_id = document.getElementById("serverid").innerHTML;
    document.getElementById("querybutton").addEventListener("click", sendQuery);
    document.getElementById("skipbutton").addEventListener("click", skipSong)
}

function skipSong(){
    var url = "/app/controls/" + server_id + "/skip";
    stompClient.publish({
            destination: url,
            body: JSON.stringify({'message': "skip"})
        });
}

function sendQuery(){
    var querybox = document.getElementById("query");
    var query = querybox.value;
    //console.log(query);
    var url = "/app/controls/" + server_id + "/add";
    stompClient.publish({
        destination: url,
        body: JSON.stringify({'message': query})
    });
    querybox.value = "";
}

function addSong(message){
    //console.log(message);
    li = document.createElement("li");
    text = document.createTextNode(message.message);
    li.appendChild(text);
    document.getElementById("queue").appendChild(li);
}

function addPlaylist(message) {
    for (const e of message.songList) {
        li = document.createElement("li");
        text = document.createTextNode(e);
        li.appendChild(text);
        document.getElementById("queue").appendChild(li);
    }
}

function trackStart(message){
    // realistically, should take the first song from the queue and put it as the playing song, but more accurate to take info from event
    document.getElementById("current").innerHTML = message.message;
    //console.log(document.getElementById("queue").firstElementChild)
    document.getElementById("queue").firstElementChild.remove();
}

function trackEnd(message){
    // fired when no songs left in queue and current track ended or cleared queue
    // in any case queue is empty after this is fired
    document.getElementById("current").innerHTML = "No song playing";
    document.getElementById("queue").innerHTML = "";
}

function pauseOrResume(){
    document.getElementById("current")
}