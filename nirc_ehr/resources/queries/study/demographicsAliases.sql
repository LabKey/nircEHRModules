SELECT Id,
       GROUP_CONCAT(alias, ', ') alias,
       category.title as aliasType
FROM alias
GROUP BY Id, category.title
    PIVOT alias BY aliasType IN ('Microchip','Nickname','Old Tattoo','ISIS Stud Book Number')