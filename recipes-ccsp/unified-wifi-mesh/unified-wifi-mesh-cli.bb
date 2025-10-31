SUMMARY = "Unified-wifi-mesh for cli "
HOMEPAGE = "http://github.com/rdkcentral/unified-wifi-mesh"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${S}/src/import/LICENSE;md5=e0b1ae637439c7d6f4487fb90163c79a"

SRC_URI = "git://github.com/rdkcentral/unified-wifi-mesh.git;branch=main;protocol=https;name=Unified-wifi-mesh-cli"
PV = "git${SRCPV}"
SRCREV_Unified-wifi-mesh-cli = "1bca8d4bb197cf295b7148d766c36894a3a9c33a"
SRCREV_FORMAT = "Unified-wifi-mesh-cli"

GO_IMPORT = "import"

S = "${WORKDIR}/git"

inherit goarch
inherit go

DEPENDS = " readline ccsp-one-wifi ccsp-one-wifi-libwebconfig unified-wifi-mesh-header unified-wifi-mesh go "
RDEPENDS:${PN} = " unified-wifi-mesh"

EXTRA_OEMAKE = "GO='${GO}'"

CFLAGS_append = " \
    -I${STAGING_INCDIR} \
    -I${STAGING_INCDIR}/ccsp \
    -I=${includedir}/rbus \ 
"
CFLAGS_append = " -g -DEASY_MESH_NODE -DEM_APP -fPIC "

LDFLAGS_append = " -lemcli "

do_fetch_mod () {
	export GOPATH="${S}"
	cd ${S}/src/import/src/cli
	go get -a
}
do_fetch_mod[network] = "1"

addtask fetch_mod after do_unpack do_prepare_recipe_sysroot before do_configure

do_compile() {
	export GOARCH="${TARGET_GOARCH}"
	export GOROOT="${STAGING_LIBDIR}/go"

	export GOPATH="${S}"

	# Pass the needed cflags/ldflags so that cgo
	# can find the needed headers files and libraries
	export CGO_ENABLED="1"
	export CFLAGS=""
	export LDFLAGS=""
	export CGO_CFLAGS="${TARGET_CFLAGS} ${CFLAGS}"
	export CGO_LDFLAGS="${TARGET_LDFLAGS} ${LDFLAGS}"
 
	cd ${S}/src/import/src/cli
	oe_runmake build 
	cd -
	# For clean task
	chmod -R u+w ${S}/pkg
}

do_install() {
        install -d ${D}/usr/bin
        install -d ${D}/nvram
        install -m 755 ${S}/src/import/src/cli/onewifi_em_cli  ${D}/usr/bin
        install -m 664 ${S}/src/import/install/config/*  ${D}/nvram 
}

FILES_${PN} += " ${bindir}/* /nvram/* "
