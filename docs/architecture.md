## Design Rationale

### 1. Automation with deploy.sh

The `deploy.sh` script automates the entire deployment process to ensure consistency and reduce the chances of human error. By checking for prerequisites and installing necessary tools, the script ensures that the environment is properly set up before proceeding with the deployment.

### 2. Jenkins on Kubernetes

Using Helm to install Jenkins in kubernetes (minikube) provides several benefits:
- Simplifies the installation process by using predefined Helm charts
- Allows for easy customization through the `jenkins-values.yaml` file
- Enables version control and rollback capabilities
- Define pod templates (containers) for different build agents allowing for clean reproducible builds which can be provisioned on demand and auto-cleaned after use. 
- High Availability 

### 4. Jenkins Configuration as Code (JCasC)

Jenkins Configuration as Code (JCasC) allows for the configuration of Jenkins to be defined in YAML files. This approach provides several advantages:
- Enables version control of Jenkins configuration
- Simplifies the setup and maintenance of Jenkins instances
- Allows for easy replication of Jenkins environments

## Extensibility

The deployment architecture is designed to be extensible and customizable. Some ways to extend the architecture include:
- Adding additional plugins to the `jenkins-values.yaml` file
- Modifying the JCasC scripts to include additional configuration
- Adding more namespaces or resources to the `deploy.sh` script
- Integrating with other tools and services by adding appropriate Helm charts and configurations

## Shared Libraries

### 1. **Checkout**

Checks out the source code from the repository defined in the Jenkins job.

### 2. **Build**

Runs inside a `nodejs` container. Installs Node.js dependencies located in the `app` directory.

### 3. **Test**

Executes unit tests using `npm test` inside the `nodejs` container.

### 4. **Docker Build & Push**

Uses the `docker` container to:
  - Build a Docker image from the `app` directory.
  - Tag the image with the build number.
  - Push the image to Docker Hub using stored credentials. Its able to use the same code to push to ECR as well by replacing the `url` in `withDockerRegistry`

### 5. **Lint Application Helm Chart**

Uses the `kube-tools` container to:
  - Lint the application helm chart located in the `helm` directory.


### 6. **Deploy to EKS**

Uses the `kube-tools` container to:
  - Deploy the application to an EKS cluster using Helm.
  - The Helm chart is located in the `helm` directory.
  - The image name is dynamically set using the pushed Docker image.
  - The same commands can be used to deploy the helm chart to EKS as well by providing `--kubeconfig` to the helm command so it can deploy to EKS

### 7. **Post Actions**

On success or failure, logs a message. Slack notifications are included but commented out. You can enable them by configuring the appropriate Slack credentials and uncommenting the lines.

## Improvements

    - The clusterRoleBinding command allows the service account to act as cluster admin. Its not a recommended policy and this is only used for this particular demo. In a live environment this should be restricted. 
    - Secret Management through AWS Secrets Manager or Vault instead of harcoding the values in `jenkins-values`. 