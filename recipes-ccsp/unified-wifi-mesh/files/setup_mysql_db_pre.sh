#!/bin/sh

#Controller onboard should be happen if we switch the same sd card to any new boards
if [ -f "/nvram/mysql_db_data_exists" ]; then
Existing_al_mac=`mysql -u bpi --password="root" -D OneWifiMesh -e "select ColocatedAgentID from NetworkList " |  sed 's/|/ /' | tail -n1`
Present_al_mac=`ifconfig eth0_virt_peer | grep HWaddr | cut -d ' ' -f6 | tr '[:upper:]' '[:lower:]'`
   if [ "$Present_al_mac" != "$Existing_al_mac" ]; then
      echo "AL_MAC address is changed now.. so wifi reset is required.."
      rm /nvram/mysql_db_*
      rm /nvram/initial_restart_ctrl
      #password is not sensitive,used to drop existing DB from mariadb
      mysql -u bpi --password="root" -e "drop database OneWifiMesh;"
      mysql -u bpi --password="root" -e "drop user bpi@localhost;"
   fi
fi

#mysql database user account creation
if [ ! -e "/nvram/mysql_db_account_exists" ]; then
mysql -e "CREATE USER 'bpi'@'localhost' IDENTIFIED BY 'root';"
mysql -e "ALTER USER 'bpi'@'localhost' IDENTIFIED BY 'root';"
mysql -e "GRANT ALL PRIVILEGES ON *.* TO 'bpi'@'localhost' IDENTIFIED BY 'root';"
mysql -e "FLUSH PRIVILEGES;"
#password is not sensitive,used to create db in mariadb
mysql -u bpi --password="root" -e "create database OneWifiMesh;"
sleep 30
touch /nvram/mysql_db_account_exists
fi
