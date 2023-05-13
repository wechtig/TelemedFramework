function getCurrentName() {
    var url = "http://localhost:8080/api/current"
    fetch(url)
        .then(response => response.text())
        .then(data => {
            document.getElementById("currentUser").innerText = data;
        })

}

function getActiveModules() {
    var url = "http://localhost:8080/api/active-modules"
    fetch(url)
        .then(response => response.text())
        .then(data => {
            document.getElementById("currentUser").innerText = data;
        })
}