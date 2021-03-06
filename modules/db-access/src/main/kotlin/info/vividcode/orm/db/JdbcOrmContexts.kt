package info.vividcode.orm.db

import info.vividcode.orm.OrmContextProvider
import info.vividcode.orm.RelationPredicate
import info.vividcode.orm.TupleClassRegistry
import info.vividcode.orm.common.BareRelationExtensionMethodInvocation
import info.vividcode.orm.common.OrmContextInvocationHandler
import info.vividcode.orm.common.OrmQueryContextFunctionInvocation
import info.vividcode.orm.common.RelvarUpdater
import kotlinx.coroutines.CoroutineDispatcher
import java.lang.reflect.Proxy
import java.sql.Connection
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

object JdbcOrmContexts {

    fun <T : Any> create(ormContextInterface: KClass<T>, connection: Connection): T = run {
        val tupleClassRegistry = TupleClassRegistry.Default
        Proxy.newProxyInstance(
                ormContextInterface.java.classLoader,
                arrayOf(ormContextInterface.java),
                OrmContextInvocationHandler(
                        tupleClassRegistry,
                        DbBareRelationRegistry(tupleClassRegistry),
                        OrmQueryContextFunctionInvocation(connection),
                        BareRelationExtensionMethodInvocation(JdbcRelvarUpdater(connection))
                )
        ).let(ormContextInterface::cast)
    }

    fun <T : Any> createProviderFactoryFor(
        ormContextInterface: KClass<T>, jdbcCoroutineContext: CoroutineDispatcher
    ): (Connection) -> OrmContextProvider<T> = {
        JdbcOrmContextProvider(ormContextInterface, it, jdbcCoroutineContext)
    }

    private class JdbcRelvarUpdater(private val connection: Connection) : RelvarUpdater {
        override fun insert(
                relationName: String,
                insertedValue: Any,
                tupleClassRegistry: TupleClassRegistry,
                returnGeneratedKeys: Boolean
        ) = insert(connection, relationName, insertedValue, tupleClassRegistry, returnGeneratedKeys)

        override fun update(
                relationName: String,
                updateValue: Any,
                predicate: RelationPredicate<*>,
                tupleClassRegistry: TupleClassRegistry
        ) = update(connection, relationName, updateValue, predicate, tupleClassRegistry)

        override fun delete(
                relationName: String,
                predicate: RelationPredicate<*>,
                tupleClassRegistry: TupleClassRegistry
        ) = delete(connection, relationName, predicate, tupleClassRegistry)
    }

}
