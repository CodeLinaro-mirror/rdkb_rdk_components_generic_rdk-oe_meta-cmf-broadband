SRC_URI_remove = "${RDKB_CCSP_ROOT_GIT}/hal/rdk-wifi-hal;protocol=${RDK_GIT_PROTOCOL};branch=${CCSP_GIT_BRANCH};name=rdk-wifi-hal"
SRC_URI += "${CMF_GIT_ROOT}/rdkb/components/opensource/ccsp/hal/rdk-wifi-hal;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=rdk-wifi-hal"

inherit coverity

DEPENDS_remove = "mountutils"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://REFPLTB-3265_Remove-duplicate-function-defination.patch;apply=no"

# we need to patch to code for RPi
do_rdk_wifi_hal_patches() {
    cd ${S}
    cd ..
    if [ ! -e rdk_wifi_hal_patch_applied ]; then
        bbnote "Patching REFPLTB-3265_Remove-duplicate-function-defination.patch"
        patch -p1 < ${WORKDIR}/REFPLTB-3265_Remove-duplicate-function-defination.patch

        touch rdk_wifi_hal_patch_applied
    fi
}
addtask rdk_wifi_hal_patches after do_unpack before do_compile
