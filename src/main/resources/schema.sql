create table customers (
    firstName varchar(256) not null,
    lastName varchar(256) not null,
    dateOfBirth date not null,
    assets decimal(14, 2),
    portfolioName varchar(32),
    primary key (firstName, lastName)
);
