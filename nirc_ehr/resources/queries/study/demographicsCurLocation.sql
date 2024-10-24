/*
 * Copyright (c) 2010-2019 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
SELECT

d2.id,

d2.cage AS Location,

d2.room.area,

d2.room,

d2.cage AS cage,

ifdefined(d2.cond) as cond,

d2.date,

d2.reason,

d2.remark,

coalesce(d2.room, '') as room_order,
d2.room_sortValue @hidden,

coalesce(d2.cage, '') as cage_order,
d2.cage_sortValue @hidden

FROM study.housing d2

WHERE d2.enddate IS NULL
AND d2.qcstate.publicdata = true