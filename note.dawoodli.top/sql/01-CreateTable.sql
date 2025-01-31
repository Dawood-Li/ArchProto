create table Users (
    ID bigint unsigned primary key auto_increment,
    Email varchar(255) not null unique,
    Password varchar(255) not null,
    Salt varchar(255) not null,
    Name varchar(255) not null,
    Role enum ('admin', 'user') not null default 'user',
    CreatedAt datetime not null default current_timestamp,
    LastLoginAt datetime default null,
    DeletedAt datetime default null
) engine=InnoDB default charset=utf8mb4;

create table NoteMetaData (
    ID bigint unsigned primary key auto_increment,
    UserID bigint unsigned,
    NewestNoteDataID bigint unsigned,
    isPublic tinyint(1) not null default 0,
    CreatedAt datetime not null default current_timestamp,
    UpdatedAt datetime default current_timestamp on update current_timestamp,
    DeletedAt datetime default null
) engine=InnoDB default charset=utf8mb4;

create table NoteData (
    ID bigint unsigned primary key auto_increment,
    NoteMetaID bigint unsigned,
    CreatedAt datetime not null default current_timestamp,
    Title varchar(255) not null,
    Content text not null
) engine=InnoDB default charset=utf8mb4;

create table Logs (
    ID bigint unsigned primary key auto_increment,
    Level enum ('debug', 'info', 'warning', 'error') not null,
    Src varchar(255) not null,
    Msg text not null,
    CreatedAt datetime not null default current_timestamp
) engine=InnoDB default charset=utf8mb4;
