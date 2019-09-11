FROM alpine:3.7 as init

ENV MOSSPARSER_HOME=/opt/mossparser

WORKDIR $MOSSPARSER_HOME

RUN apk add --update openjdk8 git \
    && rm -rf /var/cache/apk/*

RUN git clone https://github.com/nikita715/mossparser.git $MOSSPARSER_HOME \
    && chmod +x gradlew \
    && ./gradlew shadowJar

FROM alpine:3.7 as prod

ENV MOSSPARSER_HOME=/opt/mossparser
ENV MOSSPARSER_PORT=8082

WORKDIR $MOSSPARSER_HOME

RUN apk add --update openjdk8 \
    && rm -rf /var/cache/apk/*

COPY --from=init $MOSSPARSER_HOME/build/libs/mossparser-0.1-all.jar .

CMD java -jar $MOSSPARSER_HOME/mossparser-0.1-all.jar