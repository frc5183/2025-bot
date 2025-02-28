package org.frc5183.hardware.vision

import edu.wpi.first.math.Matrix
import edu.wpi.first.math.VecBuilder
import edu.wpi.first.math.geometry.Pose3d
import edu.wpi.first.math.geometry.Transform3d
import edu.wpi.first.math.numbers.N1
import edu.wpi.first.math.numbers.N3
import edu.wpi.first.networktables.NetworkTablesJNI
import org.frc5183.constants.Config
import org.frc5183.target.FieldTarget
import org.photonvision.EstimatedRobotPose
import org.photonvision.PhotonCamera
import org.photonvision.PhotonPoseEstimator
import org.photonvision.targeting.PhotonPipelineResult
import org.photonvision.targeting.PhotonTrackedTarget
import kotlin.jvm.optionals.getOrNull
import kotlin.time.Duration
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

// todo: should we move calculations, vision processing, and such out of this, or keep it here?
class Camera(
    /**
     * The nickname of the [Camera] (found in the PhotonVision UI).
     */
    cameraName: String,
    /**
     * The [Transform3d] of the [Camera] in relation to the robot's origin.
     */
    val transform: Transform3d,
    /**
     * The single April Tag standard deviations of estimated poses from the
     * [Camera].
     */
    private val singleTagStandardDeviations: Matrix<N3, N1>,
    /**
     * The multi April Tag standard deviations of estimated poses from the
     * [Camera].
     */
    private val multiTagStandardDeviations: Matrix<N3, N1>,
) : PhotonCamera(cameraName) {
    private var lastReadTimestamp: Duration = 0.seconds

    var currentStandardDeviations: Matrix<N3, N1> = singleTagStandardDeviations
        private set

    var results: List<PhotonPipelineResult> = listOf()
        private set

    var visibleTargets: List<FieldTarget> = listOf()
        private set

    var estimatedRobotPose: EstimatedRobotPose? = null
        private set

    private val poseEstimator: PhotonPoseEstimator =
        PhotonPoseEstimator(
            Config.FIELD_LAYOUT,
            PhotonPoseEstimator.PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR,
            transform,
        )

    init {
        poseEstimator.setMultiTagFallbackStrategy(PhotonPoseEstimator.PoseStrategy.LOWEST_AMBIGUITY)
    }

    /**
     * Updates the [Camera] with the latest [PhotonPipelineResult]s, then returns
     * the current [EstimatedRobotPose].
     * @return The current [EstimatedRobotPose] of the robot, or null if the
     * pose could not be estimated or it is not accurate.
     */
    fun updateAndGetEstimatedRobotPose(): EstimatedRobotPose? {
        updateUnreadResults()
        return estimatedRobotPose
    }

    /**
     * Updates the [Camera] with the latest [PhotonPipelineResult]s from
     * PhotonVision. Sorts the list by timestamp. Maximum refresh rate of
     * 1req/15ms, or if the [Camera] has no results.
     */
    fun updateUnreadResults() {
        val mostRecentTime: Duration = results.firstOrNull()?.timestampSeconds?.seconds ?: 0.seconds
        val currentTime: Duration = NetworkTablesJNI.now().microseconds
        val debounceTime: Duration = 15.milliseconds

        if ((results.isEmpty() || currentTime - mostRecentTime >= debounceTime) && currentTime - lastReadTimestamp >= debounceTime) {
            results =
                allUnreadResults
                    .sortedBy { it.timestampSeconds }
                    .toList()
        }

        if (results.isNotEmpty()) {
            results.forEach {
                estimatedRobotPose = poseEstimator.update(it).getOrNull()
                updateEstimationStdDevs(estimatedRobotPose, it.targets)
            }

            updateVisibleTargets()
        }
    }

    private fun updateVisibleTargets() {
        val newVisibleTargets = mutableListOf<FieldTarget>()
        for (target in results.flatMap { it.getTargets() }) {
            newVisibleTargets += FieldTarget.getTarget(target.fiducialId) ?: continue
        }
        visibleTargets = newVisibleTargets
    }

    private fun updateEstimationStdDevs(
        estimatedRobotPose: EstimatedRobotPose?,
        targets: List<PhotonTrackedTarget>,
    ) {
        if (estimatedRobotPose == null) {
            currentStandardDeviations = singleTagStandardDeviations
            return
        }

        var estimatedStandardDeviations = singleTagStandardDeviations
        var tags = 0
        var averageDistance = 0.0

        for (target in targets) {
            val tagPose: Pose3d = FieldTarget.getPose(target.fiducialId) ?: continue

            tags++
            averageDistance += tagPose.toPose2d().translation.getDistance(estimatedRobotPose.estimatedPose.toPose2d().translation)
        }

        if (tags == 0) {
            currentStandardDeviations = singleTagStandardDeviations
            return
        }

        if (tags > 1) estimatedStandardDeviations = multiTagStandardDeviations

        averageDistance /= tags

        estimatedStandardDeviations =
            if (tags == 1 && averageDistance > 4) {
                VecBuilder.fill(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE)
            } else {
                estimatedStandardDeviations.times(1 + (averageDistance * averageDistance / 30))
            }

        currentStandardDeviations = estimatedStandardDeviations
    }
}
