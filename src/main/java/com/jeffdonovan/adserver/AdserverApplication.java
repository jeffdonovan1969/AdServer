package com.jeffdonovan.adserver;

import javax.sql.DataSource;

import org.h2.server.web.WebServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * Application class that contains Ad Server configuration and main routine for
 * running Spring application.
 * 
 * @author Jeff.Donovan
 *
 */
@Configuration
@SpringBootApplication
public class AdserverApplication {

	// The URL for a console to access in memory H2 database
	private static String DB_CONSOLE_URL = "/adServerDb/console/*";
	
	/**
	 * Create in memory H2 data source
	 * @return {@link DataSource}
	 */
	@Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .generateUniqueName(true)
            .setType(EmbeddedDatabaseType.H2)
            .setScriptEncoding("UTF-8")
            .ignoreFailedDrops(true)
            .build();
    }
	
	/**
	 * Create in console for H2 data source
	 * @return {@link ServletRegistrationBean}
	 */
	@Bean
	public ServletRegistrationBean h2ServletRegistration(){
	        ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
	        registrationBean.addUrlMappings(DB_CONSOLE_URL);
	        return registrationBean;
	}
	
	/**
	 * Main routine for station Spring Application
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(AdserverApplication.class, args);
	}
	
}
