package dto

import play.api.libs.json.{Json, OFormat}

case class ClothPageDto(count: Int, cloths: List[ClothDto])

object ClothPageDto {
  implicit val clothPageDtoFormat: OFormat[ClothPageDto] = Json.format[ClothPageDto]
}
