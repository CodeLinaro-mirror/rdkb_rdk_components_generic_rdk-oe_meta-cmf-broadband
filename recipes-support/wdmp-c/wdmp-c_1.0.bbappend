FILESEXTRAPATHS:prepend:= "${THISDIR}/files:"

SRC_URI_remove = "file://wdmp-c.patch"
SRC_URI += "file://broadband-wdmp-c.patch"
