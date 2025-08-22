#!/bin/sh

#Ensure onewifi is up and running
while [ ! -e /tmp/wifi_initialized ] && [ ! -e /tmp/wifi_dml_complete ] ; 
do   
   sleep 1; 
done

#Ensure backhaul connectivity is established
al_mac_addr=`cat /nvram/EasymeshCfg.json | grep AL_MAC_ADDR  | cut -d '"' -f4`
channel_exists=`iw dev | grep $al_mac_addr  -A 4 | grep channel | wc -l`
ssid_exists=`iw dev | grep $al_mac_addr -A 4 | grep ssid | wc -l`

while [ "$channel_exists" != 1 ] && [ "$ssid_exists" != 1 ] ;
do
  sleep 1;
  channel_exists=`iw dev | grep $al_mac_addr  -A 4 | grep channel | wc -l`
  ssid_exists=`iw dev | grep $al_mac_addr -A 4 | grep ssid | wc -l`
done

#Run udhcpc to get ipaddr of brlan0 interface for connected clients internet connectivity
brlan0_ip_addr=`ifconfig brlan0 | grep "inet addr:" | cut -d ':' -f2 | cut -d ' ' -f1 | wc -l`
if [ "$brlan0_ip_addr" = 0 ]; then
udhcpc -i  brlan0 -q
fi
