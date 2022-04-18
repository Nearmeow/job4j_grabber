CREATE TABLE company
(
    id integer NOT NULL,
    name character varying,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);

CREATE TABLE person
(
    id integer NOT NULL,
    name character varying,
    company_id integer references company(id),
    CONSTRAINT person_pkey PRIMARY KEY (id)
);

insert into company (id, name) values (1, 'one');
insert into company (id, name) values (3, 'three');
insert into company (id, name) values (5, 'five');

insert into person (id, name, company_id) values (1, 'Vasya', 1);
insert into person (id, name, company_id) values (2, 'Petr', 3);
insert into person (id, name, company_id) values (3, 'Oleg', 3);
insert into person (id, name, company_id) values (4, 'Olga', 5);
insert into person (id, name, company_id) values (5, 'Maria', 5);

select p.name as person_name, c.name as company_name from company as c join person as p on c.id = p.company_id where not p.company_id = 5;

with heap(company_name, staff) as
(select c.name as company_name, count(c.name) as staff from company as c join person as p on c.id = p.company_id group by c.name)
select * from heap where staff = (select max(staff) from heap);