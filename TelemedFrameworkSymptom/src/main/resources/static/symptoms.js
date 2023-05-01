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