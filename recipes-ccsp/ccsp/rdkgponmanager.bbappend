inherit coverity

DEPENDS += " breakpad breakpad-wrapper utopia"

CFLAGS_append = " -Wno-unused-variable -Wno-implicit-function-declaration -Wno-unused-variable -Wno-parentheses -Wno-format -Wno-switch -Wno-incompatible-pointer-types -Wno-pointer-sign "

CFLAGS_append = " \
    -I${S}/source/GponManager \
    -I${S}/source/TR-181/middle_layer_src \
    -I${S}/source/TR-181/include \
    -I${STAGING_INCDIR}/breakpad \
    "

CXXFLAGS += "-I${STAGING_INCDIR}/breakpad "

LDFLAGS += "-lbreakpadwrapper -lpthread"
