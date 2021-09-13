package controllers

import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.impl.providers._
import models.User2
import play.api.{Application, Configuration}

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, Cookie, Request}
import play.filters.csrf.CSRF.Token
import play.filters.csrf.{CSRF, CSRFAddToken}

import java.net.URLDecoder
import scala.concurrent.{ExecutionContext, Future}

class SocialAuthController @Inject()(scc: DefaultSilhouetteControllerComponents, addToken: CSRFAddToken, conf: Configuration)(implicit ex: ExecutionContext) extends SilhouetteController(scc) {

  def authenticate(provider: String): Action[AnyContent] = addToken(Action.async { implicit request: Request[AnyContent] =>
    (socialProviderRegistry.get[SocialProvider](provider) match {
      case Some(p: SocialProvider with CommonSocialProfileBuilder) =>
        p.authenticate().flatMap {
          case Left(result) => Future.successful(result)
          case Right(authInfo) => for {
            profile <- p.retrieveProfile(authInfo)
            _ <- userRepository.isExist(profile.loginInfo.providerID, profile.email.getOrElse("")).map{
              case Some(data) => userRepository.update(data.id, User2(data.id, data.loginInfo, data.email, "USER", "-", "-"))
              case None => userRepository.create(profile.loginInfo.providerID, profile.loginInfo.providerKey, profile.email.getOrElse(""), "USER", "-", "-")
            }
            _ <- authInfoRepository.save(profile.loginInfo, authInfo)
            authenticator <- authenticatorService.create(profile.loginInfo)
            value <- authenticatorService.init(authenticator)
            result <- authenticatorService.embed(value, Redirect(s"${conf.get[String]("urls.frontend_full")}/product?email=${profile.email.get}&authenticator=${URLDecoder.decode(value.toString.split(",")(1), "UTF-8")}"))
          } yield {
            val Token(name, value) = CSRF.getToken.get
            result.withCookies(Cookie(name, value, httpOnly = false))
            result.withCookies(Cookie("email", profile.email.get, httpOnly = false))
          }
        }
      case _ => Future.failed(new ProviderException(s"Cannot authenticate with unexpected social provider $provider"))
    }).recover {
      case e: ProviderException =>
        logger.error("Unexpected provider error", e)
        Forbidden("Forbidden" + e.toString)
    }
  })
}