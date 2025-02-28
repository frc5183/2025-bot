package org.frc5183.subsystems.drive.io

// todo: find a better way to abstract this, because we want to copy most stuff for simulation io and maybe get rid of some stuff with real io in the future
class SimulatedSwerveDriveIO : RealSwerveDriveIO() {
    init {
        drive.setHeadingCorrection(false); // Should only be enabled when controlling the robot by angle.
        drive.setCosineCompensator(false)
    }
}
