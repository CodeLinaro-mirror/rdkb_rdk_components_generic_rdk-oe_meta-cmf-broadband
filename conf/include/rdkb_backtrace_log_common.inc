#breakpad support
BACKTRACE_DEPENDS_pn-unified-wifi-mesh = " breakpad-wrapper"
BACKTRACE_DEPENDS_pn-ieee1905-em = " breakpad-wrapper"

BACKTRACE_LDFLAGS_pn-unified-wifi-mesh = " -Wl,--no-as-needed -lbreakpadwrapper -Wl,--as-needed "
BACKTRACE_LDFLAGS_pn-ieee1905-em = " -Wl,--no-as-needed -lbreakpadwrapper -Wl,--as-needed "
