package models

import anorm._
import play.api.db.DB
import play.api.Play.current

/**
 * Created by Ireneusz on 2015-08-04.
 */
case class Dzialka(nazwa: String, nr_kat: String, typ: String, wlas: String, area: Double) {

}

object Dzialka {
  val simple = {
    SqlParser.str("nazwa") ~
    SqlParser.str("nr_kat") ~
      SqlParser.str("typ") ~
      SqlParser.str("wlas") ~
      SqlParser.double("area") map {
      case nazwa ~ nr_kat ~ typ ~ wlas ~ area => Dzialka(nazwa,nr_kat,typ,wlas,area)
    }
  }

  def findAll: Seq[Dzialka] = {
    DB.withConnection { implicit connection =>
      SQL("select * from Dzialki").as(simple *)
    }
  }

  def addDzialka(dzialka: Dzialka) = {
    DB.withConnection { implicit c =>
      SQL("insert into Dzialki(nazwa,nr_kat,typ,wlas,area) values ({nazwa}, {nr_kat}, {typ}, {wlas}, {area})").on(
        'nazwa -> dzialka.nazwa,
        'nr_kat -> dzialka.nr_kat,
        'typ -> dzialka.typ,
        'wlas -> dzialka.wlas,
        'area -> dzialka.area
      ).executeUpdate()
    }
  }

  def deleteDzialka(numer: String) = DB.withConnection { implicit c =>
    SQL("delete from Dzialki " + " where nr_kat = {numer}").on(
      'numer -> numer
    ).executeUpdate
  }
}


