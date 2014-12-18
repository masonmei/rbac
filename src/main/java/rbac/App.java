package rbac;


import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by chandra on 11/3/14.
 */

@ComponentScan
@EnableAutoConfiguration
//@EnableJpaRepositories
@EnableWebMvc
@EnableSpringDataWebSupport
public class App{

//    private static ApplicationContext ctx;

    public static void main( String[] args ) {

        System.setProperty("javax.net.ssl.trustStore","/home/chandra/.keystore");
        System.setProperty("javax.net.ssl.trustStorePassword","changeit");

        Logger LOGGER = LoggerFactory.getLogger(App.class);

//        new SpringApplicationBuilder()
//                .sources(App.class)
//                .showBanner(false)
//                .run(args);

        SpringApplication app = new SpringApplication(App.class);
        //app.setWebEnvironment(false);
        ConfigurableApplicationContext ctx= app.run(args);

//        AuthorizationAttributeSourceAdvisor ad = (AuthorizationAttributeSourceAdvisor) ctx.getBean("authorizationAttributeSourceAdvisor");
//        System.out.println("AD "+ ad.getSecurityManager().toString());

        //SpringApplication.run(App.class);

    }
}
