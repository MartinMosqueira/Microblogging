pipeline {
    agent any
    environment {
        DOCKERHUB_CREDENTIALS = 'DOCKERHUB_PASS'
    }
    stages {
        stage('Checkout Repository') {
            steps {
                checkout scmGit(
                    branches: [[name: 'main']],
                    userRemoteConfigs: [[credentialsId:  '133',
                    url: 'git@github.com:MartinMosqueira/Microblogging.git']])
            }
        }

        stage('Build and Push Docker Image for Database') {
            environment {
                DOCKER_IMAGE_NAME = 'tinchodevs/postgresql_jenkins'
                DOCKER_IMAGE_TAG = '1.0.0'
                DOCKERFILE_PATH = 'src/main/docker/deploy/Dockerfile-db'
            }
            steps {
                script {
                    sh "docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} -f ${DOCKERFILE_PATH} ."

                    withCredentials([usernamePassword(credentialsId: DOCKERHUB_CREDENTIALS, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh "docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD"
                    }

                    sh "docker push ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
                }
            }
        }

        stage('Build and Push Docker Image for Application') {
            environment {
                DOCKER_IMAGE_NAME = 'tinchodevs/microblogging_jenkins'
                DOCKER_IMAGE_TAG = '1.0.0'
                DOCKERFILE_PATH = 'src/main/docker/deploy/Dockerfile-app'
            }
            steps {
                script {
                    sh "docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} -f ${DOCKERFILE_PATH} ."

                    withCredentials([usernamePassword(credentialsId: DOCKERHUB_CREDENTIALS, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh "docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD"
                    }

                    sh "docker push ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
                }
            }
        }

        stage('Clean Up') {
            steps {
                deleteDir()
                sh 'docker system prune --volumes -f'
                sh 'docker rmi -f $(docker images -q)'
            }
        }
    }
}
