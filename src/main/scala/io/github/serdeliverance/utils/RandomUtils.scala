package io.github.serdeliverance.utils

import scala.util.Random

trait RandomUtils {

  private lazy val random = new Random()

  def randomIntInRage(min: Int, max: Int): Int =
    min + random.nextInt((max - min) + 1)

  def randomState(): String = if (randomIntInRage(1, 10) < 6) "approved" else "rejected"

}
