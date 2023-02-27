drop table if exists codes cascade;

create table codes (
    id bigserial not null,
    value varchar(255),
    primary key (id)
 );

alter table if exists codes add constraint UK4fve4eho9bl6ld9pbv3bvks1n unique (value);

INSERT INTO codes(value)
VALUES('a0a0');