FROM java

ENV WORK_PATH /data/eureka
ENV APP_NAME eureka
ENV APP_VERSION 1.0.0
ENV APP_SUBFIX jar

EXPOSE 21001

COPY $APP_NAME-$APP_VERSION.$APP_SUBFIX $WORK_PATH

WORKDIR $WORK_PATH

ENTRYPOINT java -jar $APP_NAME-$APP_VERSION.$APP_SUBFIX  --spring.profiles.active= eureka1

CMD ["java", "-version"]