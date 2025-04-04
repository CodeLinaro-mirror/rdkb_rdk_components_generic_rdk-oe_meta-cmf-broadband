do_install_append() {
        sed -i "s/skip-networking/#skip-networking/g" ${D}/etc/my.cnf
}
SYSTEMD_AUTO_ENABLE:${PN}-server = "enable"
