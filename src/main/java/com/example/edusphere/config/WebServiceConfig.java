package com.example.edusphere.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
            ApplicationContext applicationContext) {

        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);

        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean
    public SimpleXsdSchema studentResultsSchema() {
        return new SimpleXsdSchema(
                new ClassPathResource("student-results.xsd")
        );
    }

    @Bean(name = "student-results")
    public DefaultWsdl11Definition defaultWsdl11Definition(
            SimpleXsdSchema studentResultsSchema) {

        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();

        definition.setPortTypeName("StudentResults");
        definition.setLocationUri("/ws");
        definition.setTargetNamespace(
                "http://edusphere.com/student-results"
        );
        definition.setSchema(studentResultsSchema);

        return definition;
    }
}