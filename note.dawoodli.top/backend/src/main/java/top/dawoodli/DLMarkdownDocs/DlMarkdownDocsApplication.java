package top.dawoodli.DLMarkdownDocs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
public class DlMarkdownDocsApplication {
    public static void main(String[] args) {
        SpringApplication.run(DlMarkdownDocsApplication.class, args);
    }
}
