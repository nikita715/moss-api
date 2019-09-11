FROM alpine:3.7 as init

ENV MOSSPARSER_PORT=8082
ENV MOSSPARSER_HOME=/opt/mossparser

WORKDIR $MOSSPARSER_HOME

RUN apk --no-cache add openjdk11 --repository=http://dl-cdn.alpinelinux.org/alpine/edge/community \
    && apk --no-cache add --update git

RUN git clone https://github.com/nikita715/mossparser.git $MOSSPARSER_HOME

FROM alpine:3.7 as prod

ENV MOSSPARSER_HOME=/opt/mossparser

WORKDIR $MOSSPARSER_HOME

RUN apk --no-cache add openjdk11 --repository=http://dl-cdn.alpinelinux.org/alpine/edge/community

COPY --from=init $MOSSPARSER_HOME/core/build/libs/moss-parser-0.1.jar .

CMD java -jar $MOSSPARSER_HOME/core.jar