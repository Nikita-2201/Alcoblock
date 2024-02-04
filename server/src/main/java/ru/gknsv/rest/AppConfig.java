package ru.gknsv.rest;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.Path;
import java.util.Set;
import java.util.regex.Pattern;

@Component
@ApplicationPath("")
public class AppConfig extends ResourceConfig {

	@Autowired
	public AppConfig() throws ClassNotFoundException {
		final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));
		final Set<BeanDefinition> classes = provider.findCandidateComponents("ru.gknsv");
		for (BeanDefinition bean: classes) {
			Class<?> clazz = Class.forName(bean.getBeanClassName());
			if (clazz.isAnnotationPresent(Path.class)) {
				register(clazz);
			}
		}
		register(MultiPartFeature.class);
	}

	@PostConstruct
	public void init() {
		this.register(ApiListingResource.class);
		this.register(SwaggerSerializers.class);

		BeanConfig swaggerConfigBean = new BeanConfig();
		swaggerConfigBean.setConfigId("Alcoblock Swagger API");
		swaggerConfigBean.setTitle("Alcoblock API");
		swaggerConfigBean.setVersion("v1");
		swaggerConfigBean.setContact("AVT-910 Team");
		swaggerConfigBean.setSchemes(new String[] { "http", "https" });
		swaggerConfigBean.setBasePath("/rest");
		swaggerConfigBean.setResourcePackage("ru.gknsv");
		swaggerConfigBean.setPrettyPrint(true);
		swaggerConfigBean.setScan(true);
	}
}