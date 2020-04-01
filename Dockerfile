FROM gcr.io/distroless/java:11

COPY build/libs/spike-coc-*.jar spike-tracking.jar

USER nonroot:nonroot

EXPOSE 8080

CMD ["/spike-tracking.jar"]
