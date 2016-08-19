-- ACTIVITY template
select a.activityname, a.activitytype, a.rolename, a.options, a.duration, a.expression, p.id as pid, p.processname, p.processversion
from tc_activitytemplate a
inner join tc_processtemplate p on p.id=a.processtemplateid
order by p.id
;

-- ACTIVITY ROUTE template
select fra.activityname, toa.activityname, ar.routename, p.id as pid, p.processname, p.processversion
from tc_activityroutetemplate ar
inner join tc_activitytemplate fra on fra.id=ar.fromactivity
inner join tc_activitytemplate toa on toa.id=ar.toactivity
inner join tc_processtemplate p on p.id=fra.processtemplateid
order by p.id
;