FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append  += " file://RFCbase.sh "

do_install_append() {
        install -m 0755 ${S}/../RFCbase.sh ${D}${base_libdir}/rdk/RFCbase.sh
}
