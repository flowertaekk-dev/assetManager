FROM ubuntu:21.04

ARG ssh_prv_key="${ssh_prv_key}"
ARG ssh_pub_key="${ssh_pub_key}"

RUN apt-get update &&\
	apt-get upgrade -y &&\
	apt-get install git -y &&\
	apt-get install openjdk-11-jdk -y &&\
	mkdir -p /home/dev

RUN mkdir -p /root/.ssh &&\
	chmod 0700 /root/.ssh

RUN echo "${ssh_prv_key}" > /root/.ssh/id_rsa &&\
	echo "${ssh_pub_key}" > /root/.ssh/id_rsa.pub &&\
	chmod 0600 /root/.ssh/id_rsa &&\
	chmod 0600 /root/.ssh/id_rsa.pub

WORKDIR /home/dev

RUN git clone git@github.com:flowertaekk-dev/assetManager.git

WORKDIR /home/dev/assetManager/assetManager-server
