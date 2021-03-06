package models
import play.api.libs.json.{Json, OFormat}

case class SignUpRequest(email: String, password: String, firstName: String, lastName: String)

object SignUpRequest {
  implicit val signUpRequestForm: OFormat[SignUpRequest] = Json.format[SignUpRequest]
}