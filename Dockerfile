# Usar uma imagem do Maven para fazer o build
FROM maven:3.8.1-openjdk-17 AS build

# Diretório de trabalho
WORKDIR /app

# Copiar o pom.xml e arquivos de dependências
COPY pom.xml .

# Baixar as dependências do Maven (essa etapa será cacheada, então não vai precisar baixar de novo a cada build)
RUN mvn dependency:go-offline -B

# Copiar todo o projeto para o container
COPY . .

# Rodar o comando de build do Maven
RUN mvn clean package -DskipTests

# Segunda etapa: usar uma imagem mais leve para rodar o .jar
FROM openjdk:17-jdk-slim

# Diretório de trabalho
WORKDIR /app

# Copiar o .jar gerado na etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Comando para rodar a aplicação
CMD ["java", "-jar", "app.jar"]
