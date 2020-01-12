/**********************************************************
* Procudure to check if the user has permission to complete
* the payroll activities.
* Authors: Jocelyn Wegen & Matthew Lee
* Date: December 3, 2019
***********************************************************/
CREATE OR REPLACE NONEDITIONABLE FUNCTION CHECK_USER_PERMISSION_FN 
RETURN VARCHAR2 AS 
    lv_permission VARCHAR2(2) := 'N';
BEGIN
--Y if the permission is there
  SELECT 'Y' 
    INTO lv_permission
    FROM user_tab_privs
    WHERE table_name='UTL_FILE' 
    AND privilege = 'EXECUTE';
  RETURN lv_permission;
  
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        --if select statement throw expception -keep N
        RETURN lv_permission;
END CHECK_USER_PERMISSION_FN;

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

/*********************************************************
* Procedure to insert transactions in the new-transactions
* table for month end. These transactions zero out
* the temporary 'RE' and 'EX' accounts in the 
* account table. 
* Authors: Jocelyn Wegen & Matthew Lee
* Date: December 3, 2019
*********************************************************/
CREATE OR REPLACE NONEDITIONABLE PROCEDURE MONTH_END_SP 
AS 
    --Cursor to go through account table 'RE' and 'EX' columns
    CURSOR cur_account IS
        SELECT account_no, account_type_code, account_balance
        FROM account
        WHERE account_type_code = 'RE'
        OR account_type_code = 'EX';
        
    k_revenue CONSTANT account.account_type_code%TYPE := 'RE';
    k_expense CONSTANT account.account_type_code%TYPE := 'EX';

BEGIN
  --loop through the account table
  FOR rec_account IN cur_account LOOP
    --if account_type is 'RE' 
    IF rec_account.account_type_code = k_revenue THEN

        --debit the account with account_balance
        INSERT INTO new_transactions
            VALUES(WKIS_SEQ.NEXTVAL,
                    TO_CHAR(SYSDATE, 'DD-MON-YY'),
                    'Monthend roll to owner equity',
                    rec_account.account_no,
                    'D',
                    rec_account.account_balance);

        --credit the owner's equity account with account_balance
        INSERT INTO new_transactions
            VALUES(WKIS_SEQ.CURRVAL,
                    TO_CHAR(SYSDATE, 'DD-MON-YY'),
                    'Monthend roll to owner equity',
                    5555,
                    'C',
                    rec_account.account_balance);
    --if account_type is 'EX'
    ELSIF rec_account.account_type_code = k_expense THEN
        --credit the account with accout_balance
        INSERT INTO new_transactions
            VALUES(WKIS_SEQ.NEXTVAL,
                    TO_CHAR(SYSDATE, 'DD-MON-YY'),
                    'Monthend roll to owner equity',
                    rec_account.account_no,
                    'C',
                    rec_account.account_balance);

        --debit the owner's equity account with account_balance
        INSERT INTO new_transactions
            VALUES(WKIS_SEQ.CURRVAL,
                    TO_CHAR(SYSDATE, 'DD-MON-YY'),
                    'Monthend roll to owner equity',
                    5555,
                    'D',
                    rec_account.account_balance);
    END IF;
  END LOOP;
END MONTH_END_SP;

/****************************************************
* Procudure to write the contents of the 
* new_transaction table into a delimited
* text file. 
* Authors: Jocelyn Wegen & Matthew Lee
* Date: December 3, 2019
****************************************************/
CREATE OR REPLACE PROCEDURE WRITE_FILE_SP
    (p_directory_alias IN VARCHAR2,
     p_file IN VARCHAR2)

AS
    --cursor to go through new_transaction table
    CURSOR cur_new_tran IS
        SELECT * 
            FROM new_transactions;
    --utl_file variable
    file_handler UTL_FILE.FILE_TYPE;

BEGIN
    --create and open the file to write to
    file_handler := UTL_FILE.FOPEN(
        p_directory_alias,
        p_file,
        'w'
    );
    --loop through the new_transaction table
    FOR rec_new_tran IN cur_new_tran LOOP
        --write all the columns into file, delimited with a comma
        UTL_FILE.PUT(file_handler, rec_new_tran.transaction_no);
        UTL_FILE.PUT(file_handler, ',');
        UTL_FILE.PUT(file_handler, rec_new_tran.transaction_date);
        UTL_FILE.PUT(file_handler, ',');
        UTL_FILE.PUT(file_handler, rec_new_tran.description);
        UTL_FILE.PUT(file_handler, ',');
        UTL_FILE.PUT(file_handler, rec_new_tran.account_no);
        UTL_FILE.PUT(file_handler, ',');
        UTL_FILE.PUT(file_handler, rec_new_tran.transaction_type);
        UTL_FILE.PUT(file_handler, ',');
        UTL_FILE.PUT(file_handler, rec_new_tran.transaction_amount);
        UTL_FILE.PUT_LINE(file_handler, '');
    END LOOP;
    --close the file
     UTL_FILE.FCLOSE(file_handler);
    EXCEPTION 
        WHEN OTHERS THEN
            --just in case an error occurs.
            DBMS_OUTPUT.PUT_LINE('Error Code: ' || SQLCODE);
            DBMS_OUTPUT.PUT_LINE('Error Message: ' || SQLERRM);
END;

