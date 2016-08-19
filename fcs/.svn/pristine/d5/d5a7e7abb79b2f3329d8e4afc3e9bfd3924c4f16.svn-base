-- create DB user
drop user fcs_user cascade;
create user fcs_user identified by abcd1234;
grant connect, resource to fcs_user;

-- install DB schema
@install_fc.sql
@install_yourApp.sql
...
@install_postConfig.sql