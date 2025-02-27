package org.frc5183.log

import edu.wpi.first.units.Measure
import edu.wpi.first.units.MutableMeasure
import edu.wpi.first.util.struct.Struct
import edu.wpi.first.util.struct.StructSerializable
import org.littletonrobotics.junction.LogTable
import org.littletonrobotics.junction.inputs.LoggableInputs
import kotlin.reflect.KProperty
import edu.wpi.first.units.Unit as WPIUnit

/**
 * An abstract class that can be implemented as a replacement for the
 * @AutoLog AdvantageKit annotation for Kotlin codebases.
 *
 * @author Daniel1464 https://github.com/Mechanical-Advantage/AdvantageKit/pull/112
 */
abstract class AutoLogInputs : LoggableInputs {
    /**
     * Creates a LoggedInput instance for a double value.
     *
     * This function registers the given double value for logging by wrapping it in a LoggedInput,
     * which uses the LogTable's put and get methods to handle logging operations.
     *
     * @param value the double value to be logged.
     * @param key an optional identifier for the logged value; if omitted, the property name will be used.
     * @return a LoggedInput instance managing the specified double value.
     */
    fun log(
        value: Double,
        key: String? = null,
    ) = LoggedInput(value, key, LogTable::put, LogTable::get)

    /**
     * Logs an integer value by creating a logged input delegate.
     *
     * The returned [LoggedInput] instance registers the value with the logging framework using the
     * provided logging and retrieval functions from the log table. An optional key can be supplied
     * to override the default property name when used with property delegation.
     *
     * @param value the integer value to be logged.
     * @param key an optional identifier for the log entry; if null, the delegated property name is used.
     * @return a [LoggedInput] delegate for the integer value.
     */
    fun log(
        value: Int,
        key: String? = null,
    ) = LoggedInput(value, key, LogTable::put, LogTable::get)

    /**
     * Creates a LoggedInput for a String value.
     *
     * The returned LoggedInput instance handles logging the specified String to a LogTable using defined put and get methods.
     *
     * @param value the String value to log.
     * @param key an optional identifier for the logged value; if omitted, the property's name is used.
     * @return a LoggedInput instance managing the logging of the given value.
     */
    fun log(
        value: String,
        key: String? = null,
    ) = LoggedInput(value, key, LogTable::put, LogTable::get)

    /**
     * Logs a Boolean value.
     *
     * Wraps the given Boolean in a [LoggedInput] instance to enable its logging to a [LogTable]. If a [key] is provided, it is used as the identifier for the logged value; otherwise, the property's name is used.
     *
     * @param value the Boolean value to log.
     * @param key an optional identifier for the value to be logged.
     * @return a [LoggedInput] facilitating logging of the Boolean value.
     */
    fun log(
        value: Boolean,
        key: String? = null,
    ) = LoggedInput(value, key, LogTable::put, LogTable::get)

    /**
     * Creates a logged input for a Long value.
     *
     * This function instantiates a [LoggedInput] that encapsulates a [Long] value,
     * using [LogTable]'s default put and get methods to facilitate logging.
     * An optional key can be provided to uniquely identify the logged value;
     * if omitted, the property name will be used.
     *
     * @param value the [Long] value to log.
     * @param key an optional identifier for the log entry.
     * @return a [LoggedInput] instance configured to manage logging of the provided value.
     */
    fun log(
        value: Long,
        key: String? = null,
    ) = LoggedInput(value, key, LogTable::put, LogTable::get)

    /**
     * Wraps a StructSerializable value in a LoggedInput for logging.
     *
     * The provided value is encapsulated in a LoggedInput that registers functions for storing to
     * and retrieving from a LogTable. An optional key can be supplied to override the default naming;
     * if not provided, the property's name will be used.
     *
     * @param value the StructSerializable value to log.
     * @param key an optional custom identifier for the log entry.
     * @return a LoggedInput instance managing the value's logging and retrieval.
     */
    fun <T : StructSerializable> log(
        value: T,
        key: String? = null,
    ) = LoggedInput(value, key, LogTable::put, LogTable::get)

    /**
     * Wraps a value and its corresponding structure into a LoggedInput for logging.
     *
     * This function creates a LoggedInput instance for the given value, using the provided
     * structure to handle serialization and deserialization when interacting with a LogTable.
     * An optional key can be supplied to explicitly name the logged entry.
     *
     * @param value the value to log
     * @param struct defines how the value is serialized and deserialized for logging
     * @param key an optional identifier for the logged value; if not provided, a default name may be used
     * @return a LoggedInput instance configured with the provided logging and retrieval operations
     */
    fun <T> log(
        value: T,
        struct: Struct<T>,
        key: String? = null,
    ) = LoggedInput(value, key, { k, v -> put(k, struct, v) }, { k, v -> get(k, struct, v) })

    /**
     * Creates a [LoggedInput] delegate for logging a measured value.
     *
     * The returned [LoggedInput] wraps the provided [Measure] and links it to the logging system via
     * [LogTable.put] and [LogTable.get]. An optional [key] can be supplied to identify the logged value;
     * if omitted, the property name will be used.
     *
     * @param value the measured value to be logged.
     * @param key an optional key to uniquely identify the logged value, defaults to null.
     * @return a [LoggedInput] instance that manages logging for the given measured value.
     */
    fun <U : WPIUnit> log(
        value: Measure<U>,
        key: String? = null,
    ) = LoggedInput(value, key, LogTable::put, LogTable::get)

    /**
     * Registers a mutable measure value for logging.
     *
     * Wraps the provided mutable measure value in a LoggedInput instance that uses the log table's
     * put and get functions to automatically log and retrieve the value. An optional key can be supplied
     * to override the default log entry identifier (typically the property's name).
     *
     * @param value the mutable measure value to log.
     * @param key an optional identifier for the log entry; if null, the property's name is used.
     * @return a LoggedInput instance associated with the mutable measure value.
     */
    fun <U : WPIUnit, Base : Measure<U>, M : MutableMeasure<U, Base, M>> log(
        value: M,
        key: String? = null,
    ) = LoggedInput(value, key, LogTable::put, LogTable::get)

    /**
     * Creates a logging delegate for a double array value.
     *
     * This function registers a [DoubleArray] with the logging framework by returning a [LoggedInput] delegate.
     * When a key is provided, it serves as the identifier for the logged entry; otherwise, the property name is used.
     *
     * @param value The double array to be logged.
     * @param key An optional identifier for the logged value.
     */
    fun log(
        value: DoubleArray,
        key: String? = null,
    ) = LoggedInput(value, key, LogTable::put, LogTable::get)

    /**
     * Logs an integer array with an optional key.
     *
     * This function creates a logging delegate for the provided integer array that manages its
     * storage and retrieval in the log table. If a key is supplied, it is used as the logging
     * identifier; otherwise, the property's name is utilized.
     *
     * @param value the array of integers to be logged.
     * @param key an optional identifier to associate with the logged value.
     */
    fun log(
        value: IntArray,
        key: String? = null,
    ) = LoggedInput(value, key, LogTable::put, LogTable::get)

    /**
     * Creates a property delegate for logging an array of strings.
     *
     * This method instantiates a LoggedInput configured with the standard logging
     * functions to automatically record and retrieve the provided array of strings
     * from a LogTable. The optional key can be used to override the default property
     * name used as the log entry identifier.
     *
     * @param value the array of strings to be logged.
     * @param key an optional identifier for the log entry. If not provided, the property name is used.
     * @return a LoggedInput instance to be used as a delegated property.
     */
    fun log(
        value: Array<String>,
        key: String? = null,
    ) = LoggedInput(value, key, LogTable::put, LogTable::get)

    /**
     * Logs a Boolean array by wrapping it in a LoggedInput instance.
     *
     * The Boolean array is registered with the logging system using the LogTable's put and get methods.
     * An optional key can be provided to specify an identifier; otherwise, the delegated property's name is used.
     *
     * @param value the Boolean array to be logged.
     * @param key an optional identifier for the log entry.
     */
    fun log(
        value: BooleanArray,
        key: String? = null,
    ) = LoggedInput(value, key, LogTable::put, LogTable::get)

    /**
     * Creates a log entry for a long array value.
     *
     * This function wraps the provided [value] in a [LoggedInput] instance, associating it with a logging function
     * that writes to and reads from a log table. If [key] is provided, that identifier will be used; otherwise, the
     * property name is used as the key.
     *
     * @param value the array of long integers to be logged.
     * @param key an optional identifier for the log entry.
     * @return a [LoggedInput] instance encapsulating the long array and its logging behavior.
     */
    fun log(
        value: LongArray,
        key: String? = null,
    ) = LoggedInput(value, key, LogTable::put, LogTable::get)

    /**
     * Logs an array of [StructSerializable] objects to the log table.
     *
     * This function creates a [LoggedInput] instance for the provided array, using the default
     * methods defined by [LogTable.put] and [LogTable.get] for logging and retrieval.
     *
     * @param value the array of serializable structures to log.
     * @param key an optional identifier for the logged data; if omitted, the property name will be used.
     * @return a [LoggedInput] instance that manages logging and retrieving the specified value.
     */
    fun <T : StructSerializable> log(
        value: Array<T>,
        key: String? = null,
    ) = LoggedInput(value, key, LogTable::put, LogTable::get)

    private val toLogRunners = mutableListOf<(LogTable) -> Unit>()
    private val fromLogRunners = mutableListOf<(LogTable) -> Unit>()

    inner class LoggedInput<T>(
        private var value: T,
        private val name: String? = null,
        private val toLog: LogTable.(String, T) -> Unit,
        private val fromLog: LogTable.(String, T) -> T,
    ) {
        /**
         * Retrieves the current value through delegated property access.
         *
         * This operator function is invoked when the property is read, returning the stored value.
         *
         * @param thisRef the object owning the delegated property.
         * @param property metadata for the delegated property.
         * @return the current value.
         */
        operator fun getValue(
            thisRef: Any,
            property: KProperty<*>,
        ) = value

        /**
         * Sets the delegated property's value.
         *
         * This operator function is invoked when a new value is assigned to a delegated property,
         * updating the internal stored value.
         *
         * @param thisRef The object that contains the property.
         * @param property Metadata about the property being set.
         * @param value The new value to assign.
         */
        operator fun setValue(
            thisRef: Any,
            property: KProperty<*>,
            value: T,
        ) {
            this.value = value
        }

        /**
         * Enables property delegation for automatic logging.
         *
         * When a property is delegated to a LoggedInput, this operator is invoked to register the functions
         * that log the property's value to a LogTable and retrieve it back using a namespace derived from either
         * a custom name or the property's name.
         *
         * @param thisRef the object that owns the delegated property.
         * @param property metadata of the delegated property; its name is used as a key if no custom name is set.
         * @return this LoggedInput instance with logging and retrieval operations registered.
         */
        operator fun provideDelegate(
            thisRef: Any,
            property: KProperty<*>,
        ): LoggedInput<T> {
            val namespace = this.name ?: property.name
            toLogRunners.add { logTable -> this.toLog(logTable, namespace, value) }
            fromLogRunners.add { logTable -> value = this.fromLog(logTable, namespace, value) }
            return this
        }
    }

    /**
     * Retrieves logged input values from the provided LogTable.
     *
     * Iterates over each registered retrieval function in [fromLogRunners] and invokes it with [table],
     * updating the corresponding logged inputs with the latest values.
     *
     * @param table the LogTable instance from which to retrieve logged values.
     */
    override fun fromLog(table: LogTable) {
        fromLogRunners.forEach { it(table) }
    }

    /**
     * Logs the current input values to the specified log table.
     *
     * Iterates over all registered logging actions and invokes each one with the provided log table,
     * ensuring that the current state of inputs is captured.
     *
     * @param table the log table where input values are recorded.
     */
    override fun toLog(table: LogTable) {
        toLogRunners.forEach { it(table) }
    }
}
