def call(Map config = [:]) {
    pipeline {
        agent any
        environment {
            AWS_REGION = 'us-east-1'
            ECR_REPO = "nomis1/ns1"
            IMAGE_TAG = "sample-node-app_${env.BUILD_NUMBER}"
            PUSHED_IMAGE = "${ECR_REPO}:${IMAGE_TAG}"
        }
        stages {
            stage('Checkout') {
                steps {
                    checkout scm
                }
            }
            stage('Build') {
                steps {
                    container('nodejs') {
                        sh 'cd app && npm install'
                    }
                }
            }
            stage('Test') {
                steps {
                    container('nodejs') {
                        sh 'cd app && npm test'
                    }
                }
            }            
            stage('Docker Build & Push') {
                steps {
                    container('docker') {
                        withDockerRegistry([credentialsId: 'dockerhub', url: 'https://index.docker.io/v1/']) {
                            sh '''
                              cd app
                              docker build . -t ${IMAGE_TAG}
                              docker tag ${IMAGE_TAG} ${ECR_REPO}:${IMAGE_TAG}
                              docker push ${ECR_REPO}:${IMAGE_TAG}
                            '''
                        }

                    }
                    echo 'Simulate push to ECR or implement AWS CLI commands here'
                }
            }
            stage('Deploy to EKS') {
                steps {
                    container('kube-tools') {
                        sh '''
                        cd helm
                        helm upgrade -f ./values.yaml --install sample-node-app . --set imageName=${PUSHED_IMAGE} -n applications
                        '''

                    }
                }
            }
        }
        post {
            success {
                    echo "Deployment Success!"
                    // slackSend(teamDomain: 'team-domain',
                            // tokenCredentialId: 'slack-jenkins-id',
                            // baseUrl: 'https://<team-domain>.slack.com/services/hooks/jenkins-ci/',
                            // channel: '#jenkins',color: '#e34234',
                            // message: "${MS_NAME} has been deployed to env ${env.BRANCH_NAME}")    
            }
            failure {
                    echo "Deployment Failed!"
                    // slackSend(teamDomain: 'team-domain',
                            // tokenCredentialId: 'slack-jenkins-id',
                            // baseUrl: 'https://<team-domain>.slack.com/services/hooks/jenkins-ci/',
                            // channel: '#jenkins',color: '#e34234',
                            // message: "Deployment Failed! ${MS_NAME} to env ${env.BRANCH_NAME}")    
            }		
        }
    }
}
