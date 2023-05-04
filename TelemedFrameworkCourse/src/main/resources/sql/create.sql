create table telemed_course
(
    uid         varchar(80) primary key,
    course_name    varchar(80),
    description varchar(256)
);

create table telemed_course_entry
(
    uid         varchar(80) primary key,
    course_id    varchar(80),
    title varchar(256),
    text varchar(512),
    attachment blob,
    creation_date DATETIME
);

create table telemed_course_user (
     uid    varchar(80) primary key,
     username varchar(80),
     course_id varchar(80)
);