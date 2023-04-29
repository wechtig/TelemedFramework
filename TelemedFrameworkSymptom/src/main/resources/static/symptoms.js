function initSymptoms() {
    var url = "http://localhost:8082/api/symptoms-list"
    fetch(url)
        .then((response) => response.json())
        .then((data) => {
            let symptomDivs = '';
            for (var i = 0; i < data.length; i++) {
                var id = "symptom"+i;
                symptomDivs += "<div id='"+id+"' class='symptom'><h4>"+data[i]+"</h4><input type='checkbox'></div>";
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
    var symptomDivs = "<div class='symptom'><input type='text'><p></p><input checked='checked' type='checkbox'></div>";
    document.getElementById("symptoms").innerHTML += symptomDivs;
}

function save() {
    var symptomDiv = document.getElementById("symptoms");
    var symptomsListDiv = symptomDiv.getElementsByTagName("div");
    var activeSymptoms = [];

    for(var i = 0; i < symptomsListDiv.length; i++) {
        var symptom = symptomsListDiv[i];
        var text = symptom.getElementsByTagName("h4")[0].innerText;
        var checked = symptom.getElementsByTagName("input")[0].checked;

        if (checked) {
            var element = {
                "symptom": text,
                "description": "lol"
            }
            activeSymptoms.push(element);
        }

    }

    if(activeSymptoms.length > 0) {
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