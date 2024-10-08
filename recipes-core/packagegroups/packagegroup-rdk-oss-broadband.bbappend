RDEPENDS_packagegroup-rdk-oss-broadband_append = " \
      ${@bb.utils.contains('DISTRO_FEATURES', 'enable_debug_tool', 'valgrind', '', d)} \
"
