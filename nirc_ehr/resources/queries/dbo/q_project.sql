
SELECT min(alt.ALTERNATE_ID) as project,
       alt.project as name
FROM
    (
        SELECT trim(substring(NAME, 0, locate(' ', NAME,8))) as project,
               ALTERNATE_ID
        FROM ALTERNATE
        WHERE ALTERNATE_TYPE_ID = 6
    ) alt
GROUP BY alt.project