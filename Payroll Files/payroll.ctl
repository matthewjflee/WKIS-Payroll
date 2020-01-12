LOAD DATA
INFILE 'C:\Users\783661\Documents\Payroll\payroll.txt'
REPLACE
INTO TABLE payroll_load
FIELDS TERMINATED BY ';' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(payroll_date DATE "Month dd, yyyy",
employee_id,
amount,
status)
