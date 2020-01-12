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