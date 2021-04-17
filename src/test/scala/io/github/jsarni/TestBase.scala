package io.github.jsarni

import org.mockito.MockitoSugar
import org.scalatest.{GivenWhenThen, OptionValues, PrivateMethodTester, TryValues}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

abstract class TestBase
  extends AnyFlatSpec
    with MockitoSugar
    with GivenWhenThen
    with PrivateMethodTester
    with Matchers
    with OptionValues
    with TryValues {}
