# Running the Ktor application
FROM gradle:7-jdk11 AS build
COPY  . /src
WORKDIR /src
RUN gradle buildFatJar --no-daemon

FROM ubuntu:20.04
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /src/build/libs/Doctor*.jar /app/doctor.jar

RUN apt-get update && apt-get install -y software-properties-common gcc

# Installing Python 3.10.2
RUN apt-get update -y && apt-get upgrade -y

# Install Java 11
ARG DEBIAN_FRONTEND=noninteractive
RUN apt-get install -y openjdk-11-jdk


# Install Swift 5.7
RUN apt update -y && apt upgrade -y \
    && apt install curl ca-certificates gnupg -y \
    && curl -fsSL https://archive.swiftlang.xyz/swiftlang_repo.gpg.key | gpg --dearmor -o /usr/share/keyrings/swiftlang_repo.gpg.key \
    && echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/swiftlang_repo.gpg.key] https://archive.swiftlang.xyz/ubuntu focal main" | tee /etc/apt/sources.list.d/swiftlang.list > /dev/null \
    && apt update -y \
    && apt install swiftlang -y

# Install Kotlin
RUN apt update && apt upgrade -y
RUN apt install nginx curl zip unzip -y
RUN curl -s https://get.sdkman.io | bash
RUN chmod a+x "$HOME/.sdkman/bin/sdkman-init.sh"
RUN /bin/bash -c "source /root/.sdkman/bin/sdkman-init.sh; sdk version; sdk install kotlin"
ENV PATH=/root/.sdkman/candidates/kotlin/current/bin:$PATH

# Install Node & typescript
RUN set -e; \
    apt-get update -y && \
    apt-get install -qqy --no-install-recommends \
    curl wget nano gnupg2 software-properties-common && \
    rm -rf /var/lib/apt/lists;

RUN curl -sL https://deb.nodesource.com/setup_16.x | bash -

RUN set -e; \
    apt-get update && \
    apt-get install -qqy \
    nodejs && \
    rm -rf /var/lib/apt/lists  \
    && npm install -g typescript


# Install Rust
RUN apt-get update \
    && apt-get -qq -y install curl unzip zip  \
    && curl https://sh.rustup.rs -sSf | sh -s -- -y
ENV PATH="/root/.cargo/bin:${PATH}"
RUN echo 'source $HOME/.cargo/env' >> $HOME/.bashrc

# Install C# (DotNet SDK)
RUN wget https://packages.microsoft.com/config/ubuntu/20.04/packages-microsoft-prod.deb -O packages-microsoft-prod.deb
RUN dpkg -i packages-microsoft-prod.deb
RUN rm packages-microsoft-prod.deb
RUN apt-get update \
    && apt-get install -y dotnet-sdk-7.0

RUN kotlinc -version
RUN python3 --version
RUN java --version
RUN node --version
RUN swift --version
RUN cargo --help
RUN dotnet --info

ENTRYPOINT ["java","-jar","/app/doctor.jar"]

