/*
 * Minimal program to reproduce an application bug that led to systemd hanging indefinitely
 * when stopping the service.
 * 
 * When systemd stops the service by sending SIGTERM, handle the signal and remove the sleep between
 * watchdog notifications to emulate the application bug. Systemd should start countdown towards
 * StopTimeoutSec (default 90s) and finally send SIGKILL, but this doesn't happen and the service stop
 * hangs indefinitely. Systemd becomes unresponsive to many commands but keeps pinging the hardware watchdog
 * so that doesn't help recovering the system.
 */
#include <systemd/sd-daemon.h>
#include <csignal>
#include <atomic>
#include <chrono>
#include <thread>

std::atomic<bool> sleepBetweenPings{true};

void handle_sigterm(int) {
    sleepBetweenPings = false;
}

int main() {
    sd_notify(0, "READY=1");

    std::signal(SIGTERM, handle_sigterm);

    while (true) {
        sd_notify(0, "WATCHDOG=1");

        if (sleepBetweenPings.load()) {
            std::this_thread::sleep_for(std::chrono::seconds(1));
        }
    }
}