FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI_append = " file://RDM_HTTPS_FLAG.patch"
DEPENDS_remove = "mountutils"
EXTRA_OECONF_remove = " --enable-mountutils=yes --enable-unittest"

FILES_${PN}_remove = "${libdir}/librdmopenssl.la"
