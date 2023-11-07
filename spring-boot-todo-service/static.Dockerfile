FROM debian:stable-slim as build

RUN ["adduser", "--group", "spring-boot-group"]
RUN ["adduser", "--ingroup", "spring-boot-group", "spring-boot"]
RUN ["chmod", "777", "/tmp"]

FROM scratch

USER spring-boot:spring-boot-group
COPY --from=build /etc/passwd /etc/passwd
COPY --from=build /etc/group /etc/group
COPY --chown=spring-boot:spring-boot-group --from=build /tmp /tmp
WORKDIR /opt/todo-service
COPY --chown=spring-boot:spring-boot-group target/spring-boot-todo-service /opt/todo-service/application
ENTRYPOINT ["/opt/todo-service/application", "${0}", "${@}"]
