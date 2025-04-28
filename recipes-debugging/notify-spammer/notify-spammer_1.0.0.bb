SUMMARY = "Watchdog spammer service"
DESCRIPTION = "Service that floods systemd with sd_notify watchdog messages upon receiving SIGTERM"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${PN}.cpp;beginline=2;endline=3;md5=e2023e3f01d420378d9b2751b1d2ee4d"

SRC_URI = "file://${BPN}.cpp \
           file://${BPN}.service"

S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

DEPENDS = "systemd"

inherit pkgconfig systemd

SYSTEMD_SERVICE:${PN} = "${PN}.service"

CXXFLAGS += " -std=c++11 $(pkg-config --cflags systemd)"
LDFLAGS += " $(pkg-config --libs systemd) -lsystemd"

do_compile() {
    ${CXX} ${CXXFLAGS} ${PN}.cpp -o ${PN} ${LDFLAGS}
}

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${PN} ${D}${sbindir}/${PN}

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${S}/${PN}.service ${D}${systemd_system_unitdir}/${PN}.service
}

SYSTEMD_SERVICE:${PN} = "\
    ${PN}.service \
"

FILES:${PN}:append = " \
    ${systemd_system_unitdir} \
"