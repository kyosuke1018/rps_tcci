/*
  適用 2015/12/11 以前的 tcc-fc-model.jar, 之後的已直接加入 install_fc.sql 中.
*/
create index tc_applicationdata_idx1 on tc_applicationdata (containerclassname,containerid);

create index tc_urldata_idx1 on tc_urldata (containerclassname,containerid);

alter table tc_usergroup add constraint TC_USERGROUP_UK1 UNIQUE (USER_ID,GROUP_ID);