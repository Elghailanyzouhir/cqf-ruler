package org.opencds.cqf.config;

import ca.uhn.fhir.jpa.config.BaseJavaConfigDstu3;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.search.LuceneSearchMappingFactory;
import ca.uhn.fhir.jpa.util.SubscriptionsRequireManualActivationInterceptorDstu3;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by Chris Schuler on 12/11/2016.
 */
@Configuration
@EnableTransactionManagement()
public class FhirServerConfigDstu3 extends BaseJavaConfigDstu3 {

    @Bean()
    public DaoConfig daoConfig() {
        DaoConfig retVal = new DaoConfig();
//        retVal.setSubscriptionEnabled(true);
//        retVal.setSubscriptionPollDelay(5000);
//        retVal.setSubscriptionPurgeInactiveAfterMillis(DateUtils.MILLIS_PER_HOUR);
        retVal.setAllowMultipleDelete(true);
        return retVal;
    }

//    PostgreSQL config
//    @Bean(destroyMethod = "close")
//    public DataSource dataSource() {
//        BasicDataSource retVal = new BasicDataSource();
//        retVal.setDriver(new org.postgresql.Driver());
//        retVal.setUrl("jdbc:postgresql://localhost:5432/fhir");
//        retVal.setUsername("hapi");
//        retVal.setPassword("hapi");
//        return retVal;
//    }

//    Derby config
//    @Bean(destroyMethod = "close")
//    public DataSource dataSource() {
//        BasicDataSource retVal = new BasicDataSource();
//        retVal.setDriver(new org.apache.derby.jdbc.EmbeddedDriver());
//        retVal.setUrl("jdbc:derby:directory:target/jpaserver_derby_files;create=true");
//        retVal.setUsername("");
//        retVal.setPassword("");
//        return retVal;
//    }

    // H2 Config
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        Path path = Paths.get("target/jpaserver_h2_files").toAbsolutePath();
        BasicDataSource retVal = new BasicDataSource();
        retVal.setDriver(new org.h2.Driver());
        retVal.setUrl("jdbc:h2:directory:" + path.toString() + ";create=true");
        retVal.setUsername("");
        retVal.setPassword("");
        return retVal;
    }

    @Bean()
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean retVal = new LocalContainerEntityManagerFactoryBean();
        retVal.setPersistenceUnitName("HAPI_PU");
        retVal.setDataSource(dataSource());
        retVal.setPackagesToScan("ca.uhn.fhir.jpa.entity");
        retVal.setPersistenceProvider(new HibernatePersistenceProvider());
        retVal.setJpaProperties(jpaProperties());
        return retVal;
    }

//    PostgreSQL config
//    private Properties jpaProperties() {
//        Properties extraProperties = new Properties();
//        extraProperties.put("hibernate.dialect", org.hibernate.dialect.PostgreSQL94Dialect.class.getName());
//        extraProperties.put("hibernate.format_sql", "true");
//        extraProperties.put("hibernate.show_sql", "false");
//        extraProperties.put("hibernate.hbm2ddl.auto", "update");
//        extraProperties.put("hibernate.jdbc.batch_size", "20");
//        extraProperties.put("hibernate.cache.use_query_cache", "false");
//        extraProperties.put("hibernate.cache.use_second_level_cache", "false");
//        extraProperties.put("hibernate.cache.use_structured_entries", "false");
//        extraProperties.put("hibernate.cache.use_minimal_puts", "false");
//        extraProperties.put("hibernate.search.default.directory_provider", "filesystem");
//        extraProperties.put("hibernate.search.default.indexBase", "target/lucenefiles");
//        extraProperties.put("hibernate.search.lucene_version", "LUCENE_CURRENT");
////		extraProperties.put("hibernate.search.default.worker.execution", "async");
//        return extraProperties;
//    }

//    Derby config
//    private Properties jpaProperties() {
//        Properties extraProperties = new Properties();
//        extraProperties.put("hibernate.dialect", org.hibernate.dialect.DerbyTenSevenDialect.class.getName());
//        extraProperties.put("hibernate.format_sql", "true");
//        extraProperties.put("hibernate.show_sql", "false");
//        extraProperties.put("hibernate.hbm2ddl.auto", "update");
//        extraProperties.put("hibernate.jdbc.batch_size", "20");
//        extraProperties.put("hibernate.cache.use_query_cache", "false");
//        extraProperties.put("hibernate.cache.use_second_level_cache", "false");
//        extraProperties.put("hibernate.cache.use_structured_entries", "false");
//        extraProperties.put("hibernate.cache.use_minimal_puts", "false");
//        extraProperties.put("hibernate.search.model_mapping", LuceneSearchMappingFactory.class.getName());
//        extraProperties.put("hibernate.search.default.directory_provider", "filesystem");
//        extraProperties.put("hibernate.search.default.indexBase", "target/lucenefiles");
//        extraProperties.put("hibernate.search.lucene_version", "LUCENE_CURRENT");
////		extraProperties.put("hibernate.search.default.worker.execution", "async");
//        return extraProperties;
//    }

    // H2 config
    private Properties jpaProperties() {
        Properties extraProperties = new Properties();
        extraProperties.put("hibernate.dialect", org.hibernate.dialect.H2Dialect.class.getName());
        extraProperties.put("hibernate.format_sql", "true");
        extraProperties.put("hibernate.show_sql", "false");
        extraProperties.put("hibernate.hbm2ddl.auto", "update");
        extraProperties.put("hibernate.jdbc.batch_size", "20");
        extraProperties.put("hibernate.cache.use_query_cache", "false");
        extraProperties.put("hibernate.cache.use_second_level_cache", "false");
        extraProperties.put("hibernate.cache.use_structured_entries", "false");
        extraProperties.put("hibernate.cache.use_minimal_puts", "false");
        extraProperties.put("hibernate.search.model_mapping", LuceneSearchMappingFactory.class.getName());
        extraProperties.put("hibernate.search.default.directory_provider", "filesystem");
        extraProperties.put("hibernate.search.default.indexBase", "target/lucenefiles");
        extraProperties.put("hibernate.search.lucene_version", "LUCENE_CURRENT");
//		extraProperties.put("hibernate.search.default.worker.execution", "async");
        return extraProperties;
    }

    public IServerInterceptor loggingInterceptor() {
        LoggingInterceptor retVal = new LoggingInterceptor();
        retVal.setLoggerName("fhirtest.access");
        retVal.setMessageFormat(
                "Path[${servletPath}] Source[${requestHeader.x-forwarded-for}] Operation[${operationType} ${operationName} ${idOrResourceName}] UA[${requestHeader.user-agent}] Params[${requestParameters}] ResponseEncoding[${responseEncodingNoDefault}]");
        retVal.setLogExceptions(true);
        retVal.setErrorMessageFormat("ERROR - ${requestVerb} ${requestUrl}");
        return retVal;
    }

    @Bean(autowire = Autowire.BY_TYPE)
    public IServerInterceptor responseHighlighterInterceptor() {
        return new ResponseHighlighterInterceptor();
    }

    @Bean(autowire = Autowire.BY_TYPE)
    public IServerInterceptor subscriptionSecurityInterceptor() {
        return new SubscriptionsRequireManualActivationInterceptorDstu3();
    }

    @Bean()
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager retVal = new JpaTransactionManager();
        retVal.setEntityManagerFactory(entityManagerFactory);
        return retVal;
    }

}
