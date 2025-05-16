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