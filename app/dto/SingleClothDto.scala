package dto

import play.api.libs.functional.syntax.{toFunctionalBuilderOps, unlift}
import play.api.libs.json.{Format, JsPath}

case class SingleClothDto(id: Long, name: String, price: Double, brand: String, clothType: String, imagesUrls: List[String], clothPieces: List[String])

object SingleClothDto {
  implicit val singleClothDtoFormat: Format[SingleClothDto] = (
    (JsPath \ "id").format[Long] and
      (JsPath \ "name").format[String] and
      (JsPath \ "price").format[Double] and
      (JsPath \ "brand" \ "name").format[String] and
      (JsPath \ "clothType").format[String] and
      (JsPath \ "imagesUrls").format[List[String]] and
      (JsPath \ "clothPieces").format[List[String]]
    )(SingleClothDto.apply, unlift(SingleClothDto.unapply))
}