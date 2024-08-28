# syntax=docker/dockerfile:1

FROM amazoncorretto:22-alpine

WORKDIR /app

COPY start_app.sh start_app.sh

ENV TsundereBotToken=Njc1NzY5NDU1MTYyMTYzMjcx.Xj79WQ.-wUk_f95bsrmfjHyDJM68TaxxOM

RUN apk add --no-cache git && \
	apk add libgcc

RUN sed -i 's/\r$//' start_app.sh  && \  
        chmod +x start_app.sh

EXPOSE 12345

CMD ./start_app.sh

