/*******************************************************
* Trigger to insert transactions into the 
* new_transactions table when payroll info is added
* Authors: Jocelyn Wegen & Matthew Lee
* Date: December 3,2019
*******************************************************/
CREATE OR REPLACE NONEDITIONABLE TRIGGER LOAD_TRANSACTION_TABLE_BIR 
BEFORE INSERT ON PAYROLL_LOAD
FOR EACH ROW

BEGIN
  --Create the credit transaction
  INSERT INTO new_transactions
    VALUES(WKIS_SEQ.NEXTVAL, :NEW.payroll_date, 'Payroll Processed', 2050, 'C', :NEW.amount);  
  --Create the debit transaction
  INSERT INTO new_transactions
    VALUES(WKIS_SEQ.CURRVAL, :NEW.payroll_date, 'Payroll Processed', 4045, 'D', :NEW.amount);
    
  --Change the status to 'G' if everything is good.   
  :NEW.status := 'G';
  
  EXCEPTION
    WHEN OTHERS THEN
        --if something went wrong change the status to 'B'
        :NEW.status := 'B';
END;