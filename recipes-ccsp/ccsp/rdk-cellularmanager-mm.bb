SUMMARY = "RDK Cellular Manager MM component"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS = "ccsp-common-library rdk-logger utopia libunpriv halinterface glib-2.0 libqmi webconfig-framework curl trower-base64 msgpack-c libgudev rbus"
DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"

DEPENDS_append += "modemmanager"
DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'cellular_hybrid_support', ' libusb1', " ", d)}"
DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'cellular_hybrid_support', ' libnl', " ", d)}"
DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'cellular_hybrid_support', ' libmbim', " ", d)}"
DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'cellular_hybrid_support', ' udev', " ", d)}"

SRC_URI = "${CMF_GITHUB_ROOT}/cellular-modem-manager;protocol=https;nobranch=1;name=CellularManager-mm"
SRCREV_CellularManager-mm = "7d8b076deb7a36a06ace17eefe4bc0abd312d04a"
SRCREV_FORMAT = "CellularManager-mm"
PV = "1.0.0"
PR = "r0"

S = "${WORKDIR}/git"

inherit coverity

require recipes-ccsp/ccsp/ccsp_common.inc

inherit autotools pkgconfig systemd ${@bb.utils.contains("DISTRO_FEATURES", "kirkstone", "python3native", "pythonnative", d)}
EXTRA_OECONF_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'cellular_hybrid_support', 'IS_HYBRID_SUPPORT=true', '', d)}"

CFLAGS_append = " \
    -I${STAGING_INCDIR} \
    -I${STAGING_INCDIR}/dbus-1.0 \
    -I${STAGING_LIBDIR}/dbus-1.0/include \
    -I${STAGING_INCDIR}/ccsp \
    -I${STAGING_INCDIR}/libsafec \
    -I${STAGING_INCDIR}/glib-2.0 \
    -I${STAGING_LIBDIR}/glib-2.0/include \
    -I${STAGING_INCDIR}/libqmi-glib \
    -I${STAGING_INCDIR}/trower-base64 \
    -I${STAGING_INCDIR}/msgpackc \
    -DFEATURE_SUPPORT_RDKLOG \
    ${@bb.utils.contains('DISTRO_FEATURES', 'cellular_hybrid_support', ' -I${STAGING_INCDIR}/libusb-1.0', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'cellular_hybrid_support', ' -I${STAGING_INCDIR}/libnl3', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'cellular_hybrid_support', ' -DDUID_UUID_ENABLE', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'cellular_hybrid_support', ' -DFEATURE_RNDIS_HAL', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'cellular_hybrid_support', ' -DFEATURE_MODEM_HAL', '', d)} \
    "
LDFLAGS += " -lprivilege"
LDFLAGS_append = " -ldbus-1"
LDFLAGS += " -lgobject-2.0 -lgio-2.0 -lglib-2.0 -lgudev-1.0 -lqmi-glib"

CFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"
LDFLAGS_append_dunfell = "${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec-3.5.1 ', '', d)}"
LDFLAGS_append_kirkstone = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"

CFLAGS += "-I${STAGING_INCDIR}/libmm-glib/"
CFLAGS += "-I${STAGING_INCDIR}/ModemManager/"
CFLAGS += "-DMM_SUPPORT"
#CFLAGS += "-DQMI_SUPPORT"

LDFLAGS += "-lmm-glib"
LDFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'cellular_hybrid_support', ' -lnanomsg', '', d)}"
LDFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'cellular_hybrid_support', ' -lusb-1.0', '', d)}"
LDFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'cellular_hybrid_support', ' -ludev', '', d)}"
LDFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'cellular_hybrid_support', ' -lnl-3', '', d)}"
LDFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'cellular_hybrid_support', ' -lnl-route-3', '', d)}"

PACKAGES += "${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', '${PN}-gtest', '', d)}"

SYSTEMD_SERVICE_${PN} = "RdkCellularManager.service"

do_compile_prepend () {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'WanFailOverSupportEnable', 'true', 'false', d)}; then
    sed -i '2i <?define RBUS_BUILD_FLAG_ENABLE=True?>' ${S}/config/RdkCellularManager.xml
    fi

    if ${@bb.utils.contains('DISTRO_FEATURES', 'WanFailOverSupportEnable', 'true', 'false', d)}; then
        (${PYTHON} ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config/RdkCellularManager.xml ${S}/source/CellularManager/dm_pack_datamodel.c)
    fi
}

do_install_append () {
    # Config files and scripts
    install -d ${D}${exec_prefix}/rdk/cellularmanager
    ln -sf ${bindir}/cellularmanager ${D}${exec_prefix}/rdk/cellularmanager/cellularmanager
    install -m 644 ${S}/config/RdkCellularManager.xml ${D}${exec_prefix}/rdk/cellularmanager/
    if ${@bb.utils.contains('DISTRO_FEATURES', 'cellular_hybrid_support', 'true', 'false', d)}; then
    install -m 744 ${S}/config/run_dhcp.sh ${D}/usr/rdk/cellularmanager
    install -m 744 ${S}/config/enable_drivers.sh ${D}/usr/rdk/cellularmanager
    fi
    #Install systemd unit.
    install -d ${D}${systemd_unitdir}/system
    install -D -m 0644 ${S}/systemd_units/RdkCellularManager.service ${D}${systemd_unitdir}/system/RdkCellularManager.service
    if ${@bb.utils.contains('DISTRO_FEATURES', 'cellular_hybrid_support', 'true', 'false', d)}; then
        sed -i "/ExecStart/ i\ExecStartPre=/bin/sh -c '(/usr/rdk/cellularmanager/enable_drivers.sh)'" ${D}${systemd_unitdir}/system/RdkCellularManager.service
    fi
}

FILES_${PN} = " \
   ${bindir}/* \
   ${exec_prefix}/rdk/cellularmanager/* \
   ${systemd_unitdir}/system/RdkCellularManager.service \
   ${@bb.utils.contains('DISTRO_FEATURES', 'cellular_hybrid_support', ' ${exec_prefix}/rdk/cellularmanager/RdkCellularManager.xml', '', d)} \
   ${@bb.utils.contains('DISTRO_FEATURES', 'cellular_hybrid_support', ' ${exec_prefix}/rdk/cellularmanager/run_dhcp.sh', '', d)} \
   ${@bb.utils.contains('DISTRO_FEATURES', 'cellular_hybrid_support', ' ${exec_prefix}/rdk/cellularmanager/enable_drivers.sh', '', d)} \
"

FILES_${PN}-dbg = " \
    ${exec_prefix}/rdk/rdkcellularmanager/.debug \
    /usr/src/debug \
    ${bindir}/.debug \
    ${libdir}/.debug \
"

FILES_${PN}-gtest = "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', '${bindir}/RdkCellularManager_gtest.bin', '', d)} \
"

DOWNLOAD_APPS="${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', 'gtestapp-RdkCellularManager', '', d)}"
inherit comcast-package-deploy
CUSTOM_PKG_EXTNS="${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', 'gtest', '', d)}"
SKIP_MAIN_PKG="${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', 'yes', 'no', d)}"
DOWNLOAD_ON_DEMAND="${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', 'yes', 'no', d)}"

EXTRA_OECONF_append  = " --with-ccsp-platform=bcm --with-ccsp-arch=arm "
