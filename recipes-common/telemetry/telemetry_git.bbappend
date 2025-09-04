#RDKBACCL-991 we are not supporting mtls and its certificates in reference platform, removing the mtls enable flag
CFLAGS_remove = "-DENABLE_MTLS"
