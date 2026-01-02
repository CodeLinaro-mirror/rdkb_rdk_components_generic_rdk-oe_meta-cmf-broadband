RDEPENDS_packagegroup-rdk-ccsp-broadband_append = " crashupload"
RDEPENDS_packagegroup-rdk-ccsp-broadband_append = " rdk-wps-monitor"
RDEPENDS_packagegroup-rdk-ccsp-broadband_remove = " rdm-agent"

RDEPENDS_packagegroup-rdk-ccsp-broadband_append = "${@bb.utils.contains('DISTRO_FEATURES', 'RDM', 'rdm-agent', ' ', d)}"
