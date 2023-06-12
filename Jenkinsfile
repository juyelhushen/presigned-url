pipeline {
    environment {
        dockerImageName = 'juyel8968/presigned-project'
        containerName = 'presignedcontainer'
        containerPort = 9001
        hostPort = 9001
    }
    agent any
    stages {
        stage('Checkout Source') {
            steps {
                git credentialsId: 'github', url: 'https://github.com/juyelhushen/awss3-presigned-url-demo.git'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Build and Push Docker Image') {
            steps {
                script {
                    docker.build("${dockerImageName}:${env.BUILD_NUMBER}", '-f ./Dockerfile .')
                    docker.withRegistry('https://registry.hub.docker.com', 'dockerhub') {
                        docker.image("${dockerImageName}:${env.BUILD_NUMBER}").push()
                    }
                }
            }
        }
        stage('Create Container') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'dockerhub') {
                        docker.run("${dockerImageName}:${env.BUILD_NUMBER}", "-d -p ${hostPort}:${containerPort} --name ${containerName}")
                    }
                }
            }
        }
    }
}
