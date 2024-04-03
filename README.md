# Java 8 Application Deployment in Minikube using DockerHub

## Minikube installation.
Follow this link to install the Minikube on your local computer/laptop.
https://minikube.sigs.k8s.io/docs/start/
After successful installation use "minikube start" command to start your minikube.

## Build Docker image and push to DockerHub by below commands.

docker build -t document-management-system:version .

docker tag document-management-system:latest dockerhubuser/document-management-system:latest

docker push dockerhubuser/document-management-system:latest

## Application Deployment on Minikube by using below commands.

kubectl apply -f k8s/document-management-system-pv.yaml

kubectl apply -f k8s/document-management-system-deployment.yml

