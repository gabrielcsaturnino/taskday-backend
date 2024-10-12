# Usar uma imagem do Maven para fazer o build
FROM maven:3.8.1-openjdk-17 AS build

# Diretório de trabalho
WORKDIR /app

# Copiar o pom.xml e arquivos de dependências
COPY pom.xml .

# Baixar as dependências do Maven (essa etapa será cacheada, então não vai precisar baixar de novo a cada build)
RUN mvn dependency:go-offline -B

COPY . .

# Rodar o comando de build do Maven
RUN mvn clean package -DskipTests

# Segunda etapa: usar uma imagem mais leve para rodar o .jar
FROM openjdk:17-jdk-slim

# Diretório de trabalho
WORKDIR /app

# Copiar o .jar gerado na etapa anterior
COPY --from=build /app/target/*.jar app.jar
COPY src/main/resources/keystore.p12 /app/keystore.p12
# Definir o JAVA_OPTS
ENV JAVA_OPTS="-Dcom.sun.management.jmxremote \
               -Dcom.sun.management.jmxremote.port=9010 \
               -Dcom.sun.management.jmxremote.rmi.port=9011 \
               -Dcom.sun.management.jmxremote.authenticate=false \
               -Dcom.sun.management.jmxremote.ssl=false \
               -Djava.rmi.server.hostname=localhost"

# Comando para rodar a aplicação com as opções do JAVA_OPTS
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
