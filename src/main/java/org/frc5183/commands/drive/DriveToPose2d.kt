package org.frc5183.commands.drive

import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.path.GoalEndState
import com.pathplanner.lib.path.PathPlannerPath
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.constants.AutoConstants
import org.frc5183.subsystems.SwerveDriveSubsystem

// todo: make sure this doesn't run during teleop if we're not holding a button otherwise its not allowed by FRC rules
class DriveToPose2d(
    private val pose: Pose2d,
) : Command() {
    private lateinit var path: PathPlannerPath

    init {
        addRequirements(SwerveDriveSubsystem)
    }

    override fun initialize() {
        path =
            PathPlannerPath(
                PathPlannerPath.waypointsFromPoses(
                    // todo: calculate any field obstacles and find the fastest path around them.
                    //  pathplanner might already do this, have to check
                    //  also consider using vision to detect other robots and calculate new paths during the move?
                    //  (^ just a theory, probably wont be able to calculate fast enough; we're not a tesla)
                    Pose2d(pose.translation, Rotation2d.kZero),
                ),
                AutoConstants.PATH_CONSTRAINTS,
                null,
                GoalEndState(0.0, pose.rotation),
            )

        AutoBuilder.pathfindThenFollowPath(
            path,
            AutoConstants.PATH_CONSTRAINTS,
        )
    }

    override fun execute() {
    }
}
