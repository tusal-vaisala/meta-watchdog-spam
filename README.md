meta-watchdog-spam layer is intended for debugging a possible system hang issue related
to systemd watchdog functionality.

### Prerequisites

- Tested and layer compatible with Poky Styhead/Walnascar, older might need recipe adjustment
- Needs image with systemd as init manager (`INIT_MANAGER = "systemd"`)


### Usage

This layer is not in the layer index.

1. Clone this repo
2. [Enable layer](https://docs.yoctoproject.org/dev-manual/layers.html#enabling-your-layer) with path to the clone
3. `IMAGE_INSTALL:append = " notify-spammer"`
4. After boot, `systemctl stop notify-spammer` hangs much longer (possibly indefinitely) than `DefaultTimeoutStopSec`
