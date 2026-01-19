SUMMARY = "Unified-wifi-mesh"
HOMEPAGE = "http://github.com/rdkcentral/unified-wifi-mesh"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e0b1ae637439c7d6f4487fb90163c79a"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI = "git://github.com/rdkcentral/unified-wifi-mesh.git;nobranch=1;protocol=https;name=Unified-wifi-mesh"
PV_Unified-wifi-mesh = "v0.1.0"
SRCREV_Unified-wifi-mesh = "ee309387b2922e18702e162b8ac8dac33886d654"
SRCREV_FORMAT = "Unified-wifi-mesh"

SRC_URI += "git://github.com/rdkcentral/OneWifi.git;branch=develop;protocol=https;name=OneWifi;destsuffix=git/OneWifi"
SRCREV_OneWifi = "74ea1f6ca37612b13cfccba6213fe3fb06beb982"

SRC_URI += " ${@bb.utils.contains('DISTRO_FEATURES', 'em_extender', ' file://ext_em_agent.service', ' file://em_agent.service', d)}"
SRC_URI += " ${@bb.utils.contains('DISTRO_FEATURES', 'em_extender', '', ' file://em_ctrl.service', d)}"
SRC_URI += " ${@bb.utils.contains('DISTRO_FEATURES', 'em_extender', '', ' file://em_cli.service', d)}"
SRC_URI += " ${@bb.utils.contains('DISTRO_FEATURES', 'em_extender', '', ' file://setup_mysql_db_pre.sh', d)}"
SRC_URI += " ${@bb.utils.contains('DISTRO_FEATURES', 'em_extender', '', ' file://setup_mysql_db_post.sh', d)}"
SRC_URI += " ${@bb.utils.contains('DISTRO_FEATURES', 'em_extender', '', ' file://setup_agent_pre.sh', d)}"
SRC_URI += " ${@bb.utils.contains('DISTRO_FEATURES', 'em_extender', ' file://setup_ext_pre.sh', '', d)}"

S = "${WORKDIR}/git"

DEPENDS = " ccsp-one-wifi rbus rdk-wifi-halif mariadb gtest breakpad breakpad-wrapper"
DEPENDS += "gcc-sanitizers"
RDEPENDS:${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'em_extender', ' ', ' mariadb', d)}"

inherit autotools pkgconfig systemd breakpad-wrapper
CFLAGS += "-I${STAGING_INCDIR}/breakpad "
CXXFLAGS += "-I${STAGING_INCDIR}/breakpad "

CPPFLAGS_append = " \
    -I${STAGING_INCDIR} \
    -I${STAGING_INCDIR}/rbus \
    -I${STAGING_INCDIR}/ccsp \
    -I${STAGING_INCDIR}/dbus-1.0 \
    -I${STAGING_LIBDIR}/dbus-1.0/include \
"
CPPFLAGS_append = " -g -DEASY_MESH_NODE -DEM_APP -std=c++17 -D_PLATFORM_BANANAPI_R4_ "
CPPFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'with_alsap',' -DAL_SAP', '', d)}"
CFLAGS_append = " -D_PLATFORM_BANANAPI_R4_ "

LDFLAGS_append = " \
    -lm \
    -lcjson \
    -lpthread \
    -ldl \
    -luuid \
    -lssl \
    -lcrypto \
    -lrbus \
    -lbreakpadwrapper \
"
EXTRA_OECONF_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'em_extender', 'EM_EXTENDER=true', 'EM_EXTENDER=false', d)}"

#minidump support
BREAKPAD_BIN_append = " onewifi_em_ctrl "
BREAKPAD_BIN_append = " onewifi_em_agent"

do_install_append() {
    install -d ${D}/usr/ccsp/EasyMesh
    install -d ${D}${systemd_unitdir}/system
    install -m 644 ${S}/install/bin/*  ${D}/usr/ccsp/EasyMesh
    install -m 755 ${S}/config/rdkb/banana-pi/setup_veth*.sh  ${D}/usr/ccsp/EasyMesh
    install -m 755 ${WORKDIR}/setup_*.sh ${D}/usr/ccsp/EasyMesh
    DISTRO_EM_EXT_ENABLED="${@bb.utils.contains('DISTRO_FEATURES','em_extender','true','false',d)}"
    if [ $DISTRO_EM_EXT_ENABLED = 'true' ]; then
       cp ${WORKDIR}/ext_em_agent.service ${WORKDIR}/em_agent.service
    fi
    install -D -m 0644 ${WORKDIR}/em_*.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_SERVICE_${PN} = " em_agent.service"
SYSTEMD_SERVICE_${PN} += " ${@bb.utils.contains('DISTRO_FEATURES','em_extender','',' em_ctrl.service em_cli.service ',d)}"

FILES_${PN} += "${libdir}/*.so*  ${bindir}/* /usr/ccsp/EasyMesh/* "
FILES_${PN} += "${systemd_unitdir}/system/* "

