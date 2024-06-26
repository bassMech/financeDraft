package de.bassmech.findra.web;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import jakarta.faces.webapp.FacesServlet;


@SpringBootApplication
//@EnableAutoConfiguration
@ComponentScan(basePackages = "de.bassmech.findra")
@EnableJpaRepositories(basePackages = "de.bassmech.findra.core.repository")
@EntityScan("de.bassmech.findra.model.entity")
@ServletComponentScan
public class StartApplication {
	@Autowired 
	Environment env;
	
	protected Logger logger = LoggerFactory.getLogger(StartApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(StartApplication.class, args);
	}
	
	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		FacesServlet servlet = new FacesServlet();
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(servlet, "*.xhtml");
		return servletRegistrationBean;
	}
	
	@Bean
	public DataSource dataSource() {
	    final DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
	    dataSource.setUrl(env.getProperty("spring.datasource.url"));
	    //dataSource.setUsername(env.getProperty("user"));
	    //dataSource.setPassword(env.getProperty("password"));
	    return dataSource;
	}

}
