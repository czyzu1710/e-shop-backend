package controllers

import dao.ClothDao
import play.api.libs.json.{Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import service.ImageService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ClothController @Inject()(val controllerComponents: ControllerComponents, clothDao: ClothDao, imageService: ImageService)(implicit ec: ExecutionContext) extends BaseController {
  private val pageSize = 2

  def getCloth(id: Long): Action[AnyContent] = Action.async{ implicit req =>
    val cloth = clothDao.get(id)
    cloth.map( cloth=>
      if(cloth.isDefined) {
         Ok(Json.toJson(cloth))
      } else
         NotFound
    )
  }

  def getCloths(pageNumber: Int): Action[AnyContent] = Action.async {
    implicit req =>
      clothDao.getPage(pageSize,pageNumber * pageSize).map(clothPage=> Ok(Json.toJson(clothPage)))
  }

  def getImage(fileName: String, width: Option[Int], height: Option[Int]): Action[AnyContent] = Action {
    implicit req => Ok.sendFile(imageService.getImage(fileName, width, height))
  }
}
