package net.dzakirin.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "LinkedIn",
                        url = "https://www.linkedin.com/in/dzakirin-rahim/"
                ),
                description = "OpenApi documentation for order-management-service",
                title = "Order Management Service",
                version = "1.0.0",
                license = @License(
                        name = "GPL-3.0 license",
                        url = "https://github.com/DzakirinMD/customer-product-technical-assesment?tab=GPL-3.0-1-ov-file"
                )
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:10001"
                ),
        }
)
public class OpenApiConfig {

}
