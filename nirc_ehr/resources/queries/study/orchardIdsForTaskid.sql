PARAMETERS (MYTASKID VARCHAR)

SELECT Id FROM (SELECT Id, taskid
                FROM study.demographics
                UNION
                SELECT Id, taskid
                FROM study.housing
                UNION
                SELECT Id, taskid
                FROM study.protocolAssignment
                ) u
WHERE u.taskid IS NOT NULL AND u.taskid = MYTASKID;