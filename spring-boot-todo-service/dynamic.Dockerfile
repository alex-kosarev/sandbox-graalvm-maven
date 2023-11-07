FROM debian:stable-slim

RUN ["addgroup", "spring-boot-group"]
RUN ["adduser", "--ingroup", "spring-boot-group", "spring-boot"]
USER spring-boot:spring-boot-group

WORKDIR /opt/todo-service
COPY --chown=spring-boot:spring-boot-group target/sandbox-todo-service-maven /opt/todo-service/application

ENTRYPOINT ["/opt/todo-service/application", "${0}", "${@}"]
