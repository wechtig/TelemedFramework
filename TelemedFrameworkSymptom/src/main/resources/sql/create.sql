create table telemed_symptom
(
    uid         varchar(80) primary key,
    user_id     varchar(80),
    symptom     varchar(100),
    active      boolean,
    created     date,
    description varchar(256)
)