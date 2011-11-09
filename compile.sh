#!/bin/sh
mvn clean install -Pmysql -D maven.test.skip=true
