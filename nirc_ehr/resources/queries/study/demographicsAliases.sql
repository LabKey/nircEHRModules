SELECT Id,
       GROUP_CONCAT(alias, ', ') alias,
       category.title as aliasType
FROM alias
GROUP BY Id, category.title
    PIVOT alias BY aliasType IN (SELECT title FROM ehr_lookups.alias_category)