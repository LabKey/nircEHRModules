
SELECT Id AS project,
       Name AS name,
       PROJECT_CATEGORY_ID.NAME AS use_category,
       DESCRIPTION AS title,
       ACTIVE_YN AS avail
FROM PROJECT_CODE