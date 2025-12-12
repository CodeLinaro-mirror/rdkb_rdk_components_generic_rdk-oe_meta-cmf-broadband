SUMMARY = "Unified-wifi-mesh for cli "
HOMEPAGE = "http://github.com/rdkcentral/unified-wifi-mesh"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${S}/src/import/LICENSE;md5=e0b1ae637439c7d6f4487fb90163c79a"

SRC_URI = "git://github.com/rdkcentral/unified-wifi-mesh.git;nobranch=1;protocol=https;name=Unified-wifi-mesh-cli"
PV_Unified-wifi-mesh = "v0.1.0"
SRCREV_Unified-wifi-mesh-cli = "f7cbc0057874477685a162f7bbc40be666cc3220"
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
	cd ${S}/src/import/src/rdkb-cli
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
 
	cd ${S}/src/import/src/rdkb-cli
	oe_runmake build 
	cd -
	# For clean task
	chmod -R u+w ${S}/pkg
}

do_install() {
        install -d ${D}/usr/bin
        install -d ${D}/nvram
        install -d ${D}/nvram/static
        install -m 755 ${S}/src/import/src/rdkb-cli/onewifi_em_cli  ${D}/usr/bin
        install -m 664 ${S}/src/import/install/config/*  ${D}/nvram
        install -m 664 ${S}/src/import/install/bin/Reset.json  ${D}/nvram
        cp -rf ${S}/src/import/src/rdkb-cli/static/*  ${D}/nvram/static
}

FILES_${PN} += " ${bindir}/* /nvram/* /nvram/static/* "
