FROM java:8
COPY *.jar /home/leyou-gateway
ADD rsa /home/springcloud/rsa
EXPOSE 10010
ENTRYPOINT ["java","-jar","/home/leyou-gateway"]