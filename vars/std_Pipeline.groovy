def call(Map config = [:]) {
    pipeline {
        agent any
        stages {
            stage('Checkout') {
                steps {
                    checkout scm
                }
            }
            stage('Test') {
                steps {
                    container('node') {
                        sh 'cd app && npm install'
                    }
                }
            }
            stage('Push to ECR') {
                steps {
                    
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
