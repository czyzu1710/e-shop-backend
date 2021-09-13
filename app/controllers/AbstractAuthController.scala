
package controllers

import com.mohiva.play.silhouette.api.services.AuthenticatorResult
import models.User2
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

abstract class AbstractAuthController(scc: DefaultSilhouetteControllerComponents)(implicit ex: ExecutionContext) extends SilhouetteController(scc) {

  protected def authenticateUser(user: User2)(implicit request: RequestHeader): Future[AuthenticatorResult] = {
    authenticatorService.create(user.loginInfo)
      .flatMap { authenticator =>
        authenticatorService.init(authenticator).flatMap { v =>
          authenticatorService.embed(v, Ok(Json.toJson(user)))
        }
      }
  }
}