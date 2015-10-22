CREATE COLUMN TABLE "I075885_MASTER_OCC"."MYORDERS" ("ID" BIGINT CS_FIXED NOT NULL ,
	 "CUSTOMERNAME" NVARCHAR(255) NOT NULL ,
	 "ADDRESS" NVARCHAR(255) NOT NULL ,
	 "TOTAL" BIGINT CS_FIXED,
	 "TAX" BIGINT CS_FIXED,
	 "MEMO" NVARCHAR(255),
	 PRIMARY KEY ("ID")) UNLOAD PRIORITY 5 AUTO MERGE 
;
--drop procedure TmSp_CFF_FillDateTable; 
CREATE PROCEDURE insertMyOrders (in maximum integer) LANGUAGE SQLSCRIPT AS  
index1      integer; 
BEGIN 
	index1 := 1; 
	while (:index1 <= :maximum) do 
		insert into "I075885_MASTER_OCC"."MYORDERS" values(:index1,'customer','address', 123, 123,'');
		index1 := :index1 + 1; 
	end while;
END;
call insertMyOrders(10000000);

select * from myorders where id = 123;

delete from myorders;

select count(*) from myorders;

alter table myorders add (reservedString1 NVARCHAR(255));
alter table myorders add (reservedInteger2 integer default 1);
alter table myorders add (reservedDate1 date);

-- with 10000000 rows of data, 
-- it cost about 90ms to add a column without default value (slower than postgresql 10ms), 
-- and 950ms to add a integer column with default value (much better than postgresql 10s)