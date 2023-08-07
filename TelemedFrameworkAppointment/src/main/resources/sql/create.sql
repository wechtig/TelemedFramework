create table telemed_appointment
(
    uid         varchar(80) primary key,
    patient_id varchar(100),
    doctor_id varchar (100),
    description  varchar(256),
    accepted    boolean,
    location     varchar(100),
    date DATETIME
)