function getCurrentName() {
    var url = "http://localhost:8085/api/current"
    fetch(url)
        .then(response => response.text())
        .then(data => {
            document.getElementById("currentUser").innerText = data;
        })
}

function getCurrentRole() {
    var url = "http://localhost:8085/api/current-role"
    fetch(url)
        .then(response => response.text())
        .then(data => {
            if(data == "PATIENT") {

            }

        })
}

