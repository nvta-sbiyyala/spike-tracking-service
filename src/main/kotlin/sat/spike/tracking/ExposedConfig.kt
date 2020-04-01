package sat.spike.tracking

import org.jetbrains.exposed.spring.SpringTransactionManager
import org.jetbrains.exposed.sql.transactions.TransactionManager.Companion.resetCurrent
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionManager
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.support.ResourceTransactionManager

import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
class ExposedConfig { // TODO: Do you have to *live* with this?? - validate

    @Bean
    fun transactionManager(dataSource: DataSource) =
        FixedSpringTransactionManager(SpringTransactionManager(dataSource))

    class FixedSpringTransactionManager(
        private val stm: SpringTransactionManager
    ) : ResourceTransactionManager by stm, InitializingBean by stm, TransactionManager by stm {
        override fun getTransaction(definition: TransactionDefinition?): TransactionStatus {
            // temporary fix for https://github.com/JetBrains/Exposed/issues/407
            // TransactionManager.resetCurrent(this)
            return stm.getTransaction(definition)
        }
    }
}