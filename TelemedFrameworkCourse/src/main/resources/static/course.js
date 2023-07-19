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

            if(data == "DOCTOR") {
                document.getElementById("patientDiv").style.display = "none";
                initCourseDiv();
            }

            if(data == "PATIENT") {
                document.getElementById("doctorDiv").style.display = "none";
                initCourseDivPatientSelectBox();
                getCurrentRegisteredEntriesForCourse();
            }

        })

    var urlLgo = "http://localhost:8083/api/logo";
    fetch(urlLgo)
        .then(response => response.text())
        .then(data => {
            var imageByteArray = data;
            document.getElementById("logo").src = "data:image/png;base64," + imageByteArray;
        })
}

function initCourseDiv(){
    var url = "http://localhost:8083/api/course"
    fetch(url)
        .then(response => response.json())
        .then(data => {
            console.log(data.length);
            var courseSelectBox = "<select id='courseSelectBox''>";
            for (var i = 0; i < data.length; i++) {
                courseSelectBox += "<option>"+data[i]+"</option>";
            }
            courseSelectBox += "</select>";

            document.getElementById("courseSelectBoxDiv").innerHTML = courseSelectBox;
        })
}

function initCourseDivPatientSelectBox(){
    var url = "http://localhost:8083/api/course"
    fetch(url)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            var courseSelectBox = "<select id='patientSelectBox'>";
            for (var i = 0; i < data.length; i++) {
                courseSelectBox += "<option>"+data[i]+"</option>";
            }
            courseSelectBox += "</select>";

            document.getElementById("courseSelectBoxPatientDiv").innerHTML = courseSelectBox;
        })
}

function getCurrentRegisteredEntriesForCourse() {
    var url = "http://localhost:8083/api/currentcourseentries/";
    fetch(url)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            if(data.length < 1) {
                document.getElementById("patientCourseEntries").innerHTML
                    = "<p>Kein Eintrag!</p>";
            } else {
                for (var i = 0; i < data.length; i++) {
                    var formattedDate = getFormattedDate(data[i].creationDate);
                    var patientCourseEntries = "<div>";
                    patientCourseEntries += "<h4>"+data[i].title+"</h4>";
                    patientCourseEntries += "<p>Erstellt am "+formattedDate+"</p>";
                    patientCourseEntries += "<p>"+data[i].text+"</p>";
                    patientCourseEntries +=
                        "<a href='#' onclick='downloadAttachment(this)' data-filename='"+data[i].filename + "'>"+data[i].filename+"</a>"
                    patientCourseEntries += "<hr>";
                    patientCourseEntries += "</div>";
                }
                document.getElementById("patientCourseEntries").innerHTML = patientCourseEntries;
            }
        })
}

function removeRegistrationForCourse() {
    var courseName = document.getElementById("patientSelectBox").value;
    fetch('http://localhost:8083/api/unregister/'+courseName, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-XSRF-TOKEN': getCookie('XSRF-TOKEN')
        },
        body: JSON.stringify(courseName)
    })
        .then(response => {
            initCourseDiv();
            var alertElement = document.createElement('div');
            alertElement.classList.add('alert', 'alert-success');
            alertElement.textContent = 'Sie wurden vom Kurs abgemeldet!';

            document.body.appendChild(alertElement);
            setTimeout(function() {
                alertElement.remove();
            }, 3000);
            console.log(response);
        })
        .catch(error => {
            console.error(error);
        });
}

function getFormattedDate(date) {
    const datetime = new Date(date);
    const day = datetime.getDate();
    const month = datetime.getMonth() + 1;
    const year = datetime.getFullYear();
    const formattedDate = `${day < 10 ? '0' + day : day}.${month < 10 ? '0' + month : month}.${year}`;
    const hours = datetime.getHours();
    const minutes = datetime.getMinutes();
    const formattedTime = `${hours < 10 ? '0' + hours : hours}:${minutes < 10 ? '0' + minutes : minutes}`;
    const formattedDateTime = `${formattedDate} um ${formattedTime}`;
    return formattedDateTime;
}

function downloadAttachment(link) {
    const filename = link.getAttribute('data-filename');
    fetch('http://localhost:8083/api/download/'+filename, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
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
            link.download = filename;
            document.body.appendChild(link);
            link.click();
            link.remove();
        })
        .catch(error => {
            console.error(error);
        });
}

function convertToBlob(blobData, contentType) {
    const byteCharacters = atob(blobData);
    const byteArrays = [];
    for (let offset = 0; offset < byteCharacters.length; offset += 512) {
        const slice = byteCharacters.slice(offset, offset + 512);
        const byteNumbers = new Array(slice.length);
        for (let i = 0; i < slice.length; i++) {
            byteNumbers[i] = slice.charCodeAt(i);
        }
        const byteArray = new Uint8Array(byteNumbers);
        byteArrays.push(byteArray);
    }
    return new Blob(byteArrays, { type: contentType });
}

function addCourseEntry() {
    var title = document.getElementById("title").value;
    var text = document.getElementById("text").value;
    var attachment = document.getElementById("attachment").files[0];
    var attachmentName = document.getElementById("attachment").value;
    var courseName = document.getElementById("courseSelectBox").value;

    if (attachment) {
        var attachmentBase64;
        var reader = new FileReader();
        reader.readAsDataURL(attachment);
        reader.onload = function () {
            attachmentBase64 = reader.result;
            var downloadElement = {
                "title": title,
                "text": text,
                "courseName" : courseName,
                "filename" : attachmentName,
                "attachment": attachmentBase64,
            }

            sendCourseEntry(downloadElement);
        };
    } else {
        var downloadElement = {
            "title": title,
            "courseName" : courseName,
            "text": text,
        }

        sendCourseEntry(downloadElement)
    }

}

function sendCourseEntry(downloadElement) {
    fetch('http://localhost:8083/api/courseentry/', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(downloadElement)
    })
        .then(response => {
            initCourseDiv();
            console.log(response);
        })
        .catch(error => {
            console.error(error);
        });
}


function newCourse() {
    var courseName = document.getElementById("courseName").value;
    var courseDescription = document.getElementById("courseDescription").value;

    var newCouse = {
        "courseName" : courseName,
        "courseDescription" : courseDescription
    }


    fetch('http://localhost:8083/api/newcourse/', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-XSRF-TOKEN': getCookie('XSRF-TOKEN')
        },
        body: JSON.stringify(newCouse)
    })
        .then(response => {
            initCourseDiv();
            console.log(response);
        })
        .catch(error => {
            console.error(error);
        });
}

function getActiveModules() {
    var url = "http://localhost:8083/api/active-modules"
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

function registerForCourse() {
    var courseName = document.getElementById("patientSelectBox").value;
    fetch('http://localhost:8083/api/register/'+courseName, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-XSRF-TOKEN': getCookie('XSRF-TOKEN')
        },
        body: JSON.stringify(courseName)
    })
        .then(response => {
            var alertElement = document.createElement('div');
            alertElement.classList.add('alert', 'alert-success');
            alertElement.textContent = 'Sie haben sich beim Kurs angemeldet!';

            document.body.appendChild(alertElement);
            setTimeout(function() {
                alertElement.remove();
            }, 3000);
            console.log(response);
            initCourseDiv();
            console.log(response);
        })
        .catch(error => {
            console.error(error);
        });
}
