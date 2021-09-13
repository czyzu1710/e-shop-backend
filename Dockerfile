FROM azul/zulu-openjdk:15
ARG APP_FILE=target/*.zip
COPY $APP_FILE app.zip
RUN apt-get update
RUN apt-get install zip unzip
RUN unzip app.zip  && mkdir app-dir && mv e-shop*/* app-dir/
EXPOSE 9000
CMD ["./app-dir/bin/e-shop-backend"]


