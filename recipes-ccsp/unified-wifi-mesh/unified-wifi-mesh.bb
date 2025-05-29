SUMMARY = "Unified-wifi-mesh"
HOMEPAGE = "http://github.com/rdkcentral/unified-wifi-mesh"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e0b1ae637439c7d6f4487fb90163c79a"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI = "git://github.com/rdkcentral/unified-wifi-mesh.git;branch=main;protocol=https;name=Unified-wifi-mesh"
PV = "git${SRCPV}"
SRCREV_Unified-wifi-mesh = "4eca0f60752cb76a6e5ac86b10ba08e2ce6210bd"
SRCREV_FORMAT = "Unified-wifi-mesh"

SRC_URI += "git://github.com/rdkcentral/OneWifi.git;branch=develop;protocol=https;name=OneWifi;destsuffix=git/OneWifi"
SRCREV_OneWifi = "5f68e4e1d965d7ceaf378a4e0bd94f8d2dcbcccd"

SRC_URI += " file://em_agent.service"
SRC_URI += " file://em_ctrl.service"

S = "${WORKDIR}/git"

DEPENDS = " ccsp-one-wifi rbus halinterface mariadb "
DEPENDS += "gcc-sanitizers"
RDEPENDS:${PN} += "mariadb "

inherit autotools pkgconfig systemd

CPPFLAGS_append = " \
    -I${STAGING_INCDIR} \
    -I${STAGING_INCDIR}/rbus \
    -I${STAGING_INCDIR}/ccsp \
    -I${STAGING_INCDIR}/dbus-1.0 \
    -I${STAGING_LIBDIR}/dbus-1.0/include \
"
CPPFLAGS_append = " -g -DEASY_MESH_NODE -DEM_APP -std=c++17 -D_PLATFORM_BANANAPI_R4_ "
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
"

do_install_append() {
    install -d ${D}/usr/ccsp/EasyMesh
    install -d ${D}${systemd_unitdir}/system
    install -m 644 ${S}/install/bin/*  ${D}/usr/ccsp/EasyMesh
    install -D -m 0644 ${WORKDIR}/em_*.service ${D}${systemd_unitdir}/system/
}

FILES_${PN} += "${libdir}/*.so*  ${bindir}/* /usr/ccsp/EasyMesh/* "
FILES_${PN} += "${systemd_unitdir}/system/* "

