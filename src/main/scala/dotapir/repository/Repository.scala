package dotapir.repository

import zio.ZLayer

import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill
import io.getquill.jdbczio.Quill.Postgres

object Repository {

  // transformer un layer en un autre layer
  def quillLayer = Quill.Postgres.fromNamingStrategy(SnakeCase)

  private def datasourceLayer = Quill.DataSource.fromPrefix("db")

  // fusionner les layers
  def dataLayer: ZLayer[Any, Throwable, Postgres[SnakeCase.type]] =
    datasourceLayer >>> quillLayer
}
