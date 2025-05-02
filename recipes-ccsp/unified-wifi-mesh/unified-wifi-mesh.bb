SUMMARY = "Unified-wifi-mesh"
HOMEPAGE = "http://github.com/rdkcentral/unified-wifi-mesh"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e0b1ae637439c7d6f4487fb90163c79a"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI = "git://github.com/rdkcentral/unified-wifi-mesh.git;branch=main;protocol=https;name=Unified-wifi-mesh"
PV = "git${SRCPV}"
SRCREV_Unified-wifi-mesh = "917744996a4d97e625157124ec7a50674b85b66b"
SRCREV_FORMAT = "Unified-wifi-mesh"

SRC_URI += "git://github.com/rdkcentral/OneWifi.git;branch=develop;protocol=https;name=OneWifi;destsuffix=git/OneWifi"
SRCREV_OneWifi = "4acc4b4633a70e1bd19d8ed4f08f1585ecce8415"

SRC_URI += " file://em_agent.service"
SRC_URI += " file://em_ctrl.service"

S = "${WORKDIR}/git"

DEPENDS = " ccsp-one-wifi rbus halinterface mariadb mysql-connector-cpp "
DEPENDS += "gcc-sanitizers"

inherit autotools pkgconfig systemd

CPPFLAGS_append = " \
    -I${STAGING_INCDIR} \
    -I${STAGING_INCDIR}/rbus \
    -I${STAGING_INCDIR}/ccsp \
    -I${STAGING_INCDIR}/dbus-1.0 \
    -I${STAGING_LIBDIR}/dbus-1.0/include \
"
CPPFLAGS_append = " -g -DEASY_MESH_NODE -DEM_APP -std=c++17 "

LDFLAGS_append = " \
    -lm \
    -lcjson \
    -lpthread \
    -ldl \
    -luuid \
    -lssl \
    -lcrypto \
    -lrbus \
    -lmysqlcppconn \
"

do_install_append() {
    install -d ${D}/usr/ccsp/EasyMesh
    install -d ${D}${systemd_unitdir}/system
    install -m 644 ${S}/install/bin/*  ${D}/usr/ccsp/EasyMesh
    install -D -m 0644 ${WORKDIR}/em_*.service ${D}${systemd_unitdir}/system/
}

FILES_${PN} += "${libdir}/*.so*  ${bindir}/* /usr/ccsp/EasyMesh/* "
FILES_${PN} += "${systemd_unitdir}/system/* "

