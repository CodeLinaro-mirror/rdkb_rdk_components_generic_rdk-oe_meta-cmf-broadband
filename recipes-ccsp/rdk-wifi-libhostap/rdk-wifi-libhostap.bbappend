SRC_URI_remove += "${RDKB_CCSP_ROOT_GIT}/rdk-wifi-libhostap;protocol=${RDK_GIT_PROTOCOL};branch=${RDK_GIT_BRANCH};name=rdk-wifi-libhostap"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

S = "${WORKDIR}/git"

SRC_URI += "git://w1.fi/hostap.git;protocol=https;branch=main;destsuffix=${S}/source/hostap-${HOSTAPD_PV};name=${HOSTAPD_PV}"
SRCREV_2.10 = "9d07b9447e76059a2ddef2a879c57d0934634188"
SRCREV_2.11 = "d945ddd368085f255e68328f2d3b020ceea359af"

SRC_URI_append = " \
${@bb.utils.contains('DISTRO_FEATURES', 'HOSTAPD_2_10', '\
file://2.10/005_RDKB_40014_Integrate_hostapd_2_10.patch \
file://2.10/009-RDKB-44454-Store-assoc-request-in-sta-struct.patch ',\
' ', d)}"

do_configure_append() {
${@bb.utils.contains('DISTRO_FEATURES', 'HOSTAPD_2_11', 'echo "CONFIG_OWE=y" >> ${S}/source/hostap-${HOSTAPD_PV}/hostapd/.config', '',d)}
}
