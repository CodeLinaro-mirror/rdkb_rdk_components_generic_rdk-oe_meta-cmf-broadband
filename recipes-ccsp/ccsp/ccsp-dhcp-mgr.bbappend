
inherit coverity

DEPENDS += " nanomsg"

do_install_append() {
   sed -i 's/PsmSsp.service/& ApplySystemDefaults.service/' ${D}/lib/systemd/system/CcspDHCPMgr.service
}
