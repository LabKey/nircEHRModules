SELECT ID AS "objectId",
       ID AS "project",
       CODE_NUMBER AS "name",
       DESCRIPTION AS "title",
       CASE WHEN PROJECT_CATEGORY_ID = 1 THEN 'Standard'
            WHEN PROJECT_CATEGORY_ID = 20 THEN 'Overhead'
           END AS "projectCategory",
       ACTIVE_YN AS "isActive"
FROM PROJECT_CODE p