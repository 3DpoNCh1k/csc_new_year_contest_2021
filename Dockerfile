FROM ubuntu:20.04
ARG DEBIAN_FRONTEND=noninteractive
COPY --chown=root:root packages.txt install.sh /opt/
WORKDIR /opt
RUN ./install.sh
# for f#
ENV PATH="${PATH}:/root/.dotnet"
ENV PATH="${PATH}:/opt/kotlinc/bin"
ENV DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1
