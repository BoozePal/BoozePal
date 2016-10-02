#!/bin/bash
set -e
branch=$(git branch | sed -n -e 's/^\* \(.*\)/\1/p');
echo "Actual branch is:" $branch;
if [ $branch = "master" ]; 
then
	(cd boozepal && mvn clean install) && (cd boozepal/boozepal-ear && mvn -PCloudDeploy wildfly:undeploy && mvn -PCloudDeploy wildfly:deploy);
else 
	(cd boozepal && mvn clean install);
fi