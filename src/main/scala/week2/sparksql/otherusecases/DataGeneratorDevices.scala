package week2.sparksql.otherusecases

import java.time.Instant
import scala.util.Random

/**
 * Object to generate random IoT device data.
 *
 * This object simulates the generation of data from different types of IoT devices.
 * It is used for testing or simulating IoT device outputs in a development environment.
 */
object DataGeneratorDevices {

  /**
   * A list of possible device types.
   *
   * Includes various types of sensors and devices typically used in IoT environments:
   * - thermostat: regulates the temperature.
   * - moisture_sensor: measures the moisture level in an environment.
   * - light_detector: detects light intensity.
   */
  val deviceTypes = List("thermostat", "moisture_sensor", "light_detector")

  /**
   * Generates a random integer within a specified range.
   *
   * @param start the lower bound of the range (inclusive).
   * @param end the upper bound of the range (inclusive).
   * @return a randomly selected integer between the start and end bounds.
   */
  def generateRandomInt(start: Int, end: Int): Int = {
    start + Random.nextInt((end - start) + 1)
  }

  /**
   * Generates a string representing data from an IoT device.
   *
   * The function simulates the generation of data from an IoT device, providing:
   * - A unique device ID.
   * - A device type randomly selected from a predefined list.
   * - A timestamp in seconds since the UNIX epoch.
   * - Randomly generated temperature and humidity values within realistic ranges.
   *
   * @return a formatted string containing the simulated device data.
   */
  def generateIotData(): String = {
    val deviceId = generateRandomInt(1, 1000) // Random device ID from 1 to 1000.
    val deviceType = deviceTypes(Random.nextInt(deviceTypes.size)) // Random device type from the list.
    val timestamp = Instant.now.getEpochSecond // Current time in seconds since the UNIX epoch.
    val temperature = generateRandomInt(15, 35) // Random temperature from 15 to 35 degrees Celsius.
    val humidity = generateRandomInt(30, 70) // Random humidity from 30% to 70%.

    s"Id del dispositivo: $deviceId, Tipo de dispositivo: $deviceType, Tiempo: $timestamp, Temperatura: $temperature, Humedad: $humidity"
  }

  /**
   * Main function to generate and print IoT device data.
   *
   * Generates and prints data for 100 simulated IoT devices to the console.
   * This function is the entry point for the program and facilitates batch data generation.
   *
   * @param args command-line arguments (not used).
   */
  def main(args: Array[String]): Unit = {
    for( _ <- 1 to 100) { // Generate data for 100 devices.
      println(generateIotData())
    }
  }

}
