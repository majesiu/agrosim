package models

import anorm._
import play.api.db.DB
import play.api.Play.current
/**
 * Created by Ireneusz on 2015-08-05.
 */
case class Uprawa (nr_kat: String, uprawa: String, rok: String){

}

object Uprawa {
  val simple = {
    SqlParser.str("nr_kat") ~
      SqlParser.str("uprawa") ~
      SqlParser.str("rok") map {
      case nr_kat ~ uprawa ~ rok => Uprawa(nr_kat,uprawa,rok)
    }
  }


  def findAll(): Seq[Uprawa] = {
    DB.withConnection { implicit connection =>
      SQL("select * from Uprawy").as(simple *)
    }
  }

  def findDzialka(numer: String): Seq[Uprawa] = {
    DB.withConnection { implicit c =>
      SQL("select * from Uprawy " + " where nr_kat = {numer} order by rok desc").on(
      'numer -> numer
      ).as(simple *)
    }
  }

  def addUprawa(uprawa: Uprawa): Unit = {
    DB.withConnection { implicit c =>
      SQL("insert into Uprawy(nr_kat,uprawa,rok) values ({nr_kat}, {uprawa}, {rok})").on(
        'nr_kat -> uprawa.nr_kat,
        'uprawa -> uprawa.uprawa,
        'rok -> uprawa.rok
      ).executeUpdate()
    }
  }

  def deleteUprawa(numer: String,  uprawa: String, rok: String) = DB.withConnection { implicit c =>
    SQL("delete from Uprawy " + " where nr_kat = {numer} and uprawa = {uprawa} and rok = {rok}").on(
      'numer -> numer,
      'uprawa -> uprawa,
      'rok -> rok
    ).executeUpdate
  }
}