package org.frc5183.target

import edu.wpi.first.apriltag.AprilTagFieldLayout
import edu.wpi.first.apriltag.AprilTagFields
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Transform2d
import kotlin.jvm.optionals.getOrNull

/**
 * An enum representing the different april-tagged targets on the field.
 */
enum class FieldTarget(
    /**
     * A list of all the IDs to aim to when on the blue alliance.
     */
    val blueIds: List<Int>,
    /**
     * A list of all the IDs to aim to when on the red alliance.
     */
    val redIds: List<Int>,
    /**
     * The desired transform in relation to the target's [pose] to move and aim at.
     */
    val desired: Transform2d,
) {
    CoralStation(
        listOf(1, 2),
        listOf(12, 13),
        Transform2d(),
    ),

    Processor(
        listOf(3),
        listOf(16),
        Transform2d(),
    ),

    Barge(
        listOf(4, 14),
        listOf(5, 15),
        Transform2d(),
    ),

    Reef(
        IntRange(17, 22).toList(),
        IntRange(6, 11).toList(),
        Transform2d(),
    ),

    Pipe(
        IntRange(17, 22).toList(),
        IntRange(6, 11).toList(),
        Transform2d(),
    ),
    ;

    companion object {
        private val fieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2024Crescendo)

        /**
         * Returns the [Pose2d] of an AprilTag on the field.
         * @param tag The ID of the [tag] to get the pose of.
         * @return The pose of the [tag], or null if the [tag] does not
         * correspond to a tag in the [AprilTagFieldLayout].
         */
        fun getPose(tag: Int): Pose2d? = fieldLayout.getTagPose(tag).getOrNull()?.toPose2d()

        /**
         * Returns the [FieldTarget] that corresponds to the given [tag].
         *
         * This is not alliance specific, and will return the target regardless of
         * the current alliance.
         * @param tag The ID of the tag to get the target of.
         * @return The [FieldTarget] that corresponds to the given [tag], or
         * null if no target corresponds to the given [tag].
         */
        fun getTarget(tag: Int): FieldTarget? = entries.find { it.blueIds.contains(tag) || it.redIds.contains(tag) }
    }
}
