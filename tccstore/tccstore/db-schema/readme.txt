-- create DB user
drop user tccstore_user cascade;
create user tccstore_user identified by abcd1234;
grant connect, resource to tccstore_user;

-- install DB schema
@install_fc.sql
@install_yourApp.sql
...
@install_postConfig.sql