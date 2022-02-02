SELECT
    DEPARTMENT_ID           as DepartmentId,
    DEPARTMENT_NAME         as Name,
    DEPARTMENT_DESC         as Description,
    PARENT_DEPARTMENT_ID    as ParentDepartment,
    STAFF_ID.STAFF_FIRST_NAME
        || '|' || STAFF_ID.STAFF_LAST_NAME as Staff
FROM DEPARTMENT