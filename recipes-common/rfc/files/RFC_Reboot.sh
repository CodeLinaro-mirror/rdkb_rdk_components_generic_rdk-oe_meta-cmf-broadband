#! /bin/sh
##########################################################################
# If not stated otherwise in this file or this component's LICENSE
# file the following copyright and licenses apply:
#
# Copyright 2018 RDK Management
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
##########################################################################


if [ -f /etc/device.properties ] ; then
        . /etc/device.properties
fi

source /usr/ccsp/tad/corrective_action.sh

LOG_FILE="/rdklogs/logs/dcmrfc.log.0"

#Setting last reboot to rfc_reboot
echo_t "[RFC_Reboot.sh] setting last reboot to rfc_reboot" >> $LOG_FILE
setRebootreason rfc_reboot 1

isWanLinkHealEnabled=`syscfg get wanlinkheal`
if [ "x$isWanLinkHealEnabled" == "xtrue" ];then
        /usr/ccsp/tad/check_gw_health.sh store-health
fi

#take log back up and reboot

echo_t "[RFC_Reboot.sh] take log back up and reboot" >> $LOG_FILE
sh /rdklogger/backupLogs.sh &
