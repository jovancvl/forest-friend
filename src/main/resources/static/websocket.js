if (document.readyState === "loading") {
  // Loading hasn't finished yet
  document.addEventListener("DOMContentLoaded", initialize);
} else {
  // `DOMContentLoaded` has already fired
  initialize();
}
var server_id = null;

const stompClient = new StompJs.Client({
    brokerURL: 'ws://yoviepoo.xyz/websocket_connect'
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
    //console.log("initialize started");
    server_id = document.getElementById("serverid").innerHTML;
    document.getElementById("querybutton").addEventListener("click", sendQuery);
    document.getElementById("skipbutton").addEventListener("click", skipSong);
    document.getElementById("pausebutton").addEventListener("click", pauseOrResume);
    document.getElementById("query").addEventListener("keyup", function(event) {
        event.preventDefault();
        if (event.keyCode === 13) {
            document.getElementById("querybutton").click();
        }
    });

    tablequeue = Array.from(document.getElementById("tablequeue").firstElementChild.children);
    //console.log(tablequeue);

    tablequeue.forEach(function(td){
        // puts click event listener on the i element of the button in the tablequeue
        var button = td.lastElementChild.lastElementChild.addEventListener("click", removeFromTableQueue);
    })
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

function createListElement(songName){

    tr = document.createElement("tr");
    empty_td = document.createElement("td");
    name_td = document.createElement("td");
    button_td = document.createElement("td");
    button_a = document.createElement("a");
    button_i = document.createElement("i");

    name_td.textContent = songName;
    button_i.className = "large material-icons";
    button_i.textContent = "remove";
    button_i.addEventListener("click", removeFromTableQueue);
    button_a.className = "btn-small btn-floating right pink darken-1";

    button_a.appendChild(button_i);
    button_td.appendChild(button_a);

    tr.appendChild(empty_td);
    tr.appendChild(name_td);
    tr.appendChild(button_td);

    document.getElementById("tablequeue").firstElementChild.appendChild(tr);
}

function addSong(message){
    createListElement(message.message);
}

function addPlaylist(message) {
    for (const e of message.songList) {
        createListElement(e);
    }
}

function trackStart(message){
    // realistically, should take the first song from the queue and put it as the playing song, but more accurate to take info from event
    document.getElementById("current").innerHTML = message.message;

    firstSongInTableQueue = document.getElementById("tablequeue").firstElementChild.firstElementChild;
    if (firstSongInTableQueue !== null && (firstSongInTableQueue.children[1].textContent === message.message)){
        firstSongInTableQueue.remove();

        tablebody = document.getElementById("tablequeue").firstElementChild;
        tablebody.style.counterReset = "none";
            setTimeout(() => {
                tablebody.style.counterReset = "Serial";
            }, 0);
    }

}

function trackEnd(message){
    // fired when no songs left in queue and current track ended or cleared queue
    // in any case queue is empty after this is fired
    document.getElementById("current").innerHTML = "No song playing";
    tablebody = document.getElementById("tablequeue").firstElementChild;

    while (tablebody.firstElementChild) {
        tablebody.removeChild(tablebody.firstElementChild);
    }
}

function pauseOrResume(){
    document.getElementById("current")
}

function removeFromQueue(event) {
    songName = event.target.parentElement.firstElementChild.textContent;
    console.log(songName);
    var url = "/app/controls/" + server_id + "/removeFromQueue";
    stompClient.publish({
            destination: url,
            body: JSON.stringify({'message': songName})
        });
    event.target.parentElement.remove();
}

function removeFromTableQueue(event) {
    // gets triggered from the "i" element inside the table
    tablebody = event.target.parentElement.parentElement.parentElement.parentElement;
    tr = event.target.parentElement.parentElement.parentElement;
    songName = tr.children[1].textContent;
    console.log(songName);
    var url = "/app/controls/" + server_id + "/removeFromQueue";
    stompClient.publish({
            destination: url,
            body: JSON.stringify({'message': songName})
        });
    tr.remove();
    //console.log(tablebody.firstElementChild.firstElementChild);
    tablebody.style.counterReset = "none";
    setTimeout(() => {
        tablebody.style.counterReset = "Serial";
    }, 0);
}