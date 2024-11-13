inherit coverity

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

DEPENDS_append = " protobuf-c"
RDEPENDS_${PN}_append = " openvswitch"
LDFLAGS_remove = " -ldpp"

SRC_URI += "file://0001-RDKB-57515-Fix-RPI-segmentation-fault-issue.patch;apply=no"

# we need to patch to code for RPi
do_onewifi_patches() {
    cd ${S}
    if [ ! -e onewifi_patch_applied ]; then
        bbnote "Patching 0001-RDKB-57515-Fix-RPI-segmentation-fault-issue.patch"
        patch -p1 < ${WORKDIR}/0001-RDKB-57515-Fix-RPI-segmentation-fault-issue.patch

        touch onewifi_patch_applied
    fi
}
addtask onewifi_patches after do_unpack before do_compile
