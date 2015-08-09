package models

import java.util.Date

import anorm._
import play.api.db.DB
import play.api.Play.current

/**
 * Created by Ireneusz on 2015-08-06.
 */
case class Zabieg (numer: String, rok: String, typ: String, koszt: Double, data: Date, opis: String){

}

object Zabieg {
  val simple = {
      SqlParser.str("nr_kat") ~
      SqlParser.str("rok") ~
        SqlParser.str("typ") ~
        SqlParser.double("koszt") ~
      SqlParser.date("data") ~
    SqlParser.str("opis") map {
      case nr_kat ~ rok ~ typ ~ koszt ~ data ~ opis => Zabieg(nr_kat,rok,typ,koszt,data,opis)
    }
  }


  def findAll(): Seq[Zabieg] = {
    DB.withConnection { implicit connection =>
      SQL("select * from Zabiegi").as(simple *)
    }
  }

  def findZabiegi(numer: String, rok: String): Seq[Zabieg] = {
    DB.withConnection { implicit c =>
      SQL("select * from Zabiegi " + " where nr_kat = {numer} and rok = {rok} order by data desc").on(
      'numer -> numer,
      'rok -> rok
      ).as(simple *)
    }
  }

  def addZabieg(zabieg: Zabieg): Unit = {
    DB.withConnection { implicit c =>
      SQL("insert into Zabiegi(nr_kat,rok,typ,koszt,data,opis) values ({numer}, {rok}, {typ}, {koszt}, {data},{opis})").on(
        'numer -> zabieg.numer,
        'rok -> zabieg.rok,
        'typ -> zabieg.typ,
        'koszt -> zabieg.koszt,
        'data -> zabieg.data,
        'opis -> zabieg.opis
      ).executeUpdate
    }
  }

  def deleteZabieg(numer: String, rok: String, typ: String, koszt: Double) = DB.withConnection { implicit c =>
    SQL("delete from Zabiegi " + " where nr_kat = {numer} and rok = {rok} and typ = {typ} and koszt = {koszt}").on(
      'numer -> numer,
      'rok -> rok,
      'typ -> typ,
      'koszt -> koszt
    ).executeUpdate
  }
}