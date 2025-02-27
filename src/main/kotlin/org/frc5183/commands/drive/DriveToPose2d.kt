package org.frc5183.commands.drive

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.FunctionalCommand
import org.frc5183.subsystems.drive.SwerveDriveSubsystem
import org.frc5183.subsystems.vision.VisionSubsystem

// todo: make sure this doesn't run during teleop if we're not holding a button otherwise its not allowed by FRC rules
fun DriveToPose2d(
    pose: Pose2d,
    drive: SwerveDriveSubsystem,
    vision: VisionSubsystem
): Command =
    // todo: fix this along pathplanner
    FunctionalCommand(
        { println("DriveToPose2d (init): $pose") },
        { println("DriveToPose2d (exec): $pose ") },
        { println("DriveToPose2d (end): $pose") },
        { true },
        drive,
        vision,
    )

    /*
    AutoBuilder.pathfindThenFollowPath(
        PathPlannerPath(
            PathPlannerPath.waypointsFromPoses(
                // todo: calculate any field obstacles and find the fastest path around them.
                //  pathplanner might already do this, have to check
                //  also consider using vision to detect other robots and calculate new paths during the move?
                //  (^ just a theory, probably wont be able to calculate fast enough; we're not a tesla)
                Pose2d(SwerveDriveSubsystem.pose.translation, SwerveDriveSubsystem.pose.rotation),
                Pose2d(pose.translation, SwerveDriveSubsystem.pose.rotation), // rotation in waypoints is only the movement direction, not the final rotation
            ),
            AutoConstants.PATH_CONSTRAINTS,
            null,
            GoalEndState(0.0, pose.rotation),
        ),
        AutoConstants.PATH_CONSTRAINTS,
    )

     */
