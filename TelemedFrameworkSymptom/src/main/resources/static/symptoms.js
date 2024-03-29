function initSymptoms() {
    var url = "http://localhost:8082/api/symptoms-list"
    fetch(url)
        .then((response) => response.json())
        .then((data) => {
            let symptomDivs = '';
            for (var i = 0; i < data.length; i++) {
                var id = "symptom"+i;

                if(data[i].active) {
                    symptomDivs += "<div id='"+id+"' class='symptom'>" +
                        "<h4>"+data[i].symptom+"</h4>" +
                        "<input class='activeCheckbox' type='checkbox' checked>" +
                        "<l>Zusatzinformationen:</l> <input class='descriptionText' type='text' value='"+data[i].description+"'>" +
                        "</div>";
                } else {
                    symptomDivs += "<div id='"+id+"' class='symptom'>" +
                        "<h4>"+data[i].symptom+"</h4>" +
                        "<input class='activeCheckbox' type='checkbox'>" +
                        "</l>Zusatzinformationen:</l> <input class='descriptionText' type='text'>" +
                        "</div>"
                }

            }
            document.getElementById("symptoms").innerHTML = symptomDivs;

        })
}

function getCurrentName() {
    var url = "http://localhost:8082/api/current"
    fetch(url)
        .then(response => response.text())
        .then(data => {
            document.getElementById("currentUser").innerText = data;
        })

    var urlLgo = "http://localhost:8082/api/logo";
    fetch(urlLgo)
        .then(response => response.text())
        .then(data => {
            var imageByteArray = data;
            document.getElementById("logo").src = "data:image/png;base64," + imageByteArray;
        })
}

function getCurrentRole() {
    var url = "http://localhost:8082/api/current-role"
    fetch(url)
        .then(response => response.text())
        .then(data => {
            if(data == "PATIENT") {
                document.getElementById("doctorDiv").style.visibility = "hidden";
                document.getElementById("doctorAdd").style.display = "none";
                document.getElementById("doctorSave").style.display = "none";

                initSymptoms();
            }

            if(data == "DOCTOR") {
                document.getElementById("symptoms").style.display = "none";
                document.getElementById("clientAdd").style.display = "none";
                document.getElementById("clientSave").style.display = "none";

                initUsernames();
            }

        })
}

function initUsernames() {
    var url = "http://localhost:8082/api/usernames"
    fetch(url)
        .then((response) => response.json())
        .then((data) => {
            let usernames = "<select id='usernameSelectBox' onChange='getSymptomsByUsername()'>";
            for (var i = 0; i < data.length; i++) {
                let username = data[i];
                usernames += '<option>' + username + '</option>';
            }
            usernames += "</select>"
            console.log(data);
            document.getElementById("usernameDiv").innerHTML = usernames;
        })
}

function getSymptomsByUsername() {
    var selectBox = document.getElementById("usernameSelectBox");
    var selectedText = selectBox.options[selectBox.selectedIndex].text;
    var url = "http://localhost:8082/api/symptoms-list/"+selectedText;
    fetch(url)
        .then((response) => response.json())
        .then((data) => {
            let symptomDivs = '';
            for (var i = 0; i < data.length; i++) {
                var id = "symptom"+i;

                if(data[i].active) {
                    symptomDivs += "<div id='"+id+"' class='symptom'>" +
                        "<h4>"+data[i].symptom+"</h4>" +
                        "<input class='activeCheckbox' type='checkbox' checked>" +
                        "<l>Zusatzinformationen:</l> <input class='descriptionText' type='text' value='"+data[i].description+"'>" +
                        "</div>";
                } else {
                    symptomDivs += "<div id='"+id+"' class='symptom'>" +
                        "<h4>"+data[i].symptom+"</h4>" +
                        "<input class='activeCheckbox' type='checkbox'>" +
                        "</l>Zusatzinformationen:</l> <input class='descriptionText' type='text'>" +
                        "</div>"
                }

            }
            document.getElementById("usernameSymptomsDiv").innerHTML = symptomDivs;
        })
}

function addSymptom() {
    var symptomDivs = "" +
        "<div class='symptom'>" +
        "<h4>Weitere Symptome</h4>" +
        "<input class='descriptionText' type='text'><p></p>" +
        "<input class='activeCheckbox' checked='checked' type='checkbox'>" +
        "</div>";
    document.getElementById("symptoms").innerHTML += symptomDivs;
}

function addSymptomDoctor() {
    var symptomDivs = "" +
        "<div class='symptom'>" +
        "<h4>Weitere Symptome</h4>" +
        "<input class='descriptionText' type='text'><p></p>" +
        "<input class='activeCheckbox' checked='checked' type='checkbox'>" +
        "</div>";
    document.getElementById("usernameSymptomsDiv").innerHTML += symptomDivs;
}

function saveDoctor() {
    var symptomDiv = document.getElementById("usernameSymptomsDiv");
    var symptomsListDiv = symptomDiv.getElementsByTagName("div");
    var activeSymptoms = [];

    var selectedUsername = document.getElementById("usernameSelectBox").value;

    for (var i = 0; i < symptomsListDiv.length; i++) {
        var symptom = symptomsListDiv[i];
        var text = symptom.getElementsByTagName("h4")[0].innerText;
        var checked = symptom.getElementsByClassName("activeCheckbox")[0].checked;
        var description = symptom.getElementsByClassName("descriptionText")[0].value;

        console.log("desc : ", description);

        var element = {
            "symptom": text,
            "description": description,
            "active": checked
        }
        activeSymptoms.push(element);
    }

    if (activeSymptoms.length > 0) {
        console.log("token: ", getCookie('XSRF-TOKEN'));
        fetch('http://localhost:8082/api/symptom-save/'+selectedUsername, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-XSRF-TOKEN': getCookie('XSRF-TOKEN')
            },
            body: JSON.stringify(activeSymptoms)
        })
            .then(response => {
                console.log(response);
            })
            .catch(error => {
                console.error(error);
            });
    }
}

function save() {
    var symptomDiv = document.getElementById("symptoms");
    var symptomsListDiv = symptomDiv.getElementsByTagName("div");
    var activeSymptoms = [];

    for (var i = 0; i < symptomsListDiv.length; i++) {
        var symptom = symptomsListDiv[i];
        var text = symptom.getElementsByTagName("h4")[0].innerText;
        var checked = symptom.getElementsByClassName("activeCheckbox")[0].checked;
        var description = symptom.getElementsByClassName("descriptionText")[0].value;

        console.log("desc : ", description);

        var element = {
            "symptom": text,
            "description": description,
            "active": checked
        }
        activeSymptoms.push(element);
    }

    if (activeSymptoms.length > 0) {
        console.log("token: ", getCookie('XSRF-TOKEN'));
        fetch('http://localhost:8082/api/symptom-save', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-XSRF-TOKEN': getCookie('XSRF-TOKEN')
            },
            body: JSON.stringify(activeSymptoms)
        })
            .then(response => {
                console.log(response);
                location.reload();

            })
            .catch(error => {
                console.error(error);
            });
    }
}

function getCookie(name) {
    const cookieValue = document.cookie
        .split(';')
        .map(cookie => cookie.trim())
        .find(cookie => cookie.startsWith(`${name}=`));

    if (!cookieValue) {
        return null;
    }

    return cookieValue.substring(name.length + 1);
}

function getActiveModules() {
    var url = "http://localhost:8082/api/active-modules"
    fetch(url)
        .then(response => response.text())
        .then(data => {
            var props = data.split(",");
            console.log(props);
            if(props[1]) {
                document.getElementById("navbarSupportedContent").style.backgroundColor = props[1].split("=")[1];
            }

            if(props[2]) {
                console.log("praxisname ");
                document.getElementById("praxisname").innerText = props[2].split("=")[1];
            }

            var propsMods1 = props[0].split("=");
            var propsMods2 = propsMods1[1].split("-");
            console.log("propsMods2 ", propsMods2);

            for(var i = 0; i < propsMods2.length; i++) {
                if(propsMods2[i] == "SYMPTOM") {
                    document.getElementById("symptom").style.display = "";
                }

                if(propsMods2[i] == "APPOINTMENT") {
                    document.getElementById("appointment").style.display = "";
                }

                if(propsMods2[i] == "COMMUNICATION") {
                    document.getElementById("communication").style.display = "";
                }

                if(propsMods2[i] == "COURSE") {
                    document.getElementById("course").style.display = "";
                }
            }
        })
}