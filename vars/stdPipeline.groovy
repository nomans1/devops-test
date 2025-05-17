def call(Map config = [:]) {
    pipeline {
        agent any
        environment {
            AWS_REGION = 'us-east-1'
            ECR_REPO = 'nomis1/ns1'
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
                    dir('app') {
                        container('nodejs') {
                            sh 'npm install'
                        }
                    }
                }
            }
            stage('Test') {
                steps {
                    dir('app') {
                        container('nodejs') {
                            sh 'npm test'
                        }
                    }
                }
            }
            stage('Docker Build & Push') {
                steps {
                    dir('app') {
                        container('docker') {
                            withDockerRegistry([credentialsId: 'dockerhub', url: 'https://index.docker.io/v1/']) {
                                sh '''
                                docker build . -t ${IMAGE_TAG}
                                docker tag ${IMAGE_TAG} ${ECR_REPO}:${IMAGE_TAG}
                                docker push ${ECR_REPO}:${IMAGE_TAG}
                                '''
                            }
                        }
                    }
                }
            }
            stage('Helm Lint') {
                steps {
                    dir('helm') {
                        container('kube-tools') {
                            sh '''
                            helm lint . -f ./values.yaml
                            '''
                        }
                    }
                }
            }
            stage('Deploy to EKS') {
                steps {
                    dir('helm') {
                        container('kube-tools') {
                            sh '''
                            helm upgrade -f ./values.yaml --install sample-node-app . --set imageName=${PUSHED_IMAGE} -n applications
                            '''
                        }
                    }
                }
            }
        }
        post {
            success {
                    echo 'Deployment Success!'
            // slackSend(teamDomain: 'team-domain',
            // tokenCredentialId: 'slack-jenkins-id',
            // baseUrl: 'https://<team-domain>.slack.com/services/hooks/jenkins-ci/',
            // channel: '#jenkins',color: '#e34234',
            // message: "${MS_NAME} has been deployed to env ${env.BRANCH_NAME}")
            }
            failure {
                    echo 'Deployment Failed!'
            // slackSend(teamDomain: 'team-domain',
            // tokenCredentialId: 'slack-jenkins-id',
            // baseUrl: 'https://<team-domain>.slack.com/services/hooks/jenkins-ci/',
            // channel: '#jenkins',color: '#e34234',
            // message: "Deployment Failed! ${MS_NAME} to env ${env.BRANCH_NAME}")
            }
        }
    }
}
