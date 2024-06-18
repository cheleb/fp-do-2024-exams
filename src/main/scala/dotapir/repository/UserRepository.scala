package dotapir.repository

import zio.Task
import zio.ZLayer

import io.getquill.*
import io.getquill.jdbczio.Quill
import dotapir.model.User

// Définition des prototypes de fonction dédiées à la communication avec la DB
trait UserRepository {
  def create(user: User): Task[User]
  def getById(id: Long): Task[Option[User]]
  def getAll: Task[List[User]]
  def getByEmail(email: String): Task[Option[User]]
  def update(id: Long, op: User => User): Task[User]
  def delete(id: Long): Task[User]
}

class UserRepositoryLive private (quill: Quill.Postgres[SnakeCase])
    extends UserRepository {

  import quill.*

  inline given SchemaMeta[User] = schemaMeta[User]("users")
  inline given InsertMeta[User] = insertMeta[User](_.id, _.created)
  inline given UpdateMeta[User] = updateMeta[User](_.id, _.created)

  // Définition des fonctions qui communiquent avec la DB
  override def create(user: User): Task[User] =
    run(query[User].insertValue(lift(user)).returning(r => r))
  override def getById(id: Long): Task[Option[User]] =
    run(query[User].filter(_.id == lift(id))).map(_.headOption)
  override def getAll: Task[List[User]] =
    run(query[User]).map(_.toList)
  override def getByEmail(email: String): Task[Option[User]] =
    run(query[User].filter(_.email == lift(email))).map(_.headOption)

  // Permet de mettre à jour un User dans la base de données à partir de son ID
  override def update(id: Long, op: User => User): Task[User] =
    for {
      user <- getById(id).someOrFail(
        new RuntimeException(s"User $id not found")
      )
      updated <- run(
        query[User]
          .filter(_.id == lift(id))
          .updateValue(lift(op(user)))
          .returning(r => r)
      )
    } yield updated

  // Permet de supprimer un User dan la DB à partir de l'Id
  override def delete(id: Long): Task[User] =
    run(query[User].filter(_.id == lift(id)).delete.returning(r => r))
}

// Je suppose que c'est un Wrapper de UserRepositoryLive, qui sert à avoir la possibilité d'ajouter d'autres couches supplémentaires plus tard
object UserRepositoryLive {
  def layer = ZLayer.fromFunction(UserRepositoryLive(_))
}
