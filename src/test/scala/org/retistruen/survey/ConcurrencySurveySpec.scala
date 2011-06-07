package org.retistruen.survey

import org.mockito.Matchers._
import org.mockito.Mockito._
import org.retistruen.test.CustomMockitoMatchers._
import org.retistruen.test.CustomScalaTestMatchers._
import org.retistruen.{ Datum, Source }
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import org.scalatest.Spec

class ConcurrencySurveySpec extends Spec with ShouldMatchers with MockitoSugar {

  describe("A concurrency survey") {
    describe("when entered once") {
      it("should have emitted a 1") {
        val (src, con) = fixture
        con.enter
        verify(src) << argThat(verifies[Datum[Int]](_.value == 1))
      }
    }
    describe("when entered twice") {
      it("should have emitted a 2") {
        val (src, con) = fixture
        con.enter
        con.enter
        verify(src) << argThat(verifies[Datum[Int]](_.value == 2))
      }
    }
    describe("when entered and leaved twice") {
      it("should have emitted a 0") {
        val (src, con) = fixture
        con.enter
        con.enter
        con.leave
        con.leave
        verify(src) << argThat(verifies[Datum[Int]](_.value == 0))
      }
    }
    describe("when entered and leaved on a block") {
      it("should have emitted a 0") {
        val (src, con) = fixture
        con.survey {
          // blabla
        }
        verify(src) << argThat(verifies[Datum[Int]](_.value == 0))
      }
    }
    describe("when entered and leaved on a block returning a value") {
      it("should have emitted a 0 and returned that value") {
        val (src, con) = fixture
        expect(4) {
          con.survey {
            // Me aseguro de que este bloque se ejecute dentro de ConcurrencySurvey.survey()
            Thread.currentThread.getStackTrace
              .find(e ⇒
                e.getClassName == classOf[ConcurrencySurvey].getName &&
                  e.getMethodName == "survey") should not be ('empty)
            2 + 2
          }
        }
        verify(src) << argThat(verifies[Datum[Int]](_.value == 1))
        verify(src) << argThat(verifies[Datum[Int]](_.value == 0))
      }
    }
  }

  def fixture = {
    val target = mock[Source[Int]]
    val concurrency = new ConcurrencySurvey(target)
    (target, concurrency)
  }

}
