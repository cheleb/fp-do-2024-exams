package dotapir.repository

import zio.ZLayer

import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill
import io.getquill.jdbczio.Quill.Postgres

object Repository {

  // Postgres layer
  def quillLayer = Quill.Postgres.fromNamingStrategy(SnakeCase)

  // Get the datasource layer
  private def datasourceLayer = Quill.DataSource.fromPrefix("db")

  // inject the datasource layer into the quill layer
  def dataLayer: ZLayer[Any, Throwable, Postgres[SnakeCase.type]] =
    datasourceLayer >>> quillLayer
}
