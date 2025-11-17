do_install_append() {
        sed -i "s/skip-networking/#skip-networking/g" ${D}/etc/my.cnf
}
SYSTEMD_AUTO_ENABLE:${PN}-server = "enable"

# Optimize installed files for mariadb packages
FILES:${PN}-client = "\
    ${bindir}/mysql \
    ${bindir}/mariadb \
    ${bindir}/mariadb-admin \
    ${bindir}/mysqladmin \
    ${libexecdir}/mysqlmanager \
    ${sysconfdir}/my.cnf.d/mysql-clients.cnf \
"
FILES:${PN}-server = "\
    ${bindir}/mariadbd-safe \
    ${bindir}/mysqld_safe \
    ${bindir}/resolveip \
    ${bindir}/mysqld_safe_helper \
    ${bindir}/mariadbd-safe-helper \
    ${bindir}/mysql-systemd-start \
    ${libexecdir}/mysqld \
    ${sbindir}/mysqld \
    ${sbindir}/mariadbd \
    ${datadir}/mysql/ \
    ${sysconfdir}/my.cnf \
    ${sysconfdir}/my.cnf.d/server.cnf \
    ${sysconfdir}/security/user_map.conf \
"
