-- create DB user
drop user ecdemo_user cascade;
create user ecdemo_user identified by abcd1234;
grant connect, resource to ecdemo_user;

-- install DB schema
@install_fc.sql
@install_yourApp.sql
...
@install_postConfig.sql