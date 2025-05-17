# Jenkins Installation on Minikube using Helm

This guide provides step-by-step instructions to install and configure Jenkins on Minikube using Helm on macOS.

To learn more about the design rationale and details on the solution, please refer to the [Basic Architectural principles](docs/architecture.md). 

## Overview
**Note:** All of the steps mentioned below are executed by running the `deploy.sh` script attached.
**Note:** Before running the `deploy.sh` script, update the `jenkins-values.yaml` file with your own DockerHub and GitHub credentials. You can add these credentials through Helm or modify the credentials after Jenkins is up and running.

```bash
scripts/deploy.sh
```

This script checks for prerequisites, installs necessary tools, starts Minikube, creates namespaces, adds the Jenkins Helm repository, installs Jenkins, and sets up port forwarding to access Jenkins.

## Prerequisites

- Homebrew
- kubectl
- Minikube
- Helm

1. **Install Required Tools**  
   It installs `kubectl`, `minikube`, and `helm` using Homebrew if they are not already installed.

2. **Start Minikube**  
   Minikube is started using the Docker driver.

3. **Create Kubernetes Namespaces**  
   Namespaces `jenkins` and `applications` are created if they do not already exist.

4. **Add Jenkins Helm Repository**  
   The Jenkins Helm chart repository is added and updated.

5. **Create Cluster Role Binding**  
   A permissive cluster role binding is created to allow Jenkins to function properly.

6. **Install Jenkins**  
   Jenkins is installed using Helm with a custom `jenkins-values.yaml` file.

7. **Wait for Jenkins to be Ready**  
   The script waits for the Jenkins StatefulSet to be ready.

## Accessing Jenkins

- Port forwarding is set up to access Jenkins at http://localhost:8080.
- Press `Ctrl+C` to stop port forwarding.
- Username & Password have been set in the `jenkins-values.yaml` as `admin` & `admin123` respectively.
- A sample pipeline called `devops-test` is already created and executed once Jenkins is available to deploy a NodeJS app in the `applications` namespace. 


```bash
kubectl -n applications port-forward svc/sample-node-app 8000:8000
```