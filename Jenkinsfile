// Declarative pipeline — Zhuldyz Zhumabay (IT2-2308)
// Job name in Jenkins: Zhumabay_Zhuldyz_Student_API
pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    // Polling trigger: Jenkins checks GitHub every ~2 minutes and builds on a new commit.
    // (A GitHub webhook cannot reach Jenkins on localhost without a public URL / tunnel.)
    triggers {
        pollSCM('H/2 * * * *')
    }

    environment {
        STUDENT_NAME = 'Zhuldyz Zhumabay'
        STUDENT_GROUP = 'IT2-2308'
        IMAGE_NAME = 'zhumabay-zhuldyz-student-api'
        IMAGE_TAG = 'v1.0-zhumabay-zhuldyz'
        IMAGE = "${IMAGE_NAME}:${IMAGE_TAG}"
    }

    stages {
        stage('Checkout') {
            steps {
                echo "=== Pipeline of ${STUDENT_NAME} (${STUDENT_GROUP}) ==="
                checkout scm
                sh 'git log -1 --oneline'
                sh 'git branch -a | head -20'
            }
        }

        stage('Build') {
            steps {
                echo "Building Spring Boot application of ${STUDENT_NAME}"
                sh 'java -version'
                sh 'chmod +x gradlew'
                sh './gradlew clean bootJar -x test --no-daemon'
                sh 'ls -lh build/libs/'
            }
        }

        stage('Test') {
            steps {
                echo "Running automated tests for ${STUDENT_NAME}"
                // If any test fails, gradlew returns a non-zero exit code -> the stage fails
                // and all following stages (Docker Build, Verify) are skipped.
                sh './gradlew test --no-daemon'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'build/test-results/test/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo "Building Docker image ${IMAGE} for ${STUDENT_NAME}"
                sh 'docker build -t ${IMAGE} -t ${IMAGE_NAME}:build-${BUILD_NUMBER} .'
            }
        }

        stage('Verify') {
            steps {
                sh '''
                    if docker image inspect "$IMAGE" > /dev/null 2>&1; then
                        echo "Image $IMAGE exists"
                        docker images "$IMAGE_NAME"
                        docker image inspect "$IMAGE" --format 'Created: {{.Created}}  Size: {{.Size}} bytes'
                    else
                        echo "Image $IMAGE NOT found"
                        exit 1
                    fi
                '''
            }
        }
    }

    post {
        success {
            echo "SUCCESS: pipeline of ${STUDENT_NAME} finished, image ${IMAGE} is ready"
        }
        failure {
            echo "FAILURE: pipeline of ${STUDENT_NAME} failed - check the stage marked red above"
        }
    }
}
