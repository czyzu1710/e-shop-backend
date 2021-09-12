package dto

import play.api.libs.functional.syntax.{toFunctionalBuilderOps, unlift}
import play.api.libs.json._;

case class ClothDto(id: Long, name: String, price: Double, brand: String, clothType: String, imagesUrls: List[String])

object ClothDto {
  implicit val clothDtoFormat: Format[ClothDto] = (
    (JsPath \ "id").format[Long] and
      (JsPath \ "name").format[String] and
      (JsPath \ "price").format[Double] and
      (JsPath \ "brand" \ "name").format[String] and
      (JsPath \ "clothType").format[String] and
      (JsPath \ "imagesUrls").format[List[String]]
  )(ClothDto.apply, unlift(ClothDto.unapply))
}