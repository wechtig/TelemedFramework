create table telemed_appointment
(
    uid         varchar(80) primary key,
    username_patient varchar(100),
    username_doctor varchar (100),
    description  varchar(256),
    accepted    boolean,
    location     varchar(100),
    date DATETIME
)