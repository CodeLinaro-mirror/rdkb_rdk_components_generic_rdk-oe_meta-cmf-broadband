inherit coverity

DEPENDS += " utopia"

CFLAGS_append = " -Wno-incompatible-pointer-types -Wno-implicit-function-declaration "

CFLAGS_append = " \
    -I${S}/source/GponManager \
    -I${S}/source/TR-181/middle_layer_src \
    -I${S}/source/TR-181/include \
    "
