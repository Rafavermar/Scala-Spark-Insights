package week1.config

import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

/**
 * Test suite for ConfigManagement to ensure that all configuration-related functionality
 * is working as expected.
 *
 * These tests are essential to verify that the application can correctly read and process
 * the configuration settings as defined in application.conf.
 */
class ConfigManagementTest extends AnyFunSuite with BeforeAndAfterEach {

  /**
   * Ensures that the configuration is properly loaded before each test case is executed.
   * This method uses assertions to guarantee that the initial configuration load is successful.
   */
  override def beforeEach(): Unit = {
    assert(ConfigManagement.initConfig(), "Configuration should load successfully before tests")
  }

  test("getAppName returns the correct application name") {
    println("Testing getAppName...")
    assert(ConfigManagement.getAppName == "Example of Config Usage", "App name should be 'Example of Config Usage'")
    println(s"getAppName passed: ${ConfigManagement.getAppName}")
  }

  test("getAppVersion returns the correct application version") {
    println("Testing getAppVersion...")
    assert(ConfigManagement.getAppVersion == "1.0.0", "App version should be '1.0.0'")
    println(s"getAppVersion passed: ${ConfigManagement.getAppVersion}")
  }

  test("getCustomConfig returns the correct custom configuration value") {
    println("Testing getCustomConfig...")
    assert(ConfigManagement.getCustomConfig == "customValue", "Custom config should be 'customValue'")
    println(s"getCustomConfig passed: ${ConfigManagement.getCustomConfig}")
  }

  test("getIntList returns the correct list of integers") {
    println("Testing getIntList...")
    assert(ConfigManagement.getIntList == List(1, 2, 3, 4, 5), "Integer list should match [1, 2, 3, 4, 5]")
    println(s"getIntList passed: ${ConfigManagement.getIntList}")
  }

  test("hasSetting verifies the existence of a configuration path for 'app.custom'") {
    println("Testing hasSetting for 'app.custom'...")
    assert(ConfigManagement.hasSetting("app.custom"), "Configuration should have 'app.custom'")
    println("hasSetting 'app.custom' passed.")
  }

  test("hasSetting verifies the non-existence of a non-existent configuration path") {
    println("Testing hasSetting for 'app.nonexistent'...")
    assert(!ConfigManagement.hasSetting("app.nonexistent"), "Configuration should not have 'app.nonexistent'")
    println("hasSetting 'app.nonexistent' passed.")
  }
}
