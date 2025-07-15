# Ensure Rust and Cargo are available
inherit cargo

DESCRIPTION = "IEEE 1905 Rust Program"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b538373fe584898492d2ad3a91014d58"

# Source repository
SRC_URI = "git://github.com/rdkcentral/rdkb-ieee1905.git;branch=dev;protocol=https"
SRCREV = "6763a722505df58a9e58928c269e628bd4112297"

# Source directory
S = "${WORKDIR}/git"

#dependencies from crates.io
require includes/ieee1905_dependencies.inc

FILES_${PN} += " \
    /usr/bin/* \
"

INSANE_SKIP_${PN} = "already-stripped"
