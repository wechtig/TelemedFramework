function downloadModules() {
    var communicationChecked = document.getElementById("communication").checked;
    var symptomChecked = document.getElementById("symptom").checked;
    var appointmentChecked = document.getElementById("appointment").checked;
    var courseChecked = document.getElementById("course").checked;
    var exportChecked = document.getElementById("export").checked;
    var praxisname = document.getElementById("praxisname").value;
    var color = document.getElementById("color").value;
    var logo = document.getElementById("logo").files[0];

    if (logo) {
        var logoBase64;
        var reader = new FileReader();
        reader.readAsDataURL(logo);
        reader.onload = function () {
            logoBase64 = reader.result;
            console.log(reader.result);
            var downloadElement = {
                "communication": communicationChecked,
                "symptom": symptomChecked,
                "appointment": appointmentChecked,
                "course": courseChecked,
                "export": exportChecked,
                "praxisname": praxisname,
                "color": color,
                "logo": logoBase64
            }

            send(downloadElement);
        };
    } else {
        var downloadElement = {
            "communication": communicationChecked,
            "symptom": symptomChecked,
            "appointment": appointmentChecked,
            "course": courseChecked,
            "export": exportChecked,
            "praxisname": praxisname,
            "color": color
        }

        send(downloadElement)
    }
}

function send(downloadElement) {
    fetch('http://localhost:9000/api/download/', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(downloadElement)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.blob();
        })
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.download = 'telemedframework.zip';
            document.body.appendChild(link);
            link.click();
            link.remove();
        })
        .catch(error => {
            console.error(error);
        });
}

