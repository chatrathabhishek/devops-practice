# Network
docker network create java-net

# Mysql
docker run -d \              
-p 3306:3306 \
--name mysql \
-e MYSQL_ROOT_PASSWORD=rootpass \
-e MYSQL_DATABASE=team-member-projects \
-e MYSQL_USER=admin \
-e MYSQL_PASSWORD=adminpass \
--net java-net mysql mysqld --default-authentication-plugin=mysql_native_password

# Phpmyadmin
docker run -d \             
-p 8083:80 \
--link mysql:db \
--name phpadmin \
--net java-net \
phpmyadmin