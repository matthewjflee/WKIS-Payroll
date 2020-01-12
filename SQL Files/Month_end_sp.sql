/*********************************************************
* Procedure to insert transactions in the new-transactions
* table for month end. These transactions zero out
* the temporary 'REV' and 'EX' accounts in the 
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