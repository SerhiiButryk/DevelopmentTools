/*
	SQL/MySQL QUICK REFERENCE

	1. SQL standard instruction list:

	1) Main

	SELECT
	INSERT
	UPDATE
	MERGE
	DELETE

	2) Data manipulation

	CREATE TABLE
	DROP TABLE
	ALTER ТАВLЕ
	CREATE VI EW
	DROP VI EW
	CREATE I NDEX
	DROP I NDEX
	CREATE SСНЕМА
	DROP S СНЕМА
	CREATE DOМAIN
	ALTER DOМAIN
	DROP DOМAIN

	3) Permissions

	GRANT
	REVOKE
	CREATE ROLE
	GRANT ROLE
	DROP ROLE

	4) Transactions

	COMMIT
	ROLLBACK
	SET TRANSACTION
	START
	TRANSACTION
	SAVE POINT

	5) Script control

	DECLARE
	EXPLAIN
	OPEN
	FETCH
	CLOSE
	PRE PARE
	ЕХЕСUТЕ
	DESCRIBE

	2. Data format

	Американский   mm/dd/yyyy 5/19/1963 hh:mm am/pm 2:18 РМ
	Европейский    dd.mm.yyyy 19.5.1963 hh.mm.ss 14.18.08
	Японский       yyyy-mm-dd 1963-5-19 hh:mm:ss 14:18:08
	ISO            yyyy-mm-dd 1963-5-19 hh.mm.ss 14.18.08
    
    3. Comparison
    
    =, <> (!=), <, <=, >, >=
    
    если сравнение истинно, то результат проверки имеет значение TRUE; 
    если сравнение ложно, то результат проверки имеет значение FALSE;
	если хотя бы одно из двух выражений имеет значение NULL, то результа­ том сравнения будет NULL.
    
	в результа­ ты запроса попадают только те строки, для которых условие отбора равно TRUE
	при определении условия отбора необ­ ходимо помнить об обработке значений NULL
    
    4. Logical operations
    
    OR, AND, (NOT) BETWEEN .. AND, (NOT) IN .. , (NOT) LIKE ..
    
    5. NULL check or whether data is absent
    
    IS (NOT) NULL
    
    6. UNION operations
    
    На таблицы результатов запроса, которые можно объединять с помощью опе­рации UNION, накладываются следующие ограничения:

	1. эти таблицы должны содержатъ одинаковое число столбцов
	2. тип данных каждого столбца первой таблицы должен совпадать с типом данных соответствующего столбца во второй таблице
    3. ни одна из двух таблиц не может быть отсортирована с помощью пред­ ложения ORDER ВУ, 
    однако объединенные результаты запроса можно от­ сортировать

	Обратите внимание на то, что имена столбцов в двух запросах, объединенных с помощью операции UNION, не обязательно должны быть одинаковыми.
    
    7. JOINs
    
    a) Natural joins - comparison comlumn names
*/
    
-- ------------------- ------------------------------------------------------
-- Database modification
-- ------------------- ------------------------------------------------------
    
ALTER DATABASE trade_company_db READ ONLY = 0;
ALTER SESSION SET NLS_DATE_FORМAT= 'YYYY-MM-DD';
    
-- ------------------- ------------------------------------------------------
-- Select query
-- ------------------- ------------------------------------------------------

USE trade_company_db;

SELECT * FROM trade_company_db.OFFICES;

-- Use full table/database name to referer to proper column if names are not unique

SELECT trade_company_db.OFFICES.CITY, trade_company_db.OFFICES.SALES FROM trade_company_db.OFFICES
WHERE trade_company_db.OFFICES.SALES >= 700000.00;

SELECT CITY, SALES, TARGET, (SALES/TARGET) * 100 AS '% of plans' 
FROM OFFICES;

SELECT AVG(SALES)
FROM OFFICES;

-- We can assign custom names for columns

SELECT CITY, (SALES - TARGET) AS 'Over/below plan amount'
FROM OFFICES;

-- ------------------- ------------------------------------------------------
-- DISTINCT keyword
-- ------------------- ------------------------------------------------------
    
SELECT DISTINCT REGION FROM OFFICES;

-- ------------------- ------------------------------------------------------
-- LIMIT keyword
-- ------------------- ------------------------------------------------------

-- Select a sample of data 

SELECT CITY, REGION
FROM OFFICES
LIMIT 3;

-- ------------------- ------------------------------------------------------
-- Ranges
-- (NOT) BETWEEN .. AND 
-- Simmilar to 
-- (А >= В) AND (А <= С)
-- Range is inclusive
-- ------------------- ------------------------------------------------------

SELECT *
FROM SALESREPS
WHERE SALES BETWEEN 3000 AND 5000;
    
SELECT ORDER_NUМ, ORDER_DATE, MFR, PRODUCT, AМOUNT 
FROM ORDERS
WHERE ORDER_DATE BETWEEN '2007-10-01' AND '2007-12-31';

SELECT NАМЕ, SALES, QUOTA 
FROM SALESREPS
WHERE SALES NOT BETWEEN ( .8 * QUOTA) AND (1.2 * QUOTA);

-- ------------------- ------------------------------------------------------
-- Set checks
-- (NOT) IN (множество) 
-- Simmilar to
-- (Х = А) OR (Х = В) OR (Х = С)
-- ------------------- ------------------------------------------------------

SELECT NAME, QUOTA, SALES
FROM SALESREPS
WHERE REP_OFFICE IN (11, 13, 22);

SELECT NAME, QUOTA, SALES
FROM SALESREPS
WHERE REP_OFFICE NOT IN (11, 13, 22);

SELECT ORDER_NUМ, ORDER_DATE, AМOUNT
FROM ORDERS 
WHERE ORDER_DATE IN ('2008-01-04', '2008-01-11', '2008-01-18', '2008-01-25');

-- ------------------- ------------------------------------------------------
-- Pattern matching 
-- (NOT) LIKE (ESCAPE)
-- ------------------- ------------------------------------------------------

SELECT *
FROM SALESREPS
WHERE NAME LIKE 'Tom%' OR NAME LIKE 'Bill%';

SELECT *
FROM SALESREPS
WHERE NAME NOT LIKE 'Tom%' OR NAME NOT LIKE 'Bill%';

/*
	В стандарте ANSI/ISO определен способ проверки наличия в строке литералов, 
	использующихся в качестве подстановочных знаков. Для этого применяются управляющие символы. 
	Когда в шаблоне встречается такой символ, то символ, сле­ дующий непосредственно за ним, считается 
	не подстановочным знаком, а литералом. 

	Непосредственно за управляющим симво:лом может следовать либо 
	один из двух подстановочных символов, либо сам управляющий символ, 
	поскольку он также приобретает в шаблоне особое значение.
*/

-- Найти товары, коды которых начинаются с четырех букв "А%ВС''.

SELECT ORDER_NUМ, PRODUCT
FROM ORDERS
WHERE PRODUCT LIKE 'А$%ВС%' ESCAPE '$';

-- ------------------- ------------------------------------------------------
-- IS checks
-- (...) IS (NOT) NULL
-- (...) IS FALSE
-- (...) IS TRUE
-- (...) IS UNKNOWN
-- ------------------- ------------------------------------------------------

SELECT NАМЕ
FROM SALESREPS
WHERE REP_OFFICE IS NULL;

-- ------------------- ------------------------------------------------------
-- Complex logical instruction
-- ------------------- ------------------------------------------------------

SELECT NАМЕ
FROM SALESREPS
WHERE (REP_OFFICE IN (22, 11, 12)) 
	OR (МANAGER IS NULL AND HIRE_DATE >= '2006-06-01') 
    OR (SALES > QUOTA AND NOT SALES > 600000.00);

/*
	Sorting & Calculated colums 
    
    ORDER BY .. (ASC/DESC)
    
    Строки результатов запроса, как и строки таблицы базы данных, не имеют оп­ ределенного порядка. 
	Но, включив в инструкцию SELECT предложение ORDER ВУ, можно отсортировать результаты запроса.
    
    Если столбец результатов запроса, используемый для сортировки, является вы­ числяемым, 
    то у него нет имени, которое можно указать в предложении сорти­ ровки. В таком елучае вместо 
    имени столбца необходимо указать его порядковый номер или повторить выражение в предложении ORDER ВУ
*/

SELECT CITY, REGION, (SALES - TARGET)
FROM OFFICES
ORDER BY (SALES - TARGET) DESC;

SELECT CITY, REGION
FROM OFFICES
ORDER BY CITY;

-- Find the best person who has the best sales in the company

SELECT NAME, (SALES/QUOTA) * 100 AS 'Sales in %'
FROM SALES_REPS
ORDER BY 2 DESC
LIMIT 1;

-- ------------------- ------------------------------------------------------
-- UNION instruction
-- ------------------- ------------------------------------------------------
    
-- We can merger the results from 2 or more queries into one result (table)
-- Here repeated records are removed
    
SELECT MFR_ID, PRODUCT_ID 
FROM PRODUCTS
WHERE PRICE > 2000.00
UNION
SELECT MFR, PRODUCT 
FROM ORDERS
WHERE AМOUNT > 30000.00;

-- Here repeated records are keept
-- To order the records we should reference them by its index

SELECT MFR_ID, PRODUCT_ID 
FROM PRODUCTS
WHERE PRICE > 2000.00
UNION ALL
SELECT MFR, PRODUCT 
FROM ORDERS
WHERE AМOUNT > 30000.00
ORDER BY 1, 2;

-- ------------------- ------------------------------------------------------
-- JOIN instruction
-- ------------------- ------------------------------------------------------

/* 
	Cоединения представляют собой основу многотабличных запросов в SQL. 
    В реля­ ционной базе данных вся информация хранится в виде явных значений данных в столбцах, 
    так что все возможные отношения между таблицами можно сформировать, сопоставляя содержимое связанных столбцов.
    
    We can use simple SELECT and WHERE statments to declare simple join queries.
    But they are less flexible and less usefull
    
    Cоединение двух таблиц является произведением этих таблиц, из которого удалены некоторые строки. 
    Удаляются те строки, которые не удовлетворяют условию (WHERE), налагаемому на связанные столбцы для данного 
    соедине­ния. Понятие произведения очень важно, так как оно входит в формальное опре­ деление правил 
    выполнения мноrотабличных запросов на выборку.

*/ 

-- Show all workers together with city and region where they work

SELECT NAME, TITLE, HIRE_DATE, CITY, REGION
FROM OFFICES, SALES_REPS
WHERE REP_OFFICE = OFFICE
ORDER BY 1;

-- Вывести список офисов с именами и должностями их руководителей.

SELECT CITY, REGION, NAME, TITLE
FROM OFFICES, SALES_REPS
WHERE MGR = EMPL_NUМ;

-- New way of declaring the same query

SELECT CITY, REGION, NAME, TITLE
FROM OFFICES JOIN SALES_REPS
ON MGR = EMPL_NUМ;

-- Using this sintax we can divide condition and joining colums in 2 different sections
-- For example, let's say we have additional condition 

SELECT NAME, TITLE, CITY, SALES_REPS.SALES
FROM OFFICES JOIN SALES_REPS
ON REP_OFFICE = OFFICE
WHERE SALES_REPS.SALES > 30000.00;

-- If foreign key is compound we need to specify all columns

SELECT ORDER_NUМ, AМOUNT, DESCRIPTION
FROM ORDERS JOIN PRODUCTS
ON MFR_ID = MFR AND PRODUCT_ID = PRODUCT;

/*
	Предположим, что идентификатор производителя и идентификатор товара име­ ют 
    имена MFR и PRODUCT в нашей учебной базе данных в обеих таблицах - и в ORDERS, и в 
    PRODUCTS. В таком случае большинство естественных соединений между двумя таблицами были 
    бы соединениями по равенству на основе имен столбцов, имеющихся в обеих таблицах. 
    Такое соединение в стандарте SQL так и называется - естествеююе соединение. 
    Синтаксис соединения из SQL-стандарта позволяет легко указать, что вам требуется 
    естественное соединение.

	Эта инструкция указывает СУБД на необходимость соединения таблиц ORDERS и 
    PRODUCTS по всем столбцам, имеющим в этих таблицах одинаковые имена.
*/

SELECT ORDER_NUМ, AМOUNT, DESCRIPTION 
FROM ORDERS NATURAL JOIN PRODUCTS;

-- Explicit table name declaration

SELECT ORDER_NUМ, AМOUNT, DESCRIPTION 
FROM ORDERS JOIN PRODUCTS 
USING (MFR, PRODUCT);

-- Joins 3 tables

SELECT ORDER_NUМ, AМOUNT, COMPANY, NAME
FROM ORDERS JOIN CUSTOMERS ON CUST = CUST_NUМ 
			JOIN SALES_REPS ON REP = EMPL_NUМ
WHERE AМOUNT > 25000.00;

/*
Перечислить все комбинации служащих и офисов, где плановый объем продаж служа­ щего больше, 
чем план какого-либо офиса, независимо от места работы служащего.
*/

SELECT NAME, QUOTA, CITY, TARGET
FROM SALES_REPS, OFFICES
WHERE QUOTA > TARGET;

/*
'SALES_REPS.*' has special treatment
Сообощитъ всю информацию о служащих и офисах, где они работают.
*/

SELECT SALES_REPS.*, CITY
FROM SALES_REPS, OFFICES
WHERE REP_OFFICE = OFFICE; 

/*
	SQL self-join

	Для соединения таблицы с самой собой в SQL применяется метод "вообра­ жаемой копии". 
    Вместо того чтобы на самом деле создавать копию таблицы, СУБД просто позволяет вам 
    сослаться на нее, используя другое имя, псевдоним таблицы. Вот тот же запрос, 
    но записанный с использованием псевдонимов EMPS и MGRS для таблицы SALESREPS.
*/

-- Въюести список всех служащих и их руководителей.

SELECT EMPS.NAME AS 'EMPL NAME', MGRS.NAME AS 'MANAGER NAME'
FROM SALES_REPS EMPS, SALES_REPS MGRS 
WHERE EMPS.МANAGER = MGRS.EMPL_NUМ;

-- Въюести список служащих, планъz продаж которых превъzшают планы их руководи­телей.

SELECT SALES_REPS.NAME, SALES_REPS.QUOTA, MGRS.QUOTA 
FROM SALES_REPS, SALES_REPS MGRS
WHERE SALES_REPS.МANAGER = MGRS.EMPL_NUМ AND SALES_REPS.QUOTA > MGRS.QUOTA;

/*
	TABLE ALIAS
*/

-- Вывести список имен, плановых обьемов продаж и дней рождения служащих.

-- JUST AN EXAMPLE, THIS IS NOT VALID QUERY FOR THIS DATABASE
SELECT S.NAМE, S.QUOTA, B.BIRTH_DATE
FROM SALESREPS S, SAМ.BIRTHDAYS В 
WHERE S.NAМE = В.NАМЕ;

SELECT NAME, CITY
FROM SALES_REPS LEFT OUTER JOIN OFFICES ON REP_OFFICE = OFFICE;

/*
	Перекрестное соединение (cross join) представляет собой другое 
    название декартова произведения двух таблиц, описанного ранее в этой главе.
    По определению декартово произведение (иногда называемое перекрестным произведением (cross product), 
    отсюда и название CROSS JOIN) содержит все воз­ можные пары строк из двух таблиц.
*/

SELECT * FROM SALES_REPS, OFFICES;

SELECT * FROM SALES_REPS CROSS JOIN OFFICES;


