-- subquery to get staff info
-- email_prefix (used for display name) and transformEmailDomain '@louisiana.edu' (defined in etl xml) for user column transform
-- together creates a deactivated labkey user during an ETL run
SELECT
s.staff_id,
(s.staff_last_name ||', '|| s.staff_first_name) as staffName,
s.email_address,
(CASE
    WHEN s.email_address IS NULL THEN 'unknown'
    ELSE substr(s.email_address, 1,
                instr(s.email_address, '@') - 1) END) as email_prefix
FROM STAFF s