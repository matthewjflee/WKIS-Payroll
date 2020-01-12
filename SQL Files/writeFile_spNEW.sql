/****************************************************
* Procudure to write the contents of the 
* new_transaction table into a delimited
* text file. 
* Authors: Jocelyn Wegen & Matthew Lee
* Date: December 3, 2019
****************************************************/
CREATE or REPLACE NONEDITIONABLE PROCEDURE WRITE_FILE_SP
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
    --Close the file
    UTL_FILE.FCLOSE(file_handler);
    EXCEPTION 
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('Error Code: ' || SQLCODE);
            DBMS_OUTPUT.PUT_LINE('Error Message: ' || SQLERRM);
END;