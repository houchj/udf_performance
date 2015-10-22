do $$
declare
index integer := 1;
begin
  while index <= 1000000 loop
    insert into orders (id, customerName, address) values (index, 'customer'||index, 'address'||index);
    index := index + 1;
  end loop;
end $$;

do $$
declare
index integer := 1;
begin
  while index <= 10000000 loop
    insert into orderLines (id, order_id, productName, price, count, lineTotal) values (index, index, 'product'||index, index, index, index);
    index := index + 1;
  end loop;
end $$;


select * from orders;
select * from orderlines;

select count(id) "aaaaaaaaaaaaaaaaaaa" from orderlines;

delete from orders;
delete from orderlines;

select * from orders where id = 12345;
select * from orderLines where id = 123;

alter table orders add "reservedString1" character varying;
alter table orders add "reservedInteger1" integer;
alter table orders add "reservedDate2" date;

alter table orderlines add "reservedString3" character varying;
alter table orderlines add "reservedInteger5" integer;
alter table orderlines add "reservedDate4" date;


update orders set "reservedString1" = 'defaultString';

select * from view1 where id = 999;

SELECT 'hello' as txt;

select nextval ('orders_seq');

select nextval ('orderlines_seq');

update view1 set "reservedString2" = 'hha';

-- Table: orders
drop view OrdersView1;
drop view OrdersView2;
drop view OrderLinesView1;
drop view OrderLinesView2;
DROP TABLE orders;
drop table orderLines;
drop sequence orders_seq;
drop sequence orderLines_seq;


CREATE TABLE orders
(
  id bigint NOT NULL,
  customerName character varying,
  address character varying,
  total integer,
  tax integer,
  memo character varying,
  CONSTRAINT id_orders PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE orders
  OWNER TO postgres;

CREATE SEQUENCE orders_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 10000001
  CACHE 1;
ALTER TABLE orders_seq
  OWNER TO postgres;

CREATE TABLE orderLines
(
  id bigint NOT NULL,
  order_id integer NOT NULL,
  productName character varying,
  price integer,
  count integer,
  lineTotal integer,
  memo character varying,
  CONSTRAINT id_orderLine PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE orderLines
  OWNER TO postgres;

CREATE SEQUENCE orderLines_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 10000001
  CACHE 1;
ALTER TABLE orderLines_seq
  OWNER TO postgres;

CREATE VIEW OrdersView1 AS
    SELECT *
    FROM orders
    WHERE id > 1 order by id desc;

CREATE VIEW OrdersView2 AS
    SELECT *
    FROM orders
    WHERE id >= 1 order by id asc;


CREATE VIEW OrderLinesView1 AS
    SELECT *
    FROM orderLines
    WHERE id > 1 order by id desc;

CREATE VIEW OrderLinesView2 AS
    SELECT *
    FROM orderLines
    WHERE id >= 1 order by id asc;

CREATE OR REPLACE FUNCTION OrdersFunction1() RETURNS void AS
$$
    DECLARE
    testvalue1  VARCHAR(20);
    testvalue2  VARCHAR(20);
 BEGIN
   testvalue1 := 'First Test! ';
   update orders set "memo" = testvalue1;
 END;
 $$
 LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION OrdersFunction2() RETURNS void AS
$$
    DECLARE
    testvalue1  VARCHAR(20);
    testvalue2  VARCHAR(20);
 BEGIN
   testvalue1 := 'First Test2! ';
   update orders set "memo" = testvalue1;
 END;
 $$
 LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION OrderLinesFunction1() RETURNS void AS
$$
    DECLARE
    testvalue1  VARCHAR(20);
    testvalue2  VARCHAR(20);
 BEGIN
   testvalue1 := 'First Test! ';
   update orderLines set "memo" = testvalue1;
 END;
 $$
 LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION OrderLinesFunction2() RETURNS void AS
$$
    DECLARE
    testvalue1  VARCHAR(20);
    testvalue2  VARCHAR(20);
 BEGIN
   testvalue1 := 'First Test2! ';
   update orderLines set "memo" = testvalue1;
 END;
 $$
 LANGUAGE plpgsql;
 
-- memo: 
-- 10000000 records:  11 ms to add a varchar column, 11s to add a integer with default value or date column
-- 1000000 records:  11 ms to add a varchar column, 2.7s to add a integer with default value, 1s to add a date column
-- 100000 records:  11 ms to add a varchar column, 194ms to add a integer with default value, 12ms to add a date column
-- 10000 records:  11 ms to add a varchar column, 44ms to add a integer with default value, 12ms to add a date column
--
--
--
