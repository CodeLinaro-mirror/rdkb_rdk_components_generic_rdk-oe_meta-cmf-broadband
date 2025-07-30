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
