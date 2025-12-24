FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://process-capabilities_rdkb.json"

do_configure_prepend() {
   cp ${WORKDIR}/process-capabilities_rdkb.json ${S}/source/process-capabilities_broadband.json
}
