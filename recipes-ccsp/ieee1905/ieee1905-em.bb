# Ensure Rust and Cargo are available
inherit cargo systemd breakpad-wrapper

DESCRIPTION = "IEEE 1905 Rust Program"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b538373fe584898492d2ad3a91014d58"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# Source repository
SRC_URI = "git://github.com/rdkcentral/ieee1905-rs.git;nobranch=1;protocol=https"
SRC_URI += " file://0001-fixed-selection-of-the-node-when-multiple-nodes-have.patch"
SRCREV = "14e7efcdc4c93fa21fee8755f595bc874d72d5c4"
PV = "v0.4.0"

SRC_URI += "\
     ${@bb.utils.contains('DISTRO_FEATURES','em_extender',' file://ieee1905_em_ext_agent.service ',' file://ieee1905_em_agent.service ',d)} \
     ${@bb.utils.contains('DISTRO_FEATURES','em_extender',' ',' file://ieee1905_em_ctrl.service ',d)} \
"

#Breakpad support
DEPENDS = "breakpad breakpad-wrapper"
DEPENDS += " clang-native rbus "
CFLAGS += "-I${STAGING_INCDIR}/breakpad "
CXXFLAGS += "-I${STAGING_INCDIR}/breakpad "

export LIBCLANG_PATH = "${STAGING_LIBDIR_NATIVE}"

LDFLAGS_append = " \
    -lbreakpadwrapper \
    -lrbus \
"

RUSTFLAGS += "-L ${STAGING_LIBDIR} -l rbus"
BREAKPAD_BIN_append = " ieee1905-em"

# Source directory
S = "${WORKDIR}/git"

#dependencies from crates.io
require includes/ieee1905_dependencies.inc

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -D -m 0644 ${WORKDIR}/ieee1905_*.service ${D}${systemd_unitdir}/system/
    DISTRO_EM_EXT_ENABLED="${@bb.utils.contains('DISTRO_FEATURES','em_extender','true','false',d)}"
    if [ $DISTRO_EM_EXT_ENABLED = 'true' ]; then
       mv ${D}${systemd_unitdir}/system/ieee1905_em_ext_agent.service ${D}${systemd_unitdir}/system/ieee1905_em_agent.service
    fi
}

SYSTEMD_SERVICE_${PN} = " ${@bb.utils.contains('DISTRO_FEATURES','em_extender','',' ieee1905_em_ctrl.service',d)}"
SYSTEMD_SERVICE_${PN} += " ieee1905_em_agent.service"

FILES_${PN} += " \
    /usr/bin/* \
    ${systemd_unitdir}/system/* \
"

INSANE_SKIP_${PN} = "already-stripped"
