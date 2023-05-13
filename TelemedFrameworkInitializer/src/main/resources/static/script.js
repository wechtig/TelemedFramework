function downloadModules() {

    var communicationChecked = document.getElementById("communication").checked;
    var symptomChecked = document.getElementById("symptom").checked;
    var appointmentChecked = document.getElementById("appointment").checked;
    var courseChecked = document.getElementById("course").checked;
    var exportChecked = document.getElementById("export").checked;

    var downloadElement = {
        "communication" : communicationChecked,
        "symptom" : symptomChecked,
        "appointment" : appointmentChecked,
        "course" : courseChecked,
        "export" : exportChecked,
    }

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