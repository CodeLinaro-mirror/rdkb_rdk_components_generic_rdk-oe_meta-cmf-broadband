FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

CFLAGS_append  += " ${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_cellular_manager_mm', ' -DFEATURE_RDKB_CELLULAR_MANAGER', '', d)}"

do_install_append_class-target () {
         DISTRO_OneWiFi_ENABLED="${@bb.utils.contains('DISTRO_FEATURES','OneWifi','true','false',d)}"
         if [ $DISTRO_OneWiFi_ENABLED = 'false' ]; then
    	       install -D -m 0644 ${S}/systemd_units/ccspwifiagent.service ${D}${systemd_unitdir}/system/ccspwifiagent.service
	       sed -i "s/ExecStart=\/usr\/bin\/CcspWifiSsp -subsys \$Subsys/ExecStart=\/bin\/sh -c '\/usr\/bin\/CcspWifiSsp -subsys \$Subsys 2\&\>\/rdklogs\/logs\/wifihal.log'/g" ${D}/lib/systemd/system/ccspwifiagent.service
         fi 
}

SRC_URI += "file://0001-RDKB-58521-WAN-Manager-Telemetry-Markers-2.0-Phase-1.patch;apply=no"

do_ccspcommonlibrary_patches() {
    cd ${S}
    if [ ! -e cmf_patch_applied ]; then
        bbnote "Patching 0001-RDKB-58521-WAN-Manager-Telemetry-Markers-2.0-Phase-1.patch"
        patch -p1 < ${WORKDIR}/0001-RDKB-58521-WAN-Manager-Telemetry-Markers-2.0-Phase-1.patch
        touch cmf_patch_applied
    fi
}

addtask ccspcommonlibrary_patches after do_unpack do_patch before do_configure
