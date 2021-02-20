
#See: https://spring.io/guides/gs/spring-boot-docker/
FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","-Dspring.datasource.url=jdbc:postgresql://${POSTGRES_HOST}:5432/emission_types","moja.global.emissiontypes.Application"]