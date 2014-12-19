package rbac.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by chandra on 10/30/14.
 */

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "AppEntityManagerFactory",
        transactionManagerRef = "AppTransactionManager",
        basePackages = {"rbac.repository.app"})
public class AppDatabaseConfiguration {
    @Autowired
    Environment env;

    @Bean(name="AppEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean AppEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(postgreDatasource());
        em.setPackagesToScan(new String[] { "rbac.model.app"});

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalJpaProperties());

        return em;
    }

    Properties additionalJpaProperties() {
        Properties properties = new Properties();
//		  properties.setProperty("hibernate.hbm2ddl.auto", "validate");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.show_sql", "true");

        return properties;
    }

    @Bean(name="postgreDatasource")
    public DataSource postgreDatasource(){
        return DataSourceBuilder.create()
                .url(env.getProperty("db.postgres.url"))
                .driverClassName(env.getProperty("db.postgres.driver"))
                .username(env.getProperty("db.postgres.username"))
                .password(env.getProperty("db.postgres.password"))
                .build();
    }

    @Bean(name="AppTransactionManager")
    public PlatformTransactionManager AppTransactionManager(EntityManagerFactory emf){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    @Bean(name="AppExceptionTranslation")
    public PersistenceExceptionTranslationPostProcessor AppExceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

}
