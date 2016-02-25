package freeex

import org.scalatest.{Matchers, WordSpec}

class PersonSpec extends WordSpec with Matchers {

  case class Person(name: String, age: Int){
    def inc = Person(name, age + 1)
  }

  "a Person" should {

    "inc should increase age" in {
      val p = Person("Paul", 25)
      (p.inc.age - p.age) shouldEqual 1
    }

  }

}
