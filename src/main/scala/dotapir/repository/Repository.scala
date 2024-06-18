package dotapir.repository

import zio.ZLayer

import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill
import io.getquill.jdbczio.Quill.Postgres

// fourni l'accès à postgres à partir d'une datasource
object Repository {

  def quillLayer = Quill.Postgres.fromNamingStrategy(SnakeCase)

  private def datasourceLayer = Quill.DataSource.fromPrefix("db")

  // fournie une datasource
  def dataLayer: ZLayer[Any, Throwable, Postgres[SnakeCase.type]] =
    // combinaison de layer
    datasourceLayer >>> quillLayer
}
