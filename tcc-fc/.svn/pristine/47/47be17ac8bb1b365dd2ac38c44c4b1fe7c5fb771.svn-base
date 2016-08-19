-- create DB user
drop user bpmdemo_user cascade;
create user bpmdemo_user identified by abcd1234;
grant connect, resource to bpmdemo_user;

-- install DB schema
@install_fc.sql
@install_yourApp.sql
...
@install_postConfig.sql