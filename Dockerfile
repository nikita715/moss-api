FROM alpine:3.7 as init

ENV MOSSPARSER_HOME=/opt/moss-api

WORKDIR $MOSSPARSER_HOME

RUN apk add --update openjdk8 git \
    && rm -rf /var/cache/apk/*

RUN git clone https://github.com/nikita715/moss-api.git $MOSSPARSER_HOME \
    && chmod +x gradlew \
    && ./gradlew shadowJar

FROM alpine:3.7 as prod

ENV VERSION=0.1
ENV JAR_NAME=moss-api-$VERSION-all.jar

ENV MOSSPARSER_HOME=/opt/moss-api
ENV MOSSPARSER_PORT=8082

WORKDIR $MOSSPARSER_HOME

RUN apk add --update openjdk8 \
    && rm -rf /var/cache/apk/*

COPY --from=init $MOSSPARSER_HOME/build/libs/$JAR_NAME .

CMD java -jar $MOSSPARSER_HOME/$JAR_NAME