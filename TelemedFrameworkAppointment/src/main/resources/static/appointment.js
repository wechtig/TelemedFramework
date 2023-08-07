function getCurrentName() {
    var url = "http://localhost:8085/api/current"
    fetch(url)
        .then(response => response.text())
        .then(data => {
            document.getElementById("currentUser").innerText = data;
        })

    var urlLgo = "http://localhost:8085/api/logo";
    fetch(urlLgo)
        .then(response => response.text())
        .then(data => {
            var imageByteArray = data;
            document.getElementById("logo").src = "data:image/png;base64," + imageByteArray;
        })
}

function getCurrentRole() {
    var url = "http://localhost:8085/api/current-role"
    fetch(url)
        .then(response => response.text())
        .then(data => {
            console.log(data);
            if(data == "PATIENT") {
                document.getElementById("nameDiv").style.display = "none";
                document.getElementById("btDoctor").style.display = "none";

                getOwnAppointments();
            }

            if(data == "DOCTOR") {
                document.getElementById("btPatient").style.display = "none";
                getAppointmentsForDocotor();
            }

        })
}

var appointmentsData = [];
function getAppointmentsForDocotor() {
    var url = "http://localhost:8085/api/appointments"
    fetch(url)
        .then((response) => response.json())
        .then((data) => {
            console.log("dat: ",data);
            appointmentsData = data;
            let appointmentsDoctor = '<h2>Termine</h2>';
            for (var i = 0; i < data.length; i++) {
                if(data[i].location == "ONLINE") {
                    appointmentsDoctor += "<div><l>Termin am "+data[i].date+" online mit "+data[i].fullnameDoctor+"</l>";
                } else {
                    appointmentsDoctor += "<div><l>Termin am "+data[i].date+" mit "+data[i].fullnameDoctor +". Ort: "+data[i].location+"</l>";
                }

                if(!data[i].accepted) {
                    var id = i;
                    appointmentsDoctor += "<button class='btn btn-success' onclick='terminBestaetigen("+id+")'>Best&auml;tigen</button>";
                }

                appointmentsDoctor += "</div></hr>"
            }

            document.getElementById("ownAppointments").innerHTML = appointmentsDoctor;

        })
}
function terminBestaetigen(i) {
    var appointment = appointmentsData[i];

    fetch('http://localhost:8085/api/appointment-accept', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-XSRF-TOKEN': getCookie('XSRF-TOKEN')
        },
        body: JSON.stringify(appointment)
    })
        .then(response => {
            console.log(response);
        })
        .catch(error => {
            console.error(error);
        });
}

function getOwnAppointments() {
    var url = "http://localhost:8085/api/own-appointments"
    fetch(url)
        .then((response) => response.json())
        .then((data) => {
            console.log("dat: ",data);
            let ownAppointments = '<h2>Termine</h2>';
            for (var i = 0; i < data.length; i++) {

                if(!data[i].accepted) {
                    if(data[i].location == "ONLINE") {
                        ownAppointments += "<div><l>Terminanfrage am "+data[i].date+" online</l>";
                    } else {
                        ownAppointments += "<div><l>Terminanfrage am "+data[i].date+". Ort: "+data[i].location+"</l>";
                    }
                    ownAppointments += "<l class='warning'> Der Termin wurde vom Arzt noch nicht best&auml;tigt!</l>"
                } else {
                    if(data[i].location == "ONLINE") {
                        ownAppointments += "<div><l>Termin am "+data[i].date+" online mit "+data[i].fullnameDoctor+"</l>";
                    } else {
                        ownAppointments += "<div><l>Termin am "+data[i].date+" mit "+data[i].fullnameDoctor +". Ort: "+data[i].location+"</l>";
                    }
                }

                ownAppointments += "</div></hr>"
            }
            document.getElementById("ownAppointments").innerHTML = ownAppointments;

        })
}

function terminAnfragen() {
    var datum = document.getElementById("datetimepicker");
    var ort = document.getElementById("ort").value;
    var beschreibung = document.getElementById("beschreibung").value;

    var selectedDate = $('#datetimepicker').datetimepicker('date').format('YYYY-MM-DD HH:mm:ss');
    console.log("test: ",selectedDate);

    var element = {
        "date" : selectedDate,
        "location" : ort,
        "description" : beschreibung
    }

    fetch('http://localhost:8085/api/appointment-request', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-XSRF-TOKEN': getCookie('XSRF-TOKEN')
        },
        body: JSON.stringify(element)
    })
        .then(response => {
            console.log(response);
            location.reload();

        })
        .catch(error => {
            console.error(error);
        });

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
    var url = "http://localhost:8085/api/active-modules"
    fetch(url)
        .then(response => response.text())
        .then(data => {
            var props = data.split(",");
            console.log(props);
            if(props[1]) {
                console.log("farbe", props[1].split("=")[1]);
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