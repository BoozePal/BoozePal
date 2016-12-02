#!/bin/bash
actualBranch=$(git branch | sed -n -e 's/^\* \(.*\)/\1/p');
commitId=$( echo $actualBranch | awk '{split($0,a," "); print a[4]}');
cuttedId=$(echo $commitId | cut -c1-7);
branchMsg=$(git branch --contains $cuttedId);
neededBranch=$( echo $branchMsg | awk '{split($0,a," "); print a[length(a)]}');
echo "Actual branch is:" $neededBranch;
if [ $neededBranch = "master" ]; 
then
	(cd boozepal && mvn clean install -DskipTests && cd boozepal-ear && mvn -PCloudDeploy wildfly:undeploy && mvn -PCloudDeploy wildfly:deploy);
else 
	(cd boozepal && mvn clean install);
fi