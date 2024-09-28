SELECT
  p.Id,
 (SELECT group_concat(distinct p2.Id, chr(10)) AS Ids FROM study.pairings p2 WHERE p.Id != p2.id AND p.pairId = p2.pairId) as otherIds,
  p.pairid,
  p.date,
  p.enddate,
  TIMESTAMPDIFF('SQL_TSI_DAY', p.date, coalesce(p.enddate,curdate())) as duration,
  p.reason,
  p.goal,
  p.endState,
  p.observation,
  p.separationreason,
  p.remark as formationRemark,
  p.separationRemark,
  p.performedby,
  p.taskid,
  p.type

FROM study.pairings p
-- WHERE p.taskid IS NOT NULL

