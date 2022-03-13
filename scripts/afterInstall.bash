docker pull monkeypenthouse/api-server-spring:v1
docker create -e spring.profiles.active=prod --publish 80:80 --name api-server-spring -i monkeypenthouse/api-server-spring:v1
docker cp /home/ubuntu/resources/key-pair.der api-server-spring:/
docker cp /home/ubuntu/resources/application-prod.properties api-server-spring:/
docker start api-server-spring