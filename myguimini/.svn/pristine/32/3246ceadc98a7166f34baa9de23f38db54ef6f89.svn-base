-- create DB user
drop user myguimini_user cascade;
create user myguimini_user identified by abcd1234;
grant connect, resource to myguimini_user;

-- install DB schema
@install_fc.sql
@install_yourApp.sql
...
@install_postConfig.sql