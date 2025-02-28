package org.frc5183.commands.drive

import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.path.GoalEndState
import com.pathplanner.lib.path.PathPlannerPath
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.FunctionalCommand
import org.frc5183.constants.AutoConstants
import org.frc5183.subsystems.drive.SwerveDriveSubsystem
import org.frc5183.subsystems.vision.VisionSubsystem

// todo: make sure this doesn't run during teleop if we're not holding a button otherwise its not allowed by FRC rules
fun DriveToPose2d(
    pose: Pose2d,
    drive: SwerveDriveSubsystem,
): Command =
    AutoBuilder.pathfindThenFollowPath(
        PathPlannerPath(
            PathPlannerPath.waypointsFromPoses(
                // todo: calculate any field obstacles and find the fastest path around them.
                //  pathplanner might already do this, have to check
                //  also consider using vision to detect other robots and calculate new paths during the move?
                //  (^ just a theory, probably wont be able to calculate fast enough; we're not a tesla)
                Pose2d(drive.pose.translation, drive.pose.rotation),
                Pose2d(pose.translation, drive.pose.rotation), // rotation in waypoints is only the movement direction, not the final rotation
            ),
            AutoConstants.PATH_CONSTRAINTS,
            null,
            GoalEndState(0.0, pose.rotation),
        ),
        AutoConstants.PATH_CONSTRAINTS,
    )