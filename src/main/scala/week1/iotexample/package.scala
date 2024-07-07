package week1

import iotexample.IotDomain.{Humidity, Pressure, SensorData, Temperature}

import scala.util.matching.Regex
/**
 * Package containing the definitions for an IoT example, including types and validation logic.
 */

/**
 * Package object containing traits, classes, and objects relevant to handling IoT example data.
 */
package object iotexample {

  /**
   * Represents reasons why data might be considered invalid.
   */
  sealed trait InvalidDataReason

  // Categories of invalid data reasons based on common ID-related issues.
  sealed trait CommonIdReason extends InvalidDataReason
  sealed trait TemperatureIdReason extends InvalidDataReason
  sealed trait HumidityIdReason extends InvalidDataReason
  sealed trait PressureIdReason extends InvalidDataReason

  // Enumerated values for specific invalid data reasons.
  case object SensorId extends InvalidDataReason with CommonIdReason
  case object EventTime extends InvalidDataReason with CommonIdReason
  case object SensorType extends InvalidDataReason with CommonIdReason
  case object TemperatureRange extends InvalidDataReason with TemperatureIdReason
  case object HumidityRange extends InvalidDataReason with HumidityIdReason
  case object PressureRange extends InvalidDataReason with PressureIdReason
  case object ContainsTemperature extends InvalidDataReason with TemperatureIdReason
  case object ContainsHumidity extends InvalidDataReason with HumidityIdReason
  case object ContainsPressure extends InvalidDataReason with PressureIdReason
  case object ContainsData extends InvalidDataReason with CommonIdReason

  /**
   * Base trait for defining reasons for data invalidity with a message.
   */
  sealed trait Reason {
    def message: String
  }

  // Concrete reasons extending the base trait for detailed explanations.
  case class SensorIdReason(message: String) extends Reason
  case class EventTimeReason(message: String) extends Reason
  case class SensorTypeReason(message: String) extends Reason
  case class TemperatureReason(message: String) extends Reason
  case class HumidityReason(message: String) extends Reason
  case class PressureReason(message: String) extends Reason
  case class ContainsTemperatureReason(message: String) extends Reason
  case class ContainsHumidityReason(message: String) extends Reason
  case class ContainsPressureReason(message: String) extends Reason
  case class ContainsDataReason(message: String) extends Reason

  /**
   * Represents an invalid data entry, including the type of invalidity and the associated reason.
   */
  case class InvalidData(invalidDataId: InvalidDataReason, invalidDataReason: Reason)

  /**
   * Contains the definition of different sensor types, each with specific range constraints.
   */
  object IotDomain {

    sealed trait SensorType {
      val min: Double
      val max: Double
      def typeName: String
    }

    case object Temperature extends SensorType {
      override val min: Double = -10
      override val max: Double = 50
      override val typeName: String = "validTemperature-device"
    }

    case object Humidity extends SensorType {
      override val min: Double = 0
      override val max: Double = 100
      override val typeName: String = "validHumidity-device"
    }

    case object Pressure extends SensorType {
      override val min: Double = 800
      override val max: Double = 1200
      override val typeName: String = "validPressure-device"
    }

    case class SensorData(eventTime: String,
                          sensorId: String,
                          value: Double,
                          valid: Option[Boolean] = Some(false),
                          invalidReason: Option[InvalidDataReason] = None,
                          sensorType: String)

    /**
     * Builder class for creating SensorData instances.
     */
    class SensorDataBuilder {
      private var eventTime: String = ""
      private var sensorId: String = ""
      private var value: Double = 0.0
      private var valid: Option[Boolean] = Some(false)
      private var invalidReason: Option[InvalidDataReason] = None
      private var sensorType: String = Temperature.typeName

      def withEventTime(eventTime: String): SensorDataBuilder = {
        this.eventTime = eventTime
        this
      }

      def withSensorId(sensorId: String): SensorDataBuilder = {
        this.sensorId = sensorId
        this
      }

      def withValue(value: Double): SensorDataBuilder = {
        this.value = value
        this
      }

      def withValid(valid: Option[Boolean]): SensorDataBuilder = {
        this.valid = valid
        this
      }

      def withInvalidReason(invalidReason: Option[InvalidDataReason]): SensorDataBuilder = {
        this.invalidReason = invalidReason
        this
      }

      def withSensorType(sensorType: SensorType): SensorDataBuilder = {
        this.sensorType = sensorType.typeName
        this
      }

      def build(): SensorData = SensorData(eventTime, sensorId, value, valid, invalidReason, sensorType)
    }
  }

  /**
   * Object responsible for validating sensor data against a set of predefined rules.
   * Each function returns either a boolean indicating if the data meets the criteria,
   * or an optional InvalidData detailing why the data was invalid.
   */
  object IotDataValidations {
    val sensorIdPattern: Regex = "sensor-\\d+".r

    def validateSensorId(sensorId: String): Boolean = sensorId match {
      case sensorIdPattern() => true
      case _ => false
    }

    def validateEventTime(eventTime: String): Boolean = {
      try {
        java.time.OffsetDateTime.parse(eventTime)
        true
      } catch {
        case _: java.time.format.DateTimeParseException => false
      }
    }

    private def validateMeassure(sensorData: SensorData): Boolean = sensorData.value >= sensorData.sensorType.min && sensorData.value <= sensorData.sensorType.max

    def validateSensorType(sensorData: SensorData): Boolean = Seq(Temperature.typeName, Pressure.typeName, Humidity.typeName).contains(sensorData.sensorType)

    private def validateContainsTemperature(sensorData: SensorData): Boolean = sensorData.sensorType == Temperature.typeName && validateMeassure(sensorData)

    private def validateContainsHumidity(sensorData: SensorData): Boolean = sensorData.sensorType == Humidity.typeName && validateMeassure(sensorData)

    private def validateContainsPressure(sensorData: SensorData): Boolean = sensorData.sensorType == Pressure.typeName && validateMeassure(sensorData)

    def validateContaintsData(sensorData: SensorData): Boolean = validateContainsTemperature(sensorData) || validateContainsHumidity(sensorData) || validateContainsPressure(sensorData)

    def validateSensorIdReason(sensorId: String): Option[InvalidData] = sensorId match {
      case sensorIdPattern() => None
      case _ => Some(InvalidData(SensorId, SensorIdReason("El sensorId no tiene un formato válido")))
    }

    def validateEventTimeReason(eventTime: String): Option[InvalidData] = {
      try {
        java.time.OffsetDateTime.parse(eventTime)
        None
      } catch {
        case _: java.time.format.DateTimeParseException => Some(InvalidData(EventTime, EventTimeReason("El eventTime no tiene un formato válido")))
      }
    }

    def validateTemperatureReason(data: SensorData): Option[InvalidData] = {
      if (validateMeassure(data)) None
      else Some(InvalidData(TemperatureRange, TemperatureReason("La temperatura no está en el rango válido")))
    }

    def validateHumidityReason(data: SensorData): Option[InvalidData] = {
      if (validateMeassure(data)) None
      else Some(InvalidData(HumidityRange, HumidityReason("La humedad no está en el rango válido")))
    }

    def validatePressureReason(data: SensorData): Option[InvalidData] = {
      if (validateMeassure(data)) None
      else Some(InvalidData(PressureRange, PressureReason("La presión no está en el rango válido")))
    }

    def validateSensorTypeReason(data: SensorData): Option[InvalidData] = {
      if (validateSensorType(data)) None
      else Some(InvalidData(SensorType, SensorTypeReason("El tipo de sensor no es válido")))
    }

    def validateContainsTemperatureReason(data: SensorData): Option[InvalidData] = {
      if (validateContainsTemperature(data)) None
      else Some(InvalidData(ContainsTemperature, ContainsTemperatureReason("El sensor no contiene la temperatura o esta no es válida")))
    }

    def validateContainsHumidityReason(data: SensorData): Option[InvalidData] = {
      if (validateContainsHumidity(data)) None
      else Some(InvalidData(ContainsHumidity, ContainsHumidityReason("El sensor no contiene la humedad o esta no es válida")))
    }

    def validateContainsPressureReason(data: SensorData): Option[InvalidData] = {
      if (validateContainsPressure(data)) None
      else Some(InvalidData(ContainsPressure, ContainsPressureReason("El sensor no contiene la presión o esta no es válida")))
    }

    def validateContaintsDataReason(data: SensorData): Option[InvalidData] = {
      if (validateContainsTemperature(data) || validateContainsHumidity(data) || validateContainsPressure(data)) None
      else Some(InvalidData(ContainsData, ContainsDataReason("El sensor no contiene los datos necesarios o estos no son válidos")))
    }
  }
}
