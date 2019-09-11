FROM alpine:3.7 as init

ENV MOSSPARSER_PORT=8082
ENV MOSSPARSER_HOME=/opt/mossparser

WORKDIR $MOSSPARSER_HOME

RUN apk add --update openjdk8 git \
    && rm -rf /var/cache/apk/*

RUN git clone https://github.com/nikita715/mossparser.git $MOSSPARSER_HOME

FROM alpine:3.7 as prod

ENV MOSSPARSER_HOME=/opt/mossparser

WORKDIR $MOSSPARSER_HOME

RUN apk add --update openjdk8 \
    && rm -rf /var/cache/apk/*

COPY --from=init $MOSSPARSER_HOME/core/build/libs/moss-parser-0.1.jar .

CMD java -jar $MOSSPARSER_HOME/core.jar