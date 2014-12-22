package rbac.configuration;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by chandra on 10/30/14.
 */

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "loginEntityManager",
        transactionManagerRef = "loginTransactionManager",
        basePackages = {"rbac.repository.login"})
@EnableTransactionManagement
public class LoginDatabaseConfiguration{

    @Autowired
    private EmbeddedDatabase ed;

    @Bean(name="loginEntityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean loginEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(hsqlInMemory());
        em.setPackagesToScan(new String[] { "rbac.model.login"});
        em.setPersistenceUnitName("PU");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalJpaProperties());
        em.afterPropertiesSet();

        return em;
    }

    Properties additionalJpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        properties.setProperty("hibernate.show_sql", "true");

        return properties;
    }

    @Bean(name="hsqlInMemory")
    @Primary
    public EmbeddedDatabase hsqlInMemory() {

        if ( this.ed == null ) {
            EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
            this.ed = builder.setType(EmbeddedDatabaseType.HSQL).build();
        }

        return this.ed;

    }

    @Bean(name="loginTransactionManager")
    @Primary
    public PlatformTransactionManager loginTransactionManager(EntityManagerFactory emf){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    @Bean(name="loginExceptionTranslation")
    public PersistenceExceptionTranslationPostProcessor loginExceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

//    @Override
//    public void destroy() {
//
//        if ( this.ed != null ) {
//            this.ed.shutdown();
//        }
//
//    }

}
