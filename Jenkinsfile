pipeline {
    agent any
    tools {
        jdk 'OracleJDK11'
    }
    environment {
        STACK_NAME = 'SecretsTestStack'
        AWS_DEFAULT_REGION = 'us-east-1'
        S3_BUCKET_NAME = 'artifacbucket'
    }
    stages {
        stage('Generate Secret Name') {
            steps {
                script {
                    def randomSuffix = java.util.UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6)
                    def secretName = "MyDBCredentials-${randomSuffix}"
                    env.SECRET_NAME = secretName
                    echo "Generated secret name: ${env.SECRET_NAME}"
                }
            }
        }
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
                    def fileName = sh(script: 'ls target/cicd-secret-1.0-SNAPSHOT.jar', returnStdout: true).trim()
                    sh "aws s3 cp $fileName s3://$S3_BUCKET_NAME/secrets-lambda.jar"
                }
            }
        }
        stage('Delete Existing Stack') {
            steps {
                script {
                    def stackExists = sh(script: "aws cloudformation describe-stacks --stack-name $STACK_NAME --region $AWS_DEFAULT_REGION", returnStatus: true)
                    if (stackExists == 0) {
                        echo "Deleting existing stack..."
                        sh "aws cloudformation delete-stack --stack-name $STACK_NAME --region $AWS_DEFAULT_REGION"
                        sh "aws cloudformation wait stack-delete-complete --stack-name $STACK_NAME --region $AWS_DEFAULT_REGION"
                        echo "Existing stack deleted."
                    } else {
                        echo "No existing stack found."
                    }
                }
            }
        }
        stage('Deploy CloudFormation Stack') {
            steps {
                script {
                    sh """
                    aws cloudformation create-stack \
                        --stack-name $STACK_NAME \
                        --template-body file://cloudformation/lambda-secret-srv.yaml \
                        --parameters ParameterKey=SecretName,ParameterValue=${env.SECRET_NAME} \
                        --region $AWS_DEFAULT_REGION \
                        --capabilities CAPABILITY_IAM
                    """
                }
            }
        }
    }
}
