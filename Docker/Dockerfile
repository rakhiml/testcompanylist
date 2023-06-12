FROM jboss/wildfly:24.0.0.Final

COPY ./startup.sh /opt/startup.sh
USER root
RUN chmod +x /opt/startup.shls


CMD ["/opt/startup.sh"]
