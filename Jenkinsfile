pipeline {
    agent any
    environment {
        AWS_DEFAULT_REGION = 'us-east-1'
        S3_BUCKET_NAME = 'artifacbucket'
        SECRET_NAME = 'rds!cluster-13c5457e-746e-4ca0-a576-23facf8e6367'
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package'
                archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
            }
        }
        stage('Deploy to S3') {
            steps {
                script {
                    def fileName = sh(script: 'ls target/*.jar', returnStdout: true).trim()
                    sh "aws s3 cp $fileName s3://$S3_BUCKET_NAME/secrets-lambda.jar"
                }
            }
        }
        stage('Deploy Lambda') {
            steps {
                script {
                    def stackName = 'secrets-test-stack'
                    def cfnTemplate = readFile 'cloudformation/lambda-secret-srv.yaml'
                    def params = [
                        [name: 'SecretName', value: SECRET_NAME]
                    ]
                    awsCloudFormation(
                        role: 'arn:aws:iam::800099700786:role/JenkinsRole',
                        region: AWS_DEFAULT_REGION,
                        createStack: true,
                        stackName: stackName,
                        templateBody: cfnTemplate,
                        capabilities: 'CAPABILITY_IAM',
                        params: params
                    )
                }
            }
        }
    }
}
