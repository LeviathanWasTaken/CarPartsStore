-- auto-generated definition
create table cart_items
(
    cart_itemuuid       uuid    not null
        primary key,
    quantity            integer not null,
    cart_cartuuid       uuid
        constraint fki4rborm25j5ylr960j2agmxbl
            references carts,
    product_productuuid uuid
        constraint fk6sg8icshkuf1cf9ilg26p5yg1
            references products
);

alter table cart_items
    owner to postgres;

-- auto-generated definition
create table carts
(
    cartuuid uuid not null
        primary key
);

alter table carts
    owner to postgres;


-- auto-generated definition
create table catalogs
(
    uuid           uuid   not null
        primary key,
    catalog_name   varchar(255),
    img_source     varchar(255),
    left_boundary  integer,
    popularity     bigint not null,
    right_boundary integer,
    status         smallint,
    unique_tag     varchar(255)
        constraint uk_l7psicqxnc8lj3gwofc48xlkp
            unique,
    parent_uuid    uuid
        constraint fk79fkw3yomnpx4qp65dqdn5901
            references catalogs
);

alter table catalogs
    owner to postgres;


-- auto-generated definition
create table product_info
(
    product_infouuid    uuid    not null
        primary key,
    details             varchar(255),
    img_source          varchar(255),
    price_in_pennies    integer not null,
    product_name        varchar(255),
    product_productuuid uuid
        constraint fk9ysf7tejykdllgy13q0j9738q
            references products
);

alter table product_info
    owner to postgres;


-- auto-generated definition
create table products
(
    productuuid  uuid not null
        primary key,
    status       smallint,
    unique_tag   varchar(255)
        constraint uk_ondcvovwm2o1w6yrh0u41k2dm
            unique,
    catalog_uuid uuid
        constraint fk3lkg5rqewx1f5jvbq61povn73
            references catalogs
);

alter table products
    owner to postgres;


-- auto-generated definition
create table reviews
(
    reviewuuid          uuid    not null
        primary key,
    body                varchar(255),
    mark                integer not null,
    relevance           integer not null,
    author_uuid         uuid
        constraint fk60s69vx0ollebx5xtnru9vjmg
            references users,
    product_productuuid uuid
        constraint fklmpp33dsgiyqjx0iy5566q8na
            references products
);

alter table reviews
    owner to postgres;


-- auto-generated definition
create table user_roles
(
    user_uuid uuid not null
        constraint fkb4bms60ebskkrd05297us35x9
            references users,
    roles     varchar(255)
);

alter table user_roles
    owner to postgres;


-- auto-generated definition
create table users
(
    uuid             uuid    not null
        primary key,
    avatar           varchar(255),
    delivery_address varchar(255),
    email            varchar(255),
    github_id        integer not null,
    login            varchar(255),
    name             varchar(255),
    cart_cartuuid    uuid
        constraint fkt7yuporythjduqgl8ytty64sw
            references carts
);

alter table users
    owner to postgres;