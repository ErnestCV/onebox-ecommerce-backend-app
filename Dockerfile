FROM bellsoft/liberica-openjdk-alpine:17.0.1
ADD ["build/libs/ecommerce-backend-1.0-RELEASE.jar", "/opt/onebox/"]
ENTRYPOINT ["java", "-jar", "/opt/onebox/ecommerce-backend-1.0-RELEASE.jar"]
EXPOSE 8751