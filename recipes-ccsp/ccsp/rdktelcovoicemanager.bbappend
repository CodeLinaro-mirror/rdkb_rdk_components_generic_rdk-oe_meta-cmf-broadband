DEPENDS += "breakpad breakpad-wrapper json-hal-lib"

CFLAGS += "-I${STAGING_INCDIR}/breakpad "
CXXFLAGS += "-I${STAGING_INCDIR}/breakpad "

LDFLAGS += "-lbreakpadwrapper  -lpthread -lstdc++"

inherit coverity
