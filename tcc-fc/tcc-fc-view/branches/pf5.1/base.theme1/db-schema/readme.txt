-- create DB user
drop user fcview_user cascade;
create user fcview_user identified by abcd1234;
grant connect, resource to fcview_user;

-- install DB schema
@install_fc.sql
@install_yourApp.sql
...
@install_postConfig.sql