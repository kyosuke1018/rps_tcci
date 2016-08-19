@rem goto setup directory
cd /d %~dp0

@rem deploy war to glassfish(DEV and QA)
call asadmin --host 192.168.203.81 multimode --file resources.conf
call asadmin --host 192.168.203.62 multimode --file resources-qa.conf
