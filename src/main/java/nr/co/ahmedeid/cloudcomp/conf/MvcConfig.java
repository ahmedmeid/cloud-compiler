package nr.co.ahmedeid.cloudcomp.conf;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import nr.co.ahmedeid.cloudcomp.builder.AntProjectBuilder;
import nr.co.ahmedeid.cloudcomp.builder.JavaProjectBuilder;
import nr.co.ahmedeid.cloudcomp.builder.MavenProjectBuilder;
import nr.co.ahmedeid.cloudcomp.builder.ZipFileHandler;
/**
 * 
 * @author ahmedeid
 *
 */
@Configuration
@ComponentScan(basePackages="hello")
public class MvcConfig extends WebMvcConfigurerAdapter {
	
	 @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("/resources/css/**").addResourceLocations("/resources/css/").setCachePeriod(31556926);
	        registry.addResourceHandler("/resources/img/**").addResourceLocations("/resources/images/").setCachePeriod(31556926);
	        registry.addResourceHandler("/resources/js/**").addResourceLocations("/resources/js/").setCachePeriod(31556926);
	    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/login").setViewName("login");
    }
    
    @Bean
    public ZipFileHandler zipFileHandler()
    {
    	return new ZipFileHandler();
    }
    
    @Bean
    public JavaProjectBuilder compiler()
    {
      return new JavaProjectBuilder();
    }
    
    @Bean
    public MavenProjectBuilder mavenBuilder()
    {
    	return new MavenProjectBuilder();
    }
    
    @Bean
    public AntProjectBuilder antBuilder()
    {
    	return new AntProjectBuilder();
    }

}
