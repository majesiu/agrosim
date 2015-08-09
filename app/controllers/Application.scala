package controllers

import models.{Zabieg, Uprawa, Dzialka}
import play.api.Configuration
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.DB
import play.api.mvc.{Action, Controller}
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.data.format.Formats._

class Application extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.main())
  }

  def index2 = Action { implicit request =>
    Ok(views.html.index(Dzialka.findAll)(addDzialkaForm))
  }

  val addDzialkaForm = Form(
    mapping(
      "nazwa" -> text,
      "nr_kat" -> text,
      "typ" -> text,
      "wlas" -> text,
      "area" -> of(doubleFormat)
    )(Dzialka.apply)(Dzialka.unapply)
  )

  def addDzialka = Action { implicit request =>
    addDzialkaForm.bindFromRequest.fold(
    errors => BadRequest,
    {
      case Dzialka(nazwa,nr_kat,typ,wlas,area) =>
        Dzialka.addDzialka(Dzialka(nazwa,nr_kat,typ,wlas,area))
        Redirect("/index")
    }
    )
  }

  val addUprawaForm = Form(
    mapping(
      "nr_kat" -> text,
      "uprawa" -> text,
      "rok" -> text
    )(Uprawa.apply)(Uprawa.unapply)
  )

  def dzialka(id: Int) = Action { implicit request =>
    Ok(views.html.dzialka(Uprawa.findDzialka(id.toString))(addUprawaForm))
  }

  def addUprawa = Action { implicit c =>
    addUprawaForm.bindFromRequest.fold(
    errors => BadRequest,
    {
      case Uprawa(nr_kat,uprawa,rok) =>
        Uprawa.addUprawa(Uprawa(nr_kat,uprawa,rok))
        Redirect("/dzialka/"+nr_kat)
    }
    )
  }

  val addZabiegForm = Form(
    mapping(
      "nr_kat" -> text,
      "rok" -> text,
      "typ" -> text,
      "koszt" -> of(doubleFormat),
      "data" -> date,
      "opis" -> text
    )(Zabieg.apply)(Zabieg.unapply)
  )

  def uprawa(id: Int,rok: String) = Action { implicit request =>
    Ok(views.html.uprawa(Zabieg.findZabiegi(id.toString,rok: String))(addZabiegForm))
  }

  def addZabieg = Action { implicit c =>
    addZabiegForm.bindFromRequest.fold(
    errors => BadRequest,
    {
      case Zabieg(nr_kat,rok,typ,koszt,data,opis) =>
        Zabieg.addZabieg(Zabieg(nr_kat,rok,typ,koszt,data,opis))
        Redirect("/dzialka/"+nr_kat+"/"+rok)
    }
    )
  }

    def deluprawa(id: String,rok: String,typ :String, koszt: Double) = Action {
      Zabieg.deleteZabieg(id,rok,typ,koszt)
      Redirect("/dzialka/"+id+"/"+rok)
    }

  def deldzialka(id: String,rok: String,typ :String) = Action {
    Uprawa.deleteUprawa(id,rok,typ)
    Redirect("/dzialka/"+id)
  }

  def del(id: String) = Action {
    Dzialka.deleteDzialka(id)
    Redirect("/index")
  }
}
