package week1.config

import org.scalatest.funsuite.AnyFunSuite

/**
 * Tests for the ConfigManagement object to ensure that the configuration
 * management functionality works as expected.
 */
class ConfigManagementTest extends AnyFunSuite {
  test("doSomeStuff should return true") {
    println("Testing if doSomeStuff returns true...")
    assert(ConfigManagement.doSomeStuff() === true)
  }

  test("getApplicationName returns correct name") {
    println(s"Testing if getAppName returns the expected name: ${ConfigManagement.getAppName}")
    assert(ConfigManagement.getAppName == "Example of Config Usage")
  }

  test("getApplicationVersion returns correct version") {
    println(s"Testing if getAppVersion returns the expected version: ${ConfigManagement.getAppVersion}")
    assert(ConfigManagement.getAppVersion == "1.0.0")
  }

  test("getCustomConfig returns correct custom configuration") {
    println(s"Testing if getCustomConfig returns the expected custom configuration: ${ConfigManagement.getCustomConfig}")
    assert(ConfigManagement.getCustomConfig == "customValue")
  }

  test("getIntList correctly retrieves the list of integers") {
    println(s"Testing if getIntList correctly retrieves the list of integers: ${ConfigManagement.getIntList}")
    assert(ConfigManagement.getIntList == List(1, 2, 3, 4, 5))
  }

  test("hasSetting verifies existence of a configuration path") {
    println("Testing if hasSetting correctly identifies the existence of 'app.name'...")
    assert(ConfigManagement.hasSetting("app.name"))
    println("Testing if hasSetting correctly identifies the non-existence of 'app.nonexistent'...")
    assert(!ConfigManagement.hasSetting("app.nonexistent"))
  }
}
