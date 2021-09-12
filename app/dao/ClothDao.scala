package dao

import dto.{ClothDto, ClothPageDto, SingleClothDto}
import models.{Brand, Cloth, ClothType}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ClothDao @Inject()
(dbConfigProvider: DatabaseConfigProvider, implicit val ec: ExecutionContext) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class ClothTypeTable(tag: Tag) extends Table[ClothType](tag, "CLOTH_TYPE") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    def typeName = column[String]("TYPE_NAME", O.Unique)

    def * = (id, typeName) <> (ClothType.tupled, ClothType.unapply)
  }

  class BrandTable(tag: Tag) extends Table[Brand](tag, "BRAND") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    def brandName = column[String]("BRAND_NAME", O.Unique)

    def * = (id, brandName) <> (Brand.tupled, Brand.unapply)
  }

  implicit val clothTypes: TableQuery[ClothTypeTable] = TableQuery[ClothTypeTable]
  implicit val brands: TableQuery[BrandTable] = TableQuery[BrandTable]

  class ClothTable(tag: Tag) extends Table[Cloth](tag, "CLOTH") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    def name = column[String]("NAME")

    def price = column[Double]("PRICE")

    def typeId = column[Long]("TYPE_ID")

    def clothType = foreignKey("CLOTH_CLOTH_TYPE_FK", typeId, clothTypes)(_.id)

    def brandId = column[Long]("BRAND_ID")

    def brand = foreignKey("CLOTH_BRAND_FK", brandId, brands)(_.id)

    def * = (id, name, price, typeId, brandId) <> (Cloth.tupled, Cloth.unapply)
  }

  implicit val cloths: TableQuery[ClothTable] = TableQuery[ClothTable]

  case class ClothImage(clothId: Long, imageUrl: String)

  class ClothImageTable(tag: Tag) extends Table[ClothImage](tag, "CLOTH_IMAGE") {
    def clothId = column[Long]("CLOTH_ID")

    def imageUrl = column[String]("IMAGE_URL", O.PrimaryKey)

    def Cloth = foreignKey("CLOTH_IMAGES_FK", clothId, cloths)(_.id)

    def * = (clothId, imageUrl) <> (ClothImage.tupled, ClothImage.unapply)
  }

  implicit val clothImages: TableQuery[ClothImageTable] = TableQuery[ClothImageTable];

  def get(id: Long): Future[Option[SingleClothDto]] = {
    val query = cloths.filter(_.id === id).join(brands).on(_.brandId === _.id).joinLeft(clothImages).on(_._1.id === _.clothId)
    db.run(query.result)
      .map { a => a.groupBy(_._1._1.id)
        .map { case (_, value) =>
          val ((cloth, brand), _) = value.head
          val images = value.flatMap(_._2).map(clothImage => clothImage.imageUrl)
          SingleClothDto(cloth.id, cloth.name, cloth.price, brand.name, "T-SHIRT", images.toList, List("XS", "S", "M", "L", "XL"))
        }.headOption
      }
  }

  def add(cloth: Cloth): Future[Cloth] = {
    db.run(cloths returning cloths.map(_.id) into ((cloth, id) => cloth.copy(id = id)) += cloth)
  }

  def delete(id: Long): Future[Int] = {
    db.run(cloths.filter(_.id === id).delete)
  }

  //  def getAll(limit: Int, offset: Int): Future[ClothPageDto] = {
  //    db.run(
  //      for {
  //        clothList <- cloths.join(brands).on(_.brandId === _.id).drop(offset).take(limit)
  ////        pages <- cloths.length.result
  //      } yield (clothList)
  //    )
  //  }

  def getPage(limit: Int, offset: Int): Future[ClothPageDto] = {
    val query = cloths.drop(offset).take(limit) join brands on (_.brandId === _.id) joinLeft clothImages on (_._1.id === _.clothId)
    val count = 3
    db.run(
      query.result).map { a =>
      a.groupBy(_._1._1.id).map { case (_, value) =>
        val ((cloth, brand), _) = value.head
        val images = value.flatMap(_._2).map(clothImage => clothImage.imageUrl)
        ClothDto(cloth.id, cloth.name, cloth.price, brand.name, "T-SHIRT", images.toList)
      }.toList
    }.map(a => ClothPageDto(count, a))
  }


}
