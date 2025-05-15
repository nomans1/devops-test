def call(Map config = [:]) {
    pipeline {
        agent any
        environment {
            AWS_REGION = 'us-east-1'
            ECR_REPO = 'ns1'
            IMAGE_TAG = "sample-node-app:${env.BUILD_NUMBER}"
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
                        sh '''
                          cd app
                          docker build . -t sample-node-app:${BUILD_NUMBER}
                        '''

                    }
                    echo 'Simulate push to ECR or implement AWS CLI commands here'
                }
            }
            stage('Deploy to EKS') {
                steps {
                    echo 'Simulate deploy to EKS or run kubectl apply'
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
