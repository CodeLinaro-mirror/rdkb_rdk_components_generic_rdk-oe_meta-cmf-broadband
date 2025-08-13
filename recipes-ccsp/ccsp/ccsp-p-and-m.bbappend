DEPENDS_remove = "mountutils"

CFLAGS_append  += " ${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_cellular_manager_mm', ' -DFEATURE_RDKB_CELLULAR_MANAGER', '', d)}"
CFLAGS_append = " -DRBUS_WAN_IP "

CFLAGS_append = "${@bb.utils.contains("DISTRO_FEATURES", "resource_optimization", " -DRESOURCE_OPTIMIZATION ", " ", d)} "
ENABLE_RESOURCE_OPTIMIZATION = "--enable-resourceoptimization=${@bb.utils.contains('DISTRO_FEATURES', 'resource_optimization', 'yes', 'no', d)}"
EXTRA_OECONF_append = " ${ENABLE_RESOURCE_OPTIMIZATION}"

do_compile_prepend () {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'resource_optimization', 'true', 'false', d)}; then
        sed -i '2i <?define FEATURE_RESOURCE_OPTIMIZATION=True?>' ${S}/config-arm/TR181-USGv2.XML
    fi
}
