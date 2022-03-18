package s23456.sri.soap.config;


import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
@EnableWs
@Configuration
public class SoapWSConfig extends WsConfigurerAdapter {
    public static final String CAR_NAMESPACE ="http://soap.sri.s23456/cars";
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();
        messageDispatcherServlet.setApplicationContext(applicationContext);
        messageDispatcherServlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(messageDispatcherServlet, "/ws/*");
    }
    @Bean(name = "cars")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema carsSchema) {
        DefaultWsdl11Definition defaultWsdl11Definition = new DefaultWsdl11Definition();
        defaultWsdl11Definition.setPortTypeName("CarsPort");
        defaultWsdl11Definition.setLocationUri("/ws/cars");
        defaultWsdl11Definition.setTargetNamespace(CAR_NAMESPACE);
        defaultWsdl11Definition.setSchema(carsSchema);
        return defaultWsdl11Definition;
    }
    @Bean
    public XsdSchema carsSchema() {
        return new SimpleXsdSchema(new ClassPathResource("cars.xsd"));
    }
}
