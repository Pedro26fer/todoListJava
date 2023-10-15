# Estágio de construção
FROM ubuntu:latest AS build

# Atualize o sistema e instale o OpenJDK e o Maven
RUN apt-get update
RUN apt-get install -y openjdk-17-jdk maven

# Defina o diretório de trabalho para a pasta do projeto
WORKDIR /todolist

# Copie todos os arquivos do projeto para o contêiner
COPY . .

# Execute o comando 'mvn clean install' no diretório do projeto
RUN mvn clean install

# Estágio de implantação
FROM openjdk:17-jdk-slim

# Exponha a porta que a aplicação irá utilizar (se necessário)
EXPOSE 8080

# Copie o arquivo JAR da etapa de construção para o contêiner de implantação
COPY --from=build /todolist/target/todolist-1.0.0.jar /app.jar

# Configure o comando de entrada para iniciar o aplicativo
ENTRYPOINT ["java", "-jar", "/app.jar"]


# FROM ubuntu:latest AS build


# RUN apt-get  update
# RUN apt-get install openjdk-17-jdk -y

# COPY . . 

# RUN apt-get install maven -y
# RUN mvn clean install


# FROM openjdk:17-jdk-slim

# EXPOSE 8080

# COPY --from=build /target/todolist-1.0.0.jar app.jar

# ENTRYPOINT [ "java", "-jar", "app.jar" ]



