//package spring.educhainminiapp.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig {
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/auth/**")
//                        .allowedOrigins("https://arlan-z.github.io/mini-app-template/") // Замените на ваш домен
//                        .allowedMethods("POST")
//                        .allowedHeaders("*");
//
//                registry.addMapping("/api/users/**")
//                        .allowedOrigins("https://arlan-z.github.io/mini-app-template/") // Замените на ваш домен
//                        .allowedMethods("GET")
//                        .allowedHeaders("*");
//            }
//        };
//    }
//}
//
