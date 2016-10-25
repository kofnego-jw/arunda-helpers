package at.ac.uibk.fiba.arunda.webapp.application;

import at.ac.uibk.fiba.arunda.odb.export.impl.ArundaExport;
import at.ac.uibk.fiba.arunda.watermark.WMApplier;
import at.ac.uibk.fiba.arunda.watermark.WMContainer;
import at.ac.uibk.fiba.arunda.odb.api.OdbToHsqldb;
import at.ac.uibk.fiba.arunda.odb.api.impl.OdbToHsqldbImpl;
import at.ac.uibk.fiba.arunda.odb.export.OdbExportService;
import at.ac.uibk.fiba.arunda.odb.export.impl.OdbExportServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

/**
 * Created by joseph on 10/25/16.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"at.ac.uibk.fiba.arunda.watermark", "at.ac.uibk.fiba.arunda.webapp.web"})
public class Application {

    @Bean
    public WMContainer wmContainer() throws Exception {
        return WMContainer.createWMContainer();
    }

    @Bean
    public WMApplier wmApplier() throws Exception {
        return new WMApplier(wmContainer().getWatermark());
    }

    @Bean
    public OdbToHsqldb odbToHsqldb() {
        OdbToHsqldbImpl odbToHsqldb = new OdbToHsqldbImpl();
        return odbToHsqldb;
    }

    @Bean
    public OdbExportService odbExportService() {
        OdbExportServiceImpl service = new OdbExportServiceImpl();
        service.setOdbToHsqldb(odbToHsqldb());
        return service;
    }

    @Bean
    public ArundaExport arundaExport() {
        return new ArundaExport(odbExportService());
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class);
        Arrays.asList(ctx.getBeanDefinitionNames()).stream()
                .sorted()
                .forEach(System.out::println);

    }

}
