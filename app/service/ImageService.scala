package service

import net.coobird.thumbnailator.{Thumbnailator, Thumbnails}
import play.api.Application

import java.io.File
import javax.inject.{Inject, Singleton}

@Singleton
class ImageService @Inject()(application: Application){

  def getImage(filename: String, width: Option[Int], height: Option[Int]): File = {
    if( width.isEmpty || height.isEmpty) {
      new File(application.path.getPath + "/public/images/" + filename)
    } else {
      Thumbnails.of(application.path.getPath + "/public/images/" + filename).forceSize(width.get, height.get).toFile(application.path.getPath + "/tmp/" + filename)
      new File(application.path.getPath + "/tmp/" + filename)
    }
  }
}
