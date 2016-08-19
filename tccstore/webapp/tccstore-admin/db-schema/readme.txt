-- create DB user
drop user storeadmin_user cascade;
create user storeadmin_user identified by abcd1234;
grant connect, resource to storeadmin_user;

-- install DB schema
@install_fc.sql
@install_yourApp.sql
...
@install_postConfig.sql