FROM amazoncorretto:17
COPY build/libs/etl-1.0.0-SNAPSHOT.jar /opt/etl/etl.jar
CMD java $DEBUG_OPTS -jar /opt/etl/etl.jar
