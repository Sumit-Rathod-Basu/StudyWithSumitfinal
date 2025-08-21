package eNotesProvider.eNotesProvider;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = {
    "eNotesProvider.eNotesProvider.Controller",
    "eNotesProvider.eNotesProvider.Service",
    "eNotesProvider.eNotesProvider.Repository",
    "eNotesProvider.eNotesProvider.Mapper"
})
public class ENotesProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ENotesProviderApplication.class, args);
        System.out.println("program runnable ");
    }
}
