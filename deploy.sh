source "$HOME/.jenv/bin/jenv-init.sh"
jenv use java 1.7.0_75
mvn clean package

SONAR_CONTAINER_ID=03f9
docker cp target/sonar-lean-aggregated-test-report-plugin-1.0.1-SNAPSHOT.jar ${SONAR_CONTAINER_ID}:/opt/sonarqube/extensions/plugins/
docker restart ${SONAR_CONTAINER_ID}

