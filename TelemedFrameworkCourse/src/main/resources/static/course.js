function getCurrentName() {
    var url = "http://localhost:8083/api/current"
    fetch(url)
        .then(response => response.text())
        .then(data => {
            document.getElementById("currentUser").innerText = data;
        })
}

function getCurrentRole() {
    var url = "http://localhost:8083/api/current-role"
    fetch(url)
        .then(response => response.text())
        .then(data => {
            if(data == "PATIENT") {

            }

        })
}

function getActiveModules() {
    var url = "http://localhost:8083/api/active-modules"
    fetch(url)
        .then(response => response.text())
        .then(data => {
            var modules = data.split(",");

            var communicationElement = document.getElementById("communication");
            var symptomElement = document.getElementById("symptom");
            var courseElement = document.getElementById("course");
            var appointmentElement = document.getElementById("appointment");

            for(var i = 0; i < modules.length; i++) {
                if(modules[i] == "SYMPTOM") {
                    document.getElementById("symptom").style.display = "";
                }

                if(modules[i] == "APPOINTMENT") {
                    document.getElementById("appointment").style.display = "";
                }

                if(modules[i] == "COMMUNICATION") {
                    document.getElementById("communication").style.display = "";
                }

                if(modules[i] == "COURSE") {
                    document.getElementById("course").style.display = "";
                }
            }
        })
}
