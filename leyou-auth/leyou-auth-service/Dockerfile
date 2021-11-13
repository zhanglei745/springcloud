FROM java:8
COPY *.jar /leyou-auth.jar

ADD rsa /home/springcloud/rsa
EXPOSE 8087
ENTRYPOINT ["java","-jar","leyou-auth.jar"]