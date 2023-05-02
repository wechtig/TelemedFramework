//connecting to our signaling server
var conn = new WebSocket('ws://localhost:8081/socket');
var username = "";
var toUsername = "";
conn.onopen = function() {
    initialize();
};

function getCurrentName() {
    var url = "http://localhost:8081/api/current"
    return fetch(url)
        .then(response => response.text());
}

conn.onmessage = function(msg) {
    var content = JSON.parse(msg.data);
    var data = content.data;
    switch (content.event) {
        // when somebody wants to call us
        case "offer":
            handleOffer(data);
            break;
        case "answer":
            handleAnswer(data);
            break;
        // when a remote peer sends an ice candidate to us
        case "candidate":
            handleCandidate(data);
            break;
        default:
            break;
    }
};

function send(message) {
    conn.send(JSON.stringify(message));
}

var peerConnection;
var dataChannel;
var input = document.getElementById("messageInput");

function initialize() {
    getCurrentName().then(name => {
        username = name;
        document.getElementById("currentUser").innerText = username;
        console.log(username)
    });

    toUsername = document.getElementById("usernameInput").value;
    var configuration = null;

    peerConnection = new RTCPeerConnection(configuration);

    // Setup ice handling
    peerConnection.onicecandidate = function(event) {
        if (event.candidate) {
            send({
                name: username+":"+document.getElementById("usernameInput").value,
                event : "candidate",
                data : event.candidate
            });
        }
    };

    // creating data channel
    dataChannel = peerConnection.createDataChannel("dataChannel", {
        reliable : true
    });

    dataChannel.onerror = function(error) {
        console.log("Error:", error);
    };

    // when we receive a message from the other peer, printing it on the console
    dataChannel.onmessage = function(event) {
        console.log("message:", event.data);
    };

    dataChannel.onclose = function() {
        console.log("closed");
    };

    peerConnection.ondatachannel = function (event) {
        dataChannel = event.channel;
    };

}

function createOffer() {
    var toUser = document.getElementById("usernameInput").value;
    peerConnection.createOffer(function(offer) {
        send({
            name: username+":"+toUser,
            data : offer,
            type: "offer",
        });
        console.log("offeR: ", offer);
        peerConnection.setLocalDescription(offer);
    }, function(error) {
        alert("Error creating an offer");
    });

    const constraints = {
        video: true,audio : true
    };
    peerConnection.onaddstream = function(event) {
        var videoElement = document.getElementById("videostream")
        videoElement.srcObject = event.stream;
    };

    navigator.mediaDevices.getUserMedia(constraints).
    then(function(stream) {
        console.log("stream:", stream);
        peerConnection.addStream(stream); })
        .catch(function(err) { /* handle the error */ });

}

function handleOffer(offer) {
    peerConnection.setRemoteDescription(new RTCSessionDescription(offer));
    var toUser = document.getElementById("usernameInput").value;

    // create and send an answer to an offer
    peerConnection.createAnswer(function(answer) {
        peerConnection.setLocalDescription(answer);
        send({
            name: username+":"+toUser,
            event : "answer",
            data : answer
        });
    }, function(error) {
        alert("Error creating an answer");
    });

    peerConnection.onaddstream = function(event) {
        console.log("stream start");
        var videoElement = document.getElementById("videostream")
        videoElement.srcObject = event.stream;
    };

    navigator.mediaDevices.getUserMedia(constraints).
    then(function(stream) {
        console.log("stream:", stream);
        peerConnection.addStream(stream); })
        .catch(function(err) { /* handle the error */ });
    navigator.mediaDevices.getUserMedia({ video: true, audio: true })
        .then((stream) => {
            const videoElement = document.getElementById('videostream');
            videoElement.srcObject = stream;
            videoElement.play();
        })
        .catch((error) => {
            console.error('Error accessing media devices.', error);
        });
};

function handleCandidate(candidate) {
    peerConnection.addIceCandidate(new RTCIceCandidate(candidate));
    navigator.mediaDevices.getUserMedia({ video: true, audio: true })
        .then((stream) => {
            const videoElement = document.getElementById('videostream');
            videoElement.srcObject = stream;
            videoElement.play();
        })
        .catch((error) => {
            console.error('Error accessing media devices.', error);
        });
};

function handleAnswer(answer) {
    peerConnection.setRemoteDescription(new RTCSessionDescription(answer));
    peerConnection.onaddstream = function(event) {
        var videoElement = document.getElementById("videostream")
        videoElement.srcObject = event.stream;
    };

    navigator.mediaDevices.getUserMedia(constraints).
    then(function(stream) {
        peerConnection.addStream(stream); })
        .catch(function(err) { /* handle the error */ });
};