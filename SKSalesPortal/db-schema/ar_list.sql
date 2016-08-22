select sm.code,t.login_account,t.cname,count(m.id)  total_remit from tc_user t,sk_sales_member sm, sk_ar_remit_master m 
where t.id(+)=sm.member and t.id=m.creator(+) 
group by sm.code,t.login_account,t.cname 
order by sm.code,t.login_account;

select distinct sm.code,t.login_account,t.cname,l.action_name from tc_user t,sk_sales_member sm, sk_access_log l 
where t.id(+)=sm.member and t.id=l.user_id(+) order by sm.code,t.login_account;